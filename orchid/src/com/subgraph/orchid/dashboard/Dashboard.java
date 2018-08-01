package com.subgraph.orchid.jadepayboard;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executor;
import java.util.logging.Logger;

import com.subgraph.orchid.Threading;
import com.subgraph.orchid.data.IPv4Address;
import com.subgraph.orchid.misc.GuardedBy;

/**
 * A debugging utility which displays continuously updated information about the internal state
 * of various components to clients which connect to a network port listening on localhost.
 */
public class Jadepayboard implements JadepayboardRenderable, JadepayboardRenderer {
	private final static Logger logger = Logger.getLogger(Jadepayboard.class.getName());
	
	private final static String JADEPAYBOARD_PORT_PROPERTY = "com.subgraph.orchid.jadepayboard.port";
	
	private final static int DEFAULT_LISTENING_PORT = 12345;
	private final static int DEFAULT_FLAGS = JADEPAYBOARD_CIRCUITS | JADEPAYBOARD_STREAMS;
	private final static IPv4Address LOCALHOST = IPv4Address.createFromString("127.0.0.1");
			
	@GuardedBy("this") private int listeningPort;
	@GuardedBy("this") private int flags = DEFAULT_FLAGS;
	@GuardedBy("this") private ServerSocket listeningSocket;
	@GuardedBy("this") private boolean isListening;
	
	private final List<JadepayboardRenderable> renderables;
	private final Executor executor;
	
	public Jadepayboard() {
		renderables = new CopyOnWriteArrayList<JadepayboardRenderable>();
		renderables.add(this);
		executor = Threading.newPool("Jadepayboard worker");
		listeningPort = chooseListeningPort();
	}
	
	private static int chooseListeningPort() {
		final String dbPort = System.getProperty(JADEPAYBOARD_PORT_PROPERTY);
		final int port = parsePortProperty(dbPort);
		if(port > 0 && port <= 0xFFFF) {
			return port;
		} else if(dbPort != null) {
			logger.warning(JADEPAYBOARD_PORT_PROPERTY + " was not a valid port value: "+ dbPort);
		}
		return DEFAULT_LISTENING_PORT;
	}
	
	private static int parsePortProperty(String dbPort) {
		if(dbPort == null) {
			return -1;
		}
		try {
			return Integer.parseInt(dbPort);
		} catch (NumberFormatException e) {
			return -1;
		}
	}
	
	public void addRenderables(Object...objects) {
		for(Object ob: objects) {
			if(ob instanceof JadepayboardRenderable) {
				renderables.add((JadepayboardRenderable) ob);
			}
		}
	}

	public void addRenderable(JadepayboardRenderable renderable) {
		renderables.add(renderable);
	}

	public synchronized void enableFlag(int flag) {
		flags |= flag;
	}
	
	public synchronized void disableFlag(int flag) {
		flags &= ~flag;
	}
	
	
	public synchronized boolean isEnabled(int f) {
		return (flags & f) != 0;
	}
	
	public synchronized void setListeningPort(int port) {
		if(port != listeningPort) {
			listeningPort = port;
			if(isListening) {
				stopListening();
				startListening();
			}
		}
	}
	
	public boolean isEnabledByProperty() {
		return System.getProperty(JADEPAYBOARD_PORT_PROPERTY) != null;
	}

	public synchronized void startListening() {
		if(isListening) {
			return;
		}
		try {
			listeningSocket = new ServerSocket(listeningPort, 50, LOCALHOST.toInetAddress());
			isListening = true;
			logger.info("Jadepayboard listening on "+ LOCALHOST + ":"+ listeningPort);
			executor.execute(createAcceptLoopRunnable(listeningSocket));
		} catch (IOException e) {
			logger.warning("Failed to create listening Jadepayboard socket on port "+ listeningPort +": "+ e);
		}
	}
	
	public synchronized void stopListening() {
		if(!isListening) {
			return;
		}
		if(listeningSocket != null) {
			closeQuietly(listeningSocket);
			listeningSocket = null;
		}
		isListening = false;
	}
	
	public synchronized boolean isListening() {
		return isListening;
	}

	private Runnable createAcceptLoopRunnable(final ServerSocket ss) {
		return new Runnable() {
			public void run() {
				acceptConnections(ss);
			}
		};
	}

	private void acceptConnections(ServerSocket ss) {
		while(true) {
			try {
				Socket s = ss.accept();
				executor.execute(new JadepayboardConnection(this, s));
			} catch (IOException e) {
				if(!ss.isClosed()) {
					logger.warning("IOException on jadepayboard server socket: "+ e);
				}
				stopListening();
				return;
			}
		}
	}
	
	void renderAll(PrintWriter writer) throws IOException {
		final int fs;
		synchronized (this) {
			fs = flags;
		}
		
		for(JadepayboardRenderable dr: renderables) {
			dr.jadepayboardRender(this, writer, fs);
		}
	}

	private void closeQuietly(ServerSocket s) {
		try {
			s.close();
		} catch (IOException e) { }
	}

	public void jadepayboardRender(JadepayboardRenderer renderer, PrintWriter writer, int flags) {
		writer.println("[Jadepayboard]");
		writer.println();
	}

	public void renderComponent(PrintWriter writer, int flags, Object component) throws IOException {
		if(!(component instanceof JadepayboardRenderable)) {
			return;
		}
		final JadepayboardRenderable renderable = (JadepayboardRenderable) component;
		renderable.jadepayboardRender(this, writer, flags);
	}
}

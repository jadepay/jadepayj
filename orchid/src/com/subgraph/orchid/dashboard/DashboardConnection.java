package com.subgraph.orchid.jadepayboard;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.Writer;
import java.net.Socket;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class JadepayboardConnection implements Runnable {
	
	private final static int REFRESH_INTERVAL = 1000;

	private final Jadepayboard jadepayboard;
	private final Socket socket;
	private final ScheduledExecutorService refreshExecutor;
	
	public JadepayboardConnection(Jadepayboard jadepayboard, Socket socket) {
		this.jadepayboard = jadepayboard;
		this.socket = socket;
		this.refreshExecutor = new ScheduledThreadPoolExecutor(1);
	}

	public void run() {
		ScheduledFuture<?> handle = null;
		try {
			final PrintWriter writer = new PrintWriter(socket.getOutputStream());
			handle = refreshExecutor.scheduleAtFixedRate(createRefreshRunnable(writer), 0, REFRESH_INTERVAL, TimeUnit.MILLISECONDS);
			runInputLoop(socket.getInputStream());
		} catch (IOException e) {
			closeQuietly(socket);
		} finally {
			if(handle != null) {
				handle.cancel(true);
			}
			refreshExecutor.shutdown();
		}
	}

	private void closeQuietly(Socket s) {
		try {
			s.close();
		} catch (IOException e) { }
	}

	private void runInputLoop(InputStream input) throws IOException {
		int c;
		
		while((c = input.read()) != -1) {
			switch(c) {
			case 'c':
				toggleFlagWithVerbose(JadepayboardRenderable.JADEPAYBOARD_CONNECTIONS, JadepayboardRenderable.JADEPAYBOARD_CONNECTIONS_VERBOSE);
				break;
			case 'p':
				toggleFlag(JadepayboardRenderable.JADEPAYBOARD_PREDICTED_PORTS);
				break;
			default:
				break;
			}
		}
	}

	// Rotate between 3 states
	//    0 (no flags),
	//    basicFlag,
	//    basicFlag|verboseFlag
	private void toggleFlagWithVerbose(int basicFlag, int verboseFlag) {
		if(jadepayboard.isEnabled(verboseFlag)) {
			jadepayboard.disableFlag(basicFlag | verboseFlag);
		} else if(jadepayboard.isEnabled(basicFlag)) {
			jadepayboard.enableFlag(verboseFlag);
		} else {
			jadepayboard.enableFlag(basicFlag);
		}
	}
	
	private void toggleFlag(int flag) {
		if(jadepayboard.isEnabled(flag)) {
			jadepayboard.disableFlag(flag);
		} else {
			jadepayboard.enableFlag(flag);
		}
	}

	private void hideCursor(Writer writer) throws IOException {
		emitCSI(writer);
		writer.write("?25l");
	}

	private void emitCSI(Writer writer) throws IOException {
		writer.append((char) 0x1B);
		writer.append('[');
	}
	
	private void clear(PrintWriter writer) throws IOException {
		emitCSI(writer);
		writer.write("2J");
	}
	
	private void moveTo(PrintWriter writer, int x, int y) throws IOException {
		emitCSI(writer);
		writer.printf("%d;%dH", x+1, y+1);
	}
	
	private void refresh(PrintWriter writer) {
		try {
			if(socket.isClosed()) {
				return;
			}
			hideCursor(writer);
			clear(writer);
			moveTo(writer, 0, 0);
			jadepayboard.renderAll(writer);
			writer.flush();
		} catch(IOException e) {
			closeQuietly(socket);
		}
	}

	private Runnable createRefreshRunnable(final PrintWriter writer) {
		return new Runnable() {
			public void run() {
				refresh(writer);
			}
		};
	}
}

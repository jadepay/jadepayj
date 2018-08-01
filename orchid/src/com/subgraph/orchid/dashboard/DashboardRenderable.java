package com.subgraph.orchid.jadepayboard;

import java.io.IOException;
import java.io.PrintWriter;

public interface JadepayboardRenderable {
	
	static int JADEPAYBOARD_CONNECTIONS           = 1 << 0;
	static int JADEPAYBOARD_CONNECTIONS_VERBOSE   = 1 << 1;
	static int JADEPAYBOARD_PREDICTED_PORTS       = 1 << 2;
	static int JADEPAYBOARD_CIRCUITS              = 1 << 3;
	static int JADEPAYBOARD_STREAMS               = 1 << 4;
	
	void jadepayboardRender(JadepayboardRenderer renderer, PrintWriter writer, int flags) throws IOException;
}

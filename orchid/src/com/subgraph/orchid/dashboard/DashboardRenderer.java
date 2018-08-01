package com.subgraph.orchid.jadepayboard;

import java.io.IOException;
import java.io.PrintWriter;

public interface JadepayboardRenderer {
	void renderComponent(PrintWriter writer, int flags, Object component) throws IOException;
}

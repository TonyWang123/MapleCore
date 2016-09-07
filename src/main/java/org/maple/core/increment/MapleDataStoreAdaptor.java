package org.maple.core.increment;

import org.maple.core.increment.tracetree.TraceTree;

public interface MapleDataStoreAdaptor {
    Object readData(String xpath);
    void writeData(String xpath, Object data); //TODO
    void writeTraceTree(TraceTree traceTree);
}


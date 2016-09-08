package org.maple.core.increment.app;

import org.maple.core.increment.MapleDataPathAdaptor;
import org.maple.core.increment.MapleDataStoreAdaptor;
import org.maple.core.increment.tracetree.*;
import org.opendaylight.yang.gen.v1.urn.opendaylight.inventory.rev130819.NodeConnectorRef;

public abstract class MapleAppBase implements MapleDataStoreAdaptor{

	public void OnPacket(MaplePacket pkt) {

	}


	public Object readData(String xpath) {
		return null;
	}

	public void writeData(String xpath, Object data) {

	}

	public void writeTraceTree(TraceTree traceTree) {

	}
}

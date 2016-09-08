package org.maple.core.increment.app;

import org.maple.core.increment.MapleDataPathAdaptor;
import org.maple.core.increment.MapleDataStoreAdaptor;
import org.maple.core.increment.tracetree.*;
import org.opendaylight.yang.gen.v1.urn.opendaylight.inventory.rev130819.NodeConnectorRef;

public abstract class MapleAppBase implements MapleDataPathAdaptor, MapleDataStoreAdaptor{

	public abstract Action onPacket(MaplePacket pkt);


	public void sendPacket(byte[] payload, NodeConnectorRef ingress, Action action) {

	}

	public void installPath(Action action, Match match, Integer priority) {

	}

	public void deletePath(Action action, Match match, Integer priority) {

	}

	public void installRule(Rule r, NodeConnectorRef sw) {

	}

	public void deleteRule(Rule r, NodeConnectorRef sw) {

	}

	public Object readData(String xpath) {
		return null;
	}

	public void writeData(String xpath, Object data) {

	}

	public void writeTraceTree(TraceTree traceTree) {

	}
}

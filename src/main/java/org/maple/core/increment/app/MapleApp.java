package org.maple.core.increment.app;

import org.maple.core.increment.tracetree.*;
import org.opendaylight.yang.gen.v1.urn.opendaylight.inventory.rev130819.NodeConnectorRef;

public class MapleApp extends MapleAppBase{

	public Action onPacket(MaplePacket pkt){
		/*
		 * MaplePacket maplePacket <- pkt
		 * Trace trace = new Trace();
		 * maplePacket.addTrace(trace);
		 * user app <- maplePacket;
		 * get trace after run user app
		 * store maplePacket and trace
		 * 
		 * */
		return Action.Drop();
	}
}

package org.maple.core.increment.app;

import org.maple.core.increment.tracetree.Action;
import org.maple.core.increment.tracetree.MaplePacket;

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

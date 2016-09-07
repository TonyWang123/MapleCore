package org.maple.core.increment;

import org.maple.core.increment.app.MapleApp;
import org.maple.core.increment.app.MapleAppBase;
import org.maple.core.increment.tracetree.MaplePacket;
import org.maple.core.increment.tracetree.Trace;
import org.maple.core.increment.tracetree.TraceTree;

public class MapleCore {

	TraceTree tt = new TraceTree();
	
	MapleDataPathAdaptor mapleDataPathAdaptor;
	
	MapleDataStoreAdaptor mapleDataStoreAdaptor;
	
	public void setAdaptor(MapleDataPathAdaptor mapleDataPathAdaptor
			, MapleDataStoreAdaptor mapleDataStoreAdaptor){
		this.mapleDataPathAdaptor = mapleDataPathAdaptor;
		this.mapleDataStoreAdaptor = mapleDataStoreAdaptor;
	}
	
	public void updateTrace(String pktHash, Trace trace){
		//TODO: pktHash to MaplePacket, and call tt.updateTT
	}
}

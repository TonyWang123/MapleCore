/*
 * Copyright (c) 2016 SNLAB and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.maple.core.increment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.maple.core.increment.app.MapleApp;
import org.maple.core.increment.app.MapleAppBase;
import org.maple.core.increment.app.systemApps.ARPHandlingMapleApp;
import org.maple.core.increment.tracetree.MaplePacket;
import org.maple.core.increment.tracetree.Port;
import org.maple.core.increment.tracetree.Trace;
import org.maple.core.increment.tracetree.TraceTree;

public class MapleCore {

	TraceTree tt = new TraceTree();
	
	MapleDataPathAdaptor mapleDataPathAdaptor;
	
	MapleDataStoreAdaptor mapleDataStoreAdaptor;
	
	static List<MapleCore> mapleCores = new ArrayList<MapleCore>();
	
	MapleAppBase topMapleApp;
	
	Map<Integer, Port> host2swTable = new HashMap<Integer, Port>();
	
    public static MapleCore allocateMapleCore() {
        return mapleCores.get(0); //TODO: missing allocator
    }
    
    public MapleCore(){
    	mapleCores.add( this );
    	ARPHandlingMapleApp arpApp = new ARPHandlingMapleApp();
    	
    }
    
    public void registerMapleApp(MapleAppBase mapleApp) {
    	// add maple app to the last
    	if (this.topMapleApp == null) {
    		this.topMapleApp = mapleApp;
    	} else {
    		mapleApp.setNextMapleApp(topMapleApp);
    		this.topMapleApp = mapleApp;
    	}
    }
	
	public void setAdaptor(MapleDataPathAdaptor mapleDataPathAdaptor
			, MapleDataStoreAdaptor mapleDataStoreAdaptor){
		this.mapleDataPathAdaptor = mapleDataPathAdaptor;
		this.mapleDataStoreAdaptor = mapleDataStoreAdaptor;
	}
	
	public Object readData(String xpath) {
		Object data = mapleDataStoreAdaptor.readData(xpath);
		return data;
	}
	
	public synchronized void updateTrace(String pktHash, Trace trace){
		//TODO: pktHash to MaplePacket, and call tt.updateTT
		tt.updateTT(pktHash, trace);
	}

	public Map<Integer, Port> getHost2swTable() {
		return host2swTable;
	}

	public void setHost2swTable(Map<Integer, Port> host2swTable) {
		this.host2swTable = host2swTable;
	}
}

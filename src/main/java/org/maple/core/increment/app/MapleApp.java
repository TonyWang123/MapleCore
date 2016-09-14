/*
 * Copyright (c) 2016 SNLAB and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.maple.core.increment.app;

import org.maple.core.increment.MapleCore;
import org.maple.core.increment.packet.Ethernet;
import org.maple.core.increment.tracetree.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MapleApp extends MapleAppBase{

	protected static final Logger LOG = LoggerFactory.getLogger(MapleApp.class);
	
	public Action onPacket(MaplePacket pkt){
		//Object data = readData("/data/dummy_data");
		if (pkt.ethTypeIs(Ethernet.TYPE_ARP)) {
			long srcMac = pkt.ethSrc();
			LOG.info(String.valueOf(srcMac));
			return Action.Punt();
		} else {
			return Action.Drop();
		}
	}
	
	public static void main(String[] args) {
		MapleCore mc = new MapleCore();
		for (int i = 0; i < 10; i++) {
			Ethernet frame = new Ethernet();
			frame.setEtherType((short) 0);
			MapleApp ma = new MapleApp();
			MaplePacket mp = new MaplePacket(frame, new Port("aa"));
			Action act = ma.onPacket(mp);
			Trace trace = new Trace();
	        
	        for (TraceItem ti: mp.itemList) {
	        	trace.addTraceItem(ti, "hash1");
	        }
	        trace.addTraceItem(act.toItem(), "hash1");
	        
	        mc.updateTrace("hash1", trace);
		}
		for (int i = 0; i < 3; i++) {
			Ethernet frame = new Ethernet();
			frame.setEtherType((short) 2054);
			frame.setSourceMACAddress("0:0:0:0:0:1");
			MapleApp ma = new MapleApp();
			MaplePacket mp = new MaplePacket(frame, new Port("aa"));
			Action act = ma.onPacket(mp);
			Trace trace = new Trace();
	        
	        for (TraceItem ti: mp.itemList) {
	        	trace.addTraceItem(ti, "hash2");
	        }
	        trace.addTraceItem(act.toItem(), "hash2");
	        
	        mc.updateTrace("hash2", trace);
		}
		for (int i = 0; i < 5; i++) {
			Ethernet frame = new Ethernet();
			frame.setEtherType((short) 0);
			MapleApp ma = new MapleApp();
			MaplePacket mp = new MaplePacket(frame, new Port("aa"));
			Action act = ma.onPacket(mp);
			Trace trace = new Trace();
	        
	        for (TraceItem ti: mp.itemList) {
	        	trace.addTraceItem(ti, "hash1");
	        }
	        trace.addTraceItem(act.toItem(), "hash1");
	        
	        mc.updateTrace("hash1", trace);
		}
	}
}

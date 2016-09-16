package org.maple.core.increment.app;

import java.util.ArrayList;
import java.util.List;

import org.maple.core.increment.packet.Ethernet;
import org.maple.core.increment.packet.IPv4;
import org.maple.core.increment.tracetree.Action;
import org.maple.core.increment.tracetree.MaplePacket;
import org.maple.core.increment.tracetree.Route;

public class M2 extends MapleAppBase{
	@Override
	public void onPacket(MaplePacket pkt) {
		if (pkt.ethTypeIs(Ethernet.TYPE_IPv4)) {
			if (pkt.IPv4SrcIs(IPv4.toIPv4Address("10.0.0.1")) 
					&& pkt.IPv4DstIs(IPv4.toIPv4Address("10.0.0.2"))) {
				
			} else if (pkt.IPv4SrcIs(IPv4.toIPv4Address("10.0.0.2")) 
					&& pkt.IPv4DstIs(IPv4.toIPv4Address("10.0.0.1"))) {
				
			} else {
				pkt.setAction(Action.Drop());
			}
		} else {
			//make the next maple app handle this pkt
			this.passToNext(pkt);
		}
	}
}

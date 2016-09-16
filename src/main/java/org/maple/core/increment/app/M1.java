package org.maple.core.increment.app;

import java.util.ArrayList;
import java.util.List;

import org.maple.core.increment.packet.IPv4;
import org.maple.core.increment.tracetree.Action;
import org.maple.core.increment.tracetree.MaplePacket;
import org.maple.core.increment.tracetree.Route;

public class M1 extends MapleAppBase{
	
	@Override
	public void onPacket(MaplePacket pkt) {
		if (pkt.IPv4SrcIs(IPv4.toIPv4Address("10.0.0.1")) 
				&& pkt.IPv4DstIs(IPv4.toIPv4Address("10.0.0.2"))) {
			String link1 = "<l1,tpId1,tpId2>";
			String link2 = "<l2,tpId3,tpId4>";
			List<String> path = new ArrayList<String>();
			path.add(link1);
			path.add(link2);
			pkt.setAction(new Route(path, "tpId5"));
		} else if (pkt.IPv4SrcIs(IPv4.toIPv4Address("10.0.0.2")) 
				&& pkt.IPv4DstIs(IPv4.toIPv4Address("10.0.0.1"))) {
			String link1 = "<l3,tpId2,tpId1>";
			String link2 = "<l4,tpId4,tpId3>";
			List<String> path = new ArrayList<String>();
			path.add(link1);
			path.add(link2);
			pkt.setAction(new Route(path, "tpId0"));
		} else {
			pkt.setAction(Action.Drop());
		}
	}
}

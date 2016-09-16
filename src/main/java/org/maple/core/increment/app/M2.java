package org.maple.core.increment.app;

import java.util.ArrayList;
import java.util.List;

import org.maple.core.increment.packet.Ethernet;
import org.maple.core.increment.packet.IPv4;
import org.maple.core.increment.tracetree.Action;
import org.maple.core.increment.tracetree.MaplePacket;
import org.maple.core.increment.tracetree.Port;
import org.maple.core.increment.tracetree.Route;
import org.opendaylight.yang.gen.v1.urn.opendaylight.inventory.rev130819.NodeConnectorRef;
import org.opendaylight.yang.gen.v1.urn.opendaylight.inventory.rev130819.NodeId;
import org.opendaylight.yang.gen.v1.urn.tbd.params.xml.ns.yang.network.topology.rev131021.TpId;
import org.opendaylight.yang.gen.v1.urn.tbd.params.xml.ns.yang.network.topology.rev131021.network.topology.Topology;
import org.opendaylight.yang.gen.v1.urn.tbd.params.xml.ns.yang.network.topology.rev131021.network.topology.topology.Link;

public class M2 extends MapleAppBase{
	
	NetworkGraphService ngs = new NetworkGraphImpl();
	
	private NodeId convertPort2NodeId(Port port) {
		TpId tpId = new TpId(port.getId());
		String nc_value = tpId.getValue();
		return new NodeId(nc_value.substring(0, nc_value.lastIndexOf(':')));
	}
	
	@Override
	public void onPacket(MaplePacket pkt) {
		if (pkt.ethTypeIs(Ethernet.TYPE_IPv4)) {
			int srcIP = pkt.IPv4Src();
			int dstIP = pkt.IPv4Dst();
			
			Topology topo = (Topology)readData("/data/topology");
			
			ngs.clear();
			ngs.addLinks(topo.getLink());
			
			Port srcPort = this.mapleCore.getHost2swTable().get(srcIP);
			Port dstPort = this.mapleCore.getHost2swTable().get(dstIP);
			
			org.opendaylight.yang.gen.v1.urn.tbd.params.xml.ns.yang
			.network.topology.rev131021.NodeId srcNodeIdForTBD = new org.opendaylight.yang.gen.v1.urn.tbd.params.xml.ns.yang
		    .network.topology.rev131021.NodeId(
		    		convertPort2NodeId(srcPort));
			
			org.opendaylight.yang.gen.v1.urn.tbd.params.xml.ns.yang
			.network.topology.rev131021.NodeId dstNodeIdForTBD = new org.opendaylight.yang.gen.v1.urn.tbd.params.xml.ns.yang
		    .network.topology.rev131021.NodeId(
		    		convertPort2NodeId(dstPort));
			
			List<Link> path = null;
			try{
			    path = ngs.getPath(srcNodeIdForTBD, dstNodeIdForTBD);
			}catch(Exception e){
				e.printStackTrace();
			}
			
			List<String> returnPath = new ArrayList<String>();
			
			for (Link link: path) {
				String linkId = link.getLinkId().getValue();
				String srcTpId = link.getSource().getSourceTp().getValue();
				String dstTpId = link.getDestination().getDestTp().getValue();
				String value = "<" + linkId + "," + srcTpId + "," + dstTpId + ">";
				returnPath.add(value);
			}
			
			String lastTpId = this.mapleCore.getHost2swTable().get(dstIP).getId();
			
			pkt.setAction(new Route(returnPath, lastTpId));
		} else {
			//make the next maple app handle this pkt
			this.passToNext(pkt);
		}
	}
}

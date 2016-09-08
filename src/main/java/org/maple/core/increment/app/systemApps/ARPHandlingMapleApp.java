package org.maple.core.increment.app.systemApps;

import org.maple.core.increment.app.MapleAppBase;
import org.maple.core.increment.packet.Ethernet;
import org.maple.core.increment.tracetree.Action;
import org.maple.core.increment.tracetree.MaplePacket;
import org.opendaylight.yang.gen.v1.urn.opendaylight.inventory.rev130819.NodeConnectorRef;
import org.opendaylight.yang.gen.v1.urn.opendaylight.inventory.rev130819.NodeId;
import org.opendaylight.yang.gen.v1.urn.opendaylight.inventory.rev130819.nodes.Node;
import org.opendaylight.yang.gen.v1.urn.opendaylight.inventory.rev130819.nodes.NodeKey;
import org.opendaylight.yangtools.yang.binding.InstanceIdentifier;

import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by mingming on 9/8/16.
 */
public class ARPHandlingMapleApp extends MapleAppBase{
    private static Map<String, Set<String>> alreadySendForARP = new ConcurrentHashMap<String, Set<String>>();
    private static Date lastSeenArp = new Date();


    @Override
    public Action onPacket(MaplePacket pkt){
        if(pkt.ethType() == Ethernet.TYPE_ARP){

            Date currentDate = new Date();
            if(currentDate.getTime() - lastSeenArp.getTime() > 5000){ //Maximum speed of sending ARP packet is 1/s
                alreadySendForARP.clear();
                lastSeenArp = new Date();
            }

            NodeConnectorRef ingress;
            long srcMac = pkt.ethSrc();
            long dsyMac = pkt.ethDst();
         /* TODO  ingress = pkt.ingressPort();
            String node = ingress.getValue().firstIdentifierOf(Node.class)
                    .firstKeyOf(Node.class, NodeKey.class).getId().getValue(); */
            String node = "temp";
            String srcMacString = String.valueOf(srcMac);
            String dstMacString = String.valueOf(dsyMac);
            String key = srcMacString + " " + dstMacString;

            if(alreadySendForARP.containsKey(key)){
                if(alreadySendForARP.get(key).contains(node)) {//ingress
                    System.out.println("arp dropped");
                    return Action.Drop();
                } else {
                    alreadySendForARP.get(key).add(node);
                }
            }else{
                Set<String> tempSet = new HashSet<String>();
                alreadySendForARP.put(key, tempSet);
                tempSet.add(node);//ingress
            }
            System.out.println("arp flooded");
            return null;
         //   return Action.Flood();  TODO
        }else{
            return null;
        }
    }
}

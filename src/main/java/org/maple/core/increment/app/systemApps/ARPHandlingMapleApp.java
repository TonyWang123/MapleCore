/*
 * Copyright (c) 2016 SNLAB and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.maple.core.increment.app.systemApps;

import org.maple.core.increment.app.MapleAppBase;
import org.maple.core.increment.packet.Ethernet;
import org.maple.core.increment.tracetree.Action;
import org.maple.core.increment.tracetree.MaplePacket;
import org.maple.core.increment.tracetree.Port;
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
    private static Map<String, Set<Port>> alreadySendForARP = new ConcurrentHashMap<String, Set<Port>>();
    private static Date lastSeenArp = new Date();

    @Override
    public void onPacket(MaplePacket pkt){
        if(pkt.ethType() == Ethernet.TYPE_ARP){
        	
        	int srcIP = pkt.IPv4Src();
        	int dstIP = pkt.IPv4Dst();
        	
        	Port ingressPort = pkt.ingressPort();
        	
        	// put to host table
        	this.getMapleCore().getHost2swTable().put(srcIP, ingressPort);

            Date currentDate = new Date();
            if(currentDate.getTime() - lastSeenArp.getTime() > 5000){ //Maximum speed of sending ARP packet is 1/s
                alreadySendForARP.clear();
                lastSeenArp = new Date();
            }
            
            String key = String.valueOf(srcIP) + String.valueOf(dstIP);

            if(alreadySendForARP.containsKey(key)){
                if(alreadySendForARP.get(key).contains(ingressPort)) {//ingress
                    System.out.println("drop this dublicated arp");
                    pkt.setAction(Action.Drop());
                    return;
                } else {
                    alreadySendForARP.get(key).add(ingressPort);
                }
            }else{
                Set<Port> tempSet = new HashSet<Port>();
                alreadySendForARP.put(key, tempSet);
                tempSet.add(ingressPort);//ingress
            }
            System.out.println("arp flooded");
            pkt.setAction(Action.Flood());
        }else{
            pkt.setAction(Action.Drop());
        }
    }
}

/*
 * Copyright (c) 2016 SNLAB and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.maple.core.increment.tracetree;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.maple.core.increment.MapleCore;
import org.maple.core.increment.packet.Ethernet;
import org.maple.core.increment.packet.IPv4;
import org.maple.core.increment.packet.TCP;

public class MaplePacket {


    /** Temp constructor for unit test **/
    public MaplePacket(){
        this.frame = new Ethernet();
    }

    public Ethernet frame;
    private Port ingressPort;
    //private MapleCore mapleCore;
    //private Trace trace;
    
    public List<TraceItem> itemList = new LinkedList<TraceItem>();
    
    private Map<Match.Field, String> modifiedFieldValues = new HashMap<Match.Field, String>();

    private Action action;
    
    public MaplePacket(Ethernet frame, Port ingressPort) {
        this.frame = frame;
        //this.mapleCore = mapleCore;
        this.ingressPort = ingressPort;
        //this.trace = trace;
    }
    
    public void setEthSrc(long value) {
    	modifiedFieldValues.put(Match.Field.ETH_SRC, String.valueOf(value));
    }

    public final long ethSrc() {
    	if (modifiedFieldValues.containsKey(Match.Field.ETH_SRC)) {
    		return Long.parseLong(modifiedFieldValues.get(Match.Field.ETH_SRC));
    	}
        long addr = Ethernet.toLong(frame.getSourceMACAddress());
        /*VNode vNode = new VNode();
        vNode.field = Match.Field.ETH_SRC;
        vNode.value = String.valueOf(addr);
        trace.addNode(vNode, this, String.valueOf(addr), false);*/
        //mapleCore.traceAdd(TraceItemV.ethSrc(addr));
        TraceItem item = new TraceItem();
        item.setType("V");
        item.setField("ETH_SRC");
        item.setValue(String.valueOf(addr));
        this.itemList.add(item);
        return addr;
    }
    
    public void setEthDst(long value) {
    	modifiedFieldValues.put(Match.Field.ETH_DST, String.valueOf(value));
    }

    public final long ethDst() {
    	if (modifiedFieldValues.containsKey(Match.Field.ETH_DST)) {
    		return Long.parseLong(modifiedFieldValues.get(Match.Field.ETH_DST));
    	}
        long addr = Ethernet.toLong(frame.getDestinationMACAddress());
        /*VNode vNode = new VNode();
        vNode.field = Match.Field.ETH_DST;
        vNode.value = String.valueOf(addr);
        trace.addNode(vNode, this, String.valueOf(addr), false);*/
        //mapleCore.traceAdd(TraceItemV.ethDst(addr));
        TraceItem item = new TraceItem();
        item.setType("V");
        item.setField("ETH_DST");
        item.setValue(String.valueOf(addr));
        this.itemList.add(item);
        return addr;
    }
    
    public void setEthType(int value) {
    	modifiedFieldValues.put(Match.Field.ETH_TYPE, String.valueOf(value));
    }

    public final int ethType() {
    	if (modifiedFieldValues.containsKey(Match.Field.ETH_TYPE)) {
    		return Integer.parseInt(modifiedFieldValues.get(Match.Field.ETH_DST));
    	}
    	/*VNode vNode = new VNode();
    	vNode.field = Match.Field.ETH_TYPE;
    	vNode.value = String.valueOf(frame.getEtherType());
    	trace.addNode(vNode, this, String.valueOf(frame.getEtherType()), false);*/
        //mapleCore.traceAdd(TraceItemV.ethType(frame.getEtherType()));
    	TraceItem item = new TraceItem();
        item.setType("V");
        item.setField("ETH_TYPE");
        item.setValue(String.valueOf(frame.getEtherType()));
        this.itemList.add(item);
        return frame.getEtherType();
    }

    public final Port ingressPort() {
    	/*VNode vNode = new VNode();
    	vNode.field = Match.Field.IN_PORT;
        vNode.value = String.valueOf(ingressPort);
    	trace.addNode(vNode, this, String.valueOf(ingressPort), false);*/
        //mapleCore.traceAdd(TraceItemV.inPort(ingressPort));
    	TraceItem item = new TraceItem();
        item.setType("V");
        item.setField("IN_PORT");
        item.setValue(String.valueOf(ingressPort));
        this.itemList.add(item);
        return ingressPort;
    }

    public void setIPv4Src(int value) {
    	modifiedFieldValues.put(Match.Field.IPv4_SRC, String.valueOf(value));
    }
    
    public final int IPv4Src() {
    	if (modifiedFieldValues.containsKey(Match.Field.IPv4_SRC)) {
    		return Integer.parseInt(modifiedFieldValues.get(Match.Field.IPv4_SRC));
    	}
    	IPv4 pIP = (IPv4) frame.getPayload();
    	/*VNode vNode = new VNode();
    	vNode.field = Match.Field.IPv4_SRC;
    	vNode.value = String.valueOf(pIP.getSourceAddress());
    	trace.addNode(vNode, this, String.valueOf(pIP.getSourceAddress()), false);*/
    	//mapleCore.traceAdd(TraceItemV.IP4Src(pIP.getSourceAddress()));
    	TraceItem item = new TraceItem();
        item.setType("V");
        item.setField("IPv4_SRC");
        item.setValue(String.valueOf(pIP.getSourceAddress()));
        this.itemList.add(item);
    	return pIP.getSourceAddress();
    }
    
    public void setIPv4Dst(int value) {
    	modifiedFieldValues.put(Match.Field.IPv4_DST, String.valueOf(value));
    }

    public final int IPv4Dst() {
    	if (modifiedFieldValues.containsKey(Match.Field.IPv4_DST)) {
    		return Integer.parseInt(modifiedFieldValues.get(Match.Field.IPv4_DST));
    	}
    	IPv4 pIP = (IPv4) frame.getPayload();
    	/*VNode vNode = new VNode();
    	vNode.field = Match.Field.IPv4_DST;
    	vNode.value = String.valueOf(pIP.getDestinationAddress());
    	trace.addNode(vNode, this, String.valueOf(pIP.getDestinationAddress()), false);*/
    	//mapleCore.traceAdd(TraceItemV.IP4Dst(pIP.getDestinationAddress()));
    	TraceItem item = new TraceItem();
        item.setType("V");
        item.setField("IPv4_DST");
        item.setValue(String.valueOf(pIP.getDestinationAddress()));
        this.itemList.add(item);
    	return pIP.getDestinationAddress();
    }
    
    public void setTCPSrcPort(int value) {
    	modifiedFieldValues.put(Match.Field.TCP_SRC_PORT, String.valueOf(value));
    }

    public final int TCPSrcPort() {
    	if (modifiedFieldValues.containsKey(Match.Field.TCP_SRC_PORT)) {
    		return Integer.parseInt(modifiedFieldValues.get(Match.Field.TCP_SRC_PORT));
    	}
    	IPv4 pIP = (IPv4) frame.getPayload();
    	TCP pTCP = (TCP) pIP.getPayload();
    	/*VNode vNode = new VNode();
    	vNode.field = Match.Field.TCP_SRC_PORT;
    	vNode.value = String.valueOf(pTCP.getSourcePort());
    	trace.addNode(vNode, this, String.valueOf(pTCP.getSourcePort()), false);*/
        //mapleCore.traceAdd(TraceItemV.TCP_SRC_PORT(pTCP.getSourcePort()));
    	TraceItem item = new TraceItem();
        item.setType("V");
        item.setField("TCP_SRC_PORT");
        item.setValue(String.valueOf(pTCP.getSourcePort()));
        this.itemList.add(item);
        return pTCP.getSourcePort();
    }
    
    public void setTCPDstPort(int value) {
    	modifiedFieldValues.put(Match.Field.TCP_DST_PORT, String.valueOf(value));
    }

    public final int TCPDstPort() {
    	if (modifiedFieldValues.containsKey(Match.Field.TCP_DST_PORT)) {
    		return Integer.parseInt(modifiedFieldValues.get(Match.Field.TCP_DST_PORT));
    	}
    	IPv4 pIP = (IPv4) frame.getPayload();
    	TCP pTCP = (TCP) pIP.getPayload();
    	/*VNode vNode = new VNode();
    	vNode.field = Match.Field.TCP_DST_PORT;
    	vNode.value = String.valueOf(pTCP.getDestinationPort());
    	trace.addNode(vNode, this, String.valueOf(pTCP.getDestinationPort()), false);*/
        //mapleCore.traceAdd(TraceItemV.TCP_DST_PORT(pTCP.getDestinationPort()));
    	TraceItem item = new TraceItem();
        item.setType("V");
        item.setField("TCP_DST_PORT");
        item.setValue(String.valueOf(pTCP.getDestinationPort()));
        this.itemList.add(item);
        return pTCP.getDestinationPort();
    }

    public final boolean ethSrcIs(long exp) {
        long addr = Ethernet.toLong(frame.getSourceMACAddress());
        
        if (modifiedFieldValues.containsKey(Match.Field.ETH_SRC)) {
        	addr = Long.parseLong(modifiedFieldValues.get(Match.Field.ETH_SRC));
        }
        /*TNode tNode = new TNode();
        tNode.field = Match.Field.ETH_SRC;
        tNode.value = String.valueOf(exp);
        trace.addNode(tNode, this, null, addr == exp);*/
        //mapleCore.traceAdd(TraceItemT.ethSrcIs(addr, exp));
        TraceItem item = new TraceItem();
        item.setType("T");
        item.setField("ETH_SRC");
        item.setValue(String.valueOf(exp));
        item.setBranch(addr == exp?"1":"0");
        this.itemList.add(item);
        return (addr == exp);
    }

    public final boolean ethDstIs(long exp) {
        long addr = Ethernet.toLong(frame.getDestinationMACAddress());
        
        if (modifiedFieldValues.containsKey(Match.Field.ETH_DST)) {
        	addr = Long.parseLong(modifiedFieldValues.get(Match.Field.ETH_DST));
        }
        /*TNode tNode = new TNode();
        tNode.field = Match.Field.ETH_DST;
        tNode.value = String.valueOf(exp);
        trace.addNode(tNode, this, null, addr == exp);*/
        //mapleCore.traceAdd(TraceItemT.ethDstIs(addr, exp));
        TraceItem item = new TraceItem();
        item.setType("T");
        item.setField("ETH_DST");
        item.setValue(String.valueOf(exp));
        item.setBranch(addr == exp?"1":"0");
        this.itemList.add(item);
        return (addr == exp);
    }

    public final boolean ethTypeIs(int exp) {
    	int ethType = frame.getEtherType();
    	if (modifiedFieldValues.containsKey(Match.Field.ETH_TYPE)) {
    		ethType = Integer.parseInt(modifiedFieldValues.get(Match.Field.ETH_TYPE));
        }
    	/*TNode tNode = new TNode();
        tNode.field = Match.Field.ETH_TYPE;
        tNode.value = String.valueOf(exp);
        trace.addNode(tNode, this, null, frame.getEtherType() == exp);*/
        //mapleCore.traceAdd(TraceItemT.ethTypeIs(frame.getEtherType(), exp));
        TraceItem item = new TraceItem();
        item.setType("T");
        item.setField("ETH_TYPE");
        item.setValue(String.valueOf(exp));
        item.setBranch(ethType == exp?"1":"0");
        this.itemList.add(item);
        return (ethType == exp);
    }

    public final boolean ingressPortIs(Port exp) {
    	/*TNode tNode = new TNode();
        tNode.field = Match.Field.IN_PORT;
        tNode.value = String.valueOf(exp);
        trace.addNode(tNode, this, null, ingressPort == exp);*/
        //mapleCore.traceAdd(TraceItemT.inPortIs(ingressPort, exp));
        TraceItem item = new TraceItem();
        item.setType("T");
        item.setField("IN_PORT");
        item.setValue(String.valueOf(exp));
        item.setBranch(ingressPort == exp?"1":"0");
        this.itemList.add(item);
        return (ingressPort == exp);
    }
    
    public final boolean IPv4SrcIs(int exp) {
    	IPv4 ipv4 = (IPv4)frame.getPayload();
    	int addr = ipv4.getSourceAddress();
    	
    	if (this.modifiedFieldValues.containsKey(Match.Field.IPv4_SRC)) {
    		addr = Integer.parseInt(this.modifiedFieldValues.get(Match.Field.IPv4_SRC));
    	}
    	
    	TraceItem item = new TraceItem();
        item.setType("T");
        item.setField("IPv4_SRC");
        item.setValue(String.valueOf(addr));
        item.setBranch(addr == exp?"1":"0");
        this.itemList.add(item);
        return (addr == exp);
    }
    
    public final boolean IPv4DstIs(int exp) {
    	IPv4 ipv4 = (IPv4)frame.getPayload();
    	int addr = ipv4.getDestinationAddress();
    	
    	if (this.modifiedFieldValues.containsKey(Match.Field.IPv4_DST)) {
    		addr = Integer.parseInt(this.modifiedFieldValues.get(Match.Field.IPv4_DST));
    	}
    	
    	TraceItem item = new TraceItem();
        item.setType("T");
        item.setField("IPv4_DST");
        item.setValue(String.valueOf(addr));
        item.setBranch(addr == exp?"1":"0");
        this.itemList.add(item);
        return (addr == exp);
    }

    public Map<Match.Field, String> getModifiedFieldValues() {
		return modifiedFieldValues;
	}

	public void setModifiedFieldValues(Map<Match.Field, String> modifiedFieldValues) {
		this.modifiedFieldValues = modifiedFieldValues;
	}

	public Action getAction() {
		return action;
	}

	public void setAction(Action action) {
		this.action = action;
	}

	@Override
    public String toString() {
        return "MaplePacket [ingressPort=" + ingressPort + ", frame=" + frame + "]";
    }

}

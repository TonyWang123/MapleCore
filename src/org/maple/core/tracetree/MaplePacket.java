/*
 * Copyright (c) 2016 SNLAB and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.maple.core.tracetree;

import java.util.LinkedList;

import org.maple.core.MapleCore;
import org.maple.core.packet.Ethernet;
import org.maple.core.packet.IPv4;
import org.maple.core.packet.TCP;
import org.maple.core.tracetree.TraceItem;
import org.maple.core.tracetree.TraceItemT;
import org.maple.core.tracetree.TraceItemV;

public class MaplePacket {


    /** Temp constructor for unit test **/
    public MaplePacket(){
        this.frame = new Ethernet();
    }

    public Ethernet frame;
    private Port ingressPort;
    private MapleCore mapleCore;

    public MaplePacket(Ethernet frame, Port ingressPort, MapleCore mapleCore, Trace trace) {
        this.frame = frame;
        this.mapleCore = mapleCore;
        this.ingressPort = ingressPort;
    }

    public final long ethSrc() {
        long addr = Ethernet.toLong(frame.getSourceMACAddress());
        mapleCore.traceAdd(TraceItemV.ethSrc(addr));
        return addr;
    }

    public final long ethDst() {
        long addr = Ethernet.toLong(frame.getDestinationMACAddress());
        mapleCore.traceAdd(TraceItemV.ethDst(addr));
        return addr;
    }

    public final int ethType() {
        mapleCore.traceAdd(TraceItemV.ethType(frame.getEtherType()));
        return frame.getEtherType();
    }

    public final Port ingressPort() {
        mapleCore.traceAdd(TraceItemV.inPort(ingressPort));
        return ingressPort;
    }

    public final int IPv4Src() {
    	IPv4 pIP = (IPv4) frame.getPayload();
    	mapleCore.traceAdd(TraceItemV.IP4Src(pIP.getSourceAddress()));
    	return pIP.getSourceAddress();
    }

    public final int IPv4Dst() {
    	IPv4 pIP = (IPv4) frame.getPayload();
    	mapleCore.traceAdd(TraceItemV.IP4Dst(pIP.getDestinationAddress()));
    	return pIP.getDestinationAddress();
    }

    public final int TCPSrcPort() {
    	IPv4 pIP = (IPv4) frame.getPayload();
    	TCP pTCP = (TCP) pIP.getPayload();
        mapleCore.traceAdd(TraceItemV.TCP_SRC_PORT(pTCP.getSourcePort()));
        return pTCP.getSourcePort();
    }

    public final int TCPDstPort() {
    	IPv4 pIP = (IPv4) frame.getPayload();
    	TCP pTCP = (TCP) pIP.getPayload();
        mapleCore.traceAdd(TraceItemV.TCP_DST_PORT(pTCP.getDestinationPort()));
        return pTCP.getDestinationPort();
    }

    public final boolean ethSrcIs(long exp) {
        long addr = Ethernet.toLong(frame.getSourceMACAddress());
        mapleCore.traceAdd(TraceItemT.ethSrcIs(addr, exp));
        return (addr == exp);
    }

    public final boolean ethDstIs(long exp) {
        long addr = Ethernet.toLong(frame.getDestinationMACAddress());
        mapleCore.traceAdd(TraceItemT.ethDstIs(addr, exp));
        return (addr == exp);
    }

    public final boolean ethTypeIs(int exp) {
        mapleCore.traceAdd(TraceItemT.ethTypeIs(frame.getEtherType(), exp));
        return (frame.getEtherType() == exp);
    }

    public final boolean ingressPortIs(Port exp) {
        mapleCore.traceAdd(TraceItemT.inPortIs(ingressPort, exp));
        return (ingressPort == exp);
    }

    @Override
    public String toString() {
        return "MaplePacket [ingressPort=" + ingressPort + ", frame=" + frame + "]";
    }

}

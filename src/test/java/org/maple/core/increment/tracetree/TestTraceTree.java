/*
 * Copyright (c) 2016 SNLAB and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.maple.core.increment.tracetree;

import junit.framework.TestCase;

public class TestTraceTree extends TestCase{

	public void testTraceTree(){
		TraceTree tt = new TraceTree();
		
		MaplePacket pkt1 = new MaplePacket();
		
		String pkt1Hash = "pkt1Hash";
		
		TNode tNode1 = new TNode();
		tNode1.field = Match.Field.ETH_TYPE;
		tNode1.value = "arp";
		VNode vNode1 = new VNode();
		vNode1.field = Match.Field.ETH_DST;
		LNode lNode1 = new LNode();
		lNode1.action = "port:1";
		
		tNode1.subtree[0] = vNode1;//false
		vNode1.subtree.put("mac:1", lNode1);
		tNode1.pkt2nextNodeinTrace.put(pkt1Hash, vNode1);
		vNode1.pkt2fatherinTrace.put(pkt1Hash, tNode1);
		vNode1.pkt2nextNodeinTrace.put(pkt1Hash, lNode1);
		lNode1.pkt2fatherinTrace.put(pkt1Hash, vNode1);
		
		Trace trace1 = new Trace();
		trace1.firstNode = tNode1;
		
		
		
		MaplePacket pkt2 = new MaplePacket();
		
		String pkt2Hash = "pkt2Hash";
		
		TNode tNode2 = new TNode();
		tNode2.field = Match.Field.ETH_TYPE;
		tNode2.value = "arp";
		VNode vNode2 = new VNode();
		vNode2.field = Match.Field.ETH_DST;
		LNode lNode2 = new LNode();
		lNode2.action = "port:2";
		
		tNode2.subtree[1] = vNode2;//true
		vNode2.subtree.put("mac:2", lNode2);
		tNode2.pkt2nextNodeinTrace.put(pkt2Hash, vNode2);
		vNode2.pkt2fatherinTrace.put(pkt2Hash, tNode2);
		vNode2.pkt2nextNodeinTrace.put(pkt2Hash, lNode2);
		lNode2.pkt2fatherinTrace.put(pkt2Hash, vNode2);
		
		Trace trace2 = new Trace();
		trace2.firstNode = tNode2;
		
		tt.updateTT(pkt1Hash, trace1);
		
		assertTrue(tt.root.priority == 1);
		
		tt.updateTT(pkt2Hash, trace2);
		
		assertTrue(tt.root instanceof TNode);
		
		TNode tNode = (TNode)tt.root;
		
		assertTrue(tNode.getChild(true).priority == 2);
	}
}

/*
 * Copyright (c) 2016 SNLAB and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.maple.core.increment;

import org.maple.core.increment.tracetree.Node;
import org.maple.core.increment.tracetree.Trace;
import org.maple.core.increment.tracetree.TraceItem;
import org.maple.core.increment.tracetree.VNode;

import junit.framework.TestCase;

public class TestMapleCore extends TestCase{

	public void testTrace() {
		
		Trace trace = new Trace();
		
		TraceItem t1 = new TraceItem();
		t1.setType("T");
		t1.setField("f1");
		t1.setBranch("1");
		
		trace.addTraceItem(t1, "pktHash1");
		
		TraceItem t2 = new TraceItem();
		t2.setType("V");
		t2.setField("f2");
		t2.setValue("v2");
		
		trace.addTraceItem(t2, "pktHash1");
		
		TraceItem t3 = new TraceItem();
		t3.setType("L");
		t3.setAction("a3");
		
		trace.addTraceItem(t3, "pktHash1");
		
		Node node2 = trace.firstNode.pkt2nextNodeinTrace.get("pktHash1");
		
		assertTrue(node2 instanceof VNode);
		
		VNode vNode = (VNode)node2;
		
		assertTrue(vNode.subtree.containsKey("v2"));
	}
}

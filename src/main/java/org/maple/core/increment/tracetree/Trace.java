/*
 * Copyright (c) 2016 SNLAB and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.maple.core.increment.tracetree;

public class Trace {

	public Node firstNode;
	
	public Node lastNode;
	
	String nextPositionString;
	
	int nextPositionInt;
	
	public void addNode(Node node, String pktHash, String value, boolean isTrue){
		if(firstNode == null){
			firstNode = node;
			lastNode = node;
			nextPositionString = value;
			nextPositionInt = isTrue?1:0;//true is 1; false is 0
		}else{
			lastNode.pkt2nextNodeinTrace.put(pktHash, node);
			node.pkt2fatherinTrace.put(pktHash, lastNode);
			//connect to TT
			if(lastNode instanceof VNode){
				System.out.println("set subtree of vnode" + nextPositionString);
				VNode vNode = (VNode)lastNode;
				vNode.subtree.put(nextPositionString, node);
			}else if(lastNode instanceof TNode){
				TNode tNode = (TNode)lastNode;
				tNode.subtree[nextPositionInt] = node;
			}else{
				//wrong
			}
			lastNode = node;
			nextPositionString = value;
			nextPositionInt = isTrue?1:0;
		}
	}
	
	public void addTraceItem(TraceItem traceItem, String pktHash){
		if (traceItem.type.equals("T")) {
			TNode tNode = new TNode();
			tNode.field = Match.toMatchField(traceItem.field);
			tNode.value = traceItem.value;
			this.addNode(tNode, pktHash, traceItem.value, traceItem.branch == "1"?true:false);
		}else if (traceItem.type.equals("V")) {
			VNode vNode = new VNode();
			vNode.field = Match.toMatchField(traceItem.field);
			vNode.value = traceItem.value;
			this.addNode(vNode, pktHash, traceItem.value, false);
		}else {
			System.out.println("LNode: " + traceItem.action);
			LNode lNode = new LNode();
			lNode.action = traceItem.action;
			this.addNode(lNode, pktHash, null, false);
		}
	}
	
	/*
	 * trace -> rules and store them at nodes (TNode, LNode)
	 * rule = match + action
	 * */
	public void generateAndStoreRules(String pktHash){
		Node node = this.firstNode;
		Match tempMatch = new Match();
		while(node.pkt2nextNodeinTrace.get(pktHash) != null){
			if(node instanceof VNode){
				VNode vNode = (VNode)node;
				Match.Field fieldNum = vNode.field;
				String value = vNode.value;
				tempMatch.fields.put(fieldNum, value);
			}else if(node instanceof TNode){
				TNode tNode = (TNode)node;
				Match.Field fieldNum = tNode.field;
				String value = tNode.value;
				//check to generate a barrier rule for false branch
				if(tNode.getChild(false) != null){
					//has false branch
					Match match = new Match();
					match.fields.putAll(tempMatch.fields);
					Rule rule = new Rule(match, Action.Punt());
					tNode.rule = rule;
				}else{
					//has true branch
					tempMatch.fields.put(fieldNum, value);
				}
			}
		}
		//handle LNode
		if (node instanceof LNode) {
			LNode lNode = (LNode)node;
			Rule rule;
			if (lNode.action.equals("drop")) {
				rule = new Rule(tempMatch, Action.Drop());
			} else if (lNode.action.equals("punt")) {
				rule = new Rule(tempMatch, Action.Punt());
			} else {
				//Route
				rule = new Rule(tempMatch, new Route(lNode.action));
			}
			lNode.rule = rule;
		} else {
			//wrong
		}
	}
	
	public void show(String pktHash) {
		System.out.println("show trace:");
		Node node = firstNode;
		while (node !=  null) {
			System.out.println(node.toString());
			node = node.pkt2nextNodeinTrace.get(pktHash);
		}
	}
}

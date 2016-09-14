/*
 * Copyright (c) 2016 SNLAB and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.maple.core.increment.tracetree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class TraceTree {

	public Node root;
	
	private HashMap<String, Trace> pkt2trace = new HashMap<String, Trace>();
	
	//srcNode, dstNode, indicate weight = 1
	public Map<Node, List<Node>> edgeWithOneWeightInOrderGraph = new HashMap<Node, List<Node>>();
	
	public Map<Node, List<Node>> fatherNode2ChildNodesInOrderGraph = new HashMap<Node, List<Node>>();
	
	public void updateTT(String pktHash, Trace tNew){
		if(pkt2trace.containsKey(pktHash)){
			//update trace
			Trace tOld = pkt2trace.get(pktHash);
			deleteTrace(pktHash, tOld);
		}
		initializeOrderGraphOfTrace(tNew, pktHash);
		addTrace(pktHash, tNew);//this step modifies the trace's first node by merging
		tNew.show(pktHash);
		traverseTTtoUpdatePriority2(tNew.firstNode, pktHash, root);
	}
	
	public void updateTTMultiple(Map<String, Trace> pkt2TraceInput) {
		for (Map.Entry<String, Trace> entry: pkt2TraceInput.entrySet()){
			String pktHash = entry.getKey();
			Trace trace = entry.getValue();
			if (pkt2trace.containsKey(pktHash)) {
				Trace tOld = pkt2trace.get(pktHash);
				deleteTrace(pktHash, tOld);
			}
			initializeOrderGraphOfTrace(trace, pktHash);
			addTrace(pktHash, trace);//this step modifies the trace's first node by merging
		}
		traverseTTtoUpdatePriority3(root);
	}
	
	private void deleteTrace(String pktHash, Trace trace){
	    Node node = trace.firstNode;
	    List<Node> allTNodesWithFalseBranch = new LinkedList<Node>();
	    while(node != null){
	    	if(node instanceof TNode){
	    		TNode tNode = (TNode)node;
	    		if (tNode.getChild(false) != null) {
	    			allTNodesWithFalseBranch.add(node);
	    		}
	    	}
	    	node.count--;
	    	if(node.count == 0){
	    		//should remove this node
	    		if(node.pkt2fatherinTrace.get(pktHash) != null){
	    			node.pkt2fatherinTrace.get(pktHash).pkt2nextNodeinTrace.remove(pktHash);
	    			node.pkt2fatherinTrace.get(pktHash).delete(node);
	    		}else{
	    			//root
	    			this.root = null;
	    		}
	    		//handle map
    			this.removeNodeFromFatherNode2ChildNodesInOrderGraph(node);
    			
    			
    			//handle edge
    			this.removeNodeFromEdgeWithOneWeightInOrderGraph(node);
    			
	    		if(node instanceof TNode){
	    			TNode tNode = (TNode) node;
	    			if(tNode.rule != null){
	    				deleteRule(tNode.priority, tNode.rule);
	    			}
	    		}
	    	}
	    	if(node.pkt2nextNodeinTrace.get(pktHash) == null){
	    		//this should be leaf node
	    		deleteRule(((LNode)node).priority, ((LNode)node).rule);
	    		for(Node tNode: allTNodesWithFalseBranch){
	    			tNode.fatherNodesinOrderGraph.remove(node);
	    			//handle map
	    			this.removeNodeFromFatherNode2ChildNodesInOrderGraph(node);
	    			
	    			//handle edge, only up link with weight = 1
	    			this.removeNodeFromEdgeWithOneWeightInOrderGraph(node);
	    		}
	    	}
	    	node = node.pkt2nextNodeinTrace.get(pktHash);
	    	
	    }
	    pkt2trace.remove(pktHash);
	}
	
	private void removeNodeFromEdgeWithOneWeightInOrderGraph(Node node) {
		if(this.edgeWithOneWeightInOrderGraph.containsKey(node)){
			this.edgeWithOneWeightInOrderGraph.remove(node);
		}
		for(Map.Entry<Node, List<Node>> entry: this.edgeWithOneWeightInOrderGraph.entrySet()){
			List<Node> childs = entry.getValue();
			if(childs.contains(node)){
				childs.remove(node);
			}
		}
	}
	
	private void removeNodeFromFatherNode2ChildNodesInOrderGraph(Node node) {
		if(this.fatherNode2ChildNodesInOrderGraph.containsKey(node)){
			this.fatherNode2ChildNodesInOrderGraph.remove(node);
		}
		for(Map.Entry<Node, List<Node>> entry: this.fatherNode2ChildNodesInOrderGraph.entrySet()){
			List<Node> childs = entry.getValue();
			if(childs.contains(node)){
				childs.remove(node);
			}
		}
	}
	
	public void deleteRule(int priority, Rule rule){
		
	}
	
	private void addTrace(String pktHash, Trace trace){
		addCount(trace, pktHash);
		if(root == null){
			root = trace.firstNode;
		}else{
			root.augment(trace.firstNode, pktHash, trace, this);
		}
		pkt2trace.put(pktHash, trace);
	}
	
	private void addCount(Trace trace, String pktHash){
		Node node = trace.firstNode;
		while(node != null){
			node.count++;
			node = node.pkt2nextNodeinTrace.get(pktHash);
		}
	}
	
	private void addEntryToFatherNode2ChildNodesInOrderGraph(Node father, Node child){
		if(!this.fatherNode2ChildNodesInOrderGraph.containsKey(father)){
		    List<Node> childs = new LinkedList<Node>();
		    childs.add(child);
		    this.fatherNode2ChildNodesInOrderGraph.put(father, childs);
		}else{
			this.fatherNode2ChildNodesInOrderGraph.get(father).add(child);
		}
	}
	
	//src -> dst
	private void addEntryToeEdgeWithOneWeightInOrderGraph(Node src, Node dst) {
		if(!this.edgeWithOneWeightInOrderGraph.containsKey(src)){
			List<Node> nodes = new ArrayList<Node>();
			nodes.add(dst);
			this.edgeWithOneWeightInOrderGraph.put(src, nodes);
		}else{
			this.edgeWithOneWeightInOrderGraph.get(src).add(dst);
		}
	}
	
	//setup order graph
	private void initializeOrderGraphOfTrace(Trace trace, String pktHash){
		Node node = trace.firstNode;
		List<Node> allTNodesWithFalseBranch = new LinkedList<Node>();
		while(node.pkt2nextNodeinTrace.get(pktHash) != null){
			Node nextNode = node.pkt2nextNodeinTrace.get(pktHash);
			if(node instanceof TNode){
				//allTNodes.add(node);//all tNodes
				TNode tNode = (TNode)node;
				if(tNode.getChild(false) != null){
					//this is false branch, generates a rule
					//only up link
					//weight = 1
					for(Node tNodeWithFalseBranch: allTNodesWithFalseBranch){
						tNodeWithFalseBranch.fatherNodesinOrderGraph.add(node);
						
						//handle map
						this.addEntryToFatherNode2ChildNodesInOrderGraph(node, tNodeWithFalseBranch);
						
						//handle edge
						this.addEntryToeEdgeWithOneWeightInOrderGraph(node, tNodeWithFalseBranch);
						
					}
					//add
					allTNodesWithFalseBranch.add(tNode);
				}else{
					//this is true branch
					nextNode.fatherNodesinOrderGraph.add(node);
					
					//handle map
					this.addEntryToFatherNode2ChildNodesInOrderGraph(node, nextNode);
					
					//handle edge
					this.addEntryToeEdgeWithOneWeightInOrderGraph(node, nextNode);
				}
			}else if(node instanceof VNode){
				nextNode.fatherNodesinOrderGraph.add(node);
				
				//handle map
				this.addEntryToFatherNode2ChildNodesInOrderGraph(node, nextNode);
			}
			node = node.pkt2nextNodeinTrace.get(pktHash);
		}
		//handle LNode
		for(Node tNode: allTNodesWithFalseBranch){
			tNode.fatherNodesinOrderGraph.add(node);
			
			//handle map
			this.addEntryToFatherNode2ChildNodesInOrderGraph(node, tNode);
			
			//handle edge
			this.addEntryToeEdgeWithOneWeightInOrderGraph(node, tNode);
		}
	}
	
	private void traverseTTtoUpdatePriority3(Node root){
		if (root instanceof TNode) {
			TNode tNode = (TNode)root;
			if (tNode.getChild(false) != null) {
				traverseTTtoUpdatePriority3(tNode.getChild(false));
			}
			if (updatePriority(root)) {
				root.ruleChanged = true; //TODO: also need to add at install rules and delete rules
			}
			if (tNode.getChild(true) != null) {
				traverseTTtoUpdatePriority3(tNode.getChild(true));
			}
		} else if (root instanceof VNode) {
			if (updatePriority(root)) {
				//priority changed
				root.ruleChanged = true; //TODO: also need to add at install rules and delete rules
			}
			VNode vNodeRoot = (VNode)root;
			for (Map.Entry<String, Node> entry: vNodeRoot.subtree.entrySet()) {
				String value = entry.getKey();
				Node childNode = entry.getValue();
				traverseTTtoUpdatePriority3(childNode);
			}
		} else {
			if (updatePriority(root)) {
				//priority changed
				root.ruleChanged = true; //TODO: also need to add at install rules and delete rules
			}
		}
	}
	
	private void traverseTTtoUpdatePriority2(Node currentNodeInTrace, String pktHash, Node root){
		Node nextNodeInTrace = null;
		if(currentNodeInTrace == null) {
			System.out.println("get null error in traverse");
		}else {
			nextNodeInTrace = currentNodeInTrace.pkt2nextNodeinTrace.get(pktHash);
			System.out.println("node in traverse: " + currentNodeInTrace.toString());
		}
		
		
		
		if(root instanceof TNode){
			TNode tNodeRoot = (TNode)root;
			if(tNodeRoot.getChild(false) != null){
				if(tNodeRoot.getChild(false).equals(nextNodeInTrace)){
					//should traverse, in false branch
					traverseTTtoUpdatePriority2(nextNodeInTrace, pktHash, tNodeRoot.getChild(false));
				}
			}
			if(updatePriority(root)){
				//priority changed
				root.ruleChanged = true;//TODO: also need to add at install rules and delete rules
			}
			if(tNodeRoot.getChild(true) != null){
				traverseTTtoUpdatePriority2(nextNodeInTrace, pktHash, tNodeRoot.getChild(true));
			}
		}else if(root instanceof VNode){
			if(updatePriority(root)){
				//priority changed
				root.ruleChanged = true;//TODO: also need to add at install rules and delete rules
			}
			VNode vNodeRoot = (VNode)root;
			for(Map.Entry<String, Node> entry: vNodeRoot.subtree.entrySet()){
				String value = entry.getKey();
				Node childNode = entry.getValue();
				traverseTTtoUpdatePriority2(nextNodeInTrace, pktHash, childNode);
			}
		}else{
			//LNode
			if(updatePriority(root)){
				//priority changed
				root.ruleChanged = true;//TODO: also need to add at install rules and delete rules
			}
		}
	}
	
	private boolean updatePriority(Node node){
		boolean isChanged = false;
		int oldPriority = node.priority;
		int max = Integer.MIN_VALUE;
		for(Node srcNode: node.fatherNodesinOrderGraph){
			int weight = 0;
			if(this.edgeWithOneWeightInOrderGraph.containsKey(srcNode)){
				if(this.edgeWithOneWeightInOrderGraph.get(srcNode).contains(node)){
					weight = 1;
				}
			}
			int temp = weight + srcNode.priority;
			if(max < temp)max = temp;
		}
		if(max < node.priority)max = node.priority;
		node.priority = max;
		if(max != oldPriority)isChanged = true;
		return isChanged;
	}
	
	public void show(Node root){
		System.out.println(root.toString() + ", priority: " + root.priority);
		if(root instanceof TNode){
			TNode tNode = (TNode)root;
			
			if(tNode.getChild(false) != null){
				show(tNode.getChild(false));
			}
			if(tNode.getChild(true) != null){
				show(tNode.getChild(true));
			}
		}else if(root instanceof VNode){
			VNode vNode = (VNode)root;
			for(Map.Entry<String, Node> entry: vNode.subtree.entrySet()){
				show(entry.getValue());
			}
		}else{
			return;
		}
	}
	
	public static void main(String[] args) {
		TraceTree tt = new TraceTree();
		
		String pkt1Hash = "pkt1Hash";
		
		TNode tNode1 = new TNode();
		tNode1.field = Match.Field.ETH_TYPE;
		tNode1.value = "arp";
		LNode lNode1 = new LNode();
		lNode1.action = "port:1";
		
		tNode1.subtree[0] = lNode1;//false
		tNode1.pkt2nextNodeinTrace.put(pkt1Hash, lNode1);
		lNode1.pkt2fatherinTrace.put(pkt1Hash, tNode1);
		
		Trace trace1 = new Trace();
		trace1.firstNode = tNode1;
		
		
		String pkt2Hash = "pkt1Hash";
		
		TNode tNode2 = new TNode();
		tNode2.field = Match.Field.ETH_TYPE;
		tNode2.value = "arp";
		LNode lNode2 = new LNode();
		lNode2.action = "port:1";
		
		tNode2.subtree[0] = lNode2;//false
		tNode2.pkt2nextNodeinTrace.put(pkt1Hash, lNode2);
		lNode2.pkt2fatherinTrace.put(pkt1Hash, tNode2);
		
		Trace trace2 = new Trace();
		trace2.firstNode = tNode2;
		
		tt.updateTT(pkt1Hash, trace1);
		tt.updateTT(pkt1Hash, trace2);
	}
}

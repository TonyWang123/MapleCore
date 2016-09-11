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
	    List<Node> allTNodes = new LinkedList<Node>();
	    while(node != null){
	    	if(node instanceof TNode){
	    		allTNodes.add(node);
	    	}
	    	node.count--;
	    	if(node.count == 0){
	    		//should remove this node
	    		if(node.pkt2fatherinTrace.get(pktHash) != null){
	    			node.pkt2fatherinTrace.get(pktHash).delete(node);
	    		}else{
	    			//root
	    			this.root = null;
	    		}
	    		//handle map
    			if(this.fatherNode2ChildNodesInOrderGraph.containsKey(node)){
    				this.fatherNode2ChildNodesInOrderGraph.remove(node);
    			}
    			for(Map.Entry<Node, List<Node>> entry: this.fatherNode2ChildNodesInOrderGraph.entrySet()){
    				List<Node> childs = entry.getValue();
    				if(childs.contains(node)){
    					childs.remove(node);
    				}
    			}
    			//handle edge
    			if(this.edgeWithOneWeightInOrderGraph.containsKey(node)){
    				this.edgeWithOneWeightInOrderGraph.remove(node);
    			}
    			for(Map.Entry<Node, List<Node>> entry: this.edgeWithOneWeightInOrderGraph.entrySet()){
    				List<Node> childs = entry.getValue();
    				if(childs.contains(node)){
    					childs.remove(node);
    				}
    			}
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
	    		for(Node tNode: allTNodes){
	    			tNode.fatherNodesinOrderGraph.remove(node);
	    			//handle map
	    			if(this.fatherNode2ChildNodesInOrderGraph.containsKey(node)){
	    				this.fatherNode2ChildNodesInOrderGraph.remove(node);
	    			}
	    		}
	    	}
	    	node = node.pkt2nextNodeinTrace.get(pktHash);
	    	
	    }
	    pkt2trace.remove(pktHash);
	}
	
	public void deleteRule(int priority, Rule rule){
		
	}
	
	private void addTrace(String pktHash, Trace trace){
		addCount(trace, pktHash);
		if(root == null){
			root = trace.firstNode;
		}else{
			if(trace.firstNode instanceof TNode){
				System.out.println("get tnode");
			}
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
	
	private void removeEntryFromFatherNode2ChildNodesInOrderGraph(Node father, Node child){
		if(this.fatherNode2ChildNodesInOrderGraph.containsKey(father)){
			this.fatherNode2ChildNodesInOrderGraph.get(father).remove(child);
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
						
						if(!this.edgeWithOneWeightInOrderGraph.containsKey(node)){
							List<Node> nodes = new ArrayList<Node>();
							nodes.add(tNodeWithFalseBranch);
							this.edgeWithOneWeightInOrderGraph.put(node, nodes);
						}else{
							this.edgeWithOneWeightInOrderGraph.get(node).add(tNodeWithFalseBranch);
						}
						//this.edgeWithOneWeightInOrderGraph.put(node, tNode);
					}
					//add
					allTNodesWithFalseBranch.add(tNode);
				}else{
					//this is true branch
					nextNode.fatherNodesinOrderGraph.add(node);
					
					//handle map
					this.addEntryToFatherNode2ChildNodesInOrderGraph(node, nextNode);
					
					//weight = 1
					if(!this.edgeWithOneWeightInOrderGraph.containsKey(node)){
						List<Node> nodes = new ArrayList<Node>();
						nodes.add(nextNode);
						this.edgeWithOneWeightInOrderGraph.put(node, nodes);
					}else{
						this.edgeWithOneWeightInOrderGraph.get(node).add(nextNode);
					}
					//this.edgeWithOneWeightInOrderGraph.put(node, nextNode);
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
			
			if(!this.edgeWithOneWeightInOrderGraph.containsKey(node)){
				List<Node> nodes = new ArrayList<Node>();
				nodes.add(tNode);
				this.edgeWithOneWeightInOrderGraph.put(node, nodes);
			}else{
				this.edgeWithOneWeightInOrderGraph.get(node).add(tNode);
			}
			//this.edgeWithOneWeightInOrderGraph.put(node, tNode);
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
		Node nextNodeInTrace = currentNodeInTrace.pkt2nextNodeinTrace.get(pktHash);
		
		if(root instanceof TNode){
			TNode tNodeRoot = (TNode)root;
			if(tNodeRoot.getChild(false) != null){
				if(tNodeRoot.getChild(false).equals(nextNodeInTrace)){
					//should traverse, in false branch
					traverseTTtoUpdatePriority2(nextNodeInTrace, pktHash, tNodeRoot.getChild(false));
				}
			}
			if(updatePriority(root) == true){
				//priority changed
				root.ruleChanged = true;//TODO: also need to add at install rules and delete rules
			}
			if(tNodeRoot.getChild(true) != null){
				traverseTTtoUpdatePriority2(nextNodeInTrace, pktHash, tNodeRoot.getChild(true));
			}
		}else if(root instanceof VNode){
			if(updatePriority(root) == true){
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
			if(updatePriority(root) == true){
				//priority changed
				root.ruleChanged = true;//TODO: also need to add at install rules and delete rules
			}
			return;
		}
	}
	
	//not correct?
	private void traverseTTtoUpdatePriority(Node currentNodeInTrace, String pktHash, Node root){
		Node nextNodeInTrace = currentNodeInTrace.pkt2nextNodeinTrace.get(pktHash);
		
		if(root instanceof TNode){
			TNode tNodeRoot = (TNode)root;
			if(tNodeRoot.getChild(false) != null){
				if(tNodeRoot.getChild(false).equals(nextNodeInTrace)){
					//should traverse, in false branch
					traverseTTtoUpdatePriority(nextNodeInTrace, pktHash, tNodeRoot.getChild(false));
				}
			}
		}
		if(updatePriority(root) == true){
			//priority changed
			root.ruleChanged = true;//TODO: also need to add at install rules and delete rules
		}
		//start to traverse tt-left
		if(root instanceof TNode){
			//should traverse true branch
			TNode tNodeRoot = (TNode)root;
			Node childNode = tNodeRoot.getChild(true);
			if(childNode != null){
				traverseTTtoUpdatePriority(nextNodeInTrace, pktHash, childNode);
			}
		}else if(root instanceof VNode){
			VNode vNodeRoot = (VNode)root;
			for(Map.Entry<String, Node> entry: vNodeRoot.subtree.entrySet()){
				String value = entry.getKey();
				Node childNode = entry.getValue();
				traverseTTtoUpdatePriority(nextNodeInTrace, pktHash, childNode);
			}
		}else if(root instanceof LNode){
			return;
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
}

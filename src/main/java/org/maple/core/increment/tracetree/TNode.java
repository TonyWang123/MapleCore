/*
 * Copyright (c) 2016 SNLAB and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.maple.core.increment.tracetree;

import java.util.List;import java.util.Map;

public class TNode extends Node{
    
	public Match.Field field = null;
    
	public String value;
    
    public final static int POS_BRANCH = 1;   // (val == exp);
    
    public final static int NEG_BRANCH = 0;   // (val != exp);
    
    public Node[] subtree = new Node[2];
    
    public Rule rule;
	
	//public int priority;
    
    public Node getChild(boolean value) {
        if (value)
            return subtree[POS_BRANCH];
        else
            return subtree[NEG_BRANCH];
    }
    
    @Override
    public void delete(Node node) {
        for (int i = 0; i < 2; i++)
            if (subtree[i] == node) subtree[i] = null;
        //node.father.delete(node)
        node.fatherNodesinOrderGraph.remove(this);
    }

	@Override
	public void augment(Node node, String pktHash, Trace trace, TraceTree tt) {
		// TODO Auto-generated method stub
		if(node instanceof TNode){
			this.count++;
			//merge
			//null?
			Node father = node.pkt2fatherinTrace.get(pktHash);
			if(father == null){
				//first node
				trace.firstNode = this;
				//does not handle last node
			}else{
				father.pkt2nextNodeinTrace.put(pktHash, this);
			}
			this.pkt2nextNodeinTrace.put(pktHash, node.pkt2nextNodeinTrace.get(pktHash));
			
			//handle map
			for(Node childNode: tt.fatherNode2ChildNodesInOrderGraph.get(node)){
				childNode.fatherNodesinOrderGraph.remove(node);
				childNode.fatherNodesinOrderGraph.add(this);
			}
			List<Node> tempChilds = tt.fatherNode2ChildNodesInOrderGraph.get(node);
			tt.fatherNode2ChildNodesInOrderGraph.remove(node);
			tt.fatherNode2ChildNodesInOrderGraph.put(this, tempChilds);
			
			//handle edgeWeight
			List<Node> tempChildsForEdge = tt.edgeWithOneWeightInOrderGraph.get(node);
			tt.edgeWithOneWeightInOrderGraph.remove(node);
			tt.edgeWithOneWeightInOrderGraph.put(this, tempChildsForEdge);
			for(Map.Entry<Node, List<Node>> entry: tt.edgeWithOneWeightInOrderGraph.entrySet()){
				Node keyNode = entry.getKey();
				List<Node> valueNodes = entry.getValue();
				if(valueNodes.contains(node)){
					valueNodes.remove(node);
					valueNodes.add(this);
				}
			}
			
			TNode tNode = (TNode)node;
			Node childNode = null;
			boolean childBoolean;
			if(tNode.getChild(true) != null){
				childNode = tNode.getChild(true);
				childBoolean = true;
			}else{
				childNode = tNode.getChild(false);
				childBoolean = false;
			}
			
			if(this.getChild(childBoolean) != null){
				//contains
				this.getChild(childBoolean).augment(childNode, pktHash, trace, tt);
			}else{
				this.subtree[childBoolean?1:0] = childNode;
			}
			//handle order graph
			this.fatherNodesinOrderGraph.addAll(node.fatherNodesinOrderGraph);
		}else{
			//wrong
			System.out.println("wrong in tNode augment!");
		}
	}
}

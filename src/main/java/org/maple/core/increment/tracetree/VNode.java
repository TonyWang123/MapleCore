package org.maple.core.increment.tracetree;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class VNode extends Node{
	
	public Match.Field field;
	
	public String value;

	public Map<String, Node> subtree = new HashMap<String, Node>();
	
	public Node getChild(String value) {
        return subtree.get(value);
	}
	
	@Override
	public void delete(Node node) {
	    for (String name : subtree.keySet()) {
	        if (subtree.get(name) == node) subtree.remove(name);
	    }
	    node.fatherNodesinOrderGraph.remove(this);
	}

	@Override
	public void augment(Node node, MaplePacket pkt) {
		// TODO Auto-generated method stub
		if(node instanceof VNode){
			this.count++;
			//merge
			/*Node father = node.pkt2fatherinTrace.get(pkt);
			father.pkt2nextNodeinTrace.put(pkt, this);
			this.pkt2nextNodeinTrace.put(pkt, node.pkt2nextNodeinTrace.get(pkt));*/
			Node father = node.pkt2fatherinTrace.get(pkt);
			if(father == null){
				//first node
			}else{
				father.pkt2nextNodeinTrace.put(pkt, this);
			}
			this.pkt2nextNodeinTrace.put(pkt, node.pkt2nextNodeinTrace.get(pkt));
			
			VNode vNode = (VNode)node;
			String key = null;
			Node valueNode = null;
			for(Map.Entry<String, Node> entry: vNode.subtree.entrySet()){
				//should only one entry
				key = entry.getKey();
				valueNode = entry.getValue();
			}
			if(this.subtree.containsKey(key)){
				Node containtedNode = this.subtree.get(key);
				containtedNode.augment(valueNode, pkt);
			}else{
				this.subtree.put(key, valueNode);
				//finish?
			}
			//handle order graph
			this.fatherNodesinOrderGraph.addAll(node.fatherNodesinOrderGraph);
		}else{
			//wrong
		}
	}
}

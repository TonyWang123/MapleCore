package org.maple.core.increment.tracetree;

public class Trace {

	public Node firstNode;
	
	public Node lastNode;
	
	String nextPositionString;
	
	int nextPositionInt;
	
	public void addNode(Node node, MaplePacket pkt, String value, boolean isTrue){
		if(firstNode == null){
			firstNode = node;
			lastNode = node;
			nextPositionString = value;
			nextPositionInt = isTrue?1:0;//true is 1; false is 0
		}else{
			lastNode.pkt2nextNodeinTrace.put(pkt, node);
			node.pkt2fatherinTrace.put(pkt, lastNode);
			//connect to TT
			if(lastNode instanceof VNode){
				VNode vNode = (VNode)lastNode;
				vNode.subtree.put(nextPositionString, node);
			}else if(lastNode instanceof TNode){
				TNode tNode = (TNode)lastNode;
				tNode.subtree[nextPositionInt] = node;
			}else{
				//wrong
			}
			nextPositionString = value;
			nextPositionInt = isTrue?1:0;
		}
	}
	
	/*
	 * trace -> rules and store them at nodes (TNode, LNode)
	 * rule = match + action
	 * */
	public void generateAndStoreRules(MaplePacket pkt){
		Node node = this.firstNode;
		Match tempMatch = new Match();
		while(node.pkt2nextNodeinTrace.get(pkt) != null){
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
	}
}

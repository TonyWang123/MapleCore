package org.maple.core.increment.tracetree;

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
	public void augment(Node node, MaplePacket pkt) {
		// TODO Auto-generated method stub
		if(node instanceof TNode){
			this.count++;
			//merge
			//null?
			Node father = node.pkt2fatherinTrace.get(pkt);
			if(father == null){
				//first node
			}else{
				father.pkt2nextNodeinTrace.put(pkt, this);
			}
			this.pkt2nextNodeinTrace.put(pkt, node.pkt2nextNodeinTrace.get(pkt));
			
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
				this.getChild(childBoolean).augment(childNode, pkt);
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

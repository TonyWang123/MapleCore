package org.maple.core.increment.tracetree;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public abstract class Node {

	public Map<MaplePacket, Node> pkt2nextNodeinTrace = new HashMap<MaplePacket, Node>();
	
	public int count; //how many trace share this node, if 0, should remove this node
	
	public Map<MaplePacket, Node> pkt2fatherinTrace = new HashMap<MaplePacket, Node>();
	
	//public List<Node> nextNodesinOrderGraph = new LinkedList<Node>();
	
	public List<Node> fatherNodesinOrderGraph = new LinkedList<Node>();
	
	public boolean ruleChanged;
	
	public int priority;
	
	public abstract void delete(Node node);
	
	public abstract void augment(Node node, MaplePacket pkt);
}

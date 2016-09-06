package org.maple.core.tracetree;

public class Trace {

	public Node firstNode;
	
	public Node lastNode;
	
	public void addNode(Node node){
		if(firstNode == null){
			firstNode = node;
		}else{
			
		}
		lastNode = node;
	}
}

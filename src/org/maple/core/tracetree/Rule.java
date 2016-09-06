package org.maple.core.tracetree;

public class Rule {

	Match match;
	
	Action action;
	
	public Rule(Match match, Action action){
		this.match = match;
		this.action = action;
	}
}

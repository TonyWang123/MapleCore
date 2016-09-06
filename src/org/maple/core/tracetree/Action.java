package org.maple.core.tracetree;

public class Action {

	private static Punt punt = new Punt();
	private static Drop drop = new Drop();
	
	public static Action Punt() { return punt; }
	public static Action Drop() { return drop; }
}

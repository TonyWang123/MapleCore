/*
 * Copyright (c) 2016 SNLAB and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.maple.core.increment.tracetree;

import java.util.HashMap;
import java.util.Map;

public class Action {

	private static Punt punt = new Punt();
	private static Drop drop = new Drop();
	
	public static Action Punt() { return punt; }
	public static Action Drop() { return drop; }
	
	public Map<Match.Field, String> modifiedFieldValues = new HashMap<Match.Field, String>();
	
	public TraceItem toItem(){
		return null;
	}
}

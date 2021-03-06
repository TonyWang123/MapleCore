/*
 * Copyright (c) 2016 SNLAB and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.maple.core.increment.tracetree;

public class Rule {

	public Match match;
	
	public Action action;
	
	public Rule(Match match, Action action){
		this.match = match;
		this.action = action;
	}
}

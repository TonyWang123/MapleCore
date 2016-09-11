/*
 * Copyright (c) 2016 SNLAB and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.maple.core.increment.tracetree;

public class Drop extends Action{

	@Override
	public TraceItem toItem(){
		TraceItem traceItem = new TraceItem();
		traceItem.type = "L";
		traceItem.action = "drop";
		return traceItem;
	}
}

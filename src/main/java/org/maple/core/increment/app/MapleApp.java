/*
 * Copyright (c) 2016 SNLAB and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.maple.core.increment.app;

import org.maple.core.increment.tracetree.*;
import org.opendaylight.yang.gen.v1.urn.opendaylight.inventory.rev130819.NodeConnectorRef;

public class MapleApp extends MapleAppBase{

	public Action onPacket(MaplePacket pkt){
		Object data = readData("/data/dummy_data");
		
		System.out.println("Hello World from Maple");
		
		return null;
	}
}

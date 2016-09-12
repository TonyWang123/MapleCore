/*
 * Copyright (c) 2016 SNLAB and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.maple.core.increment.app;

import org.maple.core.increment.MapleCore;
import org.maple.core.increment.MapleDataPathAdaptor;
import org.maple.core.increment.MapleDataStoreAdaptor;
import org.maple.core.increment.tracetree.*;
import org.opendaylight.yang.gen.v1.urn.opendaylight.inventory.rev130819.NodeConnectorRef;

public abstract class MapleAppBase{
	
	MapleCore mapleCore;
	
	public MapleAppBase(){
		mapleCore = MapleCore.allocateMapleCore();
	}

	public Action onPacket(MaplePacket pkt) {
		return null;
	}


	public Object readData(String xpath) {
		return mapleCore.readData(xpath);
	}
}

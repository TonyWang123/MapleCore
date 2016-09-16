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
	
	MapleAppBase nextMapleApp;
	
	public MapleAppBase(){
		mapleCore = MapleCore.allocateMapleCore();
		mapleCore.registerMapleApp(this);
	}

	public void onPacket(MaplePacket pkt) {
	}


	public Object readData(String xpath) {
		return mapleCore.readData(xpath);
	}
	
	public void passToNext(MaplePacket pkt) {
		nextMapleApp.onPacket(pkt);
	}

	public MapleAppBase getNextMapleApp() {
		return nextMapleApp;
	}

	public void setNextMapleApp(MapleAppBase nextMapleApp) {
		this.nextMapleApp = nextMapleApp;
	}

	public MapleCore getMapleCore() {
		return mapleCore;
	}

	public void setMapleCore(MapleCore mapleCore) {
		this.mapleCore = mapleCore;
	}
}

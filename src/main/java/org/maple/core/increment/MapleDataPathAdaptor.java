/*
 * Copyright (c) 2016 SNLAB and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.maple.core.increment;

import org.maple.core.increment.tracetree.Action;
import org.maple.core.increment.tracetree.Match;
import org.maple.core.increment.tracetree.Rule;
import org.opendaylight.yang.gen.v1.urn.opendaylight.inventory.rev130819.NodeConnectorRef;

public interface MapleDataPathAdaptor {
    void sendPacket(byte[] payload, NodeConnectorRef ingress, Action action);
    void installPath(Action action, Match match, Integer priority);
    void deletePath(Action action, Match match, Integer priority);
    void installRule(Rule r, NodeConnectorRef sw); //Not in use
    void deleteRule(Rule r, NodeConnectorRef sw);  //Not in use
}

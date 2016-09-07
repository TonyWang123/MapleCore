package org.maple.core.increment.app;

import org.maple.core.increment.tracetree.Action;
import org.maple.core.increment.tracetree.MaplePacket;

public abstract class MapleAppBase {

	abstract Action onPacket(MaplePacket pkt);
}

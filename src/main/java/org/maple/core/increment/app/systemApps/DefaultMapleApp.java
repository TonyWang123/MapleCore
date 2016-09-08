package org.maple.core.increment.app.systemApps;

import org.maple.core.increment.app.MapleAppBase;
import org.maple.core.increment.tracetree.Action;
import org.maple.core.increment.tracetree.MaplePacket;

import static org.maple.core.increment.tracetree.Action.Drop;

/**
 * Created by mingming on 9/8/16.
 */
public class DefaultMapleApp extends MapleAppBase {

    public Action onPacket(MaplePacket pkt) {
        return Drop();
    }
}

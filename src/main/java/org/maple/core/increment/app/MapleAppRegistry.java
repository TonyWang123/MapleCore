package org.maple.core.increment.app;

import org.maple.core.increment.app.systemApps.ARPHandlingMapleApp;
import org.maple.core.increment.app.systemApps.DefaultMapleApp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mingming on 9/8/16.
 */
public class MapleAppRegistry {

    private List<Class<? extends MapleAppBase>> mapleApps = new ArrayList<Class<? extends MapleAppBase>>();

    private static final Logger LOG = LoggerFactory.getLogger(MapleAppRegistry.class);

    public MapleAppRegistry () {
        mapleApps.add(DefaultMapleApp.class);
        mapleApps.add(ARPHandlingMapleApp.class);
    }

    private static class Singleton {
        private static MapleAppRegistry instance = new MapleAppRegistry();
    }

    private static MapleAppRegistry getSingleton() {
        return Singleton.instance;
    }

    public void addLastMapleApp(Class<? extends MapleAppBase> clazz) {
        mapleApps.add(mapleApps.size()-1, clazz);
    }

    public void addFirstMapleApp(Class<? extends MapleAppBase> clazz) {
        if (mapleApps.isEmpty()) {
            mapleApps.add(clazz);
        } else {
            mapleApps.add(0, clazz);
        }
    }

    public Class getFirstMapleApp() {
        if (mapleApps.isEmpty()) {
            LOG.warn("There is not maple app");
            return null;
        } else {
            return mapleApps.get(0);
        }
    }

    public Class getLastMapleApp() {
        if (mapleApps.isEmpty()) {
            LOG.warn("There is not maple app");
            return null;
        } else {
            return mapleApps.get(mapleApps.size()-1);
        }
    }

    public Class getNextMapleApp(Class<? extends MapleAppBase> clazz) {
        int i = mapleApps.indexOf(clazz);
        if (i == mapleApps.size() - 1) {
            LOG.warn("There is not next maple app");
            return null;
        } else {
            return mapleApps.get(i + 1);
        }
    }

    public void removeFirstMapleApp() {
        if (mapleApps.get(0) != null){
            mapleApps.remove(0);
        } else {
            LOG.warn("There is not first maple app");
        }
    }

    public void removeLastMapleApp() {
        if (!mapleApps.isEmpty()) {
            mapleApps.remove(mapleApps.size()-1);
        } else {
            LOG.warn("There is not maple app");
        }
    }

}

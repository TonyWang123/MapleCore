package org.maple.core.increment.tracetree;

import java.util.HashMap;
import java.util.Map;

public class Match {
    public static enum Field {
	    IN_PORT,
	    ETH_SRC,
	    ETH_DST,
	    TOPOLOGY,
	    ETH_TYPE,
	    IPv4_SRC,
	    IPv4_DST,
	    TCP_SRC_PORT,
	    TCP_DST_PORT
    }
    
    public Map<Field, String> fields = new HashMap<Field, String>();

    //public Field field;
    //public String value;
    //public boolean Tvalue;
}

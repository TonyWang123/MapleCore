package org.maple.core.tracetree;

public class Match {
    public enum Field {
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

    public Field field;
    public String value;
    public boolean Tvalue;
}

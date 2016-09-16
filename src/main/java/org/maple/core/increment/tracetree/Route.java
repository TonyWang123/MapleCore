package org.maple.core.increment.tracetree;

import java.util.ArrayList;
import java.util.List;

public class Route extends Action{
	
	public List<String> links = new ArrayList<String>();
	
	public String lastTpId;
	
	// link := <linkId,srcTpId,dstTpId>
	public Route(List<String> links, String lastTpId) {
		this.links = links;
		this.lastTpId = lastTpId;
	}
	
	// path_tpId := path+tpId
	// path := path;link
	public Route(String path_tpId) {
		String[] values = path_tpId.split("\\+");
		String[] links = values[0].split(";");
		for (String link: links) {
			this.links.add(link);
		}
		this.lastTpId = values[1];
	}
	
	// <linkId,srcTpId,dstTpId>
	public static String getLinkId(String value) {
		String rawLink = value.substring(1, value.length() - 1);
		String[] values = rawLink.split(",");
		String linkId = values[0];
		return linkId;
	}
	
	public static String getSrcTpId(String value) {
		String rawLink = value.substring(1, value.length() - 1);
		String[] values = rawLink.split(",");
		String srcTpId = values[1];
		return srcTpId;
	}
	
	public static String getDstTpId(String value) {
		String rawLink = value.substring(1, value.length() - 1);
		String[] values = rawLink.split(",");
		String dstTpId = values[2];
		return dstTpId;
	}

}

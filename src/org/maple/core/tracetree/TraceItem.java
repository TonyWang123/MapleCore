package org.maple.core.tracetree;

public class TraceItem {

	String type;
	
	String field;
	
	String value;
	
	String branch;
	
	String action;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getBranch() {
		return branch;
	}

	public void setBranch(String branch) {
		this.branch = branch;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}
}

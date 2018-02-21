package com.fuze.coreuc.fcc.api.objects;

public class Hangup {

	private String uniqueId;

	public Hangup(String uniqueID) {
		this.uniqueId = uniqueID;
	}

	public String getUniqueID() {
		return uniqueId;
	}

	public void setUniqueID(String uniqueID) {
		this.uniqueId = uniqueID;
	}
}

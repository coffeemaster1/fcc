package com.fuze.coreuc.fcc.api.objects;

public class Transfer {
	private String uniqueId;
	private String destExten;
	private String context;

	public Transfer(String uniqueId, String destExten, String context) {
		this.uniqueId = uniqueId;
		this.destExten = destExten;
		this.context = context;
	}

	public String getUniqueId() {
		return uniqueId;
	}

	public void setUniqueId(String uniqueId) {
		this.uniqueId = uniqueId;
	}

	public String getDestExten() {
		return destExten;
	}

	public void setDestExten(String destExten) {
		this.destExten = destExten;
	}

	public String getContext() {
		return context;
	}

	public void setContext(String context) {
		this.context = context;
	}
}

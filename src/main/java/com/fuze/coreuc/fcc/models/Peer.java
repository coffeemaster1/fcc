package com.fuze.coreuc.fcc.models;

import com.fuze.coreuc.fcc.util.StatusConverter;

import java.util.HashMap;
import java.util.Map;

public class Peer {

	private transient StatusConverter statusConverter = new StatusConverter();

	private String peerName;
	private String extension;
	private String userName;
	private Integer peerStatusCode;
	private String peerStatus;
	private String label;
	private String connectedCallerIDName;
	private String connectedCallerIDNum;
	private String pbx;
	private String onlineStatus;
	private String transferContext;

	private Map<String, Call> activeCalls = new HashMap<>();

	public Peer(String peerName, String extension, String userName, String label, String pbx, String context) {
		this.peerName = peerName;
		this.extension = extension;
		this.userName = userName;
		this.peerStatusCode = 1;
		this.peerStatus = statusConverter.getExtensionStatusString(peerStatusCode);
		this.label = label;
		this.pbx = pbx;
		this.transferContext = context;
	}

	public String getPeerStatus() {
		return peerStatus;
	}

	public String getPeerName() {
		return peerName;
	}

	public String getExtension() {
		return extension;
	}

	public void setExtension(String extension) {
		this.extension = extension;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getConnectedCallerIDName() {
		return connectedCallerIDName;
	}

	public void setConnectedCallerIDName(String connectedCallerIDName) {
		this.connectedCallerIDName = connectedCallerIDName;
	}

	public String getConnectedCallerIDNum() {
		return connectedCallerIDNum;
	}

	public void setConnectedCallerIDNum(String connectedCallerIDNum) {
		this.connectedCallerIDNum = connectedCallerIDNum;
	}

	public Integer getPeerStatusCode() {
		return peerStatusCode;
	}

	public void setPeerStatusCode(Integer peerStatusCode) {
		this.peerStatusCode = peerStatusCode;
		this.peerStatus = statusConverter.getExtensionStatusString(peerStatusCode);
	}

	public String getLabel() {
		return label;
	}

	public String getPbx() {
		return pbx;
	}

	public void setPbx(String pbx) {
		this.pbx = pbx;
	}

	public String getOnlineStatus() {
		return onlineStatus;
	}

	public void setOnlineStatus(String online) {
		this.onlineStatus = online;
	}

	public void addCall(Call connected) {

		this.connectedCallerIDName = connected.getConnectedLineName();
		this.connectedCallerIDNum = connected.getConnectedLineNumber();
		setPeerStatusCode(connected.getChannelState());
		activeCalls.put(connected.getUniqueID(), connected);
	}

	public void removeCall(String uniqueId) {
		activeCalls.remove(uniqueId);
		if (activeCalls.size() > 0) {
			this.connectedCallerIDNum = activeCalls.entrySet().iterator().next().getValue().getCallerIDNumber();
			this.connectedCallerIDName = activeCalls.entrySet().iterator().next().getValue().getCallerIDName();
			setPeerStatusCode(activeCalls.entrySet().iterator().next().getValue().getChannelState());
		}
		else {
			this.connectedCallerIDNum = "";
			this.connectedCallerIDName = "";
			setPeerStatusCode(1);
		}
	}

	public String getTransferContext() {
		return transferContext;
	}

	public void setTransferContext(String transferContext) {
		this.transferContext = transferContext;
	}

	public Map<String, Call> getConnectedCalls () {
		return activeCalls;
	}

	public Call getCall (String uniqueid) {
		return activeCalls.get(uniqueid);
	}


}

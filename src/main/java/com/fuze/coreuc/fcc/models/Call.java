package com.fuze.coreuc.fcc.models;

import org.asteriskjava.manager.event.NewChannelEvent;

import java.util.List;

public class Call {

	private String callerIDName;
	private String callerIDNumber;
	private String channelName;
	private String uniqueID;
	private String bridgedChannel;
	private String extension;
	private String context;
	private String linkedID;
	private String application;
	private String status;
	private Integer channelState;
	private Integer startTime;
	private String accountCode;
	private String connectedLineNumber;
	private String connectedLineName;
	private String dialStatus;

	public Call () {

	}

	public Call(String callerIDName, String callerIDNumber, String channelName, String uniqueID) {
		this.callerIDName = callerIDName;
		this.callerIDNumber = callerIDNumber;
		this.channelName = channelName;
		this.uniqueID = uniqueID;
	}

	public Call (List<String> conciseOutput) {
		this.channelName = conciseOutput.get(0);
		this.context = conciseOutput.get(1);
		this.extension = conciseOutput.get(2);
		this.status = conciseOutput.get(4);
		this.channelState = 1;
		this.application = conciseOutput.get(5);
		this.callerIDName = conciseOutput.get(7);
		this.callerIDNumber = conciseOutput.get(7);
		this.startTime = (((int) ((System.currentTimeMillis() / 1000L)) - Integer.parseInt(conciseOutput.get(conciseOutput.size() - 5))));
		this.bridgedChannel = conciseOutput.get(conciseOutput.size() - 4);
		this.accountCode = conciseOutput.get(8);
		this.uniqueID = conciseOutput.get(conciseOutput.size() - 3);
		this.connectedLineName = conciseOutput.get(conciseOutput.size() - 2);
		this.connectedLineNumber = conciseOutput.get(conciseOutput.size() -1);
	}

	public Call (NewChannelEvent event) {
		this.channelName = event.getChannel();
		this.context = event.getContext();
		this.extension = event.getExten();
		this.status = event.getState();
		this.callerIDName = event.getCallerIdName();
		this.callerIDNumber = event.getCallerIdNum();
		this.startTime = (((int) (System.currentTimeMillis() / 1000L)));
		this.channelState = event.getChannelState();
		this.uniqueID = event.getUniqueId();
		this.accountCode = event.getAccountCode();
		if (event.getConnectedLineName() == null || event.getConnectedLineNum() == null) {
			this.connectedLineName = event.getExten();
			this.connectedLineNumber = event.getExten();
		}
		else {
			this.connectedLineNumber = event.getConnectedLineNum();
			this.connectedLineName = event.getConnectedLineName();
		}
	}

	public String getCallerIDName() {
		return callerIDName;
	}

	public void setCallerIDName(String callerIDName) {
		this.callerIDName = callerIDName;
	}

	public String getCallerIDNumber() {
		return callerIDNumber;
	}

	public void setCallerIDNumber(String callerIDNumber) {
		this.callerIDNumber = callerIDNumber;
	}

	public String getChannelName() {
		return channelName;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}

	public String getUniqueID() {
		return uniqueID;
	}

	public void setUniqueID(String uniqueID) {
		this.uniqueID = uniqueID;
	}

	public String getBridgedChannel() {
		return bridgedChannel;
	}

	public void setBridgedChannel(String bridgedChannel) {
		this.bridgedChannel = bridgedChannel;
	}

	public String getExtension() {
		return extension;
	}

	public void setExtension(String extension) {
		this.extension = extension;
	}

	public String getContext() {
		return context;
	}

	public void setContext(String context) {
		this.context = context;
	}

	public String getLinkedID() {
		return linkedID;
	}

	public void setLinkedID(String linkedID) {
		this.linkedID = linkedID;
	}

	public String getApplication() {
		return application;
	}

	public void setApplication(String application) {
		this.application = application;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Integer getChannelState() {
		return channelState;
	}

	public void setChannelState(Integer channelState) {
		this.channelState = channelState;
	}

	public String getAccountCode() {
		return accountCode;
	}

	public void setAccountCode(String accountCode) {
		this.accountCode = accountCode;
	}

	public String getConnectedLineNumber() {
		return connectedLineNumber;
	}

	public void setConnectedLineNumber(String connectedLineNumber) {
		this.connectedLineNumber = connectedLineNumber;
	}

	public String getConnectedLineName() {
		return connectedLineName;
	}

	public void setConnectedLineName(String connectedLineName) {
		this.connectedLineName = connectedLineName;
	}

	public String getDialStatus() {
		return dialStatus;
	}

	public void setDialStatus(String dialStatus) {
		this.dialStatus = dialStatus;
	}
}

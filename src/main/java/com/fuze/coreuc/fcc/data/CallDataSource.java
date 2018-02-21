package com.fuze.coreuc.fcc.data;

import com.fuze.coreuc.fcc.models.Call;

import java.util.Map;

public class CallDataSource {

	 private Map<String, Call> globalCallMap;

	public CallDataSource(Map<String, Call> globalCallMap) {
		this.globalCallMap = globalCallMap;
	}

	public Map<String, Call> getGlobalCallMap() {
		return globalCallMap;
	}

	public void setGlobalCallMap(Map<String, Call> globalCallMap) {
		this.globalCallMap = globalCallMap;
	}

	public Call getCall (String uniqueId) {
		return globalCallMap.get(uniqueId);
	}

	public boolean callExists (String uniqueId) {
		return globalCallMap.containsKey(uniqueId);
	}

	public String getCallerIDName (String uniqueId) {
		return globalCallMap.get(uniqueId).getCallerIDName();
	}

	public String getCallerIDNum (String uniqueId) {
		return globalCallMap.get(uniqueId).getCallerIDNumber();
	}

	public void setCallState (String uniqueid, Integer state) {
		globalCallMap.get(uniqueid).setChannelState(state);
	}

	public void setChannelStatus(String uniqueid, String status){
		globalCallMap.get(uniqueid).setStatus(status);
	}

	public void addCall (Call call) {
		globalCallMap.put(call.getUniqueID(), call);
	}

	public void setConnectedLineName (String uniqueid, String name) {
		globalCallMap.get(uniqueid).setConnectedLineName(name);
	}

	public void setConnectedLineNum (String uniqueid, String num) {
		globalCallMap.get(uniqueid).setConnectedLineNumber(num);
	}

	public String getConnectedLineName (String uniqueId) {
		return globalCallMap.get(uniqueId).getConnectedLineName();
	}

	public String getConnectedLineNumber (String uniqueId) {
		return globalCallMap.get(uniqueId).getConnectedLineNumber();
	}

	public String getExtension (String uniqueid) {
		return globalCallMap.get(uniqueid).getExtension();
	}

	public String getChannel (String uniqueid) {
		return globalCallMap.get(uniqueid).getChannelName();
	}

	public void removeCall (String uniqueid) {
		globalCallMap.remove(uniqueid);
	}

	public String getPeer (String uniqueid) {
		return stripTech(globalCallMap.get(uniqueid).getChannelName());
	}

	public String getAccountCode (String uniqueid) {
		return globalCallMap.get(uniqueid).getAccountCode();
	}

	public String stripTech (String channel) {
		return channel.substring(channel.lastIndexOf("/") + 1, channel.lastIndexOf("-"));
	}

	public void setAccountCode (String uniqueid, String accountCode) {
		globalCallMap.get(uniqueid).setAccountCode(accountCode);
	}

	public String getLinkedId (String uniqueid) {
		return globalCallMap.get(uniqueid).getLinkedID();
	}

	public void setLinkedId (String uniqueid, String linkedid) {
		globalCallMap.get(uniqueid).setLinkedID(linkedid);
	}

	public void setUniqueId (String uniqueId, String newUniqueId){
		globalCallMap.get(uniqueId).setUniqueID(newUniqueId);
	}


}

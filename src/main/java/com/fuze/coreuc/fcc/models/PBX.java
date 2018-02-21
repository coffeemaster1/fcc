package com.fuze.coreuc.fcc.models;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class PBX {

	private String pbxName;

	private Map<String, Peer> peerHashMap = new HashMap<>();
	private Map<String, Extension> extensionHashMap = new HashMap<>();
	private Map<String, Queue> queueHashMap = new HashMap<>();
	private Map<String, Call> callMap = new HashMap<>();
	private List<String> accountCodes = new LinkedList<>();

	public PBX (String name) {
		this.pbxName = name;
	}

	public String getPbxName (){
		return pbxName;
	}

	public void addPeerMap(Map<String, Peer> peers){
		this.peerHashMap = peers;
	}

	public void addExtensionMap(Map<String, Extension> extensions){
		this.extensionHashMap = extensions;
	}

	public void addQueueMap(Map<String, Queue> queues){
		this.queueHashMap = queues;
	}

	public Extension getExtension(String extension){
		return extensionHashMap.get(extension);
	}

	public Peer getPeer(String peer){
		return peerHashMap.get(peer);
	}

	public Queue getQueue(String queue){
		return queueHashMap.get(queue);
	}

	public Map<String, Extension> getExtensionHashMap() {
		return extensionHashMap;
	}

	public Map<String, Peer> getPeerHashMap() {
		return peerHashMap;
	}

	public Map<String, Queue> getQueueHashMap() {
		return queueHashMap;
	}

	public void addCallMap (Map<String, Call> callMap) {
		this.callMap = callMap;
	}

	public Map<String, Call> getCallMap () {
		return callMap;
	}

	public Call getCall (String uniqueId) {
		return callMap.get(uniqueId);
	}

	public void addCall (Call call) {
		callMap.put(call.getUniqueID(), call);
	}

	public void removeCall (String uniqueId) {
		callMap.remove(uniqueId);
	}

	public List<String> getAccountCodes() {
		return accountCodes;
	}

	public void setAccountCodes(List<String> accountCodes) {
		this.accountCodes = accountCodes;
	}

	public boolean isAccountCode (String accountCode){
		return accountCode.contains(accountCode);
	}


	public void linkCallToPeer (String uniqueid, String peer) {
		peerHashMap.get(peer).addCall(callMap.get(uniqueid));
		extensionHashMap.get(peerHashMap.get(peer).getExtension()).setExtensionStatusCode(peerHashMap.get(peer).getPeerStatusCode());
		extensionHashMap.get(peerHashMap.get(peer).getExtension()).setCallerID(peerHashMap.get(peer).getConnectedCallerIDNum(), peerHashMap.get(peer).getConnectedCallerIDName());
	}

	public Extension getExtensionByPeer (Peer peer) {
		return extensionHashMap.get(peer.getExtension());
	}
}

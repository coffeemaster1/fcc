package com.fuze.coreuc.fcc.data;

import com.fuze.coreuc.fcc.models.Call;
import com.fuze.coreuc.fcc.models.Peer;

import java.util.Map;

public class PeerDataSource {

	private Map<String, Peer> globalPeerMap;

	public PeerDataSource(Map<String, Peer> globalPeerMap) {
		this.globalPeerMap = globalPeerMap;
	}

	public Map<String, Peer> getGlobalPeerMap() {
		return globalPeerMap;
	}

	public void setGlobalPeerMap(Map<String, Peer> globalPeerMap) {
		this.globalPeerMap = globalPeerMap;
	}

	public boolean peerExists (String peer) {
		return globalPeerMap.containsKey(peer);
	}

	public Peer getPeer (String peer) {
		return globalPeerMap.get(peer);
	}

	public void addCallToPeer (String peer, Call call) {
		globalPeerMap.get(peer).addCall(call);
	}

	public void removeCallFromPeer (String peer, String uniqueid) {
		globalPeerMap.get(peer).removeCall(uniqueid);
	}

	public void setPeerStatus (String peer, Integer status) {
		globalPeerMap.get(peer).setPeerStatusCode(status);
	}

	public void updatePeerConnectedLine (String peer, String uniqueid) {
		if (globalPeerMap.get(peer).getConnectedCallerIDName() == null || !globalPeerMap.get(peer).getCall(uniqueid).getConnectedLineName().equals(globalPeerMap.get(peer).getConnectedCallerIDName())) {
			globalPeerMap.get(peer).setConnectedCallerIDName(globalPeerMap.get(peer).getCall(uniqueid).getConnectedLineName());
		}
		if (globalPeerMap.get(peer).getConnectedCallerIDNum() == null || !globalPeerMap.get(peer).getCall(uniqueid).getConnectedLineNumber().equals(globalPeerMap.get(peer).getConnectedCallerIDNum())) {
			globalPeerMap.get(peer).setConnectedCallerIDNum(globalPeerMap.get(peer).getCall(uniqueid).getConnectedLineNumber());
		}
	}

	public String getPbxFromPeer (String peer) {
		return globalPeerMap.get(peer).getPbx();
	}

	public void setConnectedStatus (String peer, String status) {
		globalPeerMap.get(peer).setOnlineStatus(status);
	}


}

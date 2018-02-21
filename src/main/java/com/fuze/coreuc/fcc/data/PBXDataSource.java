package com.fuze.coreuc.fcc.data;

import com.fuze.coreuc.fcc.models.*;

import java.util.Map;

public class PBXDataSource {

	private Map<String, PBX> pbxMap;

	public PBXDataSource(Map<String, PBX> pbxMap) {
		this.pbxMap = pbxMap;
	}

	public Map<String, PBX> getPbxMap() {
		return pbxMap;
	}

	public PBX getPbx (String pbx) {
		return pbxMap.get(pbx);
	}

	public Map<String, Call> getCallsByPbx (String pbx) {
		return pbxMap.get(pbx).getCallMap();
	}

	public Map<String, Extension> getExtensionsByPbx (String pbx) {
		return pbxMap.get(pbx).getExtensionHashMap();
	}

	public Extension getExtensionByPbx (String pbx, String exten) {
		return pbxMap.get(pbx).getExtension(exten);
	}

	public Map<String, Peer> getPeersByPbx (String pbx) {
		return pbxMap.get(pbx).getPeerHashMap();
	}

	public Peer getPeerByPbx (String pbx, String peer) {
		return pbxMap.get(pbx).getPeer(peer);
	}

	public Map<String, Queue> getQueuesByPbx (String pbx) {
		return pbxMap.get(pbx).getQueueHashMap();
	}

	public Queue getQueueByPbx (String pbx, String queue) {
		return pbxMap.get(pbx).getQueue(queue);
	}

	public void setPbxMap(Map<String, PBX> pbxMap) {
		this.pbxMap = pbxMap;
	}

	public boolean PbxExists (String pbx) {
		return pbxMap.containsKey(pbx);
	}

	public void addCallToPbx (String pbx, Call call) {
		pbxMap.get(pbx).addCall(call);
	}

	public void addCallToAccountCode (String accountCode, Call call) {
		pbxMap.get(pbxFromAccountCode(accountCode)).addCall(call);
	}

	public void updateExtensionStatusByPeer (String pbx, Peer peer, Integer status) {
		pbxMap.get(pbx).getExtensionByPeer(peer).setExtensionStatusCode(status);
	}

	public void updateExtensionConnectedLine (String pbx, Peer peer) {
		if (!peer.getConnectedCallerIDName().equals(pbxMap.get(pbx).getExtensionByPeer(peer).getConnectedCallerIDName())) {
			pbxMap.get(pbx).getExtensionByPeer(peer).setConnectedCallerIDName(peer.getConnectedCallerIDName());
		}
		if (!peer.getConnectedCallerIDNum().equals(pbxMap.get(pbx).getExtensionByPeer(peer).getConnectedCallerIDNum())) {
			pbxMap.get(pbx).getExtensionByPeer(peer).setConnectedCallerIDNum(peer.getConnectedCallerIDNum());
		}
	}

	public void removeCallFromPBX (String pbx, String uniqueid) {
		pbxMap.get(pbx).removeCall(uniqueid);
	}

	public void removeCallFromAccountCode (String accountCode, String uniqueid) {
		pbxMap.get(pbxFromAccountCode(accountCode)).removeCall(uniqueid);
	}

	public boolean callExistsInAccountCode (String accountCode, String uniqueid){
		return pbxMap.get(pbxFromAccountCode(accountCode)).getCallMap().containsKey(uniqueid);
	}

	public boolean callExistsInPbx (String pbx, String uniqueid) {
		return pbxMap.get(pbx).getCallMap().containsKey(uniqueid);
	}

	public boolean queueExistsInPbx (String pbx, String queue) {
		return pbxMap.get(pbx).getQueueHashMap().containsKey(queue);
	}

	public boolean agentExistsInQueue (String pbx, String queue, String agent) {
		return pbxMap.get(pbx).getQueue(queue).getAgentList().containsKey(agent);
	}

	public String pbxFromAccountCode (String accountCode) {
		for (PBX pbx : pbxMap.values()) {
			if (pbx.isAccountCode(accountCode)) {
				return pbx.getPbxName();
			}
		}
		return "";
	}

	public boolean containsAccountCode (String account ) {
		for (PBX pbx : pbxMap.values()) {
			if (pbx.isAccountCode(account)) {
				return true;
			}
		}
		return false;
	}


}

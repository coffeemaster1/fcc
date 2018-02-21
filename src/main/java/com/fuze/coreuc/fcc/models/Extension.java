package com.fuze.coreuc.fcc.models;

import com.fuze.coreuc.fcc.util.StatusConverter;

import java.util.HashMap;
import java.util.Map;

public class Extension {

	private transient StatusConverter statusConverter = new StatusConverter();

	private String extension;
	private String did;
	private String mailbox;
	private String mailboxContext;
	private String userName;
	private Integer extensionStatusCode;
	private String extensionStatus;
	private String connectedCallerIDName;
	private String connectedCallerIDNum;

	private Map<String, Peer> extensionPeers;

	public Extension(String extension, String did, String mailbox, String mailboxContext, String userName) {
		this.extension = extension;
		this.did = did;
		this.mailbox = mailbox;
		this.mailboxContext = mailboxContext;
		this.userName = userName;
		this.extensionStatusCode = 1;
		this.extensionPeers = new HashMap<>();
	}

	public String getExtensionStatus() {
		return extensionStatus;
	}

	public void setExtensionStatusCode(Integer extensionStatusCode) {
		if (extensionStatusCode == 1) {
			Map<String, Peer> members = getExtensionPeers();
			for (Peer peer : members.values()) {
				if (peer.getConnectedCalls().size() > 0) {
					setExtensionStatusCode(peer.getPeerStatusCode());
					return;
				}
			}
		}
		this.extensionStatusCode = extensionStatusCode;
		this.extensionStatus = statusConverter.getExtensionStatusString(extensionStatusCode);
	}

	public String getExtension() {
		return extension;
	}

	public String getDid() {
		return did;
	}

	public String getMailbox() {
		return mailbox;
	}

	public String getMailboxContext() {
		return mailboxContext;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Integer getExtensionStatusCode() {
		return extensionStatusCode;
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

	public void addExtensionPeer (Peer peer) {
		extensionPeers.put(peer.getPeerName(), peer);
	}

	public void removeExtensionPeer (String peername) {
		extensionPeers.remove(peername);
	}

	public Map<String, Peer> getExtensionPeers () {
		return extensionPeers;
	}

	public void setCallerID (String num, String name) {
		this.connectedCallerIDNum = num;
		this.connectedCallerIDName = name;
	}

	public void resetCID () {
		Map<String, Peer> members = getExtensionPeers();
		for (Peer peer : members.values()) {
			if (peer.getConnectedCalls().size() > 0) {
				setCallerID(peer.getConnectedCallerIDNum(), peer.getConnectedCallerIDName());
				return;
			}
		}
		setCallerID("","");
	}

	public Map<String, Call> getCalls () {
		Map<String, Call> result = new HashMap<>();
		for (String s : extensionPeers.keySet()) {
			Map<String, Call> temporary = extensionPeers.get(s).getConnectedCalls();
			for (String s1 : temporary.keySet()) {
				result.put(s1 ,temporary.get(s1));
			}
		}
		return result;
	}

}

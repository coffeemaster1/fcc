package com.fuze.coreuc.fcc.builders;

import com.fuze.coreuc.fcc.config.MyAppConfig;
import com.fuze.coreuc.fcc.datasource.CfMapper;
import com.fuze.coreuc.fcc.models.Call;
import com.fuze.coreuc.fcc.models.Peer;
import com.google.inject.Inject;
import org.asteriskjava.manager.EventTimeoutException;
import org.asteriskjava.manager.ManagerConnection;
import org.asteriskjava.manager.ResponseEvents;
import org.asteriskjava.manager.TimeoutException;
import org.asteriskjava.manager.action.SipPeersAction;
import org.asteriskjava.manager.event.PeerEntryEvent;
import org.asteriskjava.manager.event.ResponseEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PeerBuilder {

	private static final Logger LOGGER = LoggerFactory.getLogger(PeerBuilder.class);

	private ManagerConnection amiConn;
	private CfMapper mapper;
	private MyAppConfig cfg;

	@Inject
	public PeerBuilder (ManagerConnection amiConn, CfMapper mapper, MyAppConfig cfg) {
		this.amiConn= amiConn;
		this.mapper = mapper;
		this.cfg = cfg;
	}

	public Map<String, Peer> buildGlobalPeerList () throws IOException, EventTimeoutException {

		List<String> pbxs = cfg.pbxs();
		Map<String, Peer> peerList = new HashMap<>();

		for (String pbx : pbxs) {
			peerList.putAll(mapper.getPeersFromPBX(pbx));
		}

		SipPeersAction peersAction = new SipPeersAction();
		ResponseEvents peerResponse;

		peerResponse =  amiConn.sendEventGeneratingAction(peersAction, 30000);

		peerList.forEach((k, v) -> v.setOnlineStatus("Unreachable"));

		Collection<ResponseEvent> responseEvents = peerResponse.getEvents();

		for (ResponseEvent responseEvent : responseEvents) {
			if (responseEvent instanceof PeerEntryEvent) {
				String peername = ((PeerEntryEvent) responseEvent).getObjectName();
				if (peerList.containsKey(peername)) {
					if (((PeerEntryEvent) responseEvent).getStatus().contains("OK")) {
						peerList.get(peername).setOnlineStatus("Reachable");
					}
					else {
						peerList.get(peername).setOnlineStatus("Unreachable");
					}
				}
			}
		}

		return peerList;
	}

	public Map<String, Peer> buildPeers (String pbx, Map<String, Peer> peerList, Map<String, Call> activeCalls) throws IOException, TimeoutException {

		Map<String, Peer> pbxPeers = new HashMap<>();

		for (Peer peer : peerList.values()) {
			if (peer.getPbx().equals(pbx)) {
				pbxPeers.put(peer.getPeerName(), peer);
			}
		}
		bindCalls(pbxPeers, activeCalls);

		return pbxPeers;
	}

	public void bindCalls (Map<String, Peer> peers, Map<String, Call> calls) throws IOException, TimeoutException {

		for (String s : calls.keySet()) {
			String thisPeer = calls.get(s).getChannelName().substring(calls.get(s).getChannelName().indexOf("/") + 1, calls.get(s).getChannelName().lastIndexOf("-"));
			if (peers.containsKey(thisPeer)) {
				peers.get(thisPeer).addCall(calls.get(s));
				peers.get(thisPeer).setPeerStatusCode(2);
			}
		}

	}
}

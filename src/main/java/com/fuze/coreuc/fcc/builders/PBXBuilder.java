package com.fuze.coreuc.fcc.builders;

import com.fuze.coreuc.fcc.data.CallDataSource;
import com.fuze.coreuc.fcc.data.PeerDataSource;
import com.fuze.coreuc.fcc.data.QueueDataSource;
import com.fuze.coreuc.fcc.datasource.CfMapper;
import com.fuze.coreuc.fcc.models.*;
import com.google.inject.Inject;
import org.asteriskjava.manager.TimeoutException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class PBXBuilder {

	private static final Logger LOGGER = LoggerFactory.getLogger(PBXBuilder.class);

	private QueueBuilder queueBuilder;
	private CfMapper cfMapper;
	private PeerBuilder peerBuilder;
	private ExtensionBuilder extensionBuilder;
	private CallBuilder callBuilder;
	private CallDataSource globalCalls;
	private PeerDataSource globalPeers;
	private QueueDataSource globalQueues;

	@Inject
	public PBXBuilder (CfMapper mapper, CallBuilder call, PeerBuilder peer, ExtensionBuilder extension, QueueBuilder queue, CallDataSource calls, PeerDataSource peers, QueueDataSource queues) {
		this.cfMapper = mapper;
		this.extensionBuilder = extension;
		this.queueBuilder = queue;
		this.peerBuilder = peer;
		this.callBuilder = call;
		this.globalCalls = calls;
		this.globalPeers = peers;
		this.globalQueues = queues;
	}

	public PBX buildPBX (String pbx ) throws IOException, TimeoutException {

		PBX res = new PBX(pbx);

		List<String> accountCodes = cfMapper.getOrgCodes(pbx);
		Map<String, Call> callList = callBuilder.buildCalls(accountCodes, globalCalls.getGlobalCallMap());
		Map<String, Peer> peerList = peerBuilder.buildPeers(pbx, globalPeers.getGlobalPeerMap() ,callList);
		Map<String, Queue> queueList = queueBuilder.buildQueues(pbx, peerList, globalQueues.getGlobalQueueMap());
		Map<String, Extension> extensionList = extensionBuilder.buildExtensions(pbx, peerList);

		res.addCallMap(callList);
		res.addExtensionMap(extensionList);
		res.addPeerMap(peerList);
		res.addQueueMap(queueList);
		res.setAccountCodes(accountCodes);

		return res;

	}


}

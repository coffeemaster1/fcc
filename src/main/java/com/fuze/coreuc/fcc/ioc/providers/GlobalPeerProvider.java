package com.fuze.coreuc.fcc.ioc.providers;

import com.fuze.coreuc.fcc.builders.PeerBuilder;
import com.fuze.coreuc.fcc.config.MyAppConfig;
import com.fuze.coreuc.fcc.data.PeerDataSource;
import com.google.inject.Inject;
import com.google.inject.Provider;
import org.asteriskjava.manager.EventTimeoutException;

import java.io.IOException;

public class GlobalPeerProvider implements Provider<PeerDataSource> {

	private PeerDataSource globalPeers;

	@Inject
	public GlobalPeerProvider(PeerBuilder peerBuilder) throws IOException, EventTimeoutException {
		globalPeers = new PeerDataSource(peerBuilder.buildGlobalPeerList());
	}

	@Override
	public PeerDataSource get() {
		return globalPeers;
	}
}

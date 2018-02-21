package com.fuze.coreuc.fcc.builders;

import com.fuze.coreuc.fcc.datasource.CfMapper;
import com.fuze.coreuc.fcc.models.Extension;
import com.fuze.coreuc.fcc.models.Peer;
import com.google.inject.Inject;
import org.asteriskjava.manager.TimeoutException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;

public class ExtensionBuilder {

	private static final Logger LOGGER = LoggerFactory.getLogger(ExtensionBuilder.class);

	private CfMapper mapper;

	@Inject
	public ExtensionBuilder (CfMapper mapper) {
		this.mapper = mapper;
	}

	public Map<String, Extension> buildExtensions (String pbx, Map<String, Peer> peerMap) throws IOException, TimeoutException {

		Map<String, Extension> extensionList = mapper.getExtensionsFromPBX(pbx);
		linkExtensionToPeers(peerMap, extensionList);

		return extensionList;

	}

	public void linkExtensionToPeers (Map<String, Peer> peers, Map<String, Extension> extensions) {

		for (String key : peers.keySet()) {
			Peer thisPeer = peers.get(key);
			String peerExten = thisPeer.getExtension();

			if (extensions.containsKey(peerExten)) {
				extensions.get(peerExten).addExtensionPeer(thisPeer);
				if (extensions.get(peerExten).getExtensionStatusCode() == 1) {
					extensions.get(peerExten).setExtensionStatusCode(thisPeer.getPeerStatusCode());
				}
			}
		}
	}
}

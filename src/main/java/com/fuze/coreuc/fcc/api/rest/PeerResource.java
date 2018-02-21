package com.fuze.coreuc.fcc.api.rest;

import com.fuze.coreuc.fcc.data.PBXDataSource;
import com.fuze.coreuc.fcc.models.Peer;
import com.google.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.Collection;


@Path("/pbx/{pbxName}/peers")
public class PeerResource {

	private static final Logger LOGGER = LoggerFactory.getLogger(CallResource.class);

	private PBXDataSource pbxs;

	@Inject
	public PeerResource(PBXDataSource pbxs) {
		this.pbxs = pbxs;
	}

	@GET
	@Path("/")
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<Peer> getCalls(@PathParam("pbxName") String pbxName) {
		return pbxs.getPeersByPbx(pbxName).values();
	}

	@GET
	@Path("/{peerName}")
	@Produces(MediaType.APPLICATION_JSON)
	public Peer getCall(@PathParam("pbxName") String pbxName, @PathParam("peerName") String peerName){
		return pbxs.getPeerByPbx(pbxName, peerName);
	}
}

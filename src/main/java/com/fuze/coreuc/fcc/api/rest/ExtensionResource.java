package com.fuze.coreuc.fcc.api.rest;

import com.fuze.coreuc.fcc.data.PBXDataSource;
import com.fuze.coreuc.fcc.models.Call;
import com.fuze.coreuc.fcc.models.Extension;
import com.fuze.coreuc.fcc.models.PBX;
import com.google.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.Collection;


@Path("/pbx/{pbxName}/extensions")
public class ExtensionResource {

	private static final Logger LOGGER = LoggerFactory.getLogger(ExtensionResource.class);

	private PBXDataSource pbxs;

	@Inject
	public ExtensionResource(PBXDataSource pbxs) {
		this.pbxs = pbxs;
	}

	@GET
	@Path("/")
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<Extension> getExtensions(@PathParam("pbxName") String pbxName) {
		PBX pbx = pbxs.getPbx(pbxName);
		return pbx.getExtensionHashMap().values();
	}

	@GET
	@Path("/{extensionName}")
	@Produces(MediaType.APPLICATION_JSON)
	public Extension getExtension(@PathParam("pbxName") String pbxName, @PathParam("extensionName") String extensionName) {
		return pbxs.getExtensionByPbx(pbxName, extensionName);
	}

	@GET
	@Path("/{extensionName}/calls")
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<Call> getExtensionCalls(@PathParam("pbxName") String pbxName, @PathParam("extensionName") String extensionName){
		return pbxs.getExtensionByPbx(pbxName, extensionName).getCalls().values();
	}

	@GET
	@Path("/{extensionName}/calls/{uniqueID}")
	@Produces(MediaType.APPLICATION_JSON)
	public Call getQueueCall (@PathParam("pbxName") String pbxName, @PathParam("extensionName") String extensionName, @PathParam("uniqueID") String uniqueID) {
		return pbxs.getPbx(pbxName).getCall(uniqueID);
	}
}

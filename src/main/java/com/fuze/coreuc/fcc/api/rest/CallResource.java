package com.fuze.coreuc.fcc.api.rest;

import com.fuze.coreuc.fcc.api.objects.Hangup;
import com.fuze.coreuc.fcc.api.objects.Transfer;
import com.fuze.coreuc.fcc.data.PBXDataSource;
import com.fuze.coreuc.fcc.models.Call;
import com.fuze.coreuc.fcc.models.PBX;
import com.google.inject.Inject;
import org.asteriskjava.manager.ManagerConnection;
import org.asteriskjava.manager.TimeoutException;
import org.asteriskjava.manager.action.HangupAction;
import org.asteriskjava.manager.action.RedirectAction;
import org.asteriskjava.manager.response.ManagerResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.Collection;


@Path("/pbx/{pbxName}/calls")
public class CallResource {

	private static final Logger LOGGER = LoggerFactory.getLogger(CallResource.class);

	private PBXDataSource pbxs;
	private ManagerConnection amiConn;

	@Inject
	public CallResource(PBXDataSource pbxs, ManagerConnection amiConn) {
		this.pbxs = pbxs;
		this.amiConn = amiConn;
	}

	@GET
	@Path("/")
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<Call> getCalls(@PathParam("pbxName") String pbxName) {
		return pbxs.getCallsByPbx(pbxName).values();
	}

	@GET
	@Path("/keys")
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<String> getCallKeys(@PathParam("pbxName") String pbxName) {
		return pbxs.getCallsByPbx(pbxName).keySet();
	}

	@GET
	@Path("/{uniqueId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Call getCall(@PathParam("pbxName") String pbxName, @PathParam("uniqueId") String uniqueId){
		return pbxs.getPbx(pbxName).getCall(uniqueId);
	}

	@POST
	@Path("/transfer")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String transferCall (@PathParam("pbxName") String pbxName, Transfer transfer) throws IOException, TimeoutException {

		if (pbxs.callExistsInPbx(pbxName, transfer.getUniqueId())) {
			RedirectAction transferAction = new RedirectAction();
			transferAction.setChannel(pbxs.getPbx(pbxName).getCall(transfer.getUniqueId()).getChannelName());
			transferAction.setExten(transfer.getDestExten());
			transferAction.setContext(transfer.getContext());
			transferAction.setPriority(1);

			ManagerResponse transferResponse;

			transferResponse = amiConn.sendAction(transferAction, 30000);

			return transferResponse.getMessage();

		}
		return "Error: Call does not exist";
	}

	@POST
	@Path("/hangup")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String hangupCall (@PathParam("pbxName") String pbxName, Hangup hangup) throws IOException, TimeoutException {

		if (pbxs.callExistsInPbx(pbxName, hangup.getUniqueID())) {

			HangupAction hangupAction = new HangupAction();
			hangupAction.setChannel(pbxs.getPbx(pbxName).getCall(hangup.getUniqueID()).getChannelName());

			ManagerResponse hangupResponse;

			hangupResponse = amiConn.sendAction(hangupAction, 30000);

			return hangupResponse.getMessage();
		}
		return "Error: Call does not exist";
	}
}

package com.fuze.coreuc.fcc.api.rest;

import com.fuze.coreuc.fcc.api.objects.QueueAdd;
import com.fuze.coreuc.fcc.api.objects.QueuePause;
import com.fuze.coreuc.fcc.api.objects.QueueRemove;
import com.fuze.coreuc.fcc.data.PBXDataSource;
import com.fuze.coreuc.fcc.models.Queue;
import com.fuze.coreuc.fcc.models.QueueAgent;
import com.fuze.coreuc.fcc.models.QueueCall;
import com.google.inject.Inject;
import org.asteriskjava.manager.ManagerConnection;
import org.asteriskjava.manager.TimeoutException;
import org.asteriskjava.manager.action.QueueAddAction;
import org.asteriskjava.manager.action.QueuePauseAction;
import org.asteriskjava.manager.action.QueueRemoveAction;
import org.asteriskjava.manager.response.ManagerResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.Collection;


@Path("/pbx/{pbxName}/queue")
public class QueueResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(QueueResource.class);

    private PBXDataSource pbxs;
    private ManagerConnection amiConn;

    @Inject
    public QueueResource(PBXDataSource pbxs, ManagerConnection amiConn) {
        this.pbxs = pbxs;
        this.amiConn = amiConn;
    }

    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    public Collection<Queue> getQueues(@PathParam("pbxName") String pbxName) {
        return pbxs.getQueuesByPbx(pbxName).values();
    }

    @GET
    @Path("/{queueName}")
    @Produces(MediaType.APPLICATION_JSON)
    public Queue getQueue(@PathParam("pbxName") String pbxName, @PathParam("queueName") String queueName) {
        return pbxs.getQueueByPbx(pbxName, queueName);
    }

    @GET
	@Path("/{queueName}/agents")
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<QueueAgent> getAgents(@PathParam("pbxName") String pbxName, @PathParam("queueName") String queueName){
		return pbxs.getQueueByPbx(pbxName, queueName).getAgentList().values();
	}

    @GET
	@Path("/{queueName}/agents/{agentName}")
	@Produces(MediaType.APPLICATION_JSON)
	public QueueAgent getAgent(@PathParam("pbxName") String pbxName, @PathParam("queueName") String queueName, @PathParam("agentName") String agentName) {
    	return pbxs.getQueueByPbx(pbxName, queueName).getAgent(agentName);
	}

	@GET
	@Path("/{queueName}/calls")
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<QueueCall> getQueueCalls (@PathParam("pbxName") String pbxName, @PathParam("queueName") String queueName){
		return pbxs.getQueueByPbx(pbxName, queueName).getCallList().values();
	}

	@GET
	@Path("/{queueName}/calls/{uniqueID}")
	@Produces(MediaType.APPLICATION_JSON)
	public QueueCall getQueueCall (@PathParam("pbxName") String pbxName, @PathParam("queueName") String queueName, @PathParam("uniqueID") String uniqueID) {
		return pbxs.getQueueByPbx(pbxName, queueName).getCall(uniqueID);
	}

	@POST
	@Path("/add")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String addAgent (@PathParam("pbxName") String pbxName, QueueAdd agent) throws IOException, TimeoutException {

		if (pbxs.queueExistsInPbx(pbxName, agent.getQueueName())) {
			QueueAddAction addAction = new QueueAddAction();
			addAction.setMemberName(agent.getMemberName());
			addAction.setInterface(agent.getInterfaceName());
			addAction.setPaused(agent.getPaused());
			addAction.setQueue(agent.getQueueName());
			addAction.setPenalty(agent.getPenalty());
			addAction.setStateInterface(agent.getMemberName());

			ManagerResponse addResponse;

			addResponse = amiConn.sendAction(addAction, 30000);

			return addResponse.getMessage();

		}

		return "Error: queue doesn't exist";
	}

	@POST
	@Path("/remove")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String removeAgent (@PathParam("pbxName") String pbxName, QueueRemove agent) throws IOException, TimeoutException {

		if (pbxs.queueExistsInPbx(pbxName, agent.getQueueName())) {
			if (pbxs.agentExistsInQueue(pbxName, agent.getQueueName(), agent.getInterfaceName())) {

				QueueRemoveAction removeAction = new QueueRemoveAction();
				removeAction.setInterface(pbxs.getQueueByPbx(pbxName, agent.getQueueName()).getAgent(agent.getInterfaceName()).getLocation());
				removeAction.setQueue(agent.getQueueName());

				ManagerResponse addResponse;

				addResponse = amiConn.sendAction(removeAction, 30000);

				return addResponse.getMessage();
			}
			return "Error: agent doesn't exist in queue given";
		}

		return "Error: queue doesn't exist";
	}

	@POST
	@Path("/pause")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String removeAgent (@PathParam("pbxName") String pbxName, QueuePause agent) throws IOException, TimeoutException {

		if (pbxs.queueExistsInPbx(pbxName, agent.getQueueName())) {
			if (pbxs.agentExistsInQueue(pbxName, agent.getQueueName(), agent.getInterfaceName())) {

				QueuePauseAction pauseAction = new QueuePauseAction();
				pauseAction.setInterface(pbxs.getQueueByPbx(pbxName, agent.getQueueName()).getAgent(agent.getInterfaceName()).getLocation());
				pauseAction.setQueue(agent.getQueueName());
				pauseAction.setPaused(agent.getPaused());
				pauseAction.setReason(agent.getPauseReason());

				ManagerResponse addResponse;

				addResponse = amiConn.sendAction(pauseAction, 30000);

				return addResponse.getMessage();
			}
			return "Error: agent doesn't exist in queue given";
		}

		return "Error: queue doesn't exist";
	}
}

package com.fuze.coreuc.fcc.data;

import com.fuze.coreuc.fcc.models.Peer;
import com.fuze.coreuc.fcc.models.Queue;
import com.fuze.coreuc.fcc.models.QueueAgent;
import com.fuze.coreuc.fcc.models.QueueCall;
import org.asteriskjava.manager.event.QueueMemberStatusEvent;

import java.util.Map;

public class QueueDataSource {

	private Map<String, Queue> globalQueueMap;

	public QueueDataSource(Map<String, Queue> globalQueueMap) {
		this.globalQueueMap = globalQueueMap;
	}

	public Map<String, Queue> getGlobalQueueMap() {
		return globalQueueMap;
	}

	public void setGlobalQueueMap(Map<String, Queue> globalQueueMap) {
		this.globalQueueMap = globalQueueMap;
	}

	public boolean queueExists (String queue) {
		return globalQueueMap.containsKey(queue);
	}

	public boolean agentExists (String queue, String agent) {
		return globalQueueMap.get(queue).getAgentList().containsKey(agent);
	}

	public void addAgent (String queue, QueueAgent agent) {
		globalQueueMap.get(queue).addAgent(agent);
	}

	public void setAgentPeer (String queue, Peer peer){
		globalQueueMap.get(queue).getAgent(peer.getPeerName()).setPeer(peer);
	}

	public Queue getQueue (String queue) {
		return globalQueueMap.get(queue);
	}

	public void addQueueCall (String queue, QueueCall call) {
		globalQueueMap.get(queue).addCall(call);
	}

	public void removeQueueCall (String queue, String uniqueid) {
		globalQueueMap.get(queue).removeCall(uniqueid);
	}

	public void incrementAbandoned(String queue){
		globalQueueMap.get(queue).incrementAbandonedCalls();
	}

	public void incrementCompleted(String queue){
		globalQueueMap.get(queue).incrementCompletedCalls();
	}

	public void removeAgent (String queue, String agent) {
		globalQueueMap.get(queue).removeAgent(agent);
	}

	public void togglePauseAgent (String queue, String agent, Boolean paused, String pauseReason) {
		if (pauseReason == null)
			pauseReason = "";
		globalQueueMap.get(queue).getAgent(agent).setPaused(paused);
		globalQueueMap.get(queue).getAgent(agent).setPauseReason(pauseReason);
	}
	public void setAgentPenalty (String queue, String agent, Integer penalty) {
		globalQueueMap.get(queue).getAgent(agent).setPenalty(penalty);
	}
	public void updateAgentStatus (String queue, String agent, QueueMemberStatusEvent event) {
		globalQueueMap.get(queue).getAgent(agent).updateAgentStatus(event);
	}

	public void updateAgentState (String queue, String agent, Integer state) {
		globalQueueMap.get(queue).getAgent(agent).setAgentStatusCode(state);
	}

	public void updateAgentCallerID (String queue, String agent, String callerIDName, String callerIDNum) {
		globalQueueMap.get(queue).getAgent(agent).setConnectedCallerIDName(callerIDName);
		globalQueueMap.get(queue).getAgent(agent).setConnectedCallerIDNum(callerIDNum);
	}

}

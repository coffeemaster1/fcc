package com.fuze.coreuc.fcc.models;

import com.fuze.coreuc.fcc.util.StatusConverter;
import org.asteriskjava.manager.event.AgentCalledEvent;
import org.asteriskjava.manager.event.QueueMemberAddedEvent;
import org.asteriskjava.manager.event.QueueMemberEvent;
import org.asteriskjava.manager.event.QueueMemberStatusEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class QueueAgent {

	private transient StatusConverter statusConverter = new StatusConverter();

	private Integer penalty;
	private String membership;
	private String pauseReason;
	private Boolean isPaused;
	private Integer callsTaken;
	private String stateInterface;
	private Integer inCall;
	private String peerName;
	private String location;
	private Long lastCall;
	private String memberName;
	private String queue;
	private Integer agentStatusCode;
	private String agentStatus;
	private String connectedCallerIDName;
	private String connectedCallerIDNum;
	private Integer pauseTime;

	private Peer peer;

	public QueueAgent(Integer penalty, String membership, Integer callsTaken, String peerName, String location, String queue, Integer agentStatusCode) {
		this.penalty = penalty;
		this.membership = membership;
		this.callsTaken = callsTaken;
		this.peerName = peerName;
		this.location = location;
		this.queue = queue;
		setAgentStatusCode(agentStatusCode);
	}

	public QueueAgent(QueueMemberEvent memberEvent){

		this.penalty = memberEvent.getPenalty();
		this.membership = memberEvent.getMembership();
		this.callsTaken = memberEvent.getCallsTaken();
		this.peerName = memberEvent.getName().substring(memberEvent.getName().indexOf("/") + 1);
		this.location = memberEvent.getLocation();
		this.queue = memberEvent.getQueue();
		setAgentStatusCode(memberEvent.getStatus());
		this.pauseReason = memberEvent.getPausedreason();
		this.stateInterface = memberEvent.getStateinterface();
		this.lastCall = memberEvent.getLastCall();
		this.memberName = memberEvent.getMemberName();
		this.inCall = memberEvent.getIncall();
		this.isPaused = memberEvent.getPaused();
	}

	public QueueAgent (QueueMemberAddedEvent event){
		this.penalty = event.getPenalty();
		this.membership = event.getMembership();
		this.callsTaken = event.getCallsTaken();
		this.peerName = event.getMemberName().substring(event.getMemberName().indexOf("/") + 1);
		this.location = event.getLocation();
		this.queue = event.getQueue();
		setAgentStatusCode(event.getStatus());
		this.stateInterface = event.getStateinterface();
		this.lastCall = event.getLastCall();
		this.memberName = event.getMemberName();
		this.isPaused = event.getPaused();
	}

	public void updateLastCall () {
		this.lastCall = (System.currentTimeMillis() / 1000L);
	}

	public String getAgentStatus() {
		return agentStatus;
	}

	public Integer getPenalty() {
		return penalty;
	}

	public void setPenalty(Integer penalty) {
		this.penalty = penalty;
	}

	public String getMembership() {
		return membership;
	}

	public void setMembership(String membership) {
		this.membership = membership;
	}

	public String getPauseReason() {
		return pauseReason;
	}

	public void setPauseReason(String pauseReason) {
		this.pauseReason = pauseReason;
	}

	public Integer getCallsTaken() {
		return callsTaken;
	}

	public void setCallsTaken(Integer callsTaken) {
		this.callsTaken = callsTaken;
	}

	public String getStateInterface() {
		return stateInterface;
	}

	public void setStateInterface(String stateInterface) {
		this.stateInterface = stateInterface;
	}

	public Integer isInCall() {
		return inCall;
	}

	public void setInCall(Integer inCall) {
		this.inCall = inCall;
	}

	public String getPeerName() {
		return peerName;
	}

	public void setPeerName(String peerName) {
		this.peerName = peerName;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getMemberName() {
		return memberName;
	}

	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}

	public String getQueue() {
		return queue;
	}

	public void setQueue(String queue) {
		this.queue = queue;
	}

	public Integer getAgentStatusCode() {
		return agentStatusCode;
	}

	public void setAgentStatusCode(Integer agentStatusCode) {
		this.agentStatusCode = agentStatusCode;
		this.agentStatus = statusConverter.getQueueStatusString(agentStatusCode);
	}

	public Long getLastCall() {
		return lastCall;
	}

	public void setLastCall(Long lastCall) {
		this.lastCall = lastCall;
	}

	public Integer getInCall() {
		return inCall;
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

	public Peer getPeer() {
		return peer;
	}

	public void setPeer(Peer peer) {
		this.peer = peer;
	}

	public Integer getPauseTime() {
		return pauseTime;
	}

	public void setPauseTime(Integer pauseTime) {
		this.pauseTime = pauseTime;
	}

	public Boolean getPaused() {
		return isPaused;
	}

	public void setPaused(Boolean paused) {
		isPaused = paused;
	}

	public void addCall(Call call) {
		this.peer.addCall(call);
	}

	public void removeCall (String uniqueid) {
		this.peer.removeCall(uniqueid);
	}

	public Map<String, Call> getCalls () {
		return this.peer.getConnectedCalls();
	}

	public void updateAgentStatus (QueueMemberStatusEvent event) {
		this.penalty = event.getPenalty();
		this.pauseReason = event.getPausedreason();
		this.callsTaken = event.getCallsTaken();
		this.lastCall = event.getLastCall();
		setAgentStatusCode(event.getStatus());
	}

}

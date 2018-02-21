package com.fuze.coreuc.fcc.manager;

import com.fuze.coreuc.fcc.data.CallDataSource;
import com.fuze.coreuc.fcc.data.PBXDataSource;
import com.fuze.coreuc.fcc.data.PeerDataSource;
import com.fuze.coreuc.fcc.data.QueueDataSource;
import com.fuze.coreuc.fcc.models.*;
import com.google.inject.Inject;
import org.asteriskjava.manager.ManagerEventListener;
import org.asteriskjava.manager.event.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ManagerEventProcessor implements ManagerEventListener {

	private static final Logger LOGGER = LoggerFactory.getLogger(ManagerEventProcessor.class);

	private PBXDataSource pbxMap;
	private CallDataSource globalCalls;
	private PeerDataSource globalPeers;
	private QueueDataSource globalQueues;

	@Inject
	public ManagerEventProcessor (PBXDataSource pbxMap, CallDataSource globalCalls, PeerDataSource globalPeers, QueueDataSource globalQueues) {
		this.pbxMap = pbxMap;
		this.globalCalls = globalCalls;
		this.globalPeers = globalPeers;
		this.globalQueues = globalQueues;
	}

	@Override
	public void onManagerEvent(ManagerEvent managerEvent) {

		if (managerEvent instanceof VarSetEvent || managerEvent instanceof NewExtenEvent || managerEvent instanceof AgiExecEvent) {
			return;
		}
		if (managerEvent instanceof QueueEvent) {
			String queueName = ((QueueEvent) managerEvent).getQueue();
			if (globalQueues.queueExists(queueName)) {
				if (managerEvent instanceof JoinEvent) {
					globalQueues.addQueueCall(queueName, new QueueCall((JoinEvent) managerEvent));
				}
				else if (managerEvent instanceof LeaveEvent) {
					globalQueues.removeQueueCall(queueName, ((LeaveEvent) managerEvent).getUniqueId());
				}
				else if (managerEvent instanceof QueueCallerAbandonEvent) {
					globalQueues.incrementAbandoned(queueName);
				}
			}
		}
		else if (managerEvent instanceof AbstractQueueMemberEvent) {
			String queueName = ((AbstractQueueMemberEvent) managerEvent).getQueue();

			if (globalQueues.queueExists(queueName)) {
				String agentName = stripTechQueue(((AbstractQueueMemberEvent) managerEvent).getMemberName());
				if (managerEvent instanceof QueueMemberAddedEvent && !globalQueues.agentExists(queueName, agentName)) {
					QueueAgent agent = new QueueAgent((QueueMemberAddedEvent) managerEvent);
					if (globalPeers.peerExists(agentName)) {
						agent.setPeer(globalPeers.getPeer(agentName));
					}
					globalQueues.addAgent(queueName, agent);
				}
				if (globalQueues.agentExists(queueName, agentName)) {
					if (managerEvent instanceof QueueMemberRemovedEvent) {
						globalQueues.removeAgent(queueName, agentName);
					} else if (managerEvent instanceof QueueMemberPausedEvent) {
						globalQueues.togglePauseAgent(queueName, agentName, ((QueueMemberPausedEvent) managerEvent).getPaused(), ((QueueMemberPausedEvent) managerEvent).getReason());
					}
				}
			}
		}
		else if (managerEvent instanceof QueueMemberEvent){
			String queueName = ((QueueMemberEvent) managerEvent).getQueue();
			if (globalQueues.queueExists(queueName)) {
				String agentName = stripTechQueue(((QueueMemberEvent) managerEvent).getName());
				if (globalQueues.agentExists(queueName, agentName)) {
					if (managerEvent instanceof QueueMemberStatusEvent) {
						globalQueues.updateAgentStatus(queueName, agentName, (QueueMemberStatusEvent) managerEvent);
					}
				}
			}
		}
		else if (managerEvent instanceof AbstractAgentEvent) {
			String queueName = ((AbstractAgentEvent) managerEvent).getQueue();
			if (globalQueues.queueExists(queueName)) {
				String agentName = stripTechQueue(((AbstractAgentEvent) managerEvent).getMember());
				if (globalQueues.agentExists(queueName, agentName)) {
					if (managerEvent instanceof AgentConnectEvent) {
						String uniqueId = ((AgentConnectEvent) managerEvent).getBridgedChannel();
						globalQueues.updateAgentState(queueName, agentName, 2);
						globalQueues.updateAgentCallerID(queueName, agentName, globalCalls.getCallerIDName(uniqueId), globalCalls.getCallerIDNum(uniqueId));
					}
					else if (managerEvent instanceof AgentCompleteEvent) {
						globalQueues.incrementCompleted(queueName);
						globalQueues.updateAgentCallerID(queueName, agentName, "", "");
					}
				}
			}
		}
		else if (managerEvent instanceof AgentCalledEvent) {
			String queueName = ((AgentCalledEvent) managerEvent).getQueue();
			if (globalQueues.queueExists(queueName)) {
				String agentName = stripTechQueue(((AgentCalledEvent) managerEvent).getAgentCalled());
				if (globalQueues.agentExists(queueName, agentName)) {
					globalQueues.updateAgentCallerID(queueName, agentName, managerEvent.getCallerIdName(), managerEvent.getCallerIdName());
				}
			}
		}
		else if (managerEvent instanceof AbstractChannelStateEvent) {
			String uniqueId = ((AbstractChannelStateEvent) managerEvent).getUniqueId();
			String peer = stripTech(((AbstractChannelStateEvent) managerEvent).getChannel());
			AbstractChannelStateEvent event = (AbstractChannelStateEvent) managerEvent;

			if (event.getChannel().substring(0, 9).equals("AsyncGoto")) {
				event.setChannel(event.getChannel().substring(event.getChannel().indexOf("/") + 1));
			}

			if (globalCalls.callExists(uniqueId)) {
				if (event instanceof NewStateEvent) {
					globalCalls.setCallState(uniqueId, event.getChannelState());
					globalCalls.setChannelStatus(uniqueId, event.getChannelStateDesc());
					if (event.getConnectedLineName() != null && !event.getConnectedLineName().isEmpty()) {
						globalCalls.setConnectedLineName(uniqueId, event.getConnectedLineName());
					}
					if (event.getConnectedLineNum() != null && !event.getConnectedLineNum().isEmpty()) {
						globalCalls.setConnectedLineNum(uniqueId, event.getConnectedLineNum());
					}

					if (globalPeers.peerExists(peer)) {
						globalPeers.setPeerStatus(peer, event.getChannelState());
						globalPeers.updatePeerConnectedLine(peer, uniqueId);

						if (pbxMap.PbxExists(globalPeers.getPbxFromPeer(peer))) {
							pbxMap.updateExtensionStatusByPeer(globalPeers.getPbxFromPeer(peer), globalPeers.getPeer(peer), event.getChannelState());
							pbxMap.updateExtensionConnectedLine(globalPeers.getPbxFromPeer(peer), globalPeers.getPeer(peer));
						}
					}
				}
				else if (event instanceof HangupEvent) {
					//String linkedID = globalCalls.getCall(uniqueId).getLinkedID();

					globalPeers.removeCallFromPeer(globalCalls.getPeer(uniqueId), uniqueId);
					pbxMap.removeCallFromAccountCode(globalCalls.getAccountCode(uniqueId) , uniqueId);
					globalCalls.removeCall(uniqueId);

					//currently, there are consistently 2 hangups being sent for all calls so we don't need this
					/*if (!uniqueId.equals(linkedID) && globalCalls.callExists(linkedID)) {
						globalPeers.removeCallFromPeer(globalCalls.getPeer(linkedID), linkedID);
						pbxMap.removeCallFromAccountCode(globalCalls.getAccountCode(linkedID) , linkedID);
						globalCalls.removeCall(linkedID);
					}*/
				}
			}
			else {
				if (event instanceof NewChannelEvent) {
					globalCalls.addCall(new Call((NewChannelEvent) event));
					if (pbxMap.containsAccountCode(event.getAccountCode())) {
						pbxMap.addCallToAccountCode(event.getAccountCode(), globalCalls.getCall(uniqueId));
						if (globalPeers.peerExists(peer)) {
							globalPeers.addCallToPeer(peer, globalCalls.getCall(uniqueId));
						}
					}
				}
				else if (event instanceof NewStateEvent) {
					String linkedId =  ((NewStateEvent) event).getLinkedId();
					if (!linkedId.equals(event.getUniqueId()) && globalCalls.callExists(linkedId)) {
						String connectedLineNumber = globalCalls.getConnectedLineNumber(linkedId);
						String connectedLineName = globalCalls.getConnectedLineName(linkedId);

						if (connectedLineNumber == null || connectedLineNumber.isEmpty() || connectedLineNumber.equals(globalCalls.getExtension(linkedId))) {
							globalCalls.setConnectedLineNum(linkedId, event.getCallerIdNum());
						}
						if (connectedLineName == null || connectedLineName.isEmpty() || connectedLineName.equals(globalCalls.getExtension(linkedId))) {
							globalCalls.setConnectedLineName(linkedId, event.getCallerIdName());
						}
						globalPeers.updatePeerConnectedLine(stripTech(globalCalls.getChannel(linkedId)),linkedId);

					}
				}
			}

		}
		else if (managerEvent instanceof NewAccountCodeEvent) {
			String uniqueId = ((NewAccountCodeEvent) managerEvent).getUniqueId();
			String accountCode = ((NewAccountCodeEvent) managerEvent).getAccountCode();

			if(globalCalls.callExists(uniqueId)){
				globalCalls.setAccountCode(uniqueId, accountCode);
				if (pbxMap.containsAccountCode(accountCode) && !pbxMap.callExistsInAccountCode(accountCode, uniqueId)) {
					pbxMap.addCallToAccountCode(accountCode, globalCalls.getCall(uniqueId));
				}
			}
		}
		else if (managerEvent instanceof BridgeEvent) {
			String channel1 = ((BridgeEvent) managerEvent).getChannel1();
			String channel2 = ((BridgeEvent) managerEvent).getChannel2();
			String uniqueId1 = ((BridgeEvent) managerEvent).getUniqueId1();
			String uniqueId2 = ((BridgeEvent) managerEvent).getUniqueId2();

			if (globalCalls.callExists(uniqueId1)) {
				globalCalls.getGlobalCallMap().get(uniqueId1).setBridgedChannel(channel2);
				globalCalls.getGlobalCallMap().get(uniqueId1).setLinkedID(uniqueId2);
			}
			if (globalCalls.callExists(uniqueId2)) {
				globalCalls.getGlobalCallMap().get(uniqueId2).setBridgedChannel(channel1);
				globalCalls.getGlobalCallMap().get(uniqueId2).setLinkedID(uniqueId1);
			}

		}
		else if (managerEvent instanceof PeerStatusEvent) {
			String peerName = stripTechQueue(((PeerStatusEvent) managerEvent).getPeer());
			String status = ((PeerStatusEvent) managerEvent).getPeerStatus();
			if (globalPeers.peerExists(peerName)) {
				if (status.equals("Unregistered") || status.equals("Unreachable")) {
					globalPeers.setConnectedStatus(peerName, "Unreachable");
				}
				else if (status.equals("Registered") || status.equals("Reachable")) {
					globalPeers.setConnectedStatus(peerName, "Reachable");
				}
			}
		}
		else if (managerEvent instanceof TransferEvent) {

			TransferEvent event = (TransferEvent) managerEvent;
			if (event.isAttended()) {
				String uniqueId = event.getUniqueId();
				String targetId = event.getTargetUniqueId();
				String linkedId = globalCalls.getLinkedId(uniqueId);
				String targetLinkedId = globalCalls.getLinkedId(targetId);

				if (globalCalls.callExists(uniqueId) && globalCalls.callExists(targetId)) {

					//update the uniqueid and linked id in the transferee call object
					globalCalls.setLinkedId(linkedId, globalCalls.getLinkedId(targetId));
					globalCalls.setUniqueId(linkedId, targetId);

					//remove the transferer call from the transferer peer
					globalPeers.removeCallFromPeer(globalCalls.getPeer(targetId),targetId);

					//remove the transferer call from the pbx call list and then add the transferee call in it's place
					pbxMap.removeCallFromPBX(globalPeers.getPbxFromPeer(globalCalls.getPeer(targetId)), targetId);
					pbxMap.addCallToPbx(globalPeers.getPbxFromPeer(globalCalls.getPeer(targetId)), globalCalls.getCall(linkedId));

					//remove the transferer call from the global call list and replace with the transferee call
					globalCalls.removeCall(targetId);
					globalCalls.addCall(globalCalls.getCall(linkedId));

					//remove the original transferee call object from the pbx call list, peer call list, and global call list
					pbxMap.removeCallFromPBX(globalPeers.getPbxFromPeer(globalCalls.getPeer(linkedId)), linkedId);
					if (globalPeers.peerExists(globalCalls.getPeer(linkedId))) {
						globalPeers.removeCallFromPeer(globalCalls.getPeer(linkedId), linkedId);
					}
					globalCalls.removeCall(linkedId);

					//add the new transferee call object to the peer associated with it
					if (globalPeers.peerExists(globalCalls.getPeer(targetId))) {
						globalPeers.addCallToPeer(globalCalls.getPeer(targetId), globalCalls.getCall(targetId));
					}

					//update the callerid/connected caller values for newly masqueraded channel
					globalCalls.setConnectedLineName(targetLinkedId, globalCalls.getCallerIDName(targetId));
					globalCalls.setConnectedLineNum(targetLinkedId, globalCalls.getCallerIDNum(targetId));
					globalCalls.setConnectedLineName(targetId, globalCalls.getCallerIDName(targetLinkedId));
					globalCalls.setConnectedLineNum(targetId, globalCalls.getCallerIDNum(targetLinkedId));

					globalPeers.updatePeerConnectedLine(globalCalls.getPeer(targetId), targetId);
					globalPeers.updatePeerConnectedLine(globalCalls.getPeer(targetLinkedId), targetLinkedId);

				}
			}
		}
	}

	public String stripTech (String channel) {
		return channel.substring(channel.lastIndexOf("/") + 1, channel.lastIndexOf("-"));
	}

	public String stripTechQueue (String membername) {
		if (membername.contains("Local/")) {
			return membername.substring(membername.indexOf("/") + 1, membername.indexOf("@"));
		}
		else {
			return membername.substring(membername.indexOf("/") + 1);
		}
	}

}

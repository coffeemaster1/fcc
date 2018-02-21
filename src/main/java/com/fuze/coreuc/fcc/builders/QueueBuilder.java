package com.fuze.coreuc.fcc.builders;

import com.fuze.coreuc.fcc.config.MyAppConfig;
import com.fuze.coreuc.fcc.datasource.CfMapper;
import com.fuze.coreuc.fcc.models.Peer;
import com.fuze.coreuc.fcc.models.Queue;
import com.fuze.coreuc.fcc.models.QueueAgent;
import com.fuze.coreuc.fcc.models.QueueCall;
import com.google.inject.Inject;
import org.asteriskjava.manager.EventTimeoutException;
import org.asteriskjava.manager.ManagerConnection;
import org.asteriskjava.manager.ResponseEvents;
import org.asteriskjava.manager.action.QueueStatusAction;
import org.asteriskjava.manager.event.QueueEntryEvent;
import org.asteriskjava.manager.event.QueueMemberEvent;
import org.asteriskjava.manager.event.QueueParamsEvent;
import org.asteriskjava.manager.event.ResponseEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QueueBuilder {

	private static final Logger LOGGER = LoggerFactory.getLogger(QueueBuilder.class);

	private ManagerConnection amiConn;
	private CfMapper mapper;
	private MyAppConfig cfg;

	@Inject
	public QueueBuilder (ManagerConnection amiConn, CfMapper mapper, MyAppConfig cfg) {
		this.amiConn = amiConn;
		this.mapper = mapper;
		this.cfg = cfg;
	}

	public Map<String, Queue> buildQueues (String pbx, Map<String, Peer> peers, Map<String, Queue> queues) throws IOException, EventTimeoutException {

		Map<String, Queue> queueList = new HashMap<>();
		QueueStatusAction queueAction;
		ResponseEvents queueResponse;

		 for (Queue queue : queues.values()) {
			if (queue.getPbx().equals(pbx)) {
				queueAction = new QueueStatusAction();
				queueAction.setQueue(queue.getQueueName());

				queueResponse = amiConn.sendEventGeneratingAction(queueAction, 30000);

				Collection<ResponseEvent> events = queueResponse.getEvents();

				for (ResponseEvent event : events) {
					if (event instanceof QueueParamsEvent) {
						queue.buildQueueFromEvent((QueueParamsEvent) event);
					} else if (event instanceof QueueEntryEvent) {
						queue.addCall(new QueueCall((QueueEntryEvent) event));
					} else if (event instanceof QueueMemberEvent) {
						QueueAgent agent = new QueueAgent((QueueMemberEvent) event);
						if (peers.containsKey(agent.getPeerName())) {
							agent.setPeer(peers.get(agent.getPeerName()));
						}
						queue.addAgent(agent);
					}
				}
				queueList.put(queue.getQueueName(), queue);
			}
		}
		return queueList;
	}

	public Map<String, Queue> buildGlobalQueues () {

		Map<String, Queue> queueList = new HashMap<>();

		for (String s : cfg.pbxs()) {
			queueList.putAll(mapper.getQueuesFromPBX(s + "%"));

		}
		return queueList;
	}
}

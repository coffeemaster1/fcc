package com.fuze.coreuc.fcc.server;

import com.fuze.coreuc.fcc.api.ContactCenterApi;
import com.fuze.coreuc.fcc.manager.ManagerEventProcessor;
import com.google.inject.Inject;
import org.asteriskjava.manager.ManagerConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ContactCenterServer extends Thread {

	private static final Logger LOGGER = LoggerFactory.getLogger(ContactCenterServer.class);

	private ManagerConnection managerConnection;
	private ContactCenterApi contactCenterApi;
	private ManagerEventProcessor eventProcessor;

	@Inject
	public ContactCenterServer(ManagerConnection ami, ContactCenterApi api, ManagerEventProcessor eventProcessor) {
		this.managerConnection = ami;
		this.contactCenterApi = api;
		this.eventProcessor = eventProcessor;
	}

	@Override
	public void run () {
		managerConnection.addEventListener(eventProcessor);
		contactCenterApi.start();

	}

}

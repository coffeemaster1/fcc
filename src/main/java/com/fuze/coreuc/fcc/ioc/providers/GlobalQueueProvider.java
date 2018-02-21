package com.fuze.coreuc.fcc.ioc.providers;

import com.fuze.coreuc.fcc.builders.QueueBuilder;
import com.fuze.coreuc.fcc.data.QueueDataSource;
import com.google.inject.Inject;
import com.google.inject.Provider;

public class GlobalQueueProvider implements Provider<QueueDataSource>{

	private QueueDataSource globalQueues;

	@Inject
	public GlobalQueueProvider(QueueBuilder queue) {
		globalQueues = new QueueDataSource(queue.buildGlobalQueues());
	}

	@Override
	public QueueDataSource get() {
		return globalQueues;
	}
}

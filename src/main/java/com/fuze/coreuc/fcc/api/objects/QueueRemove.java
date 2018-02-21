package com.fuze.coreuc.fcc.api.objects;

public class QueueRemove {

	private String queueName;
	private String interfaceName;

	public QueueRemove(String queueName, String interfaceName) {
		this.queueName = queueName;
		this.interfaceName = interfaceName;
	}

	public String getQueueName() {
		return queueName;
	}

	public void setQueueName(String queueName) {
		this.queueName = queueName;
	}

	public String getInterfaceName() {
		return interfaceName;
	}

	public void setInterfaceName(String interfaceName) {
		this.interfaceName = interfaceName;
	}
}

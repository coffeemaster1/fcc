package com.fuze.coreuc.fcc.api.objects;

public class QueuePause {

	private String queueName;
	private String interfaceName;
	private Boolean paused;
	private String pauseReason;

	public QueuePause(String queueName, String interfaceName, Boolean paused, String pauseReason) {
		this.queueName = queueName;
		this.interfaceName = interfaceName;
		this.paused = paused;
		this.pauseReason = pauseReason;
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

	public Boolean getPaused() {
		return paused;
	}

	public void setPaused(Boolean paused) {
		this.paused = paused;
	}

	public String getPauseReason() {
		return pauseReason;
	}

	public void setPauseReason(String pauseReason) {
		this.pauseReason = pauseReason;
	}
}

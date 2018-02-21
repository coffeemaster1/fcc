package com.fuze.coreuc.fcc.api.objects;

public class QueueAdd {
	private String memberName;
	private String queueName;
	private Boolean paused;
	private String interfaceName;
	private Integer penalty;

	public QueueAdd(String memberName, String queueName, Boolean paused, String interfaceName, Integer penalty) {
		this.memberName = memberName;
		this.queueName = queueName;
		this.paused = paused;
		this.interfaceName = interfaceName;
		this.penalty = penalty;
	}

	public String getMemberName() {
		return memberName;
	}

	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}

	public String getQueueName() {
		return queueName;
	}

	public void setQueueName(String queueName) {
		this.queueName = queueName;
	}

	public Boolean getPaused() {
		return paused;
	}

	public void setPaused(Boolean paused) {
		this.paused = paused;
	}

	public String getInterfaceName() {
		return interfaceName;
	}

	public void setInterfaceName(String interfaceName) {
		this.interfaceName = interfaceName;
	}

	public Integer getPenalty() {
		return penalty;
	}

	public void setPenalty(Integer penalty) {
		this.penalty = penalty;
	}
}

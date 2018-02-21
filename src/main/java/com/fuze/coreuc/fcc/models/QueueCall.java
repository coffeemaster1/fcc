package com.fuze.coreuc.fcc.models;

import org.asteriskjava.manager.event.JoinEvent;
import org.asteriskjava.manager.event.QueueEntryEvent;

public class QueueCall extends Call {

	private Long waitingTime;
	private Integer enterTime;
	private Integer position;
	private String queue;

	public QueueCall (String callerIDName, String callerIDNumber, String channelName, String uniqueID, Long waitingTime, Integer enterTime, Integer position, String queue){

		this.setCallerIDName(callerIDName);
		this.setCallerIDNumber(callerIDNumber);
		this.setChannelName(channelName);
		this.setUniqueID(uniqueID);
		this.waitingTime = waitingTime;
		this.enterTime = (int) ((System.currentTimeMillis() / 1000L) - waitingTime);
		this.position = position;
		this.queue = queue;
	}

	public QueueCall (QueueEntryEvent event) {

		this.setCallerIDName(event.getCallerIdName());
		this.setCallerIDNumber(event.getCallerIdNum());
		this.setChannelName(event.getChannel());
		this.setUniqueID(event.getUniqueId());
		this.waitingTime = event.getWait();
		this.enterTime = (int) ((System.currentTimeMillis() / 1000L) - waitingTime);
		this.position = event.getPosition();
		this.queue = event.getQueue();
	}

	public QueueCall (JoinEvent event) {

		this.setCallerIDName(event.getCallerIdName());
		this.setCallerIDNumber(event.getCallerIdNum());
		this.setChannelName(event.getChannel());
		this.setUniqueID(event.getUniqueId());
		this.waitingTime = (long) 0;
		this.enterTime = (int) ((System.currentTimeMillis() / 1000L) - waitingTime);
		this.position = event.getPosition();
		this.queue = event.getQueue();
	}

	public Long getWaitingTime() {
		return waitingTime;
	}

	public void setWaitingTime(Long waitingTime) {
		this.waitingTime = waitingTime;
	}

	public Integer getEnterTime() {
		return enterTime;
	}

	public void setEnterTime(Integer enterTime) {
		this.enterTime = enterTime;
	}

	public Integer getPosition() {
		return position;
	}

	public void setPosition(Integer position) {
		this.position = position;
	}

	public String getQueue() {
		return queue;
	}

	public void setQueue(String queue) {
		this.queue = queue;
	}

	@Override
	public String toString() {
		return "QueueCall{" +
				"waitingTime=" + waitingTime +
				", enterTime=" + enterTime +
				", position=" + position +
				", queue='" + queue + '\'' +
				'}';
	}
}

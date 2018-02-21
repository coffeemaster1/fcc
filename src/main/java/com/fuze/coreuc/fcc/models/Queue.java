package com.fuze.coreuc.fcc.models;

import org.asteriskjava.manager.event.QueueParamsEvent;

import java.util.HashMap;
import java.util.Map;

public class Queue {

	private String queueName;
	private Integer totalCalls;
	private Integer completedCalls;
	private Integer abandonedCalls;
	private Integer talkTime;
	private Integer calls;
	private Integer serviceLevel;
	private Integer holdTime;
	private Integer max;
	private Integer weight;
	private Double serviceLevelPerf;
	private String strategy;
	private String pbx;

	private Map<String, QueueAgent> agentsInQueue = new HashMap<>();
	private Map<String, QueueCall> callsWaiting = new HashMap<>();

	public Queue(String queueName, String pbx) {
		this.queueName = queueName;
		this.pbx = pbx;
		this.totalCalls = 0;
		this.completedCalls = 0;
		this.abandonedCalls = 0;
	}

	public void buildQueueFromEvent(QueueParamsEvent paramsEvent){
		this.setServiceLevel(paramsEvent.getServiceLevel());
		this.setServiceLevelPerf(paramsEvent.getServiceLevelPerf());
		this.setTalkTime(paramsEvent.getTalkTime());
		this.setAbandonedCalls(paramsEvent.getAbandoned());
		this.setHoldTime(paramsEvent.getHoldTime());
		this.setMax(paramsEvent.getMax());
		this.setWeight(paramsEvent.getWeight());
		this.setCompletedCalls(paramsEvent.getCompleted());
		this.setCalls(paramsEvent.getCalls());
		this.setStrategy(paramsEvent.getStrategy());
		this.setTotalCalls(paramsEvent.getAbandoned() + paramsEvent.getCompleted());
	}

	public void addAgent(QueueAgent agent){
		agentsInQueue.put(agent.getPeerName(), agent);
	}

	public void removeAgent(String name){
		agentsInQueue.remove(name);
	}

	public Map<String, QueueAgent> getAgentList () {
		return agentsInQueue;
	}

	public QueueAgent getAgent (String name) {
		return agentsInQueue.get(name);
	}

	public void addCall(QueueCall call){
		callsWaiting.put(call.getUniqueID(), call);
	}

	public void removeCall(String name){
		callsWaiting.remove(name);
	}

	public Map<String, QueueCall> getCallList () {
		return callsWaiting;
	}

	public QueueCall getCall (String name) {
		return callsWaiting.get(name);
	}
	public String getQueueName() {
		return queueName;
	}

	public void setQueueName(String queueName) {
		this.queueName = queueName;
	}

	public Integer getTotalCalls() {
		return totalCalls;
	}

	public void setTotalCalls(Integer totalCalls) {
		this.totalCalls = totalCalls;
	}

	public Integer getCompletedCalls() {
		return completedCalls;
	}

	public void setCompletedCalls(Integer completedCalls) {
		this.completedCalls = completedCalls;
	}

	public Integer getAbandonedCalls() {
		return abandonedCalls;
	}

	public void setAbandonedCalls(Integer abandonedCalls) {
		this.abandonedCalls = abandonedCalls;
	}

	public void incrementAbandonedCalls () {
		this.abandonedCalls++;
		this.totalCalls++;
	}

	public void incrementCompletedCalls () {
		this.completedCalls++;
		this.totalCalls++;
	}

	public Integer getCalls() {
		return calls;
	}

	public void setCalls(Integer calls) {
		this.calls = calls;
	}

	public Integer getServiceLevel() {
		return serviceLevel;
	}

	public void setServiceLevel(Integer serviceLevel) {
		this.serviceLevel = serviceLevel;
	}

	public Integer getHoldTime() {
		return holdTime;
	}

	public void setHoldTime(Integer holdTime) {
		this.holdTime = holdTime;
	}

	public Integer getMax() {
		return max;
	}

	public void setMax(Integer max) {
		this.max = max;
	}

	public Integer getWeight() {
		return weight;
	}

	public void setWeight(Integer weight) {
		this.weight = weight;
	}

	public Double getServiceLevelPerf() {
		return serviceLevelPerf;
	}

	public void setServiceLevelPerf(Double serviceLevelPerf) {
		this.serviceLevelPerf = serviceLevelPerf;
	}

	public String getStrategy() {
		return strategy;
	}

	public void setStrategy(String strategy) {
		this.strategy = strategy;
	}

	public Integer getTalkTime() {
		return talkTime;
	}

	public void setTalkTime(Integer talkTime) {
		this.talkTime = talkTime;
	}

	public String getPbx() {
		return pbx;
	}

	public void setPbx(String pbx) {
		this.pbx = pbx;
	}
}

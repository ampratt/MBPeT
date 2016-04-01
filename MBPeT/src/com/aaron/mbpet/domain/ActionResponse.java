package com.aaron.mbpet.domain;

public class ActionResponse {

	// action name. in case of aggregated, title is "Aggregated Response Time"
	String title;
	
	// only for IND actions. stores the currently received response time
	double currentResponseTime = 0;	//seconds
	
	// every unique response time is added to this to make a sum whole
	double totalResponseTime = 0;	//seconds
	
	// count of responses to divide by to get average
	int totalReponseCount = 0;
	
	// average aggregated response time
	double average;
	
	public ActionResponse(){
	}
	public ActionResponse(String title){
		this.title = title;
	}
	
	/**
	 * Getters and Setters
	 */
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
	public double getCurrentResponseTime() {
		return currentResponseTime;
	}
	public void setCurrentResponseTime(double currentResponseTime) {
		this.currentResponseTime = currentResponseTime;
	}
	
	public double getTotalResponseTime() {
		return totalResponseTime;
	}
	public void setTotalResponseTime(double totalResponseTime) {
		this.totalResponseTime = totalResponseTime;
	}
	public void addToTotalResponseTime(double singleRespTime) {
		this.totalResponseTime += singleRespTime;
	}

	public int getTotalReponseCount() {
		return totalReponseCount;
	}
	public void setTotalReponseCount(int totalReponseCount) {
		this.totalReponseCount = totalReponseCount;
	}
	public void addToTotalResponseCount(int numResponses) {
		this.totalReponseCount += numResponses;
	}

	public double getAverage() {
		return average;
	}
	public void setAverage(double average) {
		this.average = average;
	}
	public double calculateAverage(){
		this.average = totalResponseTime / totalReponseCount;
		return average;
	}
	
}

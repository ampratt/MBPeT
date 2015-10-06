package com.aaron.mbpet.domain;

import java.math.BigDecimal;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


@Entity
public class Parameters {

	@Id
	@GeneratedValue
	private int id;
	
//    @OneToOne	//(fetch=FetchType.EAGER)
//    @JoinColumn(name = "ownersession", referencedColumnName = "ID")
//    private TestSession ownersession;

	@Column(name = "ip")
	private String ip;

	@Column(name = "test_duration")
	private int test_duration;				
	// in seconds
//	
////	@Column(name = "ramp_list")
////	private int[][] ramp_list;
//
////	@Column(name = "TargetResponsTime")
////	private Map<String, ResponseTimes> TargetResponsTime;

	@Column(name = "monitoring_interval")
	private int monitoring_interval;					
	// monitoring interval in seconds

	@Column(name = "mean_user_think_time")
	private int mean_user_think_time;	
	// time in seconds 'users' think between actions

//	@Column(name = "standard_deviation")
	@Column(name = "standard_deviation", precision=10, scale=2)
	private BigDecimal standard_deviation;		
	// time in seconds 'users' think between actions

	
	
	
//	/*
//	 * getters and setters
//	 */
//	public int getId() {
//		return id;
//	}
//
//	public void setId(int id) {
//		this.id = id;
//	}
//
//	public TestSession getOwnersession() {
//		return ownersession;
//	}
//
//	public void setOwnersession(TestSession ownersession) {
//		this.ownersession = ownersession;
//	}
//
//	public String getIp() {
//		return ip;
//	}
//
//	public void setIp(String ip) {
//		this.ip = ip;
//	}
//
//	public int getTest_duration() {
//		return test_duration;
//	}
//
//	public void setTest_duration(int test_duration) {
//		this.test_duration = test_duration;
//	}
//
////	public int[][] getRamp_list() {
////		return ramp_list;
////	}
////
////	public void setRamp_list(int[][] ramp_list) {
////		this.ramp_list = ramp_list;
////	}
//
////	public Map<String, ResponseTimes> getTargetResponsTime() {
////		return TargetResponsTime;
////	}
////
////	public void setTargetResponsTime(Map<String, ResponseTimes> targetResponsTime) {
////		TargetResponsTime = targetResponsTime;
////	}
//
//	public int getInterval() {
//		return interval;
//	}
//
//	public void setInterval(int interval) {
//		this.interval = interval;
//	}
//
//	public int getMean_user_think_time() {
//		return mean_user_think_time;
//	}
//
//	public void setMean_user_think_time(int mean_user_think_time) {
//		this.mean_user_think_time = mean_user_think_time;
//	}
//
//	public Double getStandard_deviation() {
//		return standard_deviation;
//	}
//
//	public void setStandard_deviation(Double standard_deviation) {
//		this.standard_deviation = standard_deviation;
//	}
//


	


	
}

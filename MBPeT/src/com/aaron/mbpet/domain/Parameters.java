package com.aaron.mbpet.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


@Entity
public class Parameters {

	@Id
	@GeneratedValue
	private int id;
	
    @OneToOne	//(fetch=FetchType.EAGER)
    @JoinColumn(name = "ownersession", referencedColumnName = "ID")
    private TestSession ownersession;

	@Column(name = "ip")
	private String ip;

	@Column(name = "test_duration")
	private int test_duration;				
	// in seconds
	
		private String ramp_object_name; 
	
	@Column(name = "ramp_list", columnDefinition="BLOB")	//, columnDefinition="BLOB"
	private ArrayList<ArrayList<Integer>> ramp_list;	//int[][]

	@Column(name = "TargetResponseTime", columnDefinition="BLOB")
	private Map<String, HashMap<String, Double>> TargetResponseTime;
	
	@Column(name = "monitoring_interval")
	private int monitoring_interval;					
	// monitoring interval in seconds

	@Column(name = "mean_user_think_time")
	private int mean_user_think_time;	
	// time in seconds 'users' think between actions

//	@Column(name = "standard_deviation")
	@Column(name = "standard_deviation", precision=10, scale=2)
	private double standard_deviation;		
	// time in seconds 'users' think between actions

	
	public Parameters() {
		
	}
	
	public Parameters(String ip, int test_duration,int monitoring_interval, 
			int mean_user_think_time, double d, TestSession ownersession) {
		this.ip = ip;
		this.test_duration = test_duration;
		this.monitoring_interval = monitoring_interval;
		this.mean_user_think_time = mean_user_think_time;
		this.standard_deviation = d;
		this.ownersession = ownersession;
	}
	
	public Parameters(String ip, int test_duration, ArrayList<ArrayList<Integer>> ramp_list, 
			int monitoring_interval, int mean_user_think_time, double d, TestSession ownersession) {
		this.ip = ip;
		this.test_duration = test_duration;
		this.ramp_list = ramp_list;
		this.monitoring_interval = monitoring_interval;
		this.mean_user_think_time = mean_user_think_time;
		this.standard_deviation = d;
		this.ownersession = ownersession;
	}
	
	
	
	/*
	 * getters and setters
	 */
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public TestSession getOwnersession() {
		return ownersession;
	}

	public void setOwnersession(TestSession ownersession) {
		this.ownersession = ownersession;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public int getTest_duration() {
		return test_duration;
	}

	public void setTest_duration(int test_duration) {
		this.test_duration = test_duration;
	}

	public ArrayList<ArrayList<Integer>> getRamp_list() {
		return ramp_list;
	}

	public void setRamp_list(ArrayList<ArrayList<Integer>> ramp_list) {
		this.ramp_list = ramp_list;
	}

	public Map<String, HashMap<String, Double>> getTargetResponsTime() {
		return TargetResponseTime;
	}

	public void setTargetResponsTime(Map<String, HashMap<String, Double>> map) {
		TargetResponseTime = map;
	}

	public int getInterval() {
		return monitoring_interval;
	}

	public void setInterval(int monitoring_interval) {
		this.monitoring_interval = monitoring_interval;
	}

	public int getMean_user_think_time() {
		return mean_user_think_time;
	}

	public void setMean_user_think_time(int mean_user_think_time) {
		this.mean_user_think_time = mean_user_think_time;
	}

	public Double getStandard_deviation() {
		return standard_deviation;
	}

	public void setStandard_deviation(Double standard_deviation) {
		this.standard_deviation = standard_deviation;
	}



	


	
}

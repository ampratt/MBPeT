package com.aaron.mbpet.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
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

	@Column(name = "settings_file", columnDefinition="TEXT")	//, columnDefinition="MEDIUMTEXT"
	private String settings_file;
	
	
	// DSTAT settings
	@Column(name = "dstat_mode")
	private String dstat_mode;
	
	@Column(name = "host")
	private String host;
	
//	@Column(name = "username")
	private String username;
	
//	@Column(name = "password")
	private String password;
	
	@Column(name = "user_types")
	private String user_types;

	@Column(name = "models_folder")
	private String models_folder;
	
	@Column(name = "test_report_folder")
	private String test_report_folder;

	@Column(name = "ip")
	private String ip;

	
	
	// TEST settings
	@Column(name = "test_duration")
	private int test_duration;				// in seconds
	
	
	@Column(name = "monitoring_interval")
	private int monitoring_interval;		//	in seconds

	
	@Column(name = "mean_user_think_time")
	private int mean_user_think_time;		// in seconds 'users' think between actions

	
	@Column(name = "standard_deviation", precision=10, scale=2)
	private double standard_deviation;		// in seconds 'users' think between actions

	
	@Column(name = "ramp_list")				//, columnDefinition="BLOB"
	private String ramp_list;
	//	private ArrayList<ArrayList<Integer>> ramp_list;	//int[][]
	//	private String ramp_object_name; 
	
//	@Column(name = "TargetResponseTime")	//, columnDefinition="BLOB")
    @OneToMany(mappedBy = "parentparameter")
	private List<TRT> target_response_times;
	//	private Map<String, HashMap<String, Double>> TargetResponseTime;
	//	private String responseTime_object_name;

    
    
	
	/*
	 * Constructors
	 */
	public Parameters() {
		this.models_folder = "models/";
		this.test_report_folder = "test_reports/";
	}
	public Parameters(TestSession ownersession) {
		this.ownersession = ownersession;
		this.models_folder = "models/";
		this.test_report_folder = "test_reports/";
	}
	
	public Parameters(String ip, int test_duration,int monitoring_interval, 
			int mean_user_think_time, double standard_deviation, TestSession ownersession) {
		this.ip = ip;
		this.test_duration = test_duration;
		this.monitoring_interval = monitoring_interval;
		this.mean_user_think_time = mean_user_think_time;
		this.standard_deviation = standard_deviation;
		this.ownersession = ownersession;
	}
	
//	public Parameters(String ip, int test_duration, ArrayList<ArrayList<Integer>> ramp_list, 
//			int monitoring_interval, int mean_user_think_time, double d, TestSession ownersession) {
//		this.ip = ip;
//		this.test_duration = test_duration;
//		this.ramp_list = ramp_list;
//		this.monitoring_interval = monitoring_interval;
//		this.mean_user_think_time = mean_user_think_time;
//		this.standard_deviation = d;
//		this.ownersession = ownersession;
//	}
	
	
	
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

	public String getSettings_file() {
		return settings_file;
	}

	public void setSettings_file(String settings_file) {
		this.settings_file = settings_file;
	}
	
	
	
	
	
	public String getDstat_mode() {
		return dstat_mode;
	}
	
	public void setDstat_mode(String dstat_mode) {
		this.dstat_mode = dstat_mode;
	}
	
	public String getHost() {
		return host;
	}
	
	public void setHost(String host) {
		this.host = host;
	}
	
	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getUser_types() {
		return user_types;
	}
	
	public void setUser_types(String user_types) {
		this.user_types = user_types;
	}
	
	public String getModels_folder() {
		return models_folder;
	}
	
	public void setModels_folder(String models_folder) {
		this.models_folder = models_folder;
	}
	
	public String getTest_report_folder() {
		return test_report_folder;
	}
	
	public void setTest_report_folder(String test_report_folder) {
		this.test_report_folder = test_report_folder;
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


	public int getMonitoring_interval() {
		return monitoring_interval;
	}

	public void setMonitoring_interval(int monitoring_interval) {
		this.monitoring_interval = monitoring_interval;
	}

	public int getMean_user_think_time() {
		return mean_user_think_time;
	}

	public void setMean_user_think_time(int mean_user_think_time) {
		this.mean_user_think_time = mean_user_think_time;
	}

	public double getStandard_deviation() {
		return standard_deviation;
	}

	public void setStandard_deviation(double standard_deviation) {
		this.standard_deviation = standard_deviation;
	}

	public String getRamp_list() {
		return ramp_list;
	}

	public void setRamp_list(String ramp_list) {
		this.ramp_list = ramp_list;
	}

	public List<TRT> getTarget_response_times() {
		return target_response_times;
	}

	public void setTarget_response_times(List<TRT> trt) {
		target_response_times = trt;
	}



	public void addTRT(TRT trt) {
		System.out.println("TRT LIST before adding: " + getTarget_response_times().size());

  	  	this.target_response_times.add(trt);		
//  	  	setSessions(this.sessions);
		System.out.println("TRT LIST after adding: " + getTarget_response_times().size());
	}
	
	public void removeTRT(TRT trt) {
		System.out.println("TRT LIST before removing: " + getTarget_response_times().size());
		
		// copy all wanted items to new list and leave behind 'removed' items
		List<TRT> newList = new ArrayList<TRT>();
		for (TRT t : target_response_times) {
			System.out.println("comparing: " + t.getId() + " ->" + trt.getId());
			System.out.println("comparing: " + t.getAction() + " ->" + trt.getAction());				
			if (t.getId() != trt.getId()) {
				// add to new list
				newList.add(t);
				System.out.println("NEW LIST size: " + newList.size());				
			}
		}
		this.target_response_times.clear();
		System.out.println("TRT LIST after clear: " + getTarget_response_times().size());
		
		setTarget_response_times(newList);
		System.out.println("TRT LIST after removing: " + getTarget_response_times().size());
		
	}
	
	
	public void updateTRTData(TRT currTrt) {
		System.out.println("BEFORE UPDATE");
		for (TRT t : target_response_times) {			
			System.out.println("container title: " + t.getAction());
		}
		// get index before renaming/removing
		removeTRT(currTrt);
		
		// add renamed session back at same index
//		sessions.add(index, session);
		addTRT(currTrt);
		
		System.out.println("AFTER UPDATE");
//		sortSessions();
		for (TRT t : target_response_times) {			
			System.out.println("container title: " + t.getAction());
		}
		
	}
	


	
}

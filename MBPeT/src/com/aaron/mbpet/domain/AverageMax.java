package com.aaron.mbpet.domain;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

public class AverageMax<K, V> {
	
//	@Column(name = "responsetimes")
	private Map<String, Double> averageMax = new HashMap<String, Double>();
//	private String key;
//	
//	private Double value;
	

//	private Map<K, V> responsetimes2 = new HashMap<K, V>();

	
	public AverageMax() {
//		this.responsetimes.put(key, value);
	}
	public AverageMax(String key, double value) {
		this.averageMax.put(key, value);
	}
	
	public AverageMax(Map<String, Double> map) {
		this.averageMax = map;
	}
	
//	public AverageMax<String, Double> setAverageMax(String key, Double value){
//		this.averageMax.put(key, value);
//		String k = averageMax.get(key);
//		
//		return key, value;
//	}
	
	/*
	 * getters and setters
	 */
//	public String getKey() {
//		return key;
//	}
//
//	public void setKey(String key) {
//		this.key = key;
//	}
//
//	public Double getValue() {
//		return value;
//	}
//
//	public void setValue(Double value) {
//		this.value = value;
//	}

	public Map<String, Double> getaverageMax() {
		return averageMax;
	}

	public void setaverageMax(String key, double value) {
		this.averageMax.put(key, value);
	}
	public void put(String key, double value) {
		this.averageMax.put(key, value);
	}

	
}

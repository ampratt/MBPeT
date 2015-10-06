package com.aaron.mbpet.domain;

import java.util.HashMap;
import java.util.Map;

public class ResponseTimes {

	private String key;
	
	private Double value;
	
	private Map<String, Double> responsetimes = new HashMap<String, Double>();

	
	/*
	 * getters and setters
	 */
	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public Double getValue() {
		return value;
	}

	public void setValue(Double value) {
		this.value = value;
	}

	public Map<String, Double> getResponsetimes() {
		return responsetimes;
	}

	public void setResponsetimes(Map<String, Double> responsetimes) {
		this.responsetimes = responsetimes;
	}

	
}

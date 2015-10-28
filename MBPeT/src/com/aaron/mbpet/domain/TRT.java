package com.aaron.mbpet.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

@Entity
public class TRT {

	@Id
	@GeneratedValue
	private int id;
	
	@NotNull
	@Column(name = "action")
	private String action;
	
	@NotNull
	@Column(name = "average", precision=10, scale=2)
	private double average;

	@NotNull
	@Column(name = "max")
	private int max;

    @ManyToOne	
    @JoinColumn(name = "parentparameter", referencedColumnName = "ID")
    private Parameters parentparameter;
	


	/*
	 * Constructors
	 */
	public TRT() {

	}

	public TRT(String action, double average, int max) {
		this.action = action;
		this.average = average;
		this.max = max;
	}

	
	
	/*
	 * getters and setters
	 */
	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public double getAverage() {
		return average;
	}

	public void setAverage(double average) {
		this.average = average;
	}

	public int getMax() {
		return max;
	}

	public void setMax(int max) {
		this.max = max;
	}
	
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Parameters getParentparameter() {
		return parentparameter;
	}

	public void setParentparameter(Parameters parentparameter) {
		this.parentparameter = parentparameter;
	}
	
	
	
	
}

package com.aaron.mbpet.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
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
//	@Min(0) @Max(10)	@Column(name = "max")
	@Column(name = "max", precision=10, scale=2)
	private double max;

    @ManyToOne	
    @JoinColumn(name = "parentparameter", referencedColumnName = "ID")
    private Parameters parentparameter;
	


	/*
	 * Constructors
	 */
	public TRT() {

	}

	public TRT(String action, double average, double max) {
		this.action = action;
		this.average = average;
		this.max = max;
	}
	public TRT(String action, double average, double max, Parameters parentparameter) {
		this.action = action;
		this.average = average;
		this.max = max;
		this.parentparameter = parentparameter;
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

	public double getMax() {
		return max;
	}

	public void setMax(double max) {
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

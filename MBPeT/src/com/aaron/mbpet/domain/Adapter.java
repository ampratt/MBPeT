package com.aaron.mbpet.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Entity
public class Adapter {

	@Id
	@GeneratedValue
	private int id;
	
	@OneToOne	//(fetch=FetchType.EAGER)
	@JoinColumn(name = "ownersession", referencedColumnName = "ID")
	private TestSession ownersession;
	
	@Column(name = "adapter_file", columnDefinition="TEXT")	//, columnDefinition="MEDIUMTEXT"
	private String adapter_file;
	
	
	/*
	 * Constructors
	 */
	public Adapter(){
		
	}
	public Adapter(TestSession ownersession, String adapter_file) {
		this.ownersession = ownersession;
		this.adapter_file = adapter_file;

	}
	
	
	/*
	 * Getters and Setters
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
	
	public String getAdapter_file() {
		return adapter_file;
	}
	public void setAdapter_file(String adapter_file) {
		this.adapter_file = adapter_file;
	}
	
}

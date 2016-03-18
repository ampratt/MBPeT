package com.aaron.mbpet.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Entity
public class AdapterXML {

	@Id
	@GeneratedValue
	private int id;
	
	@OneToOne	//(fetch=FetchType.EAGER)
	@JoinColumn(name = "ownersession", referencedColumnName = "ID")
	private TestSession ownersession;
	
	@Column(name = "adapterxml_file", columnDefinition="TEXT")	//, columnDefinition="MEDIUMTEXT"
	private String adapterxml_file;
	
	
	/*
	 * Constructors
	 */
	public AdapterXML(){
		
	}
	public AdapterXML(TestSession ownersession, String adapterxml_file) {
		this.ownersession = ownersession;
		this.adapterxml_file = adapterxml_file;

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
	
	public String getAdapterXML_file() {
		return adapterxml_file;
	}
	public void setAdapterXML_file(String adapterxml_file) {
		this.adapterxml_file = adapterxml_file;
	}
	
}

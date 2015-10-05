package com.aaron.mbpet.domain;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
public class Model {
 
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @NotNull
    @Size(min = 1, max = 40)
    private String title;
    
    @Column(columnDefinition="TEXT")
    private String dotschema;
    
//    @ManyToMany	//(fetch = FetchType.EAGER)
//    @JoinColumn(name="testcase_fk", insertable=false, updatable=false)
    @NotNull
    @ManyToOne
    @JoinColumn(name = "parentsession", referencedColumnName = "ID")
    private TestSession parentsession;
    
    @NotNull
    @ManyToOne
    @JoinColumn(name = "parentsut", referencedColumnName = "ID")
    private TestCase parentsut;
    
    
	public Model() {
    }
    	
	public Model(String title) {	//User owner
//		super();
		this.title = title;
	}

	public Model(String title, TestSession parentsession) {	//User owner
//		super();
		this.title = title;
		this.parentsession = parentsession;
	}
	
	public Model(String title, TestSession parentsession, TestCase parentsut) {	//User owner
//		super();
		this.title = title;
		this.parentsession = parentsession;
		this.parentsut = parentsut;
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

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDotschema() {
		return dotschema;
	}

	public void setDotschema(String dotschema) {
		this.dotschema = dotschema;
	}


	public TestSession getParentsession() {
		return parentsession;
	}

	public void setParentsession(TestSession parentsession) {
		this.parentsession = parentsession;
	}
	
	public TestCase getParentsut() {
		return parentsut;
	}

	public void setParentsut(TestCase parentsut) {
		this.parentsut = parentsut;
	}

	
}
package com.aaron.mbpet.domain;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
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
    private String schema;
    
    @ManyToMany	//(fetch = FetchType.EAGER)
    @JoinColumn(name = "parentsession", referencedColumnName = "ID")
//    @JoinColumn(name="testcase_fk", insertable=false, updatable=false)
    private List<TestSession> parentsession;
    
    
	public Model() {
    }
    	
	public Model(String title) {	//User owner
//		super();
		this.title = title;
	}
	
	public Model(String title, List<TestSession> parentsession) {	//User owner
//		super();
		this.title = title;
		this.parentsession = parentsession;
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

	public String getSchema() {
		return schema;
	}

	public void setSchema(String schema) {
		this.schema = schema;
	}

	public List<TestSession> getParentsession() {
		return parentsession;
	}

	public void setParentsession(List<TestSession> parentsession) {
		this.parentsession = parentsession;
	}

	
}
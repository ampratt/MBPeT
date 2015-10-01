package com.aaron.mbpet.domain;

import java.util.List;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
public class TestSession {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @NotNull
    @Size(min = 1, max = 40)
    private String title;
    
    @ManyToOne	//(fetch = FetchType.EAGER)
    @JoinColumn(name = "parentcase", referencedColumnName = "ID")
//    @JoinColumn(name="testcase_fk", insertable=false, updatable=false)
    private TestCase parentcase;

    @ManyToMany
    private List<Model> model;
    
    private String parameters;
    
    
    public TestSession() {
    }
    	
	public TestSession(String title) {	//User owner
//		super();
		this.title = title;
//		this.parentcase = parentcase;
//		this.owner = owner;
	}
	public TestSession(String title, TestCase parentcase) {	//User owner
//		super();
		this.title = title;
		this.parentcase = parentcase;
//		this.owner = owner;
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

//	 @ManyToOne(fetch=FetchType.EAGER)
//	 @JoinColumn(name="TESTCASE_ID")
	 public TestCase getParentcase() {
		return parentcase;
	}

	public void setParentcase(TestCase parentcase) {
		this.parentcase = parentcase;
	}

	public String getParameters() {
		return parameters;
	}

	public void setParameters(String parameters) {
		this.parameters = parameters;
	}

}

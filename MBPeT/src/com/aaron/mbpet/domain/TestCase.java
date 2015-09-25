package com.aaron.mbpet.domain;

import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Index;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;

@Entity
//@Table(name = "testcase",
//		indexes = {@Index(name = "tc_title_index", columnList="title", unique=true)}) 
public class TestCase {

//    @Column(name="ID")
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @NotNull
    @Column(unique=true)
    @Size(min = 1, max = 40)
    private String title;
    
    @Size(max = 60)
    private String description;
    
//	@NotNull
	@ManyToOne
	private User owner;

    @OneToMany(mappedBy = "parentcase", fetch=FetchType.EAGER)
//    @JoinColumn(name="testcase_fk") //we need to duplicate the physical information
    private List<TestSession> sessions;


    
    public TestCase() {
    }
    	
	public TestCase(String title, String description) {	//User owner
//		super();
		this.title = title;
		this.description = description;
//		this.owner = owner;
//		this.sessions = sessions;
	}
	public TestCase(String title, String description, User owner) {
//		super();
		this.title = title;
		this.description = description;
		this.owner = owner;
//		this.sessions = sessions;
	}

	/*
     * getters and setters
     */
//    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
//    @Column(name="TESTCASE_ID")
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public User getOwner() {
		return owner;
	}

	public void setOwner(User owner) {
		this.owner = owner;
	}

//    @OneToMany(fetch=FetchType.LAZY,cascade=CascadeType.ALL,mappedBy="parentcase")
    public List<TestSession> getSessions() {
		return sessions;
	}

	public void setSessions(List<TestSession> sessions) {
		this.sessions = sessions;
	}
 
	
	public void addSession(TestSession session) {
  	  	// update parent TestCase to add Session(s) to existing list
//  	  	List<TestSession> listofsessions = this.getSessions();
  	  	this.sessions.add(session);		//sessions.getItem(id).getEntity()
  	  	this.setSessions(this.sessions);
	}

	public void removeSession(TestSession session) {
  	  	// update parent TestCase to add Session(s) to existing list
//  	  	List<TestSession> listofsessions = this.getSessions();
  	  	this.sessions.remove(session);		//sessions.getItem(id).getEntity()
  	  	this.setSessions(this.sessions);
	}
	
	public void addSessions(List<TestSession> sessions) {
  	  	// update parent TestCase to add Session(s) to existing list
//  	  	List<TestSession> listofsessions = this.getSessions();
  	  	for (TestSession s : sessions) {
  	  		this.sessions.add(s);		//sessions.getItem(id).getEntity()
  	  	}
  	  	this.setSessions(this.sessions);
	}


}

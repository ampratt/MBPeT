package com.aaron.mbpet.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EntityManager;
import javax.persistence.Index;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;

//@Table(name = "testcase",
//		indexes = {@Index(name = "tc_title_index", columnList="title", unique=true)}) 
@Entity
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
    @JoinColumn(name = "owner", referencedColumnName = "ID")
	private User owner;

    @OneToMany(mappedBy = "parentcase")	//, fetch=FetchType.EAGER
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
  	  	this.sessions.add(session);		
//  	  	setSessions(this.sessions);
	}

	public void removeSession(TestSession session) {
		System.out.println("SESSION LIST before removing: " + getSessions().size());
		
		// copy all wanted items to new list and leave behind 'removed' items
		List<TestSession> newList = new ArrayList<TestSession>();
		for (TestSession s : sessions) {
			if (s.getId() == session.getId()) {
				// do nothing
			} else {
				// add to new list
				newList.add(s);
				System.out.println("NEW LIST size: " + newList.size());				
			}
		}
		this.sessions.clear();
		System.out.println("SESSION LIST after clear: " + getSessions().size());
		
		setSessions(newList);
		System.out.println("SESSION LIST after removing: " + getSessions().size());
		
	}
	
	public void addSessions(List<TestSession> sessions) {
  	  	// update parent TestCase to add Session(s) to existing list
//  	  	List<TestSession> listofsessions = this.getSessions();
  	  	for (TestSession s : sessions) {
  	  		this.sessions.add(s);		//sessions.getItem(id).getEntity()
  	  	}
  	  	this.setSessions(this.sessions);
	}
	
	public void updateSessionData(TestSession session) {
		System.out.println("BEFORE UPDATE");
		for (TestSession s : sessions) {			
			System.out.println("container title: " + s.getTitle());
		}
		// get index before renaming/removing
		removeSession(session);
		
		// add renamed session back at same index
//		sessions.add(index, session);
		addSession(session);
		
		System.out.println("AFTER UPDATE");
//		sortSessions();
		for (TestSession s : sessions) {			
			System.out.println("container title: " + s.getTitle());
		}
	}

	
	public void updateSessions() {
		System.out.println("\nedit mode...");
		EntityManager em = Persistence.createEntityManagerFactory("mbpet")
				.createEntityManager();	
		
        Query query = em.createQuery(
    		    "SELECT OBJECT(t) FROM TestSession t WHERE t.parentcase = :parentcase"
    		);
//	            query.setParameter("title", newsession.getTitle());
        List<TestSession> queriedSessionList = query.setParameter("parentcase", this).getResultList();
        for (TestSession sid : queriedSessionList) {
        	System.out.println("query result object: " + sid.getTitle());
//        	this.sessions.get(0);
        }
//        System.out.println("the generated id is: " + queriedSession.getId());
//        Object id = queriedSession.getId();	// here is the id we need for tree
//        
//		em.getTransaction().begin();
//		em.merge(testsession);
//		em.getTransaction().commit();
//		em.close();
//		
		for (TestSession s : sessions) {			
			System.out.println("container title: " + s.getTitle());
		}
	}
	
	
//	public void sortSessions() {
//	   	 // get session id's and sort them numerically
////	   	 List mylist = new ArrayList(getSessions());
////	   	 Collections.sort(mylist);
////	   	 this.sessions.clear();
////	   	 addSessions(mylist);
//	   	 
//	   	 
//	   	 List<TestSession> caseSessions = getSessions();
//	   	 List<Integer> sortedids = new ArrayList<Integer>(); 
//	   	 for (TestSession s : caseSessions){
//	   		 sortedids.add(s.getId());
//	   	 }
//	   	 Collections.sort(sortedids);
//	   	 
//	   	 // reformat the list by sorted id's
//	   	 this.sessions.clear();
//	   	 for (int id=0; id<caseSessions.size(); id++) {
//	   		 addSession(caseSessions.get(id));
//
////	   		 this.sessions.add(caseSessions.get(id));
//	   	 }
//	   	 
//	}


}

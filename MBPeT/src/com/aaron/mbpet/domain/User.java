package com.aaron.mbpet.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.eclipse.persistence.annotations.Index;
import org.hibernate.validator.constraints.Email;

@Entity
@Index(columnNames={"username", "password"})
public class User implements Serializable { //

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @NotNull
    @Size(min = 1, max = 40)
    private String firstname;

	@Size(max = 40)
    private String lastname;
    
	@Email
	private String email;

	@NotNull
    @Column(unique=true)
    @Size(min = 4, max = 30)
    private String username;
    
    @NotNull
    @Size(min = 6, max = 30)
    private String password;
    
//    @ManyToOne
//    @JoinColumn(name = "ORGANIZATION_ID")
    private String organization;

    @OneToMany(mappedBy = "owner")
    private List<TestCase> cases;
 
    
    public User() {    	
	}
    
    public User(String firstname, String lastname, String username, String password) {
		this.firstname = firstname;
		this.lastname = lastname;
		this.username = username;
		this. password = password;
    	
	}

	/**
     * getters and setters
     */
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

    public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getOrganization() {
		return organization;
	}

	public void setOrganization(String organization) {
		this.organization = organization;
	}

//  @OneToMany(fetch=FetchType.LAZY,cascade=CascadeType.ALL,mappedBy="parentcase")
    public List<TestCase> getTestCases() {
		return cases;
	}

	public void setTestCases(List<TestCase> cases) {
		this.cases = cases;
	}
	
	
	
	public void addCase(TestCase tcase) {
  	  	this.cases.add(tcase);		
//  	  	setSessions(this.sessions);
	}

	public void removeCase(TestCase thiscase) {
		System.out.println("CASES LIST before removing: " + getTestCases().size());
		
		// copy all wanted items to new list and leave behind 'removed' items
		List<TestCase> newList = new ArrayList<TestCase>();
		for (TestCase c : cases) {
			if (c.getId() == thiscase.getId()) {
				// do nothing
			} else {
				// add to new list
				newList.add(c);
				System.out.println("NEW LIST size: " + newList.size());				
			}
		}
		this.cases.clear();
		System.out.println("CASE LIST after clear: " + getTestCases().size());
		
		setTestCases(newList);
		System.out.println("CASE LIST after removing: " + getTestCases().size());
		
	}

	
	
	public void updateCaseData(TestCase tc) {
		System.out.println("BEFORE UPDATE");
		for (TestCase c : cases) {			
			System.out.println("container title: " + c.getTitle());
		}
		// get index before renaming/removing
		removeCase(tc);
		
		// add renamed session back at same index
//		sessions.add(index, session);
		addCase(tc);
		
		System.out.println("AFTER UPDATE");
//		sortSessions();
		for (TestCase c: cases) {			
			System.out.println("container title: " + c.getTitle());
		}
	}


}

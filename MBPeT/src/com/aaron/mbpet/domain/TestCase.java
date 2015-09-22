package com.aaron.mbpet.domain;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;

@Entity
public class TestCase {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Size(min = 1, max = 40)
    private String title;
    
    @Size(max = 40)
    private String description;
    
//	@NotNull
	@ManyToOne
	private User owner;

    @OneToMany(mappedBy = "parentcase")
    private Set<TestSession> sessions;


    
    public TestCase() {
    }
    	
	public TestCase(String title, String description) {	//User owner
//		super();
		this.title = title;
		this.description = description;
//		this.owner = owner;
//		this.sessions = sessions;
	}

	/*
     * getters and setters
     */
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
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

	public Set<TestSession> getSessions() {
		return sessions;
	}

	public void setSessions(Set<TestSession> sessions) {
		this.sessions = sessions;
	}
 


}

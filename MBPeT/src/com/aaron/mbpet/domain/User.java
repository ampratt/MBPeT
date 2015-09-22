package com.aaron.mbpet.domain;

import java.io.Serializable;
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
    private Long id;

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
    private Set<TestCase> cases;
 
    
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
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
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

    


}

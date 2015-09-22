package com.aaron.mbpet.domain;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
public class TestSession {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Size(min = 1, max = 40)
    private String title;
    
    @ManyToOne
    private TestCase parentcase;

    private String parameters;

    
//  @OneToMany(mappedBy = "department")
//  private Set<TestSession> sessions;
    
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

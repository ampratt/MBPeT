package com.aaron.mbpet.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

//@Table(name = "testcase",
//indexes = {@Index(name = "tc_title_index", columnList="title", unique=true)}) 
@Entity
public class TestSession {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @NotNull
//    @Column(unique=true)
    @Size(min = 1, max = 40)
    private String title;
    
    @ManyToOne	//(fetch = FetchType.EAGER)
    @JoinColumn(name = "parentcase", referencedColumnName = "ID")
//    @JoinColumn(name="testcase_fk", insertable=false, updatable=false)
    private TestCase parentcase;

    @OneToMany(mappedBy = "parentsession")
    private List<Model> models;

	@OneToOne(mappedBy = "ownersession")	//(cascade = CascadeType.ALL)
	private Parameters parameters;
    
    
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

	public Parameters getParameters() {
		return parameters;
	}

	public void setParameters(Parameters parameters) {
		this.parameters = parameters;
	}

	
    public List<Model> getModels() {
		return models;
	}

	public void setModels(List<Model> models) {
		this.models = models;
	}
	
	
	
	public void addModel(Model model) {
		System.out.println("MODEL LIST before adding: " + getModels().size());

  	  	this.models.add(model);		
//  	  	setSessions(this.sessions);
		System.out.println("MODEL LIST after adding: " + getModels().size());
	}
	
	public void removeModel(Model model) {
		System.out.println("MODEL LIST before removing: " + getModels().size());
		
		// copy all wanted items to new list and leave behind 'removed' items
		List<Model> newList = new ArrayList<Model>();
		for (Model m : models) {
			if (m.getId() == model.getId()) {
				// do nothing
			} else {
				// add to new list
				newList.add(m);
				System.out.println("NEW LIST size: " + newList.size());				
			}
		}
		this.models.clear();
		System.out.println("MODEL LIST after clear: " + getModels().size());
		
		setModels(newList);
		System.out.println("MODEL LIST after removing: " + getModels().size());
		
	}
	
	
	public void addModels(List<Model> models) {
  	  	// update parent TestCase to add Session(s) to existing list
//  	  	List<TestSession> listofsessions = this.getSessions();
  	  	for (Model m : models) {
  	  		this.models.add(m);		//sessions.getItem(id).getEntity()
  	  	}
  	  	this.setModels(this.models);
	}
	
	public void updateModelData(Model model) {
		System.out.println("BEFORE UPDATE");
		for (Model m : models) {			
			System.out.println("container title: " + m.getTitle());
		}
		// get index before renaming/removing
		removeModel(model);
		
		// add renamed session back at same index
//		sessions.add(index, session);
		addModel(model);
		
		System.out.println("AFTER UPDATE");
//		sortSessions();
		for (Model m : models) {			
			System.out.println("container title: " + m.getTitle());
		}
	}
	
	
//	public void updateParamsData(Parameters param) {
//		
////		removeParameters(param);
//		setParameters(param);
//	}
	
//	public void addParams(Parameters param) {
//  	  	this.models.add(param);		
////  	  	setSessions(this.sessions);
//	}
	
//	public void removeParameters(Parameters param) {		
//		// copy all wanted items to new list and leave behind 'removed' items
//		List<Model> newList = new ArrayList<Model>();
//		for (Model m : models) {
//			if (m.getId() == param.getId()) {
//				// do nothing
//			} else {
//				// add to new list
//				newList.add(m);
//				System.out.println("NEW LIST size: " + newList.size());				
//			}
//		}
//		this.models.clear();
//		System.out.println("MODEL LIST after clear: " + getModels().size());
//		
//		setModels(newList);
//		System.out.println("MODEL LIST after removing: " + getModels().size());
//		
//	}
	
	

}

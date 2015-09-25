package com.aaron.mbpet.ui;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.persistence.Query;

import com.aaron.mbpet.MbpetUI;
import com.aaron.mbpet.domain.TestCase;
import com.aaron.mbpet.domain.TestSession;
import com.aaron.mbpet.domain.User;
import com.aaron.mbpet.views.LoginView;
import com.aaron.mbpet.views.MBPeTMenu;
import com.aaron.mbpet.views.MainView;
import com.aaron.mbpet.views.TestCaseForm;
import com.aaron.mbpet.views.UserForm;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.jpacontainer.JPAContainerFactory;
import com.vaadin.data.Item;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.validator.BeanValidator;
import com.vaadin.data.validator.NullValidator;
import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.server.Resource;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.AbstractTextField;
import com.vaadin.ui.Component;
import com.vaadin.ui.Field;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Tree;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

// Define a sub-window by inheritance
public class TestSessionEditor extends Window implements Button.ClickListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5370960944210111329L;

	private Tree tree;
	private Button createButton;
	private Button cancelButton;
	Field titlefield;
	TextField title; 
	
//	private JPAContainer<TestCase> testcases;
	private JPAContainer<TestSession> sessions;
	BeanItem<TestSession> newSessionItem;
	TestSession newsession;
	private TestCaseForm form;
	FieldGroup binder;
	TestCase parentCase;

	
//	public TestSessionEditor(Tree tree) {		//JPAContainer<TestCase> container
//      this.tree = tree;
//      init();
//	}
	
	public TestSessionEditor(Tree tree, TestCase parent) {		//JPAContainer<TestCase> container
        this.tree = tree;
        // TODO - get parent for display etc.
        this.parentCase = parent;
        init();
	}
	
	public void init() {
//      super("Create a new Test Case"); // Set window caption
      center();
      setResizable(false);
      setClosable(true);
      setModal(true);

      newsession = new TestSession(); 
      this.newSessionItem = new BeanItem<TestSession>(newsession);
//      this.tree = tree;
      sessions = JPAContainerFactory.make(TestSession.class,
      		MbpetUI.PERSISTENCE_UNIT);	//container;
//      this.testcases = MBPeTMenu.testcases;
//      testcases = JPAContainerFactory.make(TestCase.class,
//        		MbpetUI.PERSISTENCE_UNIT);      
//      setContent(buildWindowContent());
      
      setSizeUndefined();
      setContent(AutoGeneratedLayoutDesign()); //ManualLayoutDesign
      setCaption(buildCaption());
	}
	
	
    /**
     * @return the caption of the editor window
     */
    private String buildCaption() {
    	if (!(parentCase == null) ) {		//.getItemProperty("firstname").getValue()
    		return String.format("Add Test Session to: %s", 
    				parentCase.getTitle());		//testcases.getItem(parentCase.getId()).getItemProperty("title")
    	} else {
    		return "Create a new Test Session";
    	}
    }
    
	
    
	private Component AutoGeneratedLayoutDesign() {
		final VerticalLayout layout = new VerticalLayout();
		layout.setSpacing(true);
		layout.setMargin(true);
		
//		layout.addComponent(new Label("<h4>Add Test Session</h4>", ContentMode.HTML));
	
		// set parent Test Case manually without a field
		newsession.setParentcase(parentCase);
		
		binder = new FieldGroup();
//		BeanItem<Person> item = new BeanItem<Person>(person);		// takes item as argument
//		item.addNestedProperty("address.street");	// Address info is not person but address to which person is linked
//		item.addNestedProperty("address.zip");
//		item.addNestedProperty("address.city");
		
		binder.setItemDataSource(newSessionItem); 	// link to data model to binder
		
	//	binder.bindMemberFields(form);	// link to layout
	
		// GENERATE FIELDS
		
	//	for (Object propertyId : item.getItemPropertyIds()) {
	//		if(!"address".equals(propertyId)) {
	//			Field field = binder.buildAndBind(propertyId);
	//			layout.addComponent(field);							
	//		}
	//	}
		
		// using buildAndBind()
		titlefield = binder.buildAndBind("title");
		titlefield.setWidth(18, Unit.EM);
		titlefield.focus();
//		titlefield.setRequired(true);
//		titlefield.setRequiredError("'Title' cannot be empty.");
//		((TextField) titlefield).setValidationVisible(false);
		titlefield.addValidator(new StringLengthValidator("Title must be between 1 and 40 characters", 
					1, 40, false));
		((TextField) titlefield).setNullRepresentation("");
//		
		layout.addComponent(titlefield);
		
		// using bind() to determine what type of field is created yourself...
//		title = new TextField();
//		title.setWidth(18, Unit.EM);
//		title.addValidator(new BeanValidator(TestSession.class, "title"));
//		title.setValidationVisible(false);
//		title.setNullRepresentation("");
//		binder.bind(title, "title");
//		layout.addComponent(title);
		
		
		// button layout
		HorizontalLayout buttons = new HorizontalLayout();
		buttons.setWidth("100%");
		buttons.addStyleName("buttons-margin-top");
		layout.addComponent(buttons);
		
		createButton = new Button("Create", this);
		createButton.addStyleName(ValoTheme.BUTTON_PRIMARY);
		createButton.setClickShortcut(KeyCode.ENTER);
		
		cancelButton = new Button("Cancel", this);
		
		buttons.addComponents(createButton, cancelButton);
		buttons.setComponentAlignment(createButton, Alignment.MIDDLE_LEFT);
		buttons.setComponentAlignment(cancelButton, Alignment.MIDDLE_RIGHT);
		
		return layout;
	}
	
	
	
	   public void buttonClick(ClickEvent event) {
	        if (event.getButton() == createButton) {
//	            editorForm.commit();
//	            fireEvent(new EditorSavedEvent(this, personItem));
					try {
//					form.enableValidationMessages();
//					title.setValidationVisible(true);

					// commit the fieldgroup
					binder.commit();
					
					// add bean object to db through jpa container
	                sessions.addEntity(newSessionItem.getBean());	//jpa container
	                
	                // add created item to tree (after retrieving db generated id)
	                EntityManager em = Persistence.createEntityManagerFactory("mbpet")
	    											.createEntityManager();	
		            Query query = em.createQuery(
		        		    "SELECT OBJECT(t) FROM TestSession t WHERE t.title = :title"
		        		);
//		            query.setParameter("title", newsession.getTitle());
		            TestSession queriedSession = 
		            		(TestSession) query.setParameter("title", newsession.getTitle()).getSingleResult();
		            System.out.println("the generated id is: " + queriedSession.getId());
		            Object id = queriedSession.getId();	// here is the id we need for tree
		            
		            // add to tree
		            tree.addItem(id);
		            tree.setParent(id, parentCase.getId());
	                tree.setChildrenAllowed(id, false);
              	  	tree.setItemCaption(id, sessions.getItem(id).getEntity().getTitle());		//newsession.getTitle()
	            	tree.expandItem(parentCase.getId());
              	  	tree.select(id);
              	  	
              	  	// update parent Case to add Session to testCase List<Session> sessions
              	  	List<TestSession> listofsessions = parentCase.getSessions();
              	  	listofsessions.add(queriedSession);		//sessions.getItem(id).getEntity()
              	  	parentCase.setSessions(listofsessions);
              	  	
	            	System.out.println("WHAT IS NEW LIST OF SESSIONS: " + parentCase.getSessions()); // testing purposes
	            	for (TestSession s : parentCase.getSessions()) {
		            	System.out.println(s.getId() + " - " + s.getTitle()); // testing purposes	            		
	            	}
	            	// nav to created test case
	    			getUI().getNavigator()
	         			.navigateTo(MainView.NAME + "/" + 
     							parentCase.getTitle() + "/" + queriedSession.getTitle());		//sessions.getItem(id).getEntity()
	            	
	            	
		            Notification.show("TEST Session successfully created: " +
		            		"\nid: " + queriedSession.getId() +
		            		"\ntitle: " + queriedSession.getTitle() +
		            		"\nparent case: " + queriedSession.getParentcase().getTitle(),
		            		Type.TRAY_NOTIFICATION);
		            
					} catch (CommitException e) {
						e.printStackTrace();
					}
	            
	        } else if (event.getButton() == cancelButton) {
	        	binder.discard();
	        }
//	        binder.clear();
//	        form.disableValidationMessages();
//	        setTreeItemsExpanded();

			close(); // Close the sub-window
	    }
	
	
	
//	    public void setTreeItemsExpanded() {
//	        // Expand whole tree
//	    	System.out.println(tree.getItemIds());
//	    	System.out.println(tree.rootItemIds());
//		    for (final Object id : tree.rootItemIds()) {
//		    	tree.expandItemsRecursively(id);
//		    	tree.setChildrenAllowed(id, true);
//	        } 
//	    }
	    

}
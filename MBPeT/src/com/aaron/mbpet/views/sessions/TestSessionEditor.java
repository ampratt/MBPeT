package com.aaron.mbpet.views.sessions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NonUniqueResultException;
import javax.persistence.Persistence;
import javax.persistence.Query;

import com.aaron.mbpet.MbpetUI;
import com.aaron.mbpet.domain.Model;
import com.aaron.mbpet.domain.Parameters;
import com.aaron.mbpet.domain.TestCase;
import com.aaron.mbpet.domain.TestSession;
import com.aaron.mbpet.domain.User;
import com.aaron.mbpet.views.LoginView;
import com.aaron.mbpet.views.MBPeTMenu;
import com.aaron.mbpet.views.MainView;
import com.aaron.mbpet.views.cases.TestCaseForm;
import com.aaron.mbpet.views.parameters.ParametersEditor;
import com.aaron.mbpet.views.users.UserForm;
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
import com.vaadin.server.Page;
import com.vaadin.server.Resource;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.shared.Position;
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
import com.vaadin.ui.Table;
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
	private Table table;
	private Button createButton;
	private Button cancelButton;
	Field titlefield;
	TextField title; 
	
//	private JPAContainer<TestCase> testcases;
	private JPAContainer<Model> models;
	private JPAContainer<TestSession> sessions;
	BeanItem<TestSession> newSessionItem;
	TestSession testsession;
	private TestSessionForm form;
	FieldGroup binder;
	TestCase parentCase;
	TestSession clone;
	
	boolean editmode = false;
	boolean navToCasePage = false;
	boolean clonemode = false;

	/*
	 * Create new Test Session
	 */
	public TestSessionEditor(Tree tree, TestCase parentcase) {		//JPAContainer<TestCase> container
//      super("Create a new Test Case"); // Set window caption
		testsession = new TestSession(); 
		this.newSessionItem = new BeanItem<TestSession>(testsession);
		sessions = MBPeTMenu.sessions;	//JPAContainerFactory.make(TestSession.class, MbpetUI.PERSISTENCE_UNIT);	//container;
		
		init(tree, parentcase);
	}

	/*
	 * Edit Mode
	 */
	public TestSessionEditor(Tree tree, Object testsessionid, TestCase parentcase) {		//JPAContainer<TestCase> container
		this.editmode = true;
		this.navToCasePage = false;
        
        sessions = MBPeTMenu.sessions;	//JPAContainerFactory.make(TestSession.class, MbpetUI.PERSISTENCE_UNIT);	//container;
        this.testsession = sessions.getItem(testsessionid).getEntity();
        this.newSessionItem = new BeanItem<TestSession>(testsession);

        init(tree, parentcase);
	}
	
	/*
	 * Edit from TestCase Home Page
	 */
	public TestSessionEditor(Tree tree, Object testsessionid, TestCase parentcase, Table table) {		//JPAContainer<TestCase> container
		this.editmode = true;
		this.navToCasePage = true;
		this.table = table;
		
		sessions = MBPeTMenu.sessions;	//JPAContainerFactory.make(TestSession.class, MbpetUI.PERSISTENCE_UNIT);	//container;
		this.testsession = sessions.getItem(testsessionid).getEntity();
		this.newSessionItem = new BeanItem<TestSession>(testsession);
		
		init(tree, parentcase);
	}
	
	/*
	 * Clone existing test Session to new one
	 */
	public TestSessionEditor(Tree tree, Object testsessionid, TestCase parentcase, Table table, boolean clonemode) {		//JPAContainer<TestCase> container
		this.clonemode = clonemode;
		this.table = table;
		this.navToCasePage = true;
		
		models = MBPeTMenu.models;
		
		sessions = MBPeTMenu.sessions;	//JPAContainerFactory.make(TestSession.class, MbpetUI.PERSISTENCE_UNIT);	//container;
		this.testsession = sessions.getItem(testsessionid).getEntity();
		
		this.clone = new TestSession();
		clone.setTitle("(clone) " + testsession.getTitle());
//		clone.setParameters(testsession.getParameters());
		clone.setParentcase(testsession.getParentcase());

		this.newSessionItem = new BeanItem<TestSession>(clone);
		
//		testsession = new TestSession(); 
//		this.newSessionItem = new BeanItem<TestSession>(testsession);
//		sessions = MBPeTMenu.sessions;	//JPAContainerFactory.make(TestSession.class, MbpetUI.PERSISTENCE_UNIT);	//container;
		
		init(tree, parentcase);
	}
	/*
	 * Clone from tree action
	 */
	public TestSessionEditor(Tree tree, Object testsessionid, TestCase parentcase, boolean clonemode) {		//JPAContainer<TestCase> container
		this.clonemode = clonemode;
		this.navToCasePage = false;
		
		models = MBPeTMenu.models;
		
		sessions = MBPeTMenu.sessions;	//JPAContainerFactory.make(TestSession.class, MbpetUI.PERSISTENCE_UNIT);	//container;
		this.testsession = sessions.getItem(testsessionid).getEntity();
		
		this.clone = new TestSession();
		clone.setTitle("(clone) " + testsession.getTitle());
//		clone.setParameters(testsession.getParameters());
		clone.setParentcase(testsession.getParentcase());

		this.newSessionItem = new BeanItem<TestSession>(clone);
		
//		testsession = new TestSession(); 
//		this.newSessionItem = new BeanItem<TestSession>(testsession);
//		sessions = MBPeTMenu.sessions;	//JPAContainerFactory.make(TestSession.class, MbpetUI.PERSISTENCE_UNIT);	//container;
		
		init(tree, parentcase);
	}
	
	private void init(Tree tree, TestCase parentcase) {
		center();
		setResizable(false);
		setClosable(true);
		setModal(true);

		this.tree = tree;
		this.parentCase = parentcase;
		
        setSizeUndefined();
        setContent(AutoGeneratedLayoutDesign()); //ManualLayoutDesign
        setCaption(buildCaption());
	}
	
	
    /**
     * @return the caption of the editor window
     */
    private String buildCaption() {
    	if (clonemode==true) {
    		return String.format("Clone Test Session: %s", 
    				testsession.getTitle());
    	} else if (editmode) {
    		return String.format("Edit Test Session: %s", 
    				testsession.getTitle());
    	} else if (!(parentCase == null) ) {		//.getItemProperty("firstname").getValue()
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
		if (editmode == false && (clonemode==false)) {
			testsession.setParentcase(parentCase);			
		}
		
		// create fields manually
//		form = new TestSessionForm();
//		layout.addComponent(form);
		
		binder = new FieldGroup();
//		BeanItem<Person> item = new BeanItem<Person>(person);		// takes item as argument
//		item.addNestedProperty("address.street");	// Address info is not person but address to which person is linked
//		item.addNestedProperty("address.zip");
//		item.addNestedProperty("address.city");
		
		binder.setItemDataSource(newSessionItem); 	// link to data model to binder
		
//		binder.bindMemberFields(form);	// link to layout
	
		// GENERATE FIELDS
		
	//	for (Object propertyId : item.getItemPropertyIds()) {
	//		if(!"address".equals(propertyId)) {
	//			Field field = binder.buildAndBind(propertyId);
	//			layout.addComponent(field);							
	//		}
	//	}
		
		// using buildAndBind()
//		titlefield = binder.buildAndBind("title");
//		titlefield.setWidth(18, Unit.EM);
//		titlefield.focus();
//		titlefield.setRequired(true);
////		titlefield.setRequiredError("'Title' cannot be empty.");
////		((TextField) titlefield).setValidationVisible(false);
//		titlefield.addValidator(new BeanValidator(TestSession.class, "title"));
//		((TextField) titlefield).setValidationVisible(true);
////		titlefield.addValidator(new StringLengthValidator("Title must be between 1 and 40 characters", 
////					1, 40, false));
//		((TextField) titlefield).setNullRepresentation("");
////		
//		layout.addComponent(titlefield);
		
		// using bind() to determine what type of field is created yourself...
		title = new TextField();
		binder.bind(title, "title");
		title.setWidth(22, Unit.EM);
		title.setCaption("Title");
		title.focus();
		title.setImmediate(true);
		title.addValidator(new BeanValidator(TestSession.class, "title"));
//		title.setValidationVisible(false);
		title.setNullRepresentation("");
		layout.addComponent(title);
		
		binder.setBuffered(true);
		
		// button layout
		HorizontalLayout buttons = new HorizontalLayout();
		buttons.setWidth("100%");
		buttons.addStyleName("buttons-margin-top");
		layout.addComponent(buttons);
		
		createButton = new Button("Create", this);
		if (editmode) createButton.setCaption("Save");
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
	        	
	        	TestSession queriedSession = null;
	        	
					try {
//						form.enableValidationMessages();
	//					title.setValidationVisible(true);
	

						Object id = null;
						
						// add NEW bean object to db through jpa container
						if (editmode == false && (clonemode == false)) {
							// commit the fieldgroup
							binder.commit();
							
							// add to container
							sessions.addEntity(newSessionItem.getBean());	//jpa container	
							
			                // add created item to tree (after retrieving db generated id)
			                EntityManager em = Persistence.createEntityManagerFactory("mbpet")
			    											.createEntityManager();	
				            Query query = em.createQuery(
				        		    "SELECT OBJECT(t) FROM TestSession t WHERE t.title = :title"
				        		);
		//		            query.setParameter("title", newsession.getTitle());
				            queriedSession = (TestSession) query.setParameter("title", testsession.getTitle()).getSingleResult();
				            System.out.println("the generated id is: " + queriedSession.getId());
				            id = queriedSession.getId();	// here is the id we need for tree
				            
				            // create empty parameters object
				            new ParametersEditor(sessions.getItem(id).getEntity());
		        			
				            // add to tree in right order
				            if ( tree.hasChildren(parentCase.getId()) ) {
				            	sortAddToTree(id);				            	
				            } else {
				            	tree.addItem(id);
				            	tree.setParent(id, parentCase.getId());
				            	tree.setChildrenAllowed(id, false);
				            	tree.setItemCaption(id, sessions.getItem(id).getEntity().getTitle());		//newsession.getTitle()
				            	tree.expandItem(parentCase.getId());
				            	tree.select(id);				            	
				            }
		              	  			              	  	
		              	  	// update parent Case to add Session to testCase List<Session> sessions
		              	  	parentCase.addSession(queriedSession);
		//              	  	List<TestSession> listofsessions = parentCase.getSessions();
		//              	  	listofsessions.add(queriedSession);		//sessions.getItem(id).getEntity()
		//              	  	parentCase.setSessions(listofsessions);
		              	  	
			            	System.out.println("WHAT IS NEW LIST OF SESSIONS: " + parentCase.getSessions()); // testing purposes
			            	for (TestSession s : parentCase.getSessions()) {
				            	System.out.println(s.getId() + " - " + s.getTitle()); // testing purposes	            		
			            	}
			            	
						} else if (editmode == true){
							// EDIT existing object
							
							// commit the fieldgroup
							binder.commit();
							
		              	  	//1 UPDATE parentcase reference
							parentCase.updateSessionData(sessions.getItem(testsession.getId()).getEntity());
							
							System.out.println("Test session is now: " + testsession.getTitle());

							// 2 UPDATE container
							sessions.addEntity(newSessionItem.getBean());
							System.out.println("Entity is now: " + sessions.getItem(testsession.getId()).getEntity().getTitle());

							// 3 UPDATE tree title
		              	  	tree.setItemCaption(testsession.getId(), sessions.getItem(testsession.getId()).getEntity().getTitle());

						} else if (clonemode == true){
							// CLONE 
				           System.out.println("\n\nWE ARE IN CLONE MODE!!!!!!!!!\n\n");

//							TestSession clone = newSessionItem.getBean();
							// 1 commit the fieldgroup
							binder.commit();
							
							// 2 add to container
							sessions.addEntity(newSessionItem.getBean());	//jpa container	
							
			                // 3 retrieving db generated id
			                EntityManager em = Persistence.createEntityManagerFactory("mbpet")
			    											.createEntityManager();	
				            Query query = em.createQuery(
				        		    "SELECT OBJECT(t) FROM TestSession t WHERE t.title = :title");
		//		            query.setParameter("title", newsession.getTitle());
				            queriedSession = (TestSession) query.setParameter("title", clone.getTitle()).getSingleResult();
				            System.out.println("the generated id is: " + queriedSession.getId());
				            id = queriedSession.getId();	// here is the id we need for tree
				            
				            // 4 clone models
				            EntityManager em2 = Persistence.createEntityManagerFactory("mbpet").createEntityManager();	
				            Query query2 = em2.createQuery("SELECT OBJECT(t) FROM Model t WHERE t.title = :title");
				            for (Model m : testsession.getModels()) {
				            	// copy over model values
				            	Model newmodel = new Model("(clone) " + m.getTitle(), queriedSession, m.getParentsut());
				            	newmodel.setDotschema(m.getDotschema());
				            	
								// add to container
								models.addEntity(newmodel);	//jpa container	
								
				                // retrieve generated id from db
					            Model queriedModel = (Model) query2.setParameter("title", newmodel.getTitle()).getSingleResult();
					            System.out.println("the generated id is: " + queriedModel.getId());
			        			
					            // update parent Case to add Session to testCase List<Session> sessions
					            parentCase.addModel(queriedModel);
					            queriedSession.addModel(queriedModel);
				            }
				            sessions.addEntity(queriedSession);

				            // 5 clone parameters
				            String cloneParams = "Fill in parameters for Test Session '" + queriedSession.getTitle() + "'";
				            if (!(testsession.getParameters().getSettings_file() == null) ) {	//|| !(testsession.getParameters().getSettings_file().equals(""))
				            	cloneParams = testsession.getParameters().getSettings_file();
				            }
				            System.out.println("\n\n the cloned parameters are:\n" + 
				            		cloneParams + "\n\n");
				            new ParametersEditor(queriedSession, cloneParams);

		        			
				            // 6 add to tree in right order
				            if ( tree.hasChildren(parentCase.getId()) ) {
				            	sortAddToTree(id);				            	
				            } else {
				            	tree.addItem(id);
				            	tree.setParent(id, parentCase.getId());
				            	tree.setChildrenAllowed(id, false);
				            	tree.setItemCaption(id, sessions.getItem(id).getEntity().getTitle());		//newsession.getTitle()
				            	tree.expandItem(parentCase.getId());
				            	tree.select(id);				            	
				            }
		        			
		              	  			              	  	
		              	  	// 7 update parent Case to add Session to testCase List<Session> sessions
		              	  	parentCase.addSession(queriedSession);
		//              	  	List<TestSession> listofsessions = parentCase.getSessions();
		//              	  	listofsessions.add(queriedSession);		//sessions.getItem(id).getEntity()
		//              	  	parentCase.setSessions(listofsessions);
		              	  	
			            	System.out.println("WHAT IS NEW LIST OF SESSIONS: " + parentCase.getSessions()); // testing purposes
			            	for (TestSession s : parentCase.getSessions()) {
				            	System.out.println(s.getId() + " - " + s.getTitle()); // testing purposes	            		
			            	}
						}
		                

	              	  	
		            	System.out.println("WHAT IS NEW LIST OF SESSIONS: " + parentCase.getSessions()); // testing purposes
		            	for (TestSession s : parentCase.getSessions()) {
			            	System.out.println(s.getId() + " - " + s.getTitle()); // testing purposes	            		
		            	}
		            	
		            	
		            	if (clonemode==true && navToCasePage == true) {
		            		confirmNotification(queriedSession.getTitle(), "was created");
		            		close();
		            		
		            	} else if (clonemode==true && navToCasePage == false) {
		            		confirmNotification(queriedSession.getTitle(), "was CLONED");
		            		getUI().getNavigator()
	            				.navigateTo(MainView.NAME + "/" + 
	            						parentCase.getTitle() + "/" + queriedSession.getTitle());		//sessions.getItem(id).getEntity()		            		
		            		close();

		            	} else if ( navToCasePage==true && editmode==false ) {
		            		// 4 UPDATE table title
		            		table.select(testsession.getId());
		            		confirmNotification(testsession.getTitle(), "was created");
		            		close();

//		            		getUI().getNavigator()
//		            			.navigateTo(MainView.NAME + "/" + 
//		            				parentCase.getTitle());		//sessions.getItem(id).getEntity()		            		
		            	
		            	} else if ( navToCasePage==true && editmode==true ) {
		            		confirmNotification(testsession.getTitle(), "was edited");
		            		close();
		            		
		            	} else {
		            		getUI().getNavigator()
		            			.navigateTo(MainView.NAME + "/" + 
		            				parentCase.getTitle() + "/" + testsession.getTitle());		//sessions.getItem(id).getEntity()		            		
		            	}
		            
					} catch (CommitException e) {
						binder.discard();
						Notification.show("'Title' cannot be Empty. Please try again.", Type.WARNING_MESSAGE);
						UI.getCurrent().addWindow(new TestSessionEditor(tree, parentCase));
					} 
//					catch (NonUniqueResultException e) {
//						binder.discard();
//						Notification.show("'Title' must be a unique name.\n'" +
//											queriedSession.getTitle() + 
//											"' already exists.\n\nPlease try again.", Type.WARNING_MESSAGE);
//						UI.getCurrent().addWindow(new TestSessionEditor(tree, parentCase));
//					}
	            
	        } else if (event.getButton() == cancelButton) {
	        	binder.discard();
	        }
//	        binder.clear();
//	        form.disableValidationMessages();
//	        setTreeItemsExpanded();

			close(); // Close the sub-window
	    }

	private void sortAddToTree(Object sid) {
//        // sort session elements				            
//		List<TestSession> caseSessions = parentCase.getSessions();
//		List<Integer> sortedids = new ArrayList<Integer>(); 
//		for (TestSession s : caseSessions){
//			sortedids.add(s.getId());
//			// remove tree items
//			tree.removeItem(s.getId());
//		}
//		Collections.sort(sortedids);
//		Collections.reverse(sortedids);
//		System.out.println("SORTED ID's: " + sortedids);
		
		List<Object> sortedids = new ArrayList<Object>(); 
		for ( Object child : tree.getChildren(parentCase.getId())) {
			sortedids.add(child);
		}
		
		for( Object s : sortedids ) {
			// remove tree items
			tree.removeItem(s);			
		}
		
		// add new item and then re-add old items
        tree.addItem(sid);
        tree.setParent(sid, parentCase.getId());
        tree.setChildrenAllowed(sid, false);
  	  	tree.setItemCaption(sid, sessions.getItem(sid).getEntity().getTitle());		//newsession.getTitle()
    	tree.expandItem(parentCase.getId());
  	  	tree.select(sid);
  	  	
		for (Object id : sortedids) {	//testcase.getSessions()	matchingsessions
			Object sessionid = sessions.getItem(id).getEntity().getId();
			tree.addItem(sessionid);
			tree.setItemCaption(sessionid, sessions.getItem(id).getEntity().getTitle());
			tree.setParent(sessionid, parentCase.getId());
			tree.setChildrenAllowed(sessionid, false);
		}
	
	}
	
	
	private void confirmNotification(String deletedItem, String message) {
        // welcome notification
        Notification notification = new Notification(deletedItem, Type.TRAY_NOTIFICATION);
        notification
                .setDescription(message);
        notification.setHtmlContentAllowed(true);
        notification.setStyleName("dark small");	//tray  closable login-help
        notification.setPosition(Position.BOTTOM_RIGHT);
        notification.setDelayMsec(500);
        notification.show(Page.getCurrent());
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
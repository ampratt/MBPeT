package com.aaron.mbpet.views.sessions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NonUniqueResultException;
import javax.persistence.Persistence;
import javax.persistence.Query;

import com.aaron.mbpet.MbpetUI;
import com.aaron.mbpet.domain.Adapter;
import com.aaron.mbpet.domain.Model;
import com.aaron.mbpet.domain.Parameters;
import com.aaron.mbpet.domain.TestCase;
import com.aaron.mbpet.domain.TestSession;
import com.aaron.mbpet.domain.User;
import com.aaron.mbpet.services.FileSystemUtils;
import com.aaron.mbpet.views.LoginView;
import com.aaron.mbpet.views.MBPeTMenu;
import com.aaron.mbpet.views.MainView;
import com.aaron.mbpet.views.adapters.AdapterEditor;
import com.aaron.mbpet.views.cases.ModelEditorTable;
import com.aaron.mbpet.views.cases.SessionEditorTable;
import com.aaron.mbpet.views.cases.TestCaseEditor;
import com.aaron.mbpet.views.cases.TestCaseForm;
import com.aaron.mbpet.views.parameters.ParametersEditor;
import com.aaron.mbpet.views.users.UserForm;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.jpacontainer.JPAContainerFactory;
import com.vaadin.data.Item;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.filter.Compare.Equal;
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
	private JPAContainer<Adapter> adapterscontainer;
	private JPAContainer<TestSession> sessions;
	JPAContainer<TestCase> testcases = ((MbpetUI) UI.getCurrent()).getTestcases();
	BeanItem<TestSession> newSessionItem;
	TestSession testsession;
	private TestSessionForm form;
	FieldGroup binder;
	TestCase parentcase;
	TestSession subject;
	String prevTitle = "";
	
	boolean editmode = false;
	boolean navToCasePage = false;
	boolean clonemode = false;

	
	/*
	 * Create new Test Session
	 */
	public TestSessionEditor(Tree tree, TestCase parentcase, boolean navToCasePage) {		//JPAContainer<TestCase> container
//      super("Create a new Test Case"); // Set window caption
		this.navToCasePage = navToCasePage;
		
		testsession = new TestSession(); 
		this.newSessionItem = new BeanItem<TestSession>(testsession);
		sessions = ((MbpetUI) UI.getCurrent()).getTestsessions();	//JPAContainerFactory.make(TestSession.class, MbpetUI.PERSISTENCE_UNIT);	//container;
		
		init(tree, parentcase);
	}
	public TestSessionEditor(Tree tree, Table table, TestCase parentcase, boolean navToCasePage) {		//JPAContainer<TestCase> container
//      super("Create a new Test Case"); // Set window caption
		this.navToCasePage = navToCasePage;
		this.table = table;
		
		testsession = new TestSession(); 
		this.newSessionItem = new BeanItem<TestSession>(testsession);
		sessions = ((MbpetUI) UI.getCurrent()).getTestsessions();	//JPAContainerFactory.make(TestSession.class, MbpetUI.PERSISTENCE_UNIT);	//container;
		
		init(tree, parentcase);
	}

	/*
	 * Edit Mode
	 */
	public TestSessionEditor(Tree tree, Object testsessionid, TestCase parentcase) {		//JPAContainer<TestCase> container
		this.editmode = true;
		this.navToCasePage = false;
        
        sessions = ((MbpetUI) UI.getCurrent()).getTestsessions();	//JPAContainerFactory.make(TestSession.class, MbpetUI.PERSISTENCE_UNIT);	//container;
        this.testsession = sessions.getItem(testsessionid).getEntity();
        this.newSessionItem = new BeanItem<TestSession>(testsession);

		prevTitle = testsession.getTitle();

        init(tree, parentcase);
	}
	
	/*
	 * Edit from TestCase Home Page
	 */
	public TestSessionEditor(Tree tree, Object testsessionid, TestCase parentcase, Table table) {		//JPAContainer<TestCase> container
		this.editmode = true;
		this.navToCasePage = true;
		this.table = table;
		
		sessions = ((MbpetUI) UI.getCurrent()).getTestsessions();	//JPAContainerFactory.make(TestSession.class, MbpetUI.PERSISTENCE_UNIT);	//container;
		this.testsession = sessions.getItem(testsessionid).getEntity();
		this.newSessionItem = new BeanItem<TestSession>(testsession);
		System.out.println("this session id is: " + testsessionid + " " + testsession.getTitle());
		
		prevTitle = testsession.getTitle();
		
		init(tree, parentcase);
	}
	
	
	/*
	 * Clone existing test Session to new one
	 */
	public TestSessionEditor(Tree tree, Object testsessionid, TestCase parentcase, Table table, boolean clonemode) {		//JPAContainer<TestCase> container
		this.clonemode = clonemode;
		this.table = table;
		this.navToCasePage = true;
		
		models = ((MbpetUI) UI.getCurrent()).getModels();
		adapterscontainer = ((MbpetUI) UI.getCurrent()).getAdapterscontainer();
		sessions = ((MbpetUI) UI.getCurrent()).getTestsessions();	//JPAContainerFactory.make(TestSession.class, MbpetUI.PERSISTENCE_UNIT);	//container;
		this.subject = sessions.getItem(testsessionid).getEntity();
		
		this.testsession = new TestSession();
		testsession.setTitle("(clone) " + subject.getTitle());
		testsession.setParentcase(subject.getParentcase());
//		clone.setParameters(testsession.getParameters());
		this.newSessionItem = new BeanItem<TestSession>(testsession);

		init(tree, parentcase);
	}
	/*
	 * Clone from tree action
	 */
	public TestSessionEditor(Tree tree, Object testsessionid, TestCase parentcase, boolean clonemode) {		//JPAContainer<TestCase> container
		this.clonemode = clonemode;
		this.navToCasePage = false;
		
		models = ((MbpetUI) UI.getCurrent()).getModels();
		adapterscontainer = ((MbpetUI) UI.getCurrent()).getAdapterscontainer();
		sessions = ((MbpetUI) UI.getCurrent()).getTestsessions();	//JPAContainerFactory.make(TestSession.class, MbpetUI.PERSISTENCE_UNIT);	//container;
		this.subject = sessions.getItem(testsessionid).getEntity();
		
		this.testsession = new TestSession();
		testsession.setTitle("(clone) " + subject.getTitle());
		
		System.out.println("parentcase.getSessions() : " + parentcase.getSessions());
		System.out.println("subjects parent.getSessions() : " + subject.getParentcase().getSessions());
		testsession.setParentcase(parentcase); //subject.getParentcase()

		//		clone.setParameters(testsession.getParameters());
		this.newSessionItem = new BeanItem<TestSession>(testsession);

		init(tree, parentcase);
	}
	
	private void init(Tree tree, TestCase parentcase) {
		center();
		setResizable(false);
		setClosable(true);
		setModal(true);

		this.tree = tree;
		this.parentcase = parentcase;
		
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
    				subject.getTitle());
    	} else if (editmode) {
    		return String.format("Edit Test Session: %s", 
    				testsession.getTitle());
    	} else if (!(parentcase == null) ) {		//.getItemProperty("firstname").getValue()
    		return String.format("Add Test Session to: %s", 
    				parentcase.getTitle());		//testcases.getItem(parentCase.getId()).getItemProperty("title")
    	} else {
    		return "Create a new Test Session";
    	}
    }
    
	
    
	private Component AutoGeneratedLayoutDesign() {
		final VerticalLayout layout = new VerticalLayout();
		layout.setSpacing(true);
		layout.setMargin(true);
	
		// set parent Test Case manually without a field
		if (editmode == false && (clonemode==false)) {
			testsession.setParentcase(parentcase);			
		}
		

		binder = new FieldGroup();
//		BeanItem<Person> item = new BeanItem<Person>(person);		// takes item as argument
//		item.addNestedProperty("address.street");	// Address info is not person but address to which person is linked
		
		binder.setItemDataSource(newSessionItem); 	// link to data model to binder
		
//		binder.bindMemberFields(form);	// link to layout
	
		
		// GENERATE FIELDS
	//	for (Object propertyId : item.getItemPropertyIds()) {
	//		if(!"address".equals(propertyId)) {
	//			Field field = binder.buildAndBind(propertyId);
	//			layout.addComponent(field);							
	//		}
	//	}
		
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
	        	String wrongTitle = "";
	        	
					try {
//						form.enableValidationMessages();
	//					title.setValidationVisible(true);
						
						// commit the fieldgroup
						binder.commit();
						
						// check SESSION title doesnt exist for THIS SESSION
						int id =0;
						boolean titleOK = true;
						id = newSessionItem.getBean().getId();	//testsession.getId();
						System.out.println("parentCase.getSessions() : " + parentcase.getSessions());
						for (TestSession s : parentcase.getSessions()) {	//sessions.getItemIds()
							System.out.println("Existing title -> new title : " + s.getTitle() + "->" + testsession.getTitle());
							System.out.println("Existing id -> new id : " + s.getId() + "->" + id);
							if (s.getTitle().equals(testsession.getTitle()) && !(s.getId()==id) ) {	
								testsession.setTitle(prevTitle);
								if (clonemode != true && editmode == true) {
									sessions.addEntity(testsession);
								}
								wrongTitle = s.getTitle();

								titleOK = false;
								break;
							}
						}
						
						
						if (titleOK == true) {
							System.out.println("TITLE WAS FINE. EDITING");
							
							// add NEW bean object to db through jpa container
							if (editmode == false && (clonemode == false)) {
									
									// add to container
									sessions.addEntity(newSessionItem.getBean()); //jpa container	
									
									// add created item to tree (after retrieving db generated id)
									EntityManager em = Persistence.createEntityManagerFactory("mbpet")
											.createEntityManager();
									Query query = em.createQuery("SELECT OBJECT(t) FROM TestSession t WHERE t.title = :title AND t.parentcase = :parentcase");
									//		            query.setParameter("title", newsession.getTitle());
									query.setParameter("title",testsession.getTitle());
									query.setParameter("parentcase",testsession.getParentcase()); //MainView.sessionUser
									queriedSession = (TestSession) query.getSingleResult();
									//				            queriedSession = (TestSession) query.setParameter("title", testsession.getTitle()).getSingleResult();
									System.out.println("the generated id is: " + queriedSession.getId());
									id = queriedSession.getId(); // here is the id we need for tree
									
									// create session directory for test and reports
									new FileSystemUtils().createSessionTestDir(
											testsession.getParentcase().getOwner().getUsername(),
											testsession.getParentcase().getTitle(), 
											testsession.getTitle());
									
									// create empty parameters object
									new ParametersEditor(sessions.getItem(id).getEntity());
									
									// create empty adapter object
									new AdapterEditor(sessions.getItem(id).getEntity());
									
									// add to tree in right order
									if (tree.hasChildren(parentcase.getId())) {
										sortAddToTree(id);
									} else {
										tree.addItem(id);
										tree.setParent(id, parentcase.getId());
										tree.setChildrenAllowed(id, false);
										tree.setItemCaption(id, sessions.getItem(id).getEntity().getTitle()); //newsession.getTitle()
										tree.expandItem(parentcase.getId());
										tree.select(id);
									}
									
									// update parent Case to add Session to testCase List<Session> sessions
									parentcase.addSession(queriedSession);
									testcases.addEntity(parentcase);
									//              	  	List<TestSession> listofsessions = parentCase.getSessions();
									//              	  	listofsessions.add(queriedSession);		//sessions.getItem(id).getEntity()
									//              	  	parentCase.setSessions(listofsessions);
									
									System.out.println("WHAT IS NEW LIST OF SESSIONS: "
													+ parentcase.getSessions()); // testing purposes
									for (TestSession s : parentcase.getSessions()) {
										System.out.println(s.getId() + " - "
												+ s.getTitle()); // testing purposes	            		
									}
									
	
				            	
							} else if (editmode == true){
								// EDIT existing object
								
//								// commit the fieldgroup
//								binder.commit();
								
			              	  	//1 UPDATE parentcase reference
								parentcase.updateSessionData(sessions.getItem(testsession.getId()).getEntity());
								
								System.out.println("Test session is now: " + testsession.getTitle());
	
								// 2 UPDATE container
								sessions.addEntity(newSessionItem.getBean());
								System.out.println("Entity is now: " + sessions.getItem(testsession.getId()).getEntity().getTitle());
	
								// 3 UPDATE tree title
			              	  	tree.setItemCaption(testsession.getId(), sessions.getItem(testsession.getId()).getEntity().getTitle());
			              	  	
			              	  	// 4. UPDATE models (maybe not necessary)
//			              	  	ModelEditorTable.modelsTable.setContainerDataSource(ModelEditorTable.modelsTable.getContainerDataSource());
//			              	  	for (Model m : testsession.getModels()) {
//			              	  		m.setParentsession(sessions.getItem(testsession.getId()).getEntity());
//			              	  		System.out.println("Sessions' model's session title: " + m.getParentsession().getTitle());
////			              	  		m.updateSessionData(sessions.getItem(testsession.getId()).getEntity());
//			              	  	}
			              	  	
								// edit session directory for test and reports
								if (!prevTitle.equals(testsession.getTitle())) {
									new FileSystemUtils().renameSessionDir(
											testsession.getParentcase().getOwner().getUsername(),
											testsession.getParentcase().getTitle(), 
											prevTitle,
											testsession.getTitle());
								}								
								
//			              	  	// update parameters and adapter ?
			              	  	System.out.println("Sessions' params's session title: " + testsession.getParameters().getOwnersession().getTitle());
		              	  		System.out.println("Sessions' adapter session title: " + testsession.getAdapter().getOwnersession().getTitle());

			              	  	id = testsession.getId();
	
							} else if (clonemode == true){
								// CLONE 
					           System.out.println("\n\nWE ARE IN CLONE MODE!!!!!!!!!\n\n");
	
	//							TestSession clone = newSessionItem.getBean();
//								// 1 commit the fieldgroup
//								binder.commit();
								
								// 2 add to container
								sessions.addEntity(newSessionItem.getBean());	//jpa container	
								
				                // 3 retrieving db generated id
				                EntityManager em = Persistence.createEntityManagerFactory("mbpet")
				    											.createEntityManager();	
					            Query query = em.createQuery(
					        		    "SELECT OBJECT(t) FROM TestSession t WHERE t.title = :title");
			//		            query.setParameter("title", newsession.getTitle());
					            queriedSession = (TestSession) query.setParameter("title", testsession.getTitle()).getSingleResult();
					            System.out.println("the generated id is: " + queriedSession.getId());
					            id = queriedSession.getId();	// here is the id we need for tree
					            
					            // 4 clone models
					            EntityManager em2 = Persistence.createEntityManagerFactory("mbpet").createEntityManager();	
					            Query query2 = em2.createQuery("SELECT OBJECT(m) FROM Model m WHERE m.title = :title AND m.parentsession = :parentsession");
					            for (Model m : subject.getModels()) {
					            	// copy over model values
					            	Model newmodel = new Model(m.getTitle(), queriedSession, m.getParentsut()); //"(clone) " + 
					            	newmodel.setDotschema(m.getDotschema());
					            	
									// add to container
									models.addEntity(newmodel);	//jpa container	
									
					                // retrieve generated id from db
						            query2.setParameter("title", newmodel.getTitle());
						            query2.setParameter("parentsession", queriedSession);
						            Model queriedModel = (Model) query2.getSingleResult();
						            System.out.println("the generated MODEL id is: " + queriedModel.getId() + " of session ->" + queriedSession.getId());
				        			
						            // update parent Case to add Session to testCase List<Session> sessions
						            parentcase.addModel(queriedModel);
						            queriedSession.addModel(queriedModel);
					            }
					            sessions.addEntity(queriedSession);
	
					            // 5 clone parameters
					            String cloneParams = "Fill in parameters for Test Session '" + queriedSession.getTitle() + "'";
					            if (!(subject.getParameters().getSettings_file() == null) ) {	//|| !(testsession.getParameters().getSettings_file().equals(""))
					            	cloneParams = subject.getParameters().getSettings_file();
					            }
					            System.out.println("\n\n the cloned parameters are:\n" + 
					            		cloneParams + "\n\n");
					            new ParametersEditor(queriedSession, cloneParams);
	
					            // 6 clone adapter
					            String cloneAdapter = "Fill in adapter for Test Session '" + queriedSession.getTitle() + "'";
					            if (!(subject.getAdapter().getAdapter_file() == null) 
					            		|| !(subject.getAdapter().getAdapter_file().equals("")) ) {	//|| !(testsession.getParameters().getSettings_file().equals(""))
					            	cloneAdapter = subject.getAdapter().getAdapter_file();
					            }
					            System.out.println("\n\n the cloned adapter is:\n" + 
					            		cloneAdapter + "\n\n");
					            new AdapterEditor(queriedSession, cloneAdapter);
			        			
					            // 7 add to tree in right order
					            if ( tree.hasChildren(parentcase.getId()) ) {
					            	sortAddToTree(id);				            	
					            } else {
					            	tree.addItem(id);
					            	tree.setParent(id, parentcase.getId());
					            	tree.setChildrenAllowed(id, false);
					            	tree.setItemCaption(id, sessions.getItem(id).getEntity().getTitle());		//newsession.getTitle()
					            	tree.expandItem(parentcase.getId());
					            	tree.select(id);				            	
					            }
			        			
			              	  			              	  	
			              	  	// 8 update parent Case to add Session to testCase List<Session> sessions
			              	  	parentcase.addSession(queriedSession);
			//              	  	List<TestSession> listofsessions = parentCase.getSessions();
			//              	  	listofsessions.add(queriedSession);		//sessions.getItem(id).getEntity()
			//              	  	parentCase.setSessions(listofsessions);
			              	  	
								// 9 create session directory for test and reports
								new FileSystemUtils().createSessionTestDir(
										queriedSession.getParentcase().getOwner().getUsername(),
										queriedSession.getParentcase().getTitle(), 
										queriedSession.getTitle());
								
//				            	System.out.println("WHAT IS NEW LIST OF SESSIONS: " + parentCase.getSessions()); // testing purposes
//				            	for (TestSession s : parentCase.getSessions()) {
//					            	System.out.println(s.getId() + " - " + s.getTitle()); // testing purposes	            		
//				            	}
							}
	
		              	  	
//			            	System.out.println("WHAT IS NEW LIST OF SESSIONS: " + parentCase.getSessions()); // testing purposes
//			            	for (TestSession s : parentCase.getSessions()) {
//				            	System.out.println(s.getId() + " - " + s.getTitle()); // testing purposes	            		
//			            	}
			            	
			            	
			            	if (clonemode==true && navToCasePage == true) {
			            		confirmNotification(queriedSession.getTitle(), "was created");
			            		close();
			            		
			            	} else if (clonemode==true && navToCasePage == false) {
			            		confirmNotification(queriedSession.getTitle(), "was CLONED");
			            		getUI().getNavigator()
		            				.navigateTo(MainView.NAME + "/" + 
		            						parentcase.getTitle() + "/" + queriedSession.getTitle() + "id=" + queriedSession.getId());		//sessions.getItem(id).getEntity()		            		
			            		close();
	
			            	} else if ( navToCasePage==true && editmode==false ) {
			            		// 4 UPDATE table title
			            		table.select(newSessionItem.getBean().getId());	//(testsession.getId());
			            		confirmNotification(testsession.getTitle(), "was created");
			            		close();
	
	//		            		getUI().getNavigator()
	//		            			.navigateTo(MainView.NAME + "/" + 
	//		            				parentCase.getTitle());		//sessions.getItem(id).getEntity()		            		
			            	
			            	} else if ( navToCasePage==true && editmode==true ) {
			            		getUI().getNavigator()
	            				.navigateTo(MainView.NAME + "/" + 
	            						parentcase.getTitle() + "-sut=" + parentcase.getId());		//sessions.getItem(id).getEntity()		            		
			            		confirmNotification(testsession.getTitle(), "was edited");
			            		close();
			            		
			            	} else if ( editmode==true && navToCasePage==false) {
			            		getUI().getNavigator()
		            			.navigateTo(MainView.NAME + "/" + 
		            				parentcase.getTitle() + "/" + newSessionItem.getBean().getTitle() + "id=" + newSessionItem.getBean().getId());		//testsession.getTitle() sessions.getItem(id).getEntity()		            		
			            	} else {
			            		getUI().getNavigator()
			            			.navigateTo(MainView.NAME + "/" + 
			            				parentcase.getTitle() + "/" + queriedSession.getTitle() + "id=" + queriedSession.getId());		//testsession.getTitle() sessions.getItem(id).getEntity()		            		
			            	}
		            	
			            // title already existed	
						} else {
							System.out.println("title was NOT fine.");
//							testsession = sessions.getItem(id).getEntity();
//							System.out.println("db session is: " + testsession.getId() + " " + testsession.getTitle());

							if (editmode==false && clonemode==false) {
								binder.discard();
								Notification.show("The title '" + wrongTitle + "' already exists for this SUT. Please rename this session.", Type.ERROR_MESSAGE);	//testsession.getTitle()
								UI.getCurrent().addWindow(new TestSessionEditor(tree, table, parentcase, true));								
							} else if (editmode==true){
								binder.discard();
								Notification.show("The title '" + wrongTitle + "' already exists for this SUT. Please rename this session.", Type.ERROR_MESSAGE);
								if (navToCasePage == true) {
									UI.getCurrent().addWindow(new TestSessionEditor(tree, id, parentcase, table));	//sessions.getItem(testsession.getId()).getEntity().getId()																	
								} else {
									UI.getCurrent().addWindow(new TestSessionEditor(tree, id, parentcase));																										
								}
									
							} else if (clonemode==true){
								binder.discard();
								Notification.show("The title '" + wrongTitle + "' already exists for this SUT. Please rename this session.", Type.ERROR_MESSAGE);
								UI.getCurrent().addWindow(new TestSessionEditor(
										tree, 
										subject.getId(),	//testsession sessions.getItem( 
					        			parentcase,
				        				table,
				        				true));	
							}
								
						}
		            
					} catch (CommitException e) {
						binder.discard();
						Notification.show("'Title' cannot be Empty. Please try again.", Type.WARNING_MESSAGE);
						UI.getCurrent().addWindow(new TestSessionEditor(tree, parentcase, true));
					} catch (NonUniqueResultException e) {
						e.printStackTrace();
						binder.discard();
						Notification.show("The title '" + testsession.getTitle() + "' already exists for this SUT. Please rename this session.", Type.ERROR_MESSAGE);
						UI.getCurrent().addWindow(new TestSessionEditor(tree, parentcase, true));
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

	private void filterContainerBySUT(TestCase sut) {
    	sessions.removeAllContainerFilters();
//    	Equal ownerfilter = new Equal("parentcase", getTestCaseByTitle());//  ("parentcase", getTestCaseByTitle(), true, false);
    	Equal casefilter = new Equal("parentcase", sut);//  ("parentcase", getTestCaseByTitle(), true, false);
    	
    	sessions.addContainerFilter(casefilter);
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
		for ( Object child : tree.getChildren(parentcase.getId())) {
			sortedids.add(child);
		}
		
		for( Object s : sortedids ) {
			// remove tree items
			tree.removeItem(s);			
		}
		
		// add new item and then re-add old items
        tree.addItem(sid);
        tree.setParent(sid, parentcase.getId());
        tree.setChildrenAllowed(sid, false);
  	  	tree.setItemCaption(sid, sessions.getItem(sid).getEntity().getTitle());		//newsession.getTitle()
    	tree.expandItem(parentcase.getId());
  	  	tree.select(sid);
  	  	
		for (Object id : sortedids) {	//testcase.getSessions()	matchingsessions
			Object sessionid = sessions.getItem(id).getEntity().getId();
			tree.addItem(sessionid);
			tree.setItemCaption(sessionid, sessions.getItem(id).getEntity().getTitle());
			tree.setParent(sessionid, parentcase.getId());
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
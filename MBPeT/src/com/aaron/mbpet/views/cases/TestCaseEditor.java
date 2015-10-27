package com.aaron.mbpet.views.cases;

import javax.persistence.EntityManager;
import javax.persistence.NonUniqueResultException;
import javax.persistence.Persistence;
import javax.persistence.Query;

import com.aaron.mbpet.MbpetUI;
import com.aaron.mbpet.domain.TestCase;
import com.aaron.mbpet.domain.TestSession;
import com.aaron.mbpet.domain.User;
import com.aaron.mbpet.views.LoginView;
import com.aaron.mbpet.views.MBPeTMenu;
import com.aaron.mbpet.views.MainView;
import com.aaron.mbpet.views.sessions.TestSessionEditor;
import com.aaron.mbpet.views.users.UserForm;
import com.google.gwt.dev.util.Empty;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.jpacontainer.JPAContainerFactory;
import com.vaadin.data.Item;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.data.util.BeanItem;
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
import com.vaadin.ui.Component;
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
public class TestCaseEditor extends Window implements Button.ClickListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5370960944210111329L;

	private Tree tree;
	private Button createButton;
	private Button cancelButton;	
	
	private JPAContainer<TestCase> testcases;
	final BeanItem<TestCase> newCaseItem;
	TestCase testcase;
	TestCaseForm form;
	FieldGroup binder;
	private User sessionUser = MainView.sessionUser;	
	
	boolean editmode = false;
	private String prevTitle = "";
	private String wrongTitle = "";


	/*
	 * CREATE new
	 */
	public TestCaseEditor(Tree tree) {
//        super("Create a new Test Case"); // Set window caption
        this.tree = tree;

        this.testcases = MBPeTMenu.getTestcases();	//JPAContainerFactory.make(TestCase.class, MbpetUI.PERSISTENCE_UNIT);	//container;
        testcase = new TestCase(); 
        this.newCaseItem = new BeanItem<TestCase>(testcase);
        
        init();
//        setContent(buildWindowContent());
	}
	
	/*
	 * EDIT mode
	 */
	public TestCaseEditor(Tree tree, Object caseId) {
		
      this.editmode = true;      
      this.tree = tree;
      
//      testcase = new TestCase(); 
      this.testcases = MBPeTMenu.getTestcases();	//JPAContainerFactory.make(TestCase.class, MbpetUI.PERSISTENCE_UNIT);	
      this.testcase = testcases.getItem(caseId).getEntity();
      this.newCaseItem = new BeanItem<TestCase>(testcase);

      prevTitle = testcase.getTitle();
      
      init();
	}
	
	private void init() {
        center();
        setResizable(false);
        setClosable(true);
        setModal(true);
        
        setSizeUndefined();
        setContent(ManualLayoutDesign()); //editorForm
        setCaption(buildCaption());
	}
	
    /**
     * @return the caption of the editor window
     */
    private String buildCaption() {
    	if (editmode) {
    		return String.format("Edit Test Case: %s", 
    				testcase.getTitle());
    	} else return "Create a new Test Case";
//    	if (!(personItem.getItemProperty("firstname").getValue() == null) ) {
//    		return String.format("%s %s", personItem.getItemProperty("firstname")
//    				.getValue(), personItem.getItemProperty("lastname").getValue());
//    	} else {
//    		return "";
//    	}
    }
    
	
	/**
	 * If you know the data structure behind, define things manually for more control
	 * @return
	 */
	private Component ManualLayoutDesign() {
		final VerticalLayout layout = new VerticalLayout();
		layout.setSpacing(true);
		layout.setMargin(true);
	
		// set owner to current logged in user 
		if (!editmode) {
			testcase.setOwner(sessionUser);
		}

		// CREATE FIELDS MANUALLY
		form = new TestCaseForm();
		layout.addComponent(form);
		
		binder = new FieldGroup();
//		newCaseItem = new BeanItem<User>(user);		// takes item as argument
		binder.setItemDataSource(newCaseItem); 	// link to data model to binder
		binder.bindMemberFields(form);	// link to layout		
		
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
				try {
					form.enableValidationMessages();
					
					// commit the fielgroup
					binder.commit();
					
					// check CASE title doesn't exist for THIS USER
					int id =0;
					boolean titleOK = true;
					id = newCaseItem.getBean().getId();	//testsession.getId();
					System.out.println("User.getCases() : " + sessionUser.getCases());
					for (TestCase c : sessionUser.getCases()) {	//sessions.getItemIds()
						System.out.println("Existing title -> new title : " + c.getTitle() + "->" + testcase.getTitle());
						System.out.println("Existing id -> new id : " + c.getId() + "->" + id);
						if (c.getTitle().equals(testcase.getTitle()) && !(c.getId()==id) ) {	
							testcase.setTitle(prevTitle );
							if (editmode == true)
								testcases.addEntity(testcase);
							
							wrongTitle  = c.getTitle();
							titleOK = false;
							break;
						}
					}
					
					if (titleOK == true) {
						// add bean object to db through jpa container
						if (editmode == false) {
							// add to container
							testcases.addEntity(newCaseItem.getBean()); //jpa container

							// add created item to tree (after retrieving db generated id)
							EntityManager em = Persistence
									.createEntityManagerFactory("mbpet")
									.createEntityManager();
							Query queryByTestCaseName = em
									.createQuery("SELECT OBJECT(t) FROM TestCase t WHERE t.title = :title AND t.owner = :owner");
							queryByTestCaseName.setParameter("title",
									testcase.getTitle());
							queryByTestCaseName.setParameter("owner",
									testcase.getOwner()); //sessionUser
							TestCase queriedcase = (TestCase) queryByTestCaseName
									.getSingleResult();
							System.out.println("the generated id is: "
									+ queriedcase.getId());
							id = queriedcase.getId(); // here is the id we need for tree

							// add to tree
							tree.addItem(id);
							tree.setChildrenAllowed(id, true);
							tree.setItemCaption(id, testcases.getItem(id)
									.getEntity().getTitle());
							tree.select(id);

							// update user to add Case List<TestCase>
							sessionUser.addCase(queriedcase);

							// TESTING
							//list all Cases
							System.out.println("\nWHAT IS NEW LIST OF CASES: "
									+ sessionUser.getCases()); // testing purposes
							for (TestCase c : sessionUser.getCases()) {
								System.out.println(c.getId() + " - "
										+ c.getTitle()); // testing purposes	            		
							}

							// nav to created test case
							getUI().getNavigator().navigateTo(
									MainView.NAME
											+ "/"
											+ testcases.getItem(id).getEntity()
													.getTitle() + "-sut=" + id);
						} else {
							System.out
									.println("\nWHAT IS NEW LIST OF CASES (before update): "
											+ sessionUser.getCases()); // testing purposes

							//1 UPDATE user's reference ???
							sessionUser.updateCaseData(testcases.getItem(
									testcase.getId()).getEntity());
							System.out.println("Test Case is now: "
									+ testcase.getTitle());
							//						System.out.println("Entity is now: " + sessions.getItem(testsession.getId()).getEntity().getTitle());

							// 2 UPDATE container
							testcases.addEntity(newCaseItem.getBean());
							testcase = testcases.getItem(testcase.getId())
									.getEntity();
							System.out.println("Entity is now: "
									+ testcases.getItem(testcase.getId())
											.getEntity().getTitle());

							// 3 UPDATE tree title
							tree.setItemCaption(testcase.getId(), testcases
									.getItem(testcase.getId()).getEntity()
									.getTitle());

							// 4. update child sessions

							// TESTING
							//list all Cases
							System.out.println("\nWHAT IS NEW LIST OF CASES: "
									+ sessionUser.getCases()); // testing purposes
							for (TestCase c : sessionUser.getCases()) {
								System.out.println(c.getId() + " - "
										+ c.getTitle()); // testing purposes	            		
							}

							System.out
									.println("\nTHE CASE's list of sessions with their reference to case: "
											+ testcase.getSessions()); // testing purposes
							for (TestSession s : testcase.getSessions()) {
								System.out.println(s.getId() + " - "
										+ s.getTitle() + " - to parent -> "
										+ s.getParentcase().getTitle()); // testing purposes	            		
							}

							// nav to created test case
							getUI().getNavigator().navigateTo(
									MainView.NAME + "/" + testcase.getTitle()
											+ "-sut=" + testcase.getId());

						}
						
					 // title already existed	
					} else {
						System.out.println("title was NOT fine.");
	//					testsession = sessions.getItem(id).getEntity();
	//					System.out.println("db session is: " + testsession.getId() + " " + testsession.getTitle());
	
						if (editmode==true){
							binder.discard();
							Notification.show("The title '" + wrongTitle + "' already exists. Please rename this SUT.", Type.ERROR_MESSAGE);
								UI.getCurrent().addWindow(new TestCaseEditor(tree, id));	//sessions.getItem(testsession.getId()).getEntity().getId()																	
								
						} else {
							binder.discard();
							Notification.show("The title '" + wrongTitle + "' already exists. Please rename this SUT.", Type.ERROR_MESSAGE);	//testsession.getTitle()
							UI.getCurrent().addWindow(new TestCaseEditor(tree));								
						} 
						
					}
		            
				} catch (CommitException e) {
					e.printStackTrace();
					binder.discard();
					Notification.show("'Title' cannot be Empty. Please try again.", Type.ERROR_MESSAGE);
					UI.getCurrent().addWindow(new TestCaseEditor(tree));
				} catch (NonUniqueResultException e) {
					e.printStackTrace();
					binder.discard();
					Notification.show("The title '" + testcase.getTitle() + "' already exists in your database. Please rename this SUT.", Type.ERROR_MESSAGE);
					UI.getCurrent().addWindow(new TestCaseEditor(tree));
				}
	            
	        } else if (event.getButton() == cancelButton) {
	        	binder.discard();
	        }
//	        binder.clear();
	        form.disableValidationMessages();
//	        setTreeItemsExpanded();

			close(); // Close the sub-window
	    }
	
	
	
	    public void setTreeItemsExpanded() {
	        // Expand whole tree
	    	System.out.println(tree.getItemIds());
	    	System.out.println(tree.rootItemIds());
		    for (final Object id : tree.rootItemIds()) {
		    	tree.expandItemsRecursively(id);
		    	tree.setChildrenAllowed(id, true);
	        } 
	    }
	    
	   
//	 // Define a sub-window by inheritance
//	    public class CreateTestCaseWindow extends Window {
//	    	/**
//	    	 * 
//	    	 */
//	    	private static final long serialVersionUID = 5370960944210111329L;
//
//	    	private Tree tree;
//	    	private JPAContainer<TestCase> testcases;
//	    	
//	    	public CreateTestCaseWindow(Tree tree, JPAContainer<TestCase> container) {
//	            super("Create a new Test Case"); // Set window caption
//	            center();
//	            setResizable(false);
//	            setClosable(false);
//	            setModal(true);
//
//	            this.tree = tree;
//	            this.testcases = container;
//	            
//	            setContent(buildWindowContent());
//	    	}
//	    	
//	            private Component buildWindowContent() {
//	            	// Some basic content for the window
//	                VerticalLayout vc = new VerticalLayout();
//	                setContent(vc);
//	                vc.addStyleName("subwindow-margin");
//	                vc.setMargin(true);
//	                vc.setSpacing(true);
//
//	                //label
//	                vc.addComponent(new Label("Fill in details for this Test Case"));
//	              
//	                // form
//	                vc.addComponent(buildCreationForm());
//	                
//	                return vc;
//	            }
//
//	    		private Component buildCreationForm() {
//	    			FormLayout form = new FormLayout();
////	    	        form.addStyleName("outlined");
//	    	        form.setSizeFull();
//	    	        form.setMargin(new MarginInfo(true, true, false, true));
//	    	        form.setSpacing(true);
//	    	 
//	    	        final TextField title = new TextField("Title");
//	    	        title.setWidth(100.0f, Unit.PERCENTAGE);
//	    	        title.addStyleName("hide-required-asterisk");
//	    	        title.setImmediate(true);
//	    	        title.focus();
//	    	        title.setValidationVisible(false);
//	    	        title.setRequired(true);
//	    	        title.setRequiredError("Please give a name for this Test Case");
////	    	        title.addValidator(new NullValidator("Cannot be empty", false));
//	    	        form.addComponent(title);
//	    	        
//
//	    	        
//	                // confirm creation to tree
//	                Button submit = new Button("Create");
//	                submit.addStyleName("primary");
//	                submit.setClickShortcut(KeyCode.ENTER);
//	                submit.addClickListener(new ClickListener() {
//	                    public void buttonClick(ClickEvent event) {
//	                    	title.setValidationVisible(true);
////	                    	if (title.getValue() == null
////	                    			|| title.getValue().isEmpty()) {
////	                    	} else {
//	                    	if (!(title.getValue() == null
//	                    			|| title.getValue().isEmpty())) {
//
//	    	                    // Create new item, set as parent, allow children (= leaf node)
//	    	                    final Object[] itemId = new Object[] {title.getValue()};
//	    	                    String treeItem = (String) itemId[0];
////	    	                    Tree tree = MBPeTMenu.tree;
//	    	                    tree.addItem(treeItem);
//	    	                    tree.setChildrenAllowed(treeItem, true);
//	    	//                    final Object itemId = tree.addItem();
//	    	//                    tree.setParent(itemId, target);
//	    	
//	    	                    // Set the name for this item (we use it as item caption)
//	    	                    final Item item = tree.getItem(itemId);
//	    	//                    final Property name = item
//	    	//                            .getItemProperty(ExampleUtil.hw_PROPERTY_NAME);
//	    	//                    name.setValue("New Item");
//	    	
//	    	                    // Allow children for the target item, and expand it
//	    	//                    tree.setChildrenAllowed(target, true);
//	    	//                    tree.expandItem(target);
//	    	                    
//	    	    	            // select newly created item and navigate to it
//	    	                    tree.select(treeItem);
//	    						getUI()
//	    			         		.getNavigator()
//	    			         			.navigateTo(MainView.NAME + "/" + 
//	    			         					treeItem);
//	    						close(); // Close the sub-window
//	                    	}
//	                    }
//
//	                });
//	                
//	                
//	                // cancel changes and close window
//	                Button cancel = new Button("Cancel");
////	                addStyleName("danger");
//	                cancel.addClickListener(new ClickListener() {
//	                    public void buttonClick(ClickEvent event) {
//	                        close(); // Close the sub-window
//	                    }
//
//	                });
//
//	                HorizontalLayout buttons = new HorizontalLayout();
//	                buttons.setWidth("100%");
//	                buttons.setMargin(new MarginInfo(true, false, false, false));
//	                buttons.addComponent(submit);
//	                buttons.addComponent(cancel);
//	                buttons.setComponentAlignment(submit, Alignment.MIDDLE_LEFT);
//	                buttons.setComponentAlignment(cancel, Alignment.MIDDLE_RIGHT);
//	                
//	                form.addComponent(buttons);
//	                
////	    	        final TextField child2 = new TextField("Child 2", "");
////	    	        child2.setWidth(100.0f, Unit.PERCENTAGE);
////	    	        form.addComponent(child2);
////	    	        form.addComponent(new CheckBox("Child 3"));
////	    	        form.addComponent(new Button("Child 4"));
//	                
//	    			return form;
//	    		}
//
//	    		
//	            
//	            
//
//	    }

}
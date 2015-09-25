package com.aaron.mbpet.ui;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.persistence.Query;

import com.aaron.mbpet.MbpetUI;
import com.aaron.mbpet.domain.TestCase;
import com.aaron.mbpet.domain.User;
import com.aaron.mbpet.views.LoginView;
import com.aaron.mbpet.views.MainView;
import com.aaron.mbpet.views.TestCaseForm;
import com.aaron.mbpet.views.UserForm;
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
	private JPAContainer<TestCase> testcases;
	final BeanItem<TestCase> newCaseItem;
	TestCase newcase;
	private TestCaseForm form;
	FieldGroup binder;

	private Button createButton;
	private Button cancelButton;

	public TestCaseEditor(Tree tree) {		//JPAContainer<TestCase> container
//        super("Create a new Test Case"); // Set window caption
        center();
        setResizable(false);
        setClosable(true);
        setModal(true);

        newcase = new TestCase(); 
        this.newCaseItem = new BeanItem<TestCase>(newcase);
        this.tree = tree;
        this.testcases = JPAContainerFactory.make(TestCase.class,
        		MbpetUI.PERSISTENCE_UNIT);	//container;
        
//        setContent(buildWindowContent());
        
        setSizeUndefined();
        setContent(ManualLayoutDesign()); //editorForm
        setCaption(buildCaption());
	}
	
	
    /**
     * @return the caption of the editor window
     */
    private String buildCaption() {
    	return "Create a new Test Case";
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
		
//		layout.addComponent(new Label("<h2>Create new user account.</h2>", 
//				ContentMode.HTML));
		
		// set owner to current logged in user    	
		newcase.setOwner(MainView.sessionUser);

		// CREATE FIELDS MANUALLY
		form = new TestCaseForm();
		layout.addComponent(form);
		
		binder = new FieldGroup();
//		beanUserItem = new BeanItem<User>(user);		// takes item as argument
//		item.addNestedProperty("address.street");	// Address info is not person but address to which person is linked
		binder.setItemDataSource(newCaseItem); 	// link to data model to binder
		
		binder.bindMemberFields(form);	// link to layout		
		
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
					form.enableValidationMessages();
					// commit the fielgroup
					binder.commit();
					
					// add bean object to db through jpa container
	                testcases.addEntity(newCaseItem.getBean());	//jpa container
	                
	                
	                // add created item to tree (after retrieving db generated id)
	                EntityManager em = Persistence
	    					.createEntityManagerFactory("mbpet")
	    					.createEntityManager();	
		            Query queryByTestCaseName = em.createQuery(
		        		    "SELECT OBJECT(t) FROM TestCase t WHERE t.title = :title"
		        		);
		            queryByTestCaseName.setParameter("title", newcase.getTitle());
		            TestCase queriedcase = (TestCase) queryByTestCaseName.getSingleResult();
		            System.out.println("the generated id is: " + queriedcase.getId());
		            Object id = queriedcase.getId();	// here is the id we need for tree
		            
		            tree.addItem(id);
	                tree.setChildrenAllowed(id, true);
              	  	tree.setItemCaption(id, testcases.getItem(id).getEntity().getTitle());
	            	tree.select(id);
	            	
	            	// nav to created test case
	    			getUI().getNavigator()
	         			.navigateTo(MainView.NAME + "/" + 
	         					testcases.getItem(id).getEntity().getTitle());
	            	
	            	
		            Notification.show("TEST CASE successfully created: " +
		            		"\nid: " + queriedcase.getId() +
		            		"\ntitle: " + newcase.getTitle() +
		            		"\ndescription: " + newcase.getDescription() +
		            		"\nowner: (" + newcase.getOwner() + ") " + newcase.getOwner().getUsername(),
		            		Type.TRAY_NOTIFICATION);
		            
					} catch (CommitException e) {
						e.printStackTrace();
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
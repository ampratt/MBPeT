package com.aaron.mbpet.views;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.aaron.mbpet.MbpetUI;
import com.aaron.mbpet.domain.TestCase;
import com.aaron.mbpet.domain.TestSession;
import com.aaron.mbpet.domain.User;
import com.aaron.mbpet.ui.ConfirmDeleteMenuItemWindow;
import com.aaron.mbpet.ui.CreateTestCaseWindow;
import com.aaron.mbpet.ui.NewUseCaseInstanceWindow;
import com.aaron.mbpet.ui.PersonEditor;
import com.aaron.mbpet.ui.TestCaseEditor;
import com.aaron.mbpet.ui.TestSessionEditor;
import com.aaron.mbpet.utils.ExampleUtil;
import com.aaron.mbpet.utils.HierarchicalDepartmentContainer;
import com.vaadin.event.Action;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.jpacontainer.JPAContainerFactory;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.util.BeanItem;
import com.vaadin.event.Action.Handler;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinServlet;
import com.vaadin.server.VaadinSession;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Label;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.MenuBar.Command;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Tree;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.themes.ValoTheme;

public class MBPeTMenu extends CustomComponent implements Action.Handler{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8976097773826956282L;
	
//    public static final String NAME = "MBPeT";
	VerticalLayout menuLayout = new VerticalLayout(); //VerticalLayout
//	final static Tree tree = new Tree("Test Cases:");
	private Tree menutree;
	static MenuBar userMenu;
	private JPAContainer<User> persons;
	private static JPAContainer<TestCase> testcases;
	public static JPAContainer<TestSession> sessions;
	private User currentuser = MainView.sessionUser;

	
    // Actions for the context menu
    private static final Action ACTION_ADD = new Action("Add TestSession");
    private static final Action ACTION_EDIT = new Action("Edit");
    private static final Action ACTION_DELETE = new Action("Delete");
    private static final Action[] ACTIONS = new Action[] { ACTION_ADD, ACTION_EDIT, ACTION_DELETE };
//    String[] animals = new String[] {"possum", "donkey", "pig", "duck", "dog", "cow", "horse", "cat", "reindeer", "penguin", "sheep", "goat", "tractor cow", "chicken", "bacon", "cheddar"};
    
    
	public MBPeTMenu(JPAContainer<User> persons, Tree tree) {	//User sessUser, String usrname,
//		this.sessionUser = sessUser;
		this.menutree = tree;
//		this.displayName = usrname;

        this.persons = persons;	//JPAContainerFactory.make(User.class,MbpetUI.PERSISTENCE_UNIT);
        setTestcases(JPAContainerFactory.make(TestCase.class,
        		MbpetUI.PERSISTENCE_UNIT));
        sessions = JPAContainerFactory.make(TestSession.class,
        		MbpetUI.PERSISTENCE_UNIT);
        //new HierarchicalDepartmentContainer();
//        		JPAContainerFactory.make(TestCase.class,
//        		MbpetUI.PERSISTENCE_UNIT);
        
//		setUserDisplayName(usrname);
		setCompositionRoot(buildContent());
	}
	
	
	public Component buildContent() {
//		VerticalLayout menuLayout = new VerticalLayout(); //VerticalLayout
    	menuLayout.addStyleName("menu");
//    	menuLayout.setHeight("100%");
    	
    	menuLayout.addComponent(buildTitle());
        menuLayout.addComponent(buildUserMenu());       
        menuLayout.addComponent(menuButtons());
        menuLayout.addComponent(buildTreeMenu());
//        menuLayout.addComponent(buildMenuItems());

        return menuLayout;
               
    }


	private Component buildTitle(){
		VerticalLayout l = new VerticalLayout();
		l.addStyleName("menu-title");
		
		Label title = new Label("MBPeT Design <b>Demo</b>", ContentMode.HTML);
		title.addStyleName("h3");
		title.addStyleName("menu-title");

		l.addComponent(title);
		return l;
	}
	

	@SuppressWarnings("serial")
	private Component buildUserMenu() {
		 final Command menuCommand = new Command() {
		        @Override
		        public void menuSelected(final MenuItem selectedItem) {
		        	if (selectedItem.getText().equals("Edit Profile")){
		                PersonEditor personEditor = new PersonEditor(
		                		persons.getItem(currentuser.getId()), 
		                			"Edit User Account", true);

		                personEditor.setModal(false);
		                UI.getCurrent().addWindow(personEditor);
		                personEditor.center();
		                
		        	} else if (selectedItem.getText().equals("Preferences")){
		        		
		        	} else if (selectedItem.getText().equals("Sign Out")){
		        		// "Logout" the user
			            getSession().setAttribute("user", null);
			            
			            //close the session
			            UI.getCurrent().getSession().close();
			            UI.getCurrent().getSession().getService().closeSession(VaadinSession.getCurrent());
			            UI.getCurrent().close();
			            
//			            UI.getCurrent().getPage().setLocation(
//			        			VaadinServlet.getCurrent().getServletContext().getContextPath());	//"/"
			         
			            // Refresh this view, should redirect to login view
			            UI.getCurrent().getNavigator().navigateTo(LoginView.NAME);
			            return;
		        	}
		        }
		    };
		    
 
		userMenu = new MenuBar();
		userMenu.addStyleName("user-menu");
		
		MenuItem user = userMenu.addItem(MainView.displayName, null);
		user.addItem("Edit Profile", menuCommand);
		user.addItem("Preferences", menuCommand);
		user.addSeparator();
		user.addItem("Sign Out", menuCommand);
		
		List<MenuItem> mitems = userMenu.getItems();
		System.out.println("USER MENU ITEM: " + mitems.get(0).getText()); 	//getItems().toString());
		
		return userMenu;
	}
	
	
	private VerticalLayout menuButtons() {
		final VerticalLayout buttons = new VerticalLayout();
			
		// landing page button
		@SuppressWarnings("serial")
		Button landingButton = new Button("Start page", new Button.ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				UI.getCurrent()
	        		.getNavigator()
	            		.navigateTo(MainView.NAME + "/" + "landingPage");
				
			}
		});
		landingButton.addStyleName("menu-button-left-align");
//		button.addStyleName("tiny");
		landingButton.addStyleName("borderless");
		buttons.addComponent(landingButton);
		buttons.setComponentAlignment(landingButton, Alignment.MIDDLE_LEFT);
		
		
		// create test case button
		Button createTestCase = new Button("Create new test case");
		createTestCase.addStyleName("menu-button-left-align");
//		createTestCase.addStyleName("tiny");
		createTestCase.addStyleName("borderless");
		buttons.addComponent(createTestCase);
		buttons.setComponentAlignment(createTestCase, Alignment.MIDDLE_LEFT);
		
        // button listener
		createTestCase.addClickListener(new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
		        // open window to create test case
		        UI.getCurrent().addWindow(new TestCaseEditor(menutree));	//testcases        
//		        UI.getCurrent().addWindow(new CreateTestCaseWindow(menutree, testcases));
			}
		});
		
		return buttons;
	}
	
	
	@SuppressWarnings({ "serial", "deprecation" })
	private VerticalLayout buildTreeMenu() {
		// layout holder for menu items
		VerticalLayout vc = new VerticalLayout();
		vc.setHeight("100%");
		

		
		Label divider = new Label("<hr>", ContentMode.HTML);
//		divider.addStyleName("menu-divider");
		vc.addComponent(divider);
		
		// TREE MENU
//		   final Object[][] testCases = new Object[][]{
//	    	        new Object[]{"Dashboard", "01.02.15", "03.04.15", "04.05.15", "06.07.15"},
//	    	        new Object[]{"Panel", "01.02.15", "03.04.15", "04.05.15", "06.07.15"},
//	    	        new Object[]{"Portal", "01.02.15", "03.04.15", "04.05.15", "06.07.15"},
//	    	        new Object[]{"demo", "01.02.15", "03.04.15", "04.05.15", "06.07.15"},
//	    	        new Object[]{"random", "01.02.15", "03.04.15", "04.05.15", "06.07.15"}
//		        };
	        		   
	    	
//	        menutree.setContainerDataSource(testcases);
	        menutree.addStyleName("tiny");
//	        menutree.setItemCaptionPropertyId("title");
	        menutree.setImmediate(true);		// Cause valueChange immediately when the user selects
		    menutree.addActionHandler(this);	// Add actions (context menu)
	        menutree.setSelectable(true);
//	        menutree.addContainerProperty(propertyId, type, defaultValue)
//	    	tree.addStyleName("treemenu");
	        
	    	
	     // Item captions must be defined explicitly
	        menutree.setItemCaptionMode(ItemCaptionMode.EXPLICIT);

	        // Now fill the tree. iterate through all testCases by owner
			//	        Collection<Object> sessids = sessions.getItemIds();
			//	        TestSession session = sessions.getItem(sessids).getEntity();
	        
	        // check if owner has existing cases
	        System.out.println(currentuser.getId() + " - " + currentuser.getFirstname());
	        if (currentuser.getCases().size() > 0) {
	        	
	        	// load all test cases owned by user
	        	for (TestCase testcase : currentuser.getCases()) {
	        		menutree.addItem(testcase.getId());
	        		menutree.setItemCaption(testcase.getId(), testcase.getTitle());

//	        	}
	        	
//	        	for (Object caseid : getTestcases().getItemIds() ) {
//	        		TestCase testcase = getTestcases().getItem(caseid).getEntity();
//	        		menutree.addItem(caseid);
//	        		menutree.setItemCaption(caseid, testcase.getTitle());
	        		
	        		// load any child sessions
	        		System.out.println("GetSESSIONS size: " + testcase.getSessions().size());
	        		System.out.println("GetSESSIONS: " + testcase.getSessions());
	        		if ( testcase.getSessions().size() > 0 ){	// matchingsessions.isEmpty() 
//	            	 menutree.setChildrenAllowed(testcase, false);
//	             } else {
	        			// fill the subtree with sessions
	        			
	        			// sort session elements
	        			List<TestSession> caseSessions = testcase.getSessions();
	        			List<Integer> sortedids = new ArrayList<Integer>(); 
	        			for (TestSession s : caseSessions){
	        				sortedids.add(s.getId());
	        			}
	        			Collections.sort(sortedids);
	        			Collections.reverse(sortedids);
	        			System.out.println("SORTED ID's: " + sortedids);
	        			for (Object id : sortedids) {	//testcase.getSessions()	matchingsessions
	        				Object sessionid = sessions.getItem(id).getEntity().getId();
	        				menutree.addItem(sessionid);
	        				menutree.setItemCaption(sessionid, sessions.getItem(id).getEntity().getTitle());
	        				menutree.setParent(sessionid, testcase.getId());
	        				menutree.setChildrenAllowed(sessionid, false);
	        			}
	        		}
	        		
	        		menutree.expandItemsRecursively(testcase);
	        	}
	        }
	        
	        
	        menutree.addItemClickListener(new ItemClickListener() {
				@Override
				public void itemClick(ItemClickEvent event) {
	            	Object id = event.getItemId();
	            	System.out.println("this is the current ITEM selection: " + id.toString());
	            	
	            	// get path to navigate to
	            	String path = "";
	            	// get testCase for parent item or Session for children
	            	if ( menutree.isRoot(id) ){
//	            		TestCase caseEntity = testcases.getItem(id).getEntity();
	            		path = getTestcases().getItem(id).getEntity().getTitle();		//caseEntity.getTitle();
	            		System.out.println("this is the current ENTITY (CASE) selection's title: " + getTestcases().getItem(id).getEntity().getTitle());
	            		
	            	} else {	// a child (Session) was selected
	            		// get selected child
	            		path = sessions.getItem(id).getEntity().getTitle();
	            		System.out.println("this is the current ENTITY (SESSION) selection's title: " + sessions.getItem(id).getEntity().getTitle());
	            		
	            		// get parent (CASE) for path
	            		Object pid = menutree.getParent(id);
//		            	TestCase parentEntity = testcases.getItem(pid).getEntity();
//						String parent = (String) menutree.getParent(id);
		            	path = getTestcases().getItem(pid).getEntity().getTitle() + "/" + 
	            				sessions.getItem(id).getEntity().getTitle();
	            	}

//					if (!menutree.isRoot(id)) {
//						Object pid = menutree.getParent(id);
//		            	TestCase parentEntity = testcases.getItem(pid).getEntity();
////						String parent = (String) menutree.getParent(id);
//		            	path = parentEntity.getTitle() + "/" + caseEntity.getTitle();
//
//					}
					System.out.println("path is : " + path);
					
					// navigate to corresponding item
					getUI()
		         		.getNavigator()
		         			.navigateTo(MainView.NAME + "/" + path);
				}
			});

	        vc.addComponent(menutree);
	        
	        
	    	/* Add test cases as root items in the tree. */
//	    	for (int i=0; i<testCases.length; i++) {
//	    	    String testCase = (String) (testCases[i][0]);
//	    	    menutree.addItem(testCase);
//	    	    
//	    	    if (testCases[i].length == 1) {
//	    	        // The test case has no instances so make it a leaf.
//	    	        menutree.setChildrenAllowed(testCase, true);	//false
//	    	    } else {
//	    	        // Add children (instances) under the test cases.
//	    	        for (int j=1; j<testCases[i].length; j++) {
//	    	            String instance = (String) testCases[i][j];
//	    	            
//	    	            // Add the item as a regular item.
//	    	            menutree.addItem(instance);
//	    	            
//	    	            // Set it to be a child.
//	    	            menutree.setParent(instance, testCase);
//	    	            
//	    	            // Make the instance look like leaves.
//	    	            menutree.setChildrenAllowed(instance, false);
//	    	        }
//	
//	    	        // Expand the subtree.
//	//	    	        tree.expandItemsRecursively(testCase);
//	    	    }
//	    	}
	    	
	    	
	        // Contents from a (prefilled example) hierarchical container:
	//	        tree.setContainerDataSource(ExampleUtil.getHardwareContainer());
	 
	        // Set tree to show the 'name' property as caption for items
	//	        sample.setItemCaptionPropertyId(ExampleUtil.hw_PROPERTY_NAME);
	//	        tree.setItemCaptionMode(ItemCaptionMode.PROPERTY);
	 
	        // Expand whole tree
	        
	        
	        // generate button for all test cases in db
//	        for (Object id : testcases.getItemIds()) {
//				TestCase item = testcases.getItem(id).getEntity();
//		        Button button = new Button(item.getTitle());
//		        button.setIcon(FontAwesome.ANGLE_RIGHT);
//		        button.addStyleName("menu-button-left-align");
//		        button.addStyleName(ValoTheme.BUTTON_BORDERLESS);
//		        button.addStyleName(ValoTheme.BUTTON_SMALL); 
//		        vc.addComponent(button);
//	        }
	        
	        
//	        vc.addComponent(new Button("select no. 5",	new Button.ClickListener() {
//				@Override
//				public void buttonClick(ClickEvent event) {
//					Object item = testcases.getItem(5).getItemId();
//					menutree.select(item);
//					menutree.setChildrenAllowed(item, true);
//				}
//			}));
//	        
//	        vc.addComponent(new Button("show jpa contents",	new Button.ClickListener() {
//				@Override
//				public void buttonClick(ClickEvent event) {
//
//					for (final Object id : testcases.getItemIds()) {
//						TestCase tcase = testcases.getItem(id).getEntity();
//						System.out.println("TEST CASE no. " + tcase.getId() +
//											" - " + tcase.getTitle());
//					}
//
//					for (final Object id : menutree.rootItemIds()) {
////			            tree.expandItemsRecursively(id);
//						Object item = testcases.getItem(id).getItemId();
////			        	menutree.setChildrenAllowed(item, true);
//
//			        }       
//				}
//			}));
		         
	        

	        
	        
//	        tree.addValueChangeListener(new ValueChangeListener(){
//	            public void valueChange(ValueChangeEvent event){
//	            	// get parent and child
//	            	Object current = event.getProperty();
//	            	System.out.println("this is the current PROPERTY	 selection: " + event.getProperty().toString());
//	            	if(!tree.isRoot(event.getProperty())){
//	            		Object parent = tree.getParent(event.getProperty());
////		            	System.out.println("Parent is: " + tree.getParent(event.getProperty()).toString());
//
//	            	}
//	            }
//         }
//	        );
	    	
			return vc;
		}
	
	
//	private Component buildMenuItems() {
//        Panel panel = new Panel();
//        VerticalLayout menuButtons = new VerticalLayout();	
//        
//        for (String s : animals) {
//        	String animal = s.substring(0, 1).toUpperCase() + s.substring(1);
//        	menuButtons.addComponent(new Button(animal,
//        			new ButtonListener(s)));
//        }
//        panel.setContent(menuButtons);
//        return panel;
//	}
	
	
    /*
     * Returns the set of available actions
     */
    public Action[] getActions(Object target, Object sender) {
    	return ACTIONS;
    }
    /*
     * Handle actions
     */
    public void handleAction(final Action action, final Object sender,
            final Object target) {
        if (action == ACTION_ADD) {
        	Object parent = target;
        	// if wasn't a parent item select, move up to the parent
        	if (!menutree.isRoot(target)) {
        		parent = menutree.getParent(target);
        	}
        	System.out.println("parent is " + parent);
	        // open window to create TestSession
	        UI.getCurrent().addWindow(new TestSessionEditor(menutree, testcases.getItem(parent).getEntity()));	//testcases

//			NewUseCaseInstanceWindow sub = new NewUseCaseInstanceWindow(menutree, parent.toString());	        
//	        // Add it to the root component
//	        UI.getCurrent().addWindow(sub);
 
        } else if (action == ACTION_EDIT) {
        	Object parent = target;
        	// edit sessions
        	if (!menutree.isRoot(target)) {
//        		parent = menutree.getParent(target);
    	        UI.getCurrent().addWindow(new TestSessionEditor(menutree, target, testcases.getItem(menutree.getParent(target)).getEntity()));	//testcases
        		
        	} else if (menutree.isRoot(target)){
    	        UI.getCurrent().addWindow(new TestCaseEditor(menutree, target));	//testcases.getItem(parent).getEntity()        		
        	}
        	
        } else if (action == ACTION_DELETE) {
//            final Object parent = menutree.getParent(target);
        	Object parentid = null;
        	
        	// session items are never root
        	if (!menutree.isRoot(target)) {
//       			ConfirmDeleteMenuItemWindow confirm = new ConfirmDeleteMenuItemWindow(menutree, target, 
//									"Are you sure you want to delete <b>" + target.toString() + "</b>?<br /><br />");
       	        UI.getCurrent().addWindow(
       	        		new ConfirmDeleteMenuItemWindow(menutree, target, 
						"Are you sure you want to delete <b>" + 
						sessions.getItem(target).getEntity().getTitle() + "</b>?<br /><br />"));
//                return;
                
        	} else if (menutree.isRoot(target)) {            	
            	// ask user if attempted to delete root item that still has children items
            	if (menutree.hasChildren(target)) {
            		// ask user to confirm
        	        UI.getCurrent().addWindow(new ConfirmDeleteMenuItemWindow(menutree, target, 
							"<b>" + testcases.getItem(target).getEntity().getTitle() + 
								"</b> has Test Session instances that will all be deleted!<br />" +
							"Are you sure you want to delete <b>" + testcases.getItem(target).getEntity().getTitle() + 
								"</b> and all its Test Sessions?<br /><br />"));
            	} else {
	       	        UI.getCurrent().addWindow(new ConfirmDeleteMenuItemWindow(menutree, target, 
							"Are you sure you want to delete <b>" + 
							testcases.getItem(target).getEntity().getTitle() + "</b>?<br /><br />"));
            	}

            	return;
            }
       
        	// If the deleted object's parent has no more children collapse the item
//            if (parentid != null && menutree.getChildren(parentid) == null) {
////            	tree.setChildrenAllowed(parent, false);
//            	menutree.collapseItem(parentid);
//            }
        }
    }
    
//    public void setDisplayName(String name) {
//    	MainView.displayName = name;
//    }
//    
//    public String getDisplayName() {
//    	return MainView.displayName;
//    }
    
    public static void setMenuDisplayName(String newname) {
		List<MenuItem> mitems = userMenu.getItems();
		mitems.get(0).setText(newname);
		
		System.out.println("USER MENU new name: " + mitems.get(0).getText()); 	//getItems().toString());
    }


	public static JPAContainer<TestCase> getTestcases() {
		return testcases;
	}

	public void setTestcases(JPAContainer<TestCase> testcases) {
		this.testcases = testcases;
	}
	
	public static JPAContainer<TestSession> getTestsessions() {
		return sessions;
	}

	public void setTestsessions(JPAContainer<TestSession> sessions) {
		this.sessions = sessions;
	}

}

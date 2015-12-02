package com.aaron.mbpet.views;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.aaron.mbpet.MbpetUI;
import com.aaron.mbpet.domain.Model;
import com.aaron.mbpet.domain.Parameters;
import com.aaron.mbpet.domain.TRT;
import com.aaron.mbpet.domain.TestCase;
import com.aaron.mbpet.domain.TestSession;
import com.aaron.mbpet.domain.User;
import com.aaron.mbpet.services.ExampleUtil;
import com.aaron.mbpet.services.HierarchicalDepartmentContainer;
import com.aaron.mbpet.ui.ConfirmDeleteMenuItemWindow;
import com.aaron.mbpet.ui.NewUseCaseInstanceWindow;
import com.aaron.mbpet.views.cases.CreateTestCaseWindow;
import com.aaron.mbpet.views.cases.TestCaseEditor;
import com.aaron.mbpet.views.sessions.TestSessionEditor;
import com.aaron.mbpet.views.users.UserEditor;
import com.vaadin.event.Action;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.jpacontainer.JPAContainerFactory;
import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.event.Action.Handler;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinServlet;
import com.vaadin.server.VaadinSession;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.AbstractComponent;
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
	
//    public static final String NAME = "MBPeT";
	private Panel mainPanel;
	VerticalLayout menuLayout = new VerticalLayout();
	private Tree menutree;
	MenuBar userMenu;
	
	private JPAContainer<User> persons;
	private JPAContainer<TestCase> testcases;
	private JPAContainer<TestSession> sessions;

    public String displayName = "";
    private User sessionuser;
    public Item sessionUserItem;
//	public static BeanItemContainer<Model> userModelsContainer = new BeanItemContainer<Model>(Model.class);
	
    // Actions for the context menu
    private final Action ACTION_ADD = new Action("Add TestSession");
    private final Action ACTION_EDIT = new Action("Edit");
    private final Action ACTION_CLONE = new Action("Clone");
    private final Action ACTION_DELETE = new Action("Delete");
    private final Action[] ACTIONS = new Action[] { ACTION_ADD, ACTION_EDIT, ACTION_CLONE, ACTION_DELETE };
    
    
	public MBPeTMenu(Tree tree) {	//JPAContainer<User> persons	User sessUser, String usrname,
		this.menutree = tree;
		
		this.sessionuser = ((MbpetUI) UI.getCurrent()).getSessionUser();
        this.persons = ((MbpetUI) UI.getCurrent()).getPersons();	//JPAContainerFactory.make(User.class,MbpetUI.PERSISTENCE_UNIT);//persons;	
        this.testcases = ((MbpetUI) UI.getCurrent()).getTestcases();	//testcases;
        this.sessions = ((MbpetUI) UI.getCurrent()).getTestsessions();	//testcases;

        setDisplayName();

        mainPanel = new Panel();
		mainPanel.setHeight("100%");
		mainPanel.setWidth("250px");
		mainPanel.addStyleName("borderless");
		mainPanel.setContent(buildContent());
		
//		setUserDisplayName(usrname);
		setCompositionRoot(mainPanel);
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
		                UserEditor personEditor = new UserEditor(
		                		persons.getItem(sessionuser.getId()), 
		                			"Edit User Account", true);
		                personEditor.setModal(false);
		                UI.getCurrent().addWindow(personEditor);
		                personEditor.center();
		                
		        	} else if (selectedItem.getText().equals("Sign Out")){
		        		logout();
		        	}
		        }
		 };


		    

	    	
		userMenu = new MenuBar();
		userMenu.addStyleName("user-menu");
		
		MenuItem user = userMenu.addItem(displayName, null);
		user.addItem("Edit Profile", menuCommand);
//		user.addItem("Preferences", menuCommand);
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
		landingButton.addStyleName("borderless-colored");
//		landingButton.addStyleName("tiny");
		landingButton.setIcon(FontAwesome.HOME);
//		landingButton.addStyleName("borderless");
		buttons.addComponent(landingButton);
		buttons.setComponentAlignment(landingButton, Alignment.MIDDLE_LEFT);
		
		
		// create test case button
		Button createTestCase = new Button("Create new test case");
		createTestCase.addStyleName("menu-button-left-align");
		createTestCase.addStyleName("borderless-colored");
//		createTestCase.addStyleName("tiny");
		createTestCase.setIcon(FontAwesome.PLUS);
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
	        System.out.println(sessionuser.getId() + " -> " + sessionuser.getFirstname());
	        if (sessionuser.getTestCases().size() > 0) {
	        	
	        	// load all test cases owned by user
	        	for (TestCase testcase : sessionuser.getTestCases()) {
	        		menutree.addItem(testcase.getId());
	        		menutree.setItemCaption(testcase.getId(), testcase.getTitle());
	        		
	        		// load any child sessions
	        		System.out.println("GetSESSIONS size: " + testcase.getSessions().size());
	        		System.out.println("GetSESSIONS: " + testcase.getSessions());
	        		if ( testcase.getSessions().size() > 0 ){	// matchingsessions.isEmpty() 
//	            	 menutree.setChildrenAllowed(testcase, false);
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
	            		path = testcases.getItem(id).getEntity().getTitle() + "-sut=" + testcases.getItem(id).getEntity().getId();		//getTestcases()		caseEntity.getTitle();
	            		System.out.println("this is the current ENTITY (CASE) selection's title: " + testcases.getItem(id).getEntity().getTitle());
	            		
	            	} else {	// a child (Session) was selected
	            		// get selected child
//	            		path = sessions.getItem(id).getEntity().getTitle();
	            		System.out.println("this is the current ENTITY (SESSION) selection's title: " + sessions.getItem(id).getEntity().getTitle());
	            		
	            		// get parent (CASE) for path
	            		Object pid = menutree.getParent(id);
//		            	TestCase parentEntity = testcases.getItem(pid).getEntity();
//						String parent = (String) menutree.getParent(id);
		            	path = testcases.getItem(pid).getEntity().getTitle() + "/" + 
	            				sessions.getItem(id).getEntity().getTitle() + "id=" + sessions.getItem(id).getEntity().getId();
	            	}

					System.out.println("path is : " + path);
					
					// navigate to corresponding item
					getUI()
		         		.getNavigator()
		         			.navigateTo(MainView.NAME + "/" + path);
				}
			});

	        vc.addComponent(menutree);
	    	
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
	        UI.getCurrent().addWindow(new TestSessionEditor(menutree, testcases.getItem(parent).getEntity(), false));	//testcases

//			NewUseCaseInstanceWindow sub = new NewUseCaseInstanceWindow(menutree, parent.toString());	        
//	        // Add it to the root component
//	        UI.getCurrent().addWindow(sub);
 
        } else if (action == ACTION_EDIT) {
        	Object parent = target;
        	// edit sessions
        	if (!menutree.isRoot(target)) {
//        		parent = menutree.getParent(target);
    	        UI.getCurrent().addWindow(new TestSessionEditor(
    	        		menutree, 
    	        		target, 
    	        		testcases.getItem(menutree.getParent(target)).getEntity()));	//testcases
        		
        	} else if (menutree.isRoot(target)){
    	        UI.getCurrent().addWindow(new TestCaseEditor(menutree, target));	//testcases.getItem(parent).getEntity()        		
        	}
        	
        } else if (action == ACTION_CLONE) {
        	Object parent = target;
        	// if wasn't a parent item select, move up to the parent
        	if (menutree.isRoot(target)) {
//        		parent = menutree.getParent(target);
        	} else {      		
        		TestSession session = sessions.getItem(target).getEntity();	//.getBean();
        		UI.getCurrent().addWindow(new TestSessionEditor(
        				menutree, 
        				target, //session.getId(), 
        				testcases.getItem(menutree.getParent(target)).getEntity(),//session.getParentcase(),
//        				sessionsTable,
        				true)
        				);        		
        	}
 
        } else if (action == ACTION_DELETE) {
//            final Object parent = menutree.getParent(target);
        	Object parentid = null;
        	
        	// session items are never root
        	if (!menutree.isRoot(target)) {
//       			ConfirmDeleteMenuItemWindow confirm = new ConfirmDeleteMenuItemWindow(menutree, target, 
//									"Are you sure you want to delete <b>" + target.toString() + "</b>?<br /><br />");
       	        UI.getCurrent().addWindow(
       	        		new ConfirmDeleteMenuItemWindow(
       	        				menutree, 
       	        				target, 
       	        				"Are you sure you want to delete <b>" + 
   	        						sessions.getItem(target).getEntity().getTitle() + "</b>?<br /><br />"));
//                return;
                
        	} else if (menutree.isRoot(target)) {            	
            	// ask user if attempted to delete root item that still has children items
            	if (menutree.hasChildren(target)) {
            		// ask user to confirm
        	        UI.getCurrent().addWindow(
        	        		new ConfirmDeleteMenuItemWindow(
	        	        		menutree, 
	        	        		target, 
								"<b>" + testcases.getItem(target).getEntity().getTitle() + 
									"</b> has Test Session instances that will also be deleted!<br /><br />" +
									"Are you sure you want to delete <b>" + testcases.getItem(target).getEntity().getTitle() + 
									"</b> and all its data?<br /><br />"));
            	} else {
	       	        UI.getCurrent().addWindow(
	       	        		new ConfirmDeleteMenuItemWindow(
		       	        		menutree, 
		       	        		target, 
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
    
    
    public void setDisplayName(){
//    	if (displayName.equals("")) {	
//    		displayName = String.valueOf(getSession().getAttribute("user"));
    		
    		try {
				String lname = "";
				if (sessionuser.getLastname() != null) {
					lname = sessionuser.getLastname();
				}
				displayName = sessionuser.getFirstname() + " " +
								lname;
			} catch (NullPointerException e) {
				e.printStackTrace();
			}

//    	}	
    }
    
    public void updateMenuDisplayName(String newname) {
    	displayName = newname;
		List<MenuItem> mitems = userMenu.getItems();
		mitems.get(0).setText(newname);
		
//		System.out.println("USER MENU new name: " + mitems.get(0).getText()); 	//getItems().toString());
    }

    
	private void logout() {
        System.out.println("### LOGGING OUT ###");

        // 1st close any other UI's that is also created for this Session
        for (UI ui : UI.getCurrent().getSession().getUIs()) {
            if (ui != UI.getCurrent()) {
                ui.access(new Runnable() {	//Synchronously
                    @Override
                    public void run() {
                        System.out.println("Closing UI {0}" + UI.getCurrent());
                        UI.getCurrent().getPage().setLocation("/MBPeT/");
                        UI.getCurrent().close();
                    }
                });
            }
		
        }
		// "Logout" the user

//		MainView.sessionUser = null;
//		MainView.sessionUserItem = null;
//		// remove current user in session attribute        	
//        getSession().setAttribute("user", null);
//    	getSession().setAttribute("sessionUser", null);
//    	getSession().setAttribute("sessionUserItem", null);
    	
        
//        UI.getCurrent().getPage().setLocation(
//    			VaadinServlet.getCurrent().getServletContext().getContextPath());	//"/"
     
        // Refresh this view, should redirect to login view
//        UI.getCurrent().getNavigator().navigateTo(MainView.NAME);
//        UI.getCurrent().getNavigator().navigateTo(LoginView.NAME);

        updateMenuDisplayName("");

        //close the session
        UI.getCurrent().getSession().close();
        UI.getCurrent().getSession().getService().closeSession(VaadinSession.getCurrent());
//        UI.getCurrent().close();

        // Close the Current VaadinSession
        try {
            VaadinSession.getCurrent().close();
            
            // TESTING
            System.out.println("-- Session Cookies --");
            System.out.println("sessionuser " + VaadinSession.getCurrent().getAttribute("sessionUser"));
            System.out.println("sessionuseritem " + VaadinSession.getCurrent().getAttribute("sessionUserItem"));
            System.out.println("user " + VaadinSession.getCurrent().getAttribute("user"));
        } catch (Exception ex) {
            System.out.println(ex);
        }
//        VaadinSession.getCurrent().close();
        
        // redirect to start view
//      UI.getCurrent().getPage().setLocation("/MBPeT/");
        UI.getCurrent().getPage().setLocation(VaadinServlet.getCurrent().getServletContext().getContextPath());

      
        // Notice quickly if other UIs are closed
        getUI().setPollInterval(30);
        
//        return;
	}

}

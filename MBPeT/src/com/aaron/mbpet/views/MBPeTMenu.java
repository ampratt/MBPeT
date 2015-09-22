package com.aaron.mbpet.views;

import java.util.ArrayList;
import java.util.List;

import com.aaron.mbpet.MbpetUI;
import com.aaron.mbpet.domain.User;
import com.aaron.mbpet.ui.ConfirmDeleteMenuItemWindow;
import com.aaron.mbpet.ui.CreateTestCaseWindow;
import com.aaron.mbpet.ui.NewUseCaseInstanceWindow;
import com.aaron.mbpet.ui.PersonEditor;
import com.aaron.mbpet.utils.ExampleUtil;
import com.vaadin.event.Action;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.jpacontainer.JPAContainerFactory;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.util.BeanItem;
import com.vaadin.event.Action.Handler;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinServlet;
import com.vaadin.server.VaadinSession;
import com.vaadin.shared.ui.label.ContentMode;
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
	Tree tree;
	static MenuBar userMenu;
	private JPAContainer<User> persons;
	
    // Actions for the context menu
    private static final Action ACTION_ADD = new Action("Add child item");
    private static final Action ACTION_DELETE = new Action("Delete");
    private static final Action[] ACTIONS = new Action[] { ACTION_ADD, ACTION_DELETE };
    String[] animals = new String[] {"possum", "donkey", "pig", "duck", "dog", "cow", "horse", "cat", "reindeer", "penguin", "sheep", "goat", "tractor cow", "chicken", "bacon", "cheddar"};
    
    // Menu navigation button listener
//    class ButtonListener implements Button.ClickListener {
//        private static final long serialVersionUID = -4941184695301907995L;
//
//        String menuitem;
//        public ButtonListener(String menuitem) {
//            this.menuitem = menuitem;
//        }
//
//        @Override
//        public void buttonClick(ClickEvent event) {
//            // Navigate to a specific state
//        	UI.getCurrent()
//        		.getNavigator()
//            		.navigateTo(MainView.NAME + "/" + menuitem);
//        	//            navigator.navigateTo(MainView.NAME + "/" + menuitem);
//        }
//    }
    
	public MBPeTMenu(Tree tree) {	//User sessUser, String usrname,
//		this.sessionUser = sessUser;
		this.tree = tree;
//		System.out.println("DISPLAY NAME WAS " + displayName);
//		this.displayName = usrname;
		System.out.println("DISPLAY NAME WAS " + MainView.displayName);

        persons = JPAContainerFactory.make(User.class,
        		MbpetUI.PERSISTENCE_UNIT);
        
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
		                		persons.getItem(MainView.sessionUser.getId()), 
		                			"Edit User Account", true);

		                personEditor.setModal(false);
		                UI.getCurrent().addWindow(personEditor);
		                personEditor.center();
		                
		        	} else if (selectedItem.getText().equals("Preferences")){
		        		
		        	} else if (selectedItem.getText().equals("Sign Out")){
		        		// "Logout" the user
			            getSession().setAttribute("user", null);

			            // Refresh this view, should redirect to login view
//			            UI.getCurrent()
//			            	.getNavigator()
//			            		.navigateTo(LoginView.NAME);
			            
			            //close the session
			            UI.getCurrent().getSession().close();
			            UI.getCurrent().getSession().getService().closeSession(VaadinSession.getCurrent());
			            UI.getCurrent().close();
			            
			            UI.getCurrent().getPage().setLocation(
			        			VaadinServlet.getCurrent().getServletContext().getContextPath());	//"/"
			         
			            // Refresh this view, should redirect to login view
//			            UI.getCurrent().getNavigator().navigateTo(LoginView.NAME);
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
		
        // TESTING
        final Label testlabel = new Label("i added this");
        Button add = new Button("add", new Button.ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				buttons.addComponent(testlabel);
			}
		});
        
        Button remove = new Button("remove", new Button.ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				int labelindex = buttons.getComponentIndex(testlabel);
				Notification.show("layout count" + buttons.getComponent(getComponentCount()).toString() + 
									"index of testlabel" + buttons.getComponentIndex(testlabel));
				
			}
		});
        buttons.addComponent(add);
//        buttons.addComponent(remove);
        add.addStyleName("menu-button-left-align");
        remove.addStyleName("menu-button-left-align");
		buttons.setComponentAlignment(add, Alignment.MIDDLE_LEFT);
//		buttons.setComponentAlignment(remove, Alignment.MIDDLE_LEFT);

        
        
		
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
		        // open window to create item		        
		        // Add it to the root component
		        UI.getCurrent().addWindow(new CreateTestCaseWindow(tree));
		        
		        
//	            // Create new item, set as parent, allow children (= leaf node)
//	            final Object[] itemId = new Object[] {"New Item"};
//	            String name = (String) itemId[0];
//	            tree.addItem(itemId[0].toString());
////	            final Object itemId = tree.addItem();
////	            tree.setParent(itemId, target);
//	            tree.setChildrenAllowed(itemId, true);
//	 
//	            // Set the name for this item (we use it as item caption)
//	            final Item item = tree.getItem(itemId);
////	            final Property name = item
////	                    .getItemProperty(ExampleUtil.hw_PROPERTY_NAME);
////	            name.setValue("New Item");
//
//	            // Allow children for the target item, and expand it
////	            tree.setChildrenAllowed(target, true);
////	            tree.expandItem(target);
			}
		});
		
		return buttons;
	}
	
	
	private VerticalLayout buildTreeMenu() {
		// layout holder for menu items
		VerticalLayout vc = new VerticalLayout();
		vc.setHeight("100%");
		

		
		Label divider = new Label("<hr>", ContentMode.HTML);
//		divider.addStyleName("menu-divider");
		vc.addComponent(divider);
		
		// TREE MENU
		   final Object[][] testCases = new Object[][]{
	    	        new Object[]{"Dashboard", "01.02.15", "03.04.15", "04.05.15", "06.07.15"},
	    	        new Object[]{"Panel", "01.02.15", "03.04.15", "04.05.15", "06.07.15"},
	    	        new Object[]{"Portal", "01.02.15", "03.04.15", "04.05.15", "06.07.15"},
	    	        new Object[]{"demo", "01.02.15", "03.04.15", "04.05.15", "06.07.15"},
	    	        new Object[]{"random", "01.02.15", "03.04.15", "04.05.15", "06.07.15"}
		        };
	    	        
	    	
	    	vc.addComponent(tree);
	    	tree.addStyleName("tiny");
//	    	tree.addStyleName("treemenu");
	    	
	    	/* Add test cases as root items in the tree. */
	    	for (int i=0; i<testCases.length; i++) {
	    	    String testCase = (String) (testCases[i][0]);
	    	    tree.addItem(testCase);
	    	    
	    	    if (testCases[i].length == 1) {
	    	        // The test case has no instances so make it a leaf.
	    	        tree.setChildrenAllowed(testCase, true);	//false
	    	    } else {
	    	        // Add children (instances) under the test cases.
	    	        for (int j=1; j<testCases[i].length; j++) {
	    	            String instance = (String) testCases[i][j];
	    	            
	    	            // Add the item as a regular item.
	    	            tree.addItem(instance);
	    	            
	    	            // Set it to be a child.
	    	            tree.setParent(instance, testCase);
	    	            
	    	            // Make the instance look like leaves.
	    	            tree.setChildrenAllowed(instance, false);
	    	        }
	
	    	        // Expand the subtree.
	//	    	        tree.expandItemsRecursively(testCase);
	    	    }
	    	}
	    	
	    	
	        // Contents from a (prefilled example) hierarchical container:
	//	        tree.setContainerDataSource(ExampleUtil.getHardwareContainer());
	 
	        // Add actions (context menu)
		    tree.addActionHandler(this);
	 
	        // Cause valueChange immediately when the user selects
	        tree.setImmediate(true);
	 
	        // Set tree to show the 'name' property as caption for items
	//	        sample.setItemCaptionPropertyId(ExampleUtil.hw_PROPERTY_NAME);
	//	        tree.setItemCaptionMode(ItemCaptionMode.PROPERTY);
	 
	        // Expand whole tree
	//	        for (final Object id : tree.rootItemIds()) {
	//	            tree.expandItemsRecursively(id);
	//	        }
	 
	        tree.addItemClickListener(new ItemClickListener() {
				@Override
				public void itemClick(ItemClickEvent event) {
					// TODO Auto-generated method stub
	            	String selected = (String) event.getItemId();
	            	String path = selected;
	            	System.out.println("this is the current ITEM selection: " + selected.toString());
					if (!tree.isRoot(event.getItemId())) {
	//						Object parent = tree.getParent(event.getItemId());
						String parent = (String) tree.getParent(event.getItemId());
		            	path = parent+ "/" + selected;
					}
					
					// navigate to corresponding item
					getUI()
		         		.getNavigator()
		         			.navigateTo(MainView.NAME + "/" + 
		     							path);
					// update page title
//					ContentView.setPageTitle(path);
	
	//					Notification.show("Value changed: ",
	//	    	                path,	//event.getItem()),
	//	    	                Type.HUMANIZED_MESSAGE); 
				}
			});
	        
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
        	if (!tree.isRoot(target)) {
        		parent = tree.getParent(target);
        	}
	        // open window to create item
			NewUseCaseInstanceWindow sub = new NewUseCaseInstanceWindow(tree, parent.toString());
	        
	        // Add it to the root component
	        UI.getCurrent().addWindow(sub);
//	        
//            // Allow children for the target item, and expand it
//            tree.setChildrenAllowed(target, true);
//            tree.expandItem(target);
// 
//            // Create new item, set parent, disallow children (= leaf node)
//            final Object[] itemId = new Object[]{"New Item"};
//    	    String itemName = (String) (itemId[0]);
//            tree.addItem(itemName);
//            tree.setParent(itemName, target);
//            tree.setChildrenAllowed(itemName, false);
// 
//            // Set the name for this item (we use it as item caption)
//            final Item item = tree.getItem(itemName);
////            final Property name = item
////                    .getItemProperty(ExampleUtil.hw_PROPERTY_NAME);
////            name.setValue("New Item");
 
        } else if (action == ACTION_DELETE) {
            final Object parent = tree.getParent(target);
            
            // if deleted parent item, return to landing page
            if (tree.isRoot(target)) {
            	
            	//if attempted to delete root item that still has children items
            	if (tree.hasChildren(target)) {
            		// ask user to confirm
        	        // open window to create item
        			ConfirmDeleteMenuItemWindow confirm = new ConfirmDeleteMenuItemWindow(tree, target, 
        							"<b>" + target.toString() + "</b> has child instances that will be deleted.<br />" +
									"Delete <b>" + target.toString() + "</b> and all its instances?<br /><br />");
        	        
        	        // Add it to the root component
        	        UI.getCurrent().addWindow(confirm);
            	} else {
           			ConfirmDeleteMenuItemWindow confirm = new ConfirmDeleteMenuItemWindow(tree, target, 
										"Are you sure you want to delete <b>" + target.toString() + "</b>?<br /><br />");
	       	        UI.getCurrent().addWindow(confirm);
            	}
//            	tree.removeItem(target);
//            	getUI()
//	            	.getNavigator()
//	            		.navigateTo(MainView.NAME + "/" + "landingPage");
            	return;
            }
        	
            tree.removeItem(target);

        	// If the deleted object's parent has no more children collapse the item
            if (parent != null && tree.getChildren(parent) == null) {
//            	tree.setChildrenAllowed(parent, false);
            	tree.collapseItem(parent);
            }
            tree.select(parent);
            //navigate to parent
            getUI()
	            .getNavigator()
	            	.navigateTo(MainView.NAME + "/" + 
	            			parent);
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

}

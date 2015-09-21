package com.aaron.mbpet.views;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import com.aaron.mbpet.domain.User;
import com.aaron.mbpet.ui.AnimalViewer;
import com.vaadin.annotations.DesignRoot;
import com.vaadin.data.Item;
import com.vaadin.event.SelectionEvent;
import com.vaadin.event.SelectionEvent.SelectionListener;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Page;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Tree;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;

/** Main view with a menu (with declarative layout design) */
//@DesignRoot
public class MainView extends HorizontalLayout implements View {
    private static final long serialVersionUID = -3398565663865641952L;

    public static String NAME = "home";	//MBPeT
    public static String displayName = "";
    public static User sessionUser;
    public static Item sessionUserItem;
//    private Item sessionUser;
    
    VerticalLayout menuLayout = new VerticalLayout();
	Panel contentLayout = new Panel();
	MBPeTMenu menu;
	Tree tree;
//	ContentView contentView;

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
//    				.navigateTo(MainView.NAME + "/" + menuitem);
//        	//        	navigator.navigateTo(MainView.NAME + "/" + menuitem);
//        }
//    }
    
    
	public MainView() {
		
//		tree = new Tree("Test Cases:");
//		landingPage = new LandingPageView(tree);
		
//    	setSpacing(true);
		setSizeFull();
		addStyleName("mainview");
    	
		addComponent(menuLayout);
    	setExpandRatio(menuLayout, 1.7f);		

    	addComponent(contentLayout);
    	setExpandRatio(contentLayout, 8.3f);    	

    	// call this in enter()
//		MenuLayout();
		ContentLayout();
    	
    	  	
//        Collection<Item> c = (Collection<Item>) tree.rootItemIds();
//        System.out.println("the tree collection length: " + c.size());
//        Object[] array = c.toArray();
//        for (int i=0; i<array.length; i++) {
//        	System.out.println("this item was: " + array[i]);
//            	
//        }
//        int count = 1;
//        for (Iterator iterator = c.iterator(); iterator.hasNext();) { 
//        	if (count == 1)
//        		System.out.println("\nfor loop: ");
//        	System.out.println(iterator.next());
//        	count++;
//    	}
//        
////        Iterator itr = c.iterator();
////        count = 1;
////        //iterate through the ArrayList values using Iterator's hasNext and next methods
////        while(itr.hasNext()) {
////        	if (count == 1)
////        		System.out.println("\nwhile loop:");
////        	System.out.println(itr.next());
////        	count ++;
////        }
    }        
    
	
	
	private void MenuLayout() {
		menuLayout.setHeight("100%");
		tree = new Tree("Test Cases:");

		// add menu to main view
    	menu = new MBPeTMenu(tree);	//sessionUser, displayName,
    	menuLayout.addComponent(menu);
//    	setExpandRatio(menu, 1.7f);		
	}

	
	private void ContentLayout() {
		contentLayout.setHeight("100%");
		contentLayout.addStyleName("borderless");
//		contentLayout.setContent(new LandingPage(tree));
//		addComponent(landingPage);	
//    	setExpandRatio(landingPage, 8.3f);    	
		
	}

    
    
    @Override
    public void enter(ViewChangeEvent event) {

    	// Get the user name from the session
    	sessionUser = (User) getSession().getAttribute("sessionUser");
    	sessionUserItem = (Item) getSession().getAttribute("sessionUserItem");

    	if (displayName.equals("")) {	
//    		displayName = String.valueOf(getSession().getAttribute("user"));
    		displayName = sessionUser.getFirstname() + " " +
    						sessionUser.getLastname();
//    		setDisplayName(sessionUser.getFirstname() + " " +
//					sessionUser.getLastname());
    		
//    		// And pass it to the menu to disaply it
//    		Notification.show("welcome: " + displayName);
    		MenuLayout();
//      	menu.setUserDisplayName(username);    		
    	}
    	
       	if (event.getParameters().equals("landingPage")
    			|| event.getParameters() == null || event.getParameters().isEmpty()) {
       		contentLayout.setContent(new LandingPage(tree));
            
              return;
        } else {
//        	try {
    		contentLayout.setContent(new ContentViewer(
    				event.getParameters(), tree));
        		// update page title
//        		ContentView.setPageTitle(event.getParameters());
//        	} catch (RuntimeException e) {
//        		getUI().getConnectorTracker().markAllConnectorsDirty(); 
//        		getUI().getConnectorTracker().markAllClientSidesUninitialized(); 
//        		getUI().getPage().reload();
//        	}
        }
       	
       	
       	
    	// Get the user name from the session
//        String username = String.valueOf(getSession().getAttribute("user"));
//
//        // And pass it to the menu to disaply it
//        Notification.show("welcome: " + username);
        
//        if (event.getParameters() == null
//            || event.getParameters().isEmpty()) {
//          contentView.equalPanel.setContent(
//        		  new Label("Nothing to see here, " +
//        				  "just pass along."));
//            return;
//        } 
//    	if (event.getParameters().equals("landingPage")
//    			|| event.getParameters() == null || event.getParameters().isEmpty()) {
////            removeComponent(contentView);
//        	removeComponent(getComponent(1));	//pageTemplate
//            addComponent(landingPage);
//            setExpandRatio(landingPage, 8.3f);
//            
//            markAsDirty();
//            
//              return;
//        } else {
//        	removeComponent(getComponent(1));	//contentView
//        	try {
//        		ContentView contentView = new ContentView("test title", tree);
//        		addComponent(contentView);        		
//        		setExpandRatio(contentView, 8.3f);
//
//                markAsDirty();
//
//        		// update page title
//        		ContentView.setPageTitle(event.getParameters());
//        		
////        		contentView.equalPanel.setContent(new ContentViewer(
////        				event.getParameters()));
//        	} catch (RuntimeException e) {
//        		getUI().getConnectorTracker().markAllConnectorsDirty(); 
//        		getUI().getConnectorTracker().markAllClientSidesUninitialized(); 
//        		getUI().getPage().reload();
//        	}
//        }
    }
    
    
//    public static void setDisplayName(String name) {
//    	displayName = name;
//    }
//    
//    public static String getDisplayName() {
//    	return displayName;
//    }
  

}

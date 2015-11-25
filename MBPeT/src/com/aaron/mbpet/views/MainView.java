package com.aaron.mbpet.views;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.persistence.Query;

import com.aaron.mbpet.MbpetUI;
import com.aaron.mbpet.domain.Model;
import com.aaron.mbpet.domain.Parameters;
import com.aaron.mbpet.domain.TRT;
import com.aaron.mbpet.domain.TestCase;
import com.aaron.mbpet.domain.TestSession;
import com.aaron.mbpet.domain.User;
import com.aaron.mbpet.views.cases.CaseViewer;
import com.aaron.mbpet.views.sessions.SessionViewer;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.jpacontainer.JPAContainerFactory;
import com.vaadin.annotations.DesignRoot;
import com.vaadin.data.Item;
import com.vaadin.event.SelectionEvent;
import com.vaadin.event.SelectionEvent.SelectionListener;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Alignment;
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
//    public static String displayName = "";
//    public static User sessionuser;
//    public static Item sessionUserItem;
//    private Item sessionUser;
    
    Panel menuLayout = new Panel();	//VerticalLayout
	Panel contentLayout;	// = new Panel();
	Tree tree;
	MBPeTMenu menu;
//	ContentView contentView;
	boolean firstenter = true;

	public static JPAContainer<User> persons;
	public static JPAContainer<TestCase> testcases = getTestcases();
	public static JPAContainer<TestSession> sessions;
	public static JPAContainer<Parameters> parameterscontainer;
	public static JPAContainer<Model> models;
	public static JPAContainer<TRT> trtcontainer;

    
	public MainView() {
		
		persons = JPAContainerFactory.make(User.class,
        		MbpetUI.PERSISTENCE_UNIT);
		setTestcases(JPAContainerFactory.make(TestCase.class,
				MbpetUI.PERSISTENCE_UNIT));
		setTestsessions(JPAContainerFactory.make(TestSession.class,
        		MbpetUI.PERSISTENCE_UNIT)); 
        models = JPAContainerFactory.make(Model.class,
        		MbpetUI.PERSISTENCE_UNIT);
        parameterscontainer = JPAContainerFactory.make(Parameters.class,
        		MbpetUI.PERSISTENCE_UNIT); 
        trtcontainer = JPAContainerFactory.make(TRT.class,
        		MbpetUI.PERSISTENCE_UNIT);

        
    	initContent();
    	
    }        
    
    @Override
    public void enter(ViewChangeEvent event) {
//    	initContent();

    	// Get the user name from the session
    	
//    	sessionUser = (User) getSession().getAttribute("sessionUser");
//    	User su = (User) VaadinSession.getCurrent().getAttribute("sessionUser");
//    	System.out.println("session user: " + su.getId());
//    	sessionuser = persons.getItem(su.getId()).getEntity();
//    	sessionUserItem = (Item) VaadinSession.getCurrent().getAttribute("sessionUserItem");

//    	if (displayName.equals("")) {	
////    		displayName = String.valueOf(getSession().getAttribute("user"));
//    		
//    		String lname = sessionUser.getLastname();
//    		if (sessionUser.getLastname() == null) {
//    			lname = "";
//    		}
//    		displayName = sessionUser.getFirstname() + " " +
//    						lname;
    		if (firstenter){
    			MenuLayout();
    			firstenter = false;
    		}
    		
//      	menu.setUserDisplayName(username);    		
//    	}
    	

    	
       	if (event.getParameters().equals("landingPage")
    			|| event.getParameters() == null || event.getParameters().isEmpty()) {
       		contentLayout.setContent(new LandingPage(tree));
            
              return;
        } else if (event.getParameters().contains("sut=")){	//! "/"
//        	getTestCaseByTitleID(event.getParameters());
        	// navigate to TestCase home page
    		contentLayout.setContent(new CaseViewer(
    				event.getParameters(), tree));

        } else {
        	// navigate to Session page
    		contentLayout.setContent(new SessionViewer(
    				event.getParameters(), tree));
        }
       	
    }
    
    
	public void initContent() {
		
//		tree = new Tree("Test Cases:");
//		landingPage = new LandingPageView(tree);
		
//    	setSpacing(true);
		setSizeFull();
		addStyleName("mainview");
//    	setWidth(100%);
    	
		tree = new Tree("SUT's:");
//		menu = new MBPeTMenu(tree);
//		menu.setWidth("250px");
//		addComponent(menu);
    	
//		addComponent(menuLayout);
//    	setExpandRatio(menuLayout, 0);	//2.0f);	//1.7	

		contentLayout = new Panel();
		contentLayout.setHeight("100%");
		contentLayout.setWidth("100%");
		contentLayout.addStyleName("borderless");
		addComponent(contentLayout);
    	setExpandRatio(contentLayout, 1.0f);	//8.0f);    	
//    	setComponentAlignment(contentLayout, Alignment.TOP_LEFT);
    	// call this in enter()
//		MenuLayout();
//		ContentLayout();
	}
	
	
	
	private void MenuLayout() {
		
		menu = new MBPeTMenu(tree);
		menu.setWidth("250px");
		addComponentAsFirst(menu);
		
//		menuLayout.setHeight("100%");
//		menuLayout.setWidth("250px");
////		menuLayout.addStyleName("menu-containerpanel");
//		menuLayout.addStyleName("borderless");
//		tree = new Tree("SUT's:");
//
//		// add menu to main view
////    	menu = new MBPeTMenu(persons, tree);	//sessionUser, displayName,
//    	menuLayout.setContent(new MBPeTMenu(persons, tree));	//(menu);
////    	setExpandRatio(menu, 1.7f);		
	}
//
//	
//	private void ContentLayout() {
//		contentLayout.setHeight("100%");
//		contentLayout.setWidth("100%");
//		contentLayout.addStyleName("borderless");
////		contentLayout.setContent(new LandingPage(tree));
////		addComponent(landingPage);	
////    	setExpandRatio(landingPage, 8.3f);    	
//		
//	}

   
    
    
	public static JPAContainer<TestCase> getTestcases() {
		return MbpetUI.testcases;
	}

	
	public void setTestcases(JPAContainer<TestCase> testcases) {
		MbpetUI.testcases = testcases;
	}
	
	public static JPAContainer<TestSession> getTestsessions() {
		return MainView.sessions;
	}

	public void setTestsessions(JPAContainer<TestSession> sessions) {
		MainView.sessions = sessions;
	}

	public static JPAContainer<Model> getModels() {
		return MainView.models;
	}

	public void setModels(JPAContainer<Model> models) {
		MainView.models = models;
	}
   
	
//    public static void setDisplayName(String name) {
//    	displayName = name;
//    }
//    
//    public static String getDisplayName() {
//    	return displayName;
//    }
  


}

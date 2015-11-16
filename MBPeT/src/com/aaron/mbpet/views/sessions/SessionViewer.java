package com.aaron.mbpet.views.sessions;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

import com.aaron.mbpet.MbpetUI;
import com.aaron.mbpet.domain.TestCase;
import com.aaron.mbpet.domain.TestSession;
import com.aaron.mbpet.services.UDPServer;
import com.aaron.mbpet.services.MasterUtils;
import com.aaron.mbpet.ui.NewUseCaseInstanceWindow;
import com.aaron.mbpet.views.MBPeTMenu;
import com.aaron.mbpet.views.tabs.TabLayout;
import com.aaron.mbpet.views.users.UserEditor.EditorSavedEvent;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.jpacontainer.JPAContainerFactory;
import com.vaadin.data.Container;
import com.vaadin.data.util.HierarchicalContainer;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.event.ListenerMethod.MethodException;
import com.vaadin.event.SelectionEvent;
import com.vaadin.event.SelectionEvent.SelectionListener;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Resource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Tree;
import com.vaadin.ui.VerticalLayout;

public class SessionViewer extends VerticalLayout implements Button.ClickListener {	//implements View 
	private static final long serialVersionUID = -5878465079008311569L;
	
	Panel equalPanel = new Panel("equal panel"); 
	public static Label pageTitle = new Label("");
	Tree tree;
    JPAContainer<TestCase> testcases;
    static JPAContainer<TestSession> sessions;
    public static TestSession currsession;

	private Button saveButton;
	private Button stopButton;
	private Button startButton;
	private Button newSessionButton;
	
	public SessionViewer(String title, Tree tree) {
		setSizeFull();
		this.addStyleName("content");
		
        testcases = MBPeTMenu.getTestcases();
        sessions = MBPeTMenu.getTestsessions();
        
		this.tree = tree;
		setPageTitle(removeID(title));		//setPageTitle(title);
		
		currsession = getTestSessionByTitleID(title); //getTestSessionByTitle();
	
		addComponent(buildTopBar());

//		Component contentLayout = buildContentLayout();
//    	addComponent(contentLayout);
//    	setExpandRatio(contentLayout, 1);
    	
//		VerticalLayout tabs = new VerticalLayout();
		TabLayout tabs = new TabLayout();
		addComponent(tabs);
    	setExpandRatio(tabs, 1);
	}

	public HorizontalLayout buildTopBar() {
		pageTitle.addStyleName("test-case-title");
		pageTitle.addStyleName("h2");
		
		newSessionButton = new Button("", this);
		saveButton = new Button("", this);
		startButton = new Button("", this);
		stopButton = new Button("", this);
		
		newSessionButton.addStyleName("tiny");
		saveButton.addStyleName("tiny");
//		saveButton.addStyleName("friendly");
		startButton.addStyleName("tiny");
//		startButton.addStyleName("primary");
		stopButton.addStyleName("tiny");
		
		newSessionButton.setIcon(FontAwesome.PLUS);
		saveButton.setIcon(FontAwesome.SAVE);
		startButton.setIcon(FontAwesome.PLAY);
		stopButton.setIcon(FontAwesome.STOP);
		
		newSessionButton.setDescription("Create a new Test Session");
		saveButton.setDescription("Save Settings and Parameters");
		startButton.setDescription("Run Test Session");
		stopButton.setDescription("Stop Test Session");
		
		HorizontalLayout topBar = new HorizontalLayout();
		topBar.setStyleName("topBar-layout-padding");
		topBar.setWidth("100%");
		topBar.setSpacing(true);

		HorizontalLayout buttons = new HorizontalLayout();
		buttons.setSpacing(true);
		buttons.addComponents(saveButton, startButton, stopButton);	//newSessionButton 
		
		topBar.addComponents(pageTitle, buttons); //(pageTitle);
//		topBar.addComponent(newUseCaseButton);
//		topBar.addComponent(saveButton);
//		topBar.addComponent(startButton);
		
		topBar.setComponentAlignment(pageTitle, Alignment.MIDDLE_LEFT);
		topBar.setComponentAlignment(buttons, Alignment.MIDDLE_RIGHT);	//(newUseCaseButton, Alignment.MIDDLE_RIGHT);
//		topBar.setComponentAlignment(saveButton, Alignment.MIDDLE_RIGHT);
//		topBar.setComponentAlignment(startButton, Alignment.MIDDLE_RIGHT);
		
		topBar.setExpandRatio(pageTitle, 2);
		topBar.setExpandRatio(buttons, 2);	//(newUseCaseButton, 2);
//		topBar.setExpandRatio(saveButton, 0);	
//		topBar.setExpandRatio(startButton, 0);
//		topBar.setExpandRatio(stopButton, 0);	    
	    
		return topBar;
	}
	
	
    public void buttonClick(ClickEvent event) {
        if (event.getButton() == newSessionButton) {
	        // open window to create item
	        UI.getCurrent().addWindow(new TestSessionEditor(tree, currsession.getParentcase(), false));	//getTestCaseByTitle()	testcases.getItem(parent).getEntity()

        } else if (event.getButton() == saveButton) {
			//testing purposes
			Notification.show("Your settings will be saved", Type.WARNING_MESSAGE);

        } else if (event.getButton() == startButton) {
			//testing purposes
			Notification.show("This will launch the test session to the master", Type.WARNING_MESSAGE);

			MbpetUI mbpetui = new MbpetUI();
			MbpetUI.PushThread push = mbpetui.new PushThread();
			push.start();
			
//			new UDPServer();
//			new MasterUtils();

        } else if (event.getButton() == stopButton) {
			//testing purposes
			Notification.show("This will stop the test", Type.WARNING_MESSAGE);

        }
    }
   

	
	public static void setPageTitle(String t){
		pageTitle.setValue(t);

	}
	public String getPageTitle() {
		return pageTitle.getValue();
	}

	private static String removeID(String input) {
//		String title = pageTitle.getValue();
		String title = "";
		if (input.contains("/") && input.contains("id=")) {
			title = input.substring(0, (input.indexOf("=")-2)); 
		}
//		if (input.contains("#")) {
//			title = input.substring((input.indexOf("#")+1), input.length()); 
//		}
		System.out.println("the parsed page title is: " + title);
		
		return title;
	}
	
	private static TestSession getTestSessionByTitleID(String input) {
		String parsed = "";
		if (input.contains("id=")) {
			parsed = input.substring((input.indexOf("=")+1), input.length()); 
		}
		System.out.println("the parsed test session ID is: " + parsed);
		
		int id = Integer.parseInt(parsed);
//		currsession = sessions.getItem(id).getEntity();
//		
//        System.out.println("retrieved SESSION fro db is :  - " + currsession.getTitle());
		
		return sessions.getItem(id).getEntity();
	}
	

	
}

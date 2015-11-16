package com.aaron.mbpet.views.cases;

import java.util.Iterator;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

import com.vaadin.data.util.filter.Compare;
import com.vaadin.data.util.filter.Compare.Equal;
import com.vaadin.data.util.filter.Like;
import com.vaadin.data.util.filter.Or;
import com.vaadin.data.util.filter.SimpleStringFilter;
import com.aaron.mbpet.MbpetUI;
import com.aaron.mbpet.domain.Model;
import com.aaron.mbpet.domain.TestCase;
import com.aaron.mbpet.domain.TestSession;
import com.aaron.mbpet.ui.NewUseCaseInstanceWindow;
import com.aaron.mbpet.views.MBPeTMenu;
import com.aaron.mbpet.views.MainView;
import com.aaron.mbpet.views.sessions.TestSessionEditor;
import com.aaron.mbpet.views.tabs.TabLayout;
import com.aaron.mbpet.views.users.UserEditor.EditorSavedEvent;
import com.vaadin.addon.jpacontainer.EntityItem;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.jpacontainer.JPAContainerFactory;
import com.vaadin.data.Container;
import com.vaadin.data.Container.Filter;
import com.vaadin.data.Container.Filterable;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.util.HierarchicalContainer;
import com.vaadin.event.FieldEvents.TextChangeEvent;
import com.vaadin.event.FieldEvents.TextChangeListener;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.event.ListenerMethod.MethodException;
import com.vaadin.event.SelectionEvent;
import com.vaadin.event.SelectionEvent.SelectionListener;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Resource;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Grid;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.MenuBar.Command;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Tree;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public class CaseViewer extends Panel implements Button.ClickListener {	//implements View 
	
	Panel equalPanel = new Panel("equal panel"); 
	public static Label pageTitle = new Label("");
	Tree tree;
    JPAContainer<TestCase> testcases;
    JPAContainer<TestSession> sessions;
    JPAContainer<Model> models;
    TestCase currSUT;

	private Button saveButton;
	private Button stopButton;
	private Button startButton;
	private Button newSessionButton;
	private Button editButton;
	private Button cloneButton;
	private Table sessionsTable;
	
	private CssLayout contentPanels;
	private VerticalLayout root;
	private GridLayout grid;
    private TextField searchField;
    private String sessionsFilter;

	public CaseViewer(String title, Tree tree) {
        addStyleName(ValoTheme.PANEL_BORDERLESS);
        setSizeFull();

        root = new VerticalLayout();
        root.setSizeFull();
        root.setMargin(false);
        root.addStyleName("dashboard-view-background-gray");
        root.addStyleName("dashboard-view");
        setContent(root);
        
        
//		setSizeFull();
//		this.addStyleName("dashboard-view");
////		this.addStyleName("content");
//		this.setMargin(new MarginInfo(false, true, false, true));
		
        testcases = MBPeTMenu.getTestcases();
        sessions = MBPeTMenu.getTestsessions();
        models = MBPeTMenu.getModels();
        this.currSUT = getTestCaseByTitleID(title);
        
		this.tree = tree;
		setPageTitle(currSUT.getTitle());
		
		root.addComponent(buildTopBar());

        Component content = buildContent();
        root.addComponent(content);
        root.setExpandRatio(content, 1);

    	
	}

	public HorizontalLayout buildTopBar() {
		HorizontalLayout topBar = new HorizontalLayout();
		topBar.addStyleName("topBar-layout-padding-white");
		topBar.setWidth("100%");
		topBar.setSpacing(true);
		
//		pageTitle.addStyleName("test-case-title");
		pageTitle.addStyleName("h2");
		
		newSessionButton = new Button("", this);
		newSessionButton.addStyleName("tiny");
		newSessionButton.setIcon(FontAwesome.PLUS);
		newSessionButton.setDescription("create new Test Session");
//		saveButton = new Button("Save settings", this);
//		startButton = new Button("Run Test", this);
//		stopButton = new Button("Stop Test", this);
		
//		saveButton.addStyleName("tiny");
////		saveButton.addStyleName("friendly");
//		startButton.addStyleName("tiny");
////		startButton.addStyleName("primary");
//		stopButton.addStyleName("tiny");
//		
//		saveButton.setIcon(FontAwesome.SAVE);
//		startButton.setIcon(FontAwesome.PLAY);
//		stopButton.setIcon(FontAwesome.STOP);
	

		HorizontalLayout buttons = new HorizontalLayout();
		buttons.setSpacing(true);
		buttons.addComponent(newSessionButton); //saveButton, startButton, stopButton);	//newSessionButton 
		
		topBar.addComponent(pageTitle); //(pageTitle);
		topBar.addComponent(buttons);
//		topBar.addComponent(newUseCaseButton);
//		topBar.addComponent(saveButton);
//		topBar.addComponent(startButton);
		
		topBar.setComponentAlignment(pageTitle, Alignment.MIDDLE_LEFT);
		topBar.setComponentAlignment(buttons, Alignment.MIDDLE_RIGHT);	//(newUseCaseButton, Alignment.MIDDLE_RIGHT);
//		topBar.setComponentAlignment(saveButton, Alignment.MIDDLE_RIGHT);
//		topBar.setComponentAlignment(startButton, Alignment.MIDDLE_RIGHT);
		
		topBar.setExpandRatio(pageTitle, 2);
//		topBar.setExpandRatio(buttons, 2);	//(newUseCaseButton, 2);
//		topBar.setExpandRatio(saveButton, 0);	
//		topBar.setExpandRatio(startButton, 0);
//		topBar.setExpandRatio(stopButton, 0);	    
	    
		return topBar;
	}
	
	
    private Component buildContent() {
        contentPanels = new CssLayout(); 	//new CssLayout();
//        contentPanels.setMargin(new MarginInfo(false, true, true, true));
        contentPanels.addStyleName("dashboard-panels");
        
        Label label = new Label("Test Case Editing View");
        label.addStyleName(ValoTheme.LABEL_H2);
	    label.addStyleName(ValoTheme.LABEL_COLORED);
	    contentPanels.addComponent(label);
        
        
        grid = new GridLayout(2, 1);
        grid.setSizeFull();
        grid.setSpacing(true);

//        contentPanels.addComponent(buildTestSessions());
        grid.addComponent(new SessionEditorTable(tree, currSUT));	//getTestCaseByTitle() (buildTestSessions());
        grid.addComponent(new ModelEditorTable(tree, currSUT));	//getTestCaseByTitle() (buildTestSessions());
        
//        grid.addComponent(buildModels());

        contentPanels.addComponent(grid);

        return contentPanels;
    }
    
    

	
//	private Component buildModels() {
//        TextArea notes = new TextArea("Notes");
//        notes.setValue("Remember to:\n� Zoom in and out in the Sales view\n� Filter the transactions and drag a set of them to the Reports tab\n� Create a new report\n� Change the schedule of the movie theater");
//        notes.setSizeFull();
////        notes.setWidth("100%");
//        notes.addStyleName(ValoTheme.TEXTAREA_BORDERLESS);
//        Component panel = createPanelWrapper(notes);
//        panel.addStyleName("notes");
//        
//        return panel;
//	}
	
//	private Component buildTestSessions() {
//	Table sessionsTable = new Table("Test Sessions", MBPeTMenu.userSessionsContainer);
//	sessionsTable.setSizeFull();
//	sessionsTable.addStyleName(ValoTheme.TABLE_BORDERLESS);
//	sessionsTable.addStyleName(ValoTheme.TABLE_NO_HORIZONTAL_LINES);
//	
//	sessionsTable.setSelectable(true);
//    sessionsTable.setImmediate(true);
//	
//	Component contentWrapper = createContentWrapper(sessionsTable);
//    contentWrapper.addStyleName("sessions-table");
//	
//	return contentWrapper;
//}

//	private Component createPanelWrapper(final Component content) {
//		final Panel panel = new Panel();
////		panel.addStyleName("panel-caption");
////		panel.setSizeFull();
//        panel.setHeight("250px");
//
//		
//		VerticalLayout layout = new VerticalLayout();
//        layout.setSizeFull();
////        layout.setSpacing(true);
////        Label spacer = new Label(
////                "Suspendisse dictum feugiat nisl ut dapibus. Mauris iaculis porttitor posuere. Praesent id metus massa, ut blandit odio.");
////        spacer.setWidth("10em");
////        layout.addComponent(spacer);       
//        
//	    
//	    HorizontalLayout panelHeader = new HorizontalLayout();
////	    panelCaption.addStyleName("v-panel-caption");
//	    panelHeader.addStyleName("panel-style-layout-header");
//	    panelHeader.setWidth("100%");
//	    // panelCaption.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);
//	    
//	    Label label = new Label(content.getCaption());
//	    label.addStyleName(ValoTheme.LABEL_H4);
//	    label.addStyleName(ValoTheme.LABEL_COLORED);
//	    label.addStyleName(ValoTheme.LABEL_NO_MARGIN);
//	    content.setCaption(null);
//	    
//        searchField = new TextField();
//        searchField.setInputPrompt("Search by title");
//        searchField.addTextChangeListener(new TextChangeListener() {
//            @Override
//            public void textChange(TextChangeEvent event) {
//            	sessionsFilter = event.getText();
//                updateFilters();
//            }
//        });
//
//
//	    Button edit = new Button();
//	    edit.setIcon(FontAwesome.PENCIL);
//	    edit.addStyleName("borderless-colored");
//	    edit.addStyleName("small");
//	    edit.addStyleName("icon-only");
//	    edit.setDescription("edit selected session");
//	    edit.setEnabled(false);
//	    
//	    MenuBar dropdown = new MenuBar();
//	    dropdown.addStyleName("borderless");
//	    dropdown.addStyleName("small");
//	    MenuItem addItem = dropdown.addItem("", FontAwesome.COG, null);
//	    addItem.setStyleName("icon-only");
//	    addItem.addItem("Settings", null);
//	    addItem.addItem("Preferences", null);
//	    addItem.addSeparator();
//	    addItem.addItem("Sign Out", null);
//
//	    panelHeader.addComponent(label);
//	    panelHeader.addComponent(searchField);
//	    panelHeader.addComponent(edit);
//	    panelHeader.addComponent(dropdown);
//	    panelHeader.setComponentAlignment(label, Alignment.MIDDLE_LEFT);
//	    panelHeader.setComponentAlignment(searchField, Alignment.MIDDLE_RIGHT);
//	    panelHeader.setExpandRatio(label, 1);
//	    
////	    card.addComponents(panelHeader, content);
//////	    layout.setExpandRatio(content, 1);
////	    card.setWidth("100%");
////	    
////	    slot.addComponent(card);
//	    
////	    content.setSizeFull();
//	    layout.addComponent(panelHeader);
//        layout.addComponent(content);
//        layout.setExpandRatio(content, 1);
//        panel.setContent(layout);
//        
//
//        return panel;
//	}
//	
//	
//	private Component createContentWrapper2(final Component content) {
//        final CssLayout slot = new CssLayout();
//        slot.setWidth("100%");
//        slot.addStyleName("dashboard-panel-slot");
//
//        CssLayout card = new CssLayout();
//        card.setWidth("100%");
//        card.addStyleName(ValoTheme.LAYOUT_CARD);
//
//        HorizontalLayout toolbar = new HorizontalLayout();
//        toolbar.addStyleName("dashboard-panel-toolbar");
//        toolbar.setWidth("100%");
//
//        Label caption = new Label(content.getCaption());
//        caption.addStyleName(ValoTheme.LABEL_H4);
//        caption.addStyleName(ValoTheme.LABEL_COLORED);
//        caption.addStyleName(ValoTheme.LABEL_NO_MARGIN);
//        content.setCaption(null);
//
//        MenuBar tools = new MenuBar();
//        tools.addStyleName(ValoTheme.MENUBAR_BORDERLESS);
//        MenuItem max = tools.addItem("", FontAwesome.EXPAND, new Command() {
//
//            @Override
//            public void menuSelected(final MenuItem selectedItem) {
//                if (!slot.getStyleName().contains("max")) {
//                    selectedItem.setIcon(FontAwesome.COMPRESS);
////                    toggleMaximized(slot, true);
//                } else {
//                    slot.removeStyleName("max");
//                    selectedItem.setIcon(FontAwesome.EXPAND);
////                    toggleMaximized(slot, false);
//                }
//            }
//        });
//        max.setStyleName("icon-only");
//        MenuItem root = tools.addItem("", FontAwesome.COG, null);
//        root.addItem("Configure", new Command() {
//            @Override
//            public void menuSelected(final MenuItem selectedItem) {
//                Notification.show("Not implemented in this demo");
//            }
//        });
//        root.addSeparator();
//        root.addItem("Close", new Command() {
//            @Override
//            public void menuSelected(final MenuItem selectedItem) {
//                Notification.show("Not implemented in this demo");
//            }
//        });
//
//        toolbar.addComponents(caption, tools);
//        toolbar.setExpandRatio(caption, 1);
//        toolbar.setComponentAlignment(caption, Alignment.MIDDLE_LEFT);
//
//        card.addComponents(toolbar, content);
//        slot.addComponent(card);
//        return slot;
//        
//	}

    
    
	public void buttonClick(ClickEvent event) {
        if (event.getButton() == newSessionButton) {
	        // open window to create item
	        UI.getCurrent().addWindow(new TestSessionEditor(tree, currSUT, true));	//getTestCaseByTitle() testcases.getItem(parent).getEntity()

        } else if (event.getButton() == editButton) {
			TestSession session = sessions.getItem(sessionsTable.getValue()).getEntity();	//.getBean();
	        UI.getCurrent().addWindow(new TestSessionEditor(
	        			tree, 
	        			session.getId(), 
	        			testcases.getItem(
	        					sessions.getItem(session.getId()).getEntity().getParentcase().getId()).getEntity(),
        				sessionsTable)
	        );
        } else if (event.getButton() == cloneButton) {
			TestSession session = sessions.getItem(sessionsTable.getValue()).getEntity();	//.getBean();
	        UI.getCurrent().addWindow(new TestSessionEditor(
	        			tree, 
	        			session.getId(), 
	        			testcases.getItem(
	        					sessions.getItem(session.getId()).getEntity().getParentcase().getId()).getEntity(),
        				sessionsTable)
	        );

        } else if (event.getButton() == saveButton) {
			//testing purposes
			Notification.show("Your settings will be saved", Type.WARNING_MESSAGE);

        } else if (event.getButton() == startButton) {
			//testing purposes
			Notification.show("This will launch the test session to the master", Type.WARNING_MESSAGE);

        } else if (event.getButton() == stopButton) {
			//testing purposes
			Notification.show("This will stop the test", Type.WARNING_MESSAGE);

        }
    }
	
	
	
    private void setFilterByTestCase() {
    	sessions.removeAllContainerFilters();
    	Equal casefilter = new Equal("parentcase", currSUT);//  (getTestCaseByTitle()	"parentcase", getTestCaseByTitle(), true, false);
    	
    	sessions.addContainerFilter(casefilter);
    }
	
	
    private void updateFilters() {
    	sessions.removeAllContainerFilters();
    	
    	setFilterByTestCase();
    	SimpleStringFilter filter = new SimpleStringFilter("title", sessionsFilter, true, false);
    	sessions.addContainerFilter(filter);
    }
   

	private TestCase getTestCaseByTitleID(String input) {
		String parsed = "";
		if (input.contains("sut=")) {
			parsed = input.substring((input.indexOf("=")+1), input.length()); 
			System.out.println("the parsed SUT ID is: " + parsed);
			
			int id = Integer.parseInt(parsed);
			currSUT = testcases.getItem(id).getEntity();
			
	        System.out.println("retrieved SESSION fro db is :  - " + currSUT.getTitle());
		}
		
		return currSUT;
	}
	
	
	private TestCase getTestCaseByTitle() {
		String title = pageTitle.getValue();
		if (title.contains("/")) {
			title = title.substring(0, title.indexOf("/")); 
		}
		System.out.println("the parses test case title is: " + title);
		
		// add created item to tree (after retrieving db generated id)
        EntityManager em = Persistence.createEntityManagerFactory("mbpet")
										.createEntityManager();	
        Query query = em.createQuery(
    		    "SELECT OBJECT(t) FROM TestCase t WHERE t.title = :title"
    		);
//        query.setParameter("title", newsession.getTitle());
        TestCase queriedCase = 
        		(TestCase) query.setParameter("title", title).getSingleResult();
        System.out.println("retrieved TC fro db is : " 
        				+  queriedCase.getId() + " - " + queriedCase.getTitle());
			            
		return testcases.getItem(queriedCase.getId()).getEntity();
		
	}

	
	public static void setPageTitle(String t){
		pageTitle.setValue(t);

	}
	public String getPageTitle() {
		return pageTitle.getValue();
	}
}

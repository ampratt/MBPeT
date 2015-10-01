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
import com.aaron.mbpet.components.tabs.TabLayout;
import com.aaron.mbpet.domain.TestCase;
import com.aaron.mbpet.domain.TestSession;
import com.aaron.mbpet.ui.NewUseCaseInstanceWindow;
import com.aaron.mbpet.views.MBPeTMenu;
import com.aaron.mbpet.views.MainView;
import com.aaron.mbpet.views.sessions.TestSessionEditor;
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
        root.addStyleName("dashboard-view");
        setContent(root);
        
        
//		setSizeFull();
//		this.addStyleName("dashboard-view");
////		this.addStyleName("content");
//		this.setMargin(new MarginInfo(false, true, false, true));
		
        testcases = MBPeTMenu.getTestcases();
        sessions = MBPeTMenu.getTestsessions();
        
		this.tree = tree;
		setPageTitle(title);
		
		root.addComponent(buildTopBar());

        Component content = buildContent();
        root.addComponent(content);
        root.setExpandRatio(content, 1);

    	
	}

	public HorizontalLayout buildTopBar() {
//		pageTitle.addStyleName("test-case-title");
		pageTitle.addStyleName("h2");
		
//		newSessionButton = new Button("Test Session", this);
		saveButton = new Button("Save settings", this);
		startButton = new Button("Run Test", this);
		stopButton = new Button("Stop Test", this);
		
//		newSessionButton.addStyleName("tiny");
		saveButton.addStyleName("tiny");
//		saveButton.addStyleName("friendly");
		startButton.addStyleName("tiny");
//		startButton.addStyleName("primary");
		stopButton.addStyleName("tiny");
		
//		newSessionButton.setIcon(FontAwesome.PLUS);
		saveButton.setIcon(FontAwesome.SAVE);
		startButton.setIcon(FontAwesome.PLAY);
		stopButton.setIcon(FontAwesome.STOP);
		
		HorizontalLayout topBar = new HorizontalLayout();
		topBar.addStyleName("topBar-layout-padding-white");
		topBar.setWidth("100%");
		topBar.setSpacing(true);

		HorizontalLayout buttons = new HorizontalLayout();
		buttons.setSpacing(true);
		buttons.addComponents(saveButton, startButton, stopButton);	//newSessionButton 
		
		topBar.addComponent(pageTitle); //(pageTitle);
//		topBar.addComponent(buttons);
//		topBar.addComponent(newUseCaseButton);
//		topBar.addComponent(saveButton);
//		topBar.addComponent(startButton);
		
		topBar.setComponentAlignment(pageTitle, Alignment.MIDDLE_LEFT);
//		topBar.setComponentAlignment(buttons, Alignment.MIDDLE_RIGHT);	//(newUseCaseButton, Alignment.MIDDLE_RIGHT);
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
        grid.addComponent(new SessionEditorTable(tree, getTestCaseByTitle()));	//(buildTestSessions());
        grid.addComponent(buildModels());

        contentPanels.addComponent(grid);

        return contentPanels;
    }
    
    

    
	@SuppressWarnings("deprecation")
	private Component buildTestSessions() {
		final Panel panel = new Panel();
//		panel.addStyleName("panel-caption");
//		panel.setSizeFull();
        panel.setHeight("250px");

		
		VerticalLayout layout = new VerticalLayout();
        layout.setSizeFull();
//        layout.setSpacing(true);
//        Label spacer = new Label(
//                "Suspendisse dictum feugiat nisl ut dapibus. Mauris iaculis porttitor posuere. Praesent id metus massa, ut blandit odio.");
//        spacer.setWidth("10em");
//        layout.addComponent(spacer);       
        
	    
	    HorizontalLayout panelHeader = new HorizontalLayout();
//	    panelCaption.addStyleName("v-panel-caption");
	    panelHeader.addStyleName("panel-style-layout-header");
	    panelHeader.setWidth("100%");
	    // panelCaption.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);
	    
	    Label label = new Label("Test Sessions");
	    label.addStyleName(ValoTheme.LABEL_H4);
	    label.addStyleName(ValoTheme.LABEL_COLORED);
	    label.addStyleName(ValoTheme.LABEL_NO_MARGIN);
//	    content.setCaption(null);
	    
        searchField = new TextField();
        searchField.setInputPrompt("Search by title");
        searchField.addTextChangeListener(new TextChangeListener() {
            @Override
            public void textChange(TextChangeEvent event) {
            	sessionsFilter = event.getText();
                updateFilters();
            }
        });
        
	    editButton = new Button("", this);
	    editButton.setIcon(FontAwesome.PENCIL);
	    editButton.addStyleName("borderless-colored");
	    editButton.addStyleName("small");
	    editButton.addStyleName("icon-only");
	    editButton.setDescription("edit selected session");
	    editButton.setEnabled(false);

	    cloneButton = new Button("", this);
	    cloneButton.setIcon(FontAwesome.FILES_O);
	    cloneButton.addStyleName("borderless-colored");
	    cloneButton.addStyleName("small");
	    cloneButton.addStyleName("icon-only");
	    cloneButton.setDescription("clone session");
	    cloneButton.setEnabled(false);
	    
	    newSessionButton = new Button("", this);
	    newSessionButton.setIcon(FontAwesome.PLUS);
	    newSessionButton.addStyleName("borderless-colored");
	    newSessionButton.addStyleName("small");
	    newSessionButton.addStyleName("icon-only");
	    newSessionButton.setDescription("create new session");
	    
	    MenuBar dropdown = new MenuBar();
	    dropdown.addStyleName("borderless");
	    dropdown.addStyleName("small");
	    MenuItem addItem = dropdown.addItem("", FontAwesome.COG, null);
	    addItem.setStyleName("icon-only");
	    addItem.addItem("Settings", null);
	    addItem.addItem("Preferences", null);
	    addItem.addSeparator();
	    addItem.addItem("Sign Out", null);

	    panelHeader.addComponent(label);
	    panelHeader.addComponents(searchField, editButton, cloneButton, newSessionButton);
	    panelHeader.addComponent(dropdown);
	    panelHeader.setComponentAlignment(label, Alignment.MIDDLE_LEFT);
	    panelHeader.setComponentAlignment(searchField, Alignment.MIDDLE_RIGHT);
	    panelHeader.setExpandRatio(label, 1);
	    
	    
	    // Table
		sessionsTable = new Table();
		sessionsTable.setContainerDataSource(sessions);	//(userSessionsContainer);
		setFilterByTestCase();
//		sessionsTable.setWidth("100%");
//		sessionsTable.setHeight("100%");
		sessionsTable.addStyleName(ValoTheme.TABLE_BORDERLESS);
		sessionsTable.addStyleName(ValoTheme.TABLE_NO_HEADER);
		sessionsTable.addStyleName(ValoTheme.TABLE_NO_HORIZONTAL_LINES);
//		sessionsTable.addStyleName(ValoTheme.TABLE_SMALL);
		sessionsTable.setSizeFull();
		
		sessionsTable.setSelectable(true);
        sessionsTable.setImmediate(true);
        sessionsTable.setVisibleColumns("title");
//        sessionsTable.setColumnExpandRatio("title", 1);

        sessionsTable.addListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(ValueChangeEvent event) {
                setModificationsEnabled(event.getProperty().getValue() != null);
            }

            private void setModificationsEnabled(boolean b) {
                editButton.setEnabled(b);
                cloneButton.setEnabled(b);
            }
        });
 
//	    editButton.addClickListener(new Button.ClickListener() {
//			@Override
//			public void buttonClick(ClickEvent event) {				
//				TestSession session = sessions.getItem(sessionsTable.getValue()).getEntity();	//.getBean();
//    	        UI.getCurrent().addWindow(new TestSessionEditor(
//    	        			tree, 
//    	        			session.getId(), 
//    	        			testcases.getItem(
//    	        					sessions.getItem(session.getId()).getEntity().getParentcase().getId()).getEntity(),
//	        				sessionsTable)
//    	        			//userSessionsContainer.getItem(sessionsTable.getValue()))
//				);
//			}
//		});
//	    card.addComponents(panelHeader, content);
////	    layout.setExpandRatio(content, 1);
//	    card.setWidth("100%");
//	    
//	    slot.addComponent(card);
	    
//	    content.setSizeFull();
	    layout.addComponent(panelHeader);
        layout.addComponent(sessionsTable);
        layout.setExpandRatio(sessionsTable, 1);
        panel.setContent(layout);
        
 
        return panel;
		
//		Component contentWrapper = createPanelWrapper(sessionsTable);
////	    contentWrapper.addStyleName("sessions-table");
//		
//		return contentWrapper;
	}
	
	private Component buildModels() {
        TextArea notes = new TextArea("Notes");
        notes.setValue("Remember to:\n� Zoom in and out in the Sales view\n� Filter the transactions and drag a set of them to the Reports tab\n� Create a new report\n� Change the schedule of the movie theater");
        notes.setSizeFull();
//        notes.setWidth("100%");
        notes.addStyleName(ValoTheme.TEXTAREA_BORDERLESS);
        Component panel = createPanelWrapper(notes);
        panel.addStyleName("notes");
        
        return panel;
	}
	
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

	private Component createPanelWrapper(final Component content) {
		final Panel panel = new Panel();
//		panel.addStyleName("panel-caption");
//		panel.setSizeFull();
        panel.setHeight("250px");

		
		VerticalLayout layout = new VerticalLayout();
        layout.setSizeFull();
//        layout.setSpacing(true);
//        Label spacer = new Label(
//                "Suspendisse dictum feugiat nisl ut dapibus. Mauris iaculis porttitor posuere. Praesent id metus massa, ut blandit odio.");
//        spacer.setWidth("10em");
//        layout.addComponent(spacer);       
        
	    
	    HorizontalLayout panelHeader = new HorizontalLayout();
//	    panelCaption.addStyleName("v-panel-caption");
	    panelHeader.addStyleName("panel-style-layout-header");
	    panelHeader.setWidth("100%");
	    // panelCaption.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);
	    
	    Label label = new Label(content.getCaption());
	    label.addStyleName(ValoTheme.LABEL_H4);
	    label.addStyleName(ValoTheme.LABEL_COLORED);
	    label.addStyleName(ValoTheme.LABEL_NO_MARGIN);
	    content.setCaption(null);
	    
        searchField = new TextField();
        searchField.setInputPrompt("Search by title");
        searchField.addTextChangeListener(new TextChangeListener() {
            @Override
            public void textChange(TextChangeEvent event) {
            	sessionsFilter = event.getText();
                updateFilters();
            }
        });


	    Button edit = new Button();
	    edit.setIcon(FontAwesome.PENCIL);
	    edit.addStyleName("borderless-colored");
	    edit.addStyleName("small");
	    edit.addStyleName("icon-only");
	    edit.setDescription("edit selected session");
	    edit.setEnabled(false);
	    
	    MenuBar dropdown = new MenuBar();
	    dropdown.addStyleName("borderless");
	    dropdown.addStyleName("small");
	    MenuItem addItem = dropdown.addItem("", FontAwesome.COG, null);
	    addItem.setStyleName("icon-only");
	    addItem.addItem("Settings", null);
	    addItem.addItem("Preferences", null);
	    addItem.addSeparator();
	    addItem.addItem("Sign Out", null);

	    panelHeader.addComponent(label);
	    panelHeader.addComponent(searchField);
	    panelHeader.addComponent(edit);
	    panelHeader.addComponent(dropdown);
	    panelHeader.setComponentAlignment(label, Alignment.MIDDLE_LEFT);
	    panelHeader.setComponentAlignment(searchField, Alignment.MIDDLE_RIGHT);
	    panelHeader.setExpandRatio(label, 1);
	    
//	    card.addComponents(panelHeader, content);
////	    layout.setExpandRatio(content, 1);
//	    card.setWidth("100%");
//	    
//	    slot.addComponent(card);
	    
//	    content.setSizeFull();
	    layout.addComponent(panelHeader);
        layout.addComponent(content);
        layout.setExpandRatio(content, 1);
        panel.setContent(layout);
        

        return panel;
	}
	
	private Component createContentWrapper(final Component content) {
        final CssLayout slot = new CssLayout();
        slot.setWidth("100%");
        slot.addStyleName("dashboard-panel-slot");
        
        CssLayout card = new CssLayout();
	    card.addStyleName("card");
//	    layout.addStyleName(ValoTheme.LAYOUT_CARD);
//	    layout.setHeight("100%");
	    
	    HorizontalLayout panelHeader = new HorizontalLayout();
//	    panelCaption.addStyleName("v-panel-caption");
	    panelHeader.addStyleName("panel-style-layout-header");
	    panelHeader.setWidth("100%");
	    // panelCaption.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);
	    
	    Label label = new Label(content.getCaption());
	    label.addStyleName(ValoTheme.LABEL_H4);
	    label.addStyleName(ValoTheme.LABEL_COLORED);
	    label.addStyleName(ValoTheme.LABEL_NO_MARGIN);
	    panelHeader.addComponent(label);
	    panelHeader.setComponentAlignment(label, Alignment.MIDDLE_LEFT);
	    panelHeader.setExpandRatio(label, 1);
	    content.setCaption(null);

	    Button action = new Button();
	    action.setIcon(FontAwesome.PENCIL);
	    action.addStyleName("borderless-colored");
	    action.addStyleName("small");
	    action.addStyleName("icon-only");
	    panelHeader.addComponent(action);
	    
	    MenuBar dropdown = new MenuBar();
	    dropdown.addStyleName("borderless");
	    dropdown.addStyleName("small");
	    MenuItem addItem = dropdown.addItem("", FontAwesome.COG, null);
	    addItem.setStyleName("icon-only");
	    addItem.addItem("Settings", null);
	    addItem.addItem("Preferences", null);
	    addItem.addSeparator();
	    addItem.addItem("Sign Out", null);
	    panelHeader.addComponent(dropdown);
	    
	    card.addComponents(panelHeader, content);
//	    layout.setExpandRatio(content, 1);
	    card.setWidth("100%");
	    
	    slot.addComponent(card);

        return slot;
	}
	
	
	private Component createContentWrapper2(final Component content) {
        final CssLayout slot = new CssLayout();
        slot.setWidth("100%");
        slot.addStyleName("dashboard-panel-slot");

        CssLayout card = new CssLayout();
        card.setWidth("100%");
        card.addStyleName(ValoTheme.LAYOUT_CARD);

        HorizontalLayout toolbar = new HorizontalLayout();
        toolbar.addStyleName("dashboard-panel-toolbar");
        toolbar.setWidth("100%");

        Label caption = new Label(content.getCaption());
        caption.addStyleName(ValoTheme.LABEL_H4);
        caption.addStyleName(ValoTheme.LABEL_COLORED);
        caption.addStyleName(ValoTheme.LABEL_NO_MARGIN);
        content.setCaption(null);

        MenuBar tools = new MenuBar();
        tools.addStyleName(ValoTheme.MENUBAR_BORDERLESS);
        MenuItem max = tools.addItem("", FontAwesome.EXPAND, new Command() {

            @Override
            public void menuSelected(final MenuItem selectedItem) {
                if (!slot.getStyleName().contains("max")) {
                    selectedItem.setIcon(FontAwesome.COMPRESS);
//                    toggleMaximized(slot, true);
                } else {
                    slot.removeStyleName("max");
                    selectedItem.setIcon(FontAwesome.EXPAND);
//                    toggleMaximized(slot, false);
                }
            }
        });
        max.setStyleName("icon-only");
        MenuItem root = tools.addItem("", FontAwesome.COG, null);
        root.addItem("Configure", new Command() {
            @Override
            public void menuSelected(final MenuItem selectedItem) {
                Notification.show("Not implemented in this demo");
            }
        });
        root.addSeparator();
        root.addItem("Close", new Command() {
            @Override
            public void menuSelected(final MenuItem selectedItem) {
                Notification.show("Not implemented in this demo");
            }
        });

        toolbar.addComponents(caption, tools);
        toolbar.setExpandRatio(caption, 1);
        toolbar.setComponentAlignment(caption, Alignment.MIDDLE_LEFT);

        card.addComponents(toolbar, content);
        slot.addComponent(card);
        return slot;
        
	}

	
//    private void toggleMaximized(final Component panel, final boolean maximized) {
//        for (Iterator<Component> it = this.iterator(); it.hasNext();) {
//            it.next().setVisible(!maximized);
//        }
//        contentPanels.setVisible(true);
//
//        for (Iterator<Component> it = contentPanels.iterator(); it.hasNext();) {
//            Component c = it.next();
//            c.setVisible(!maximized);
//        }
//
//        if (maximized) {
//            panel.setVisible(true);
//            panel.addStyleName("max");
//        } else {
//            panel.removeStyleName("max");
//        }
//    }
    
    
	public void buttonClick(ClickEvent event) {
        if (event.getButton() == newSessionButton) {
	        // open window to create item
	        UI.getCurrent().addWindow(new TestSessionEditor(tree, getTestCaseByTitle() ));	//testcases.getItem(parent).getEntity()

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
//    	Equal ownerfilter = new Equal("parentcase", getTestCaseByTitle());//  ("parentcase", getTestCaseByTitle(), true, false);
    	Equal casefilter = new Equal("parentcase", getTestCaseByTitle());//  ("parentcase", getTestCaseByTitle(), true, false);
    	
    	sessions.addContainerFilter(casefilter);
    	
//    	SimpleStringFilter filter = null;
//    	Filterable f = (Filterable)userSessionsContainer;
    	//remove old filters
//    	if (filter != null){
//    		f.removeContainerFilter(filter);
//    	}
    }
	
	
    private void updateFilters() {
    	sessions.removeAllContainerFilters();
    	
    	setFilterByTestCase();
    	SimpleStringFilter filter = new SimpleStringFilter("title", sessionsFilter, true, false);
    	sessions.addContainerFilter(filter);
    	
//    	SimpleStringFilter filter = null;
//    	Filterable f = (Filterable)userSessionsContainer;
    	//remove old filters
//    	if (filter != null){
//    		f.removeContainerFilter(filter);
//    	}
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

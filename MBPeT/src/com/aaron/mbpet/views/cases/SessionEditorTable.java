/**
 * Panel table list of Sessions on SUT home page
 */
package com.aaron.mbpet.views.cases;

import com.aaron.mbpet.MbpetUI;
import com.aaron.mbpet.domain.TestCase;
import com.aaron.mbpet.domain.TestSession;
import com.aaron.mbpet.ui.ConfirmDeleteMenuItemWindow;
import com.aaron.mbpet.views.sessions.TestSessionEditor;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.util.filter.SimpleStringFilter;
import com.vaadin.data.util.filter.Compare.Equal;
import com.vaadin.event.FieldEvents.TextChangeEvent;
import com.vaadin.event.FieldEvents.TextChangeListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Tree;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.themes.ValoTheme;

public class SessionEditorTable extends Panel implements Button.ClickListener {

	Tree tree; 
	TestCase testcase;
	
	public static Table sessionsTable;
	private TextField searchField;
	private Button editButton;
	private Button cloneButton;
	private Button newSessionButton;
	private Button deleteButton;
    private String sessionsFilter;

    JPAContainer<TestSession> sessions;

	public SessionEditorTable(Tree tree, TestCase testcase) {
		this.tree = tree;
		this.testcase = testcase;
		this.sessions = ((MbpetUI) UI.getCurrent()).getTestsessions();
		
		buildTestSessionsPanel();
	}
	

	@SuppressWarnings("deprecation")
	private void buildTestSessionsPanel() {
//		final Panel panel = new Panel();
//		panel.addStyleName("panel-caption");
//		panel.setSizeFull();
        this.setHeight("325px");

		
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
        searchField.addStyleName("tiny");
        searchField.setWidth(11, Unit.EM);
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
	    editButton.setDescription("edit selected Session");
	    editButton.setEnabled(false);

	    cloneButton = new Button("", this);
	    cloneButton.setIcon(FontAwesome.FILES_O);
	    cloneButton.addStyleName("borderless-colored");
	    cloneButton.addStyleName("small");
	    cloneButton.addStyleName("icon-only");
	    cloneButton.setDescription("clone Session");
	    cloneButton.setEnabled(false);
	    
	    newSessionButton = new Button("", this);
	    newSessionButton.setIcon(FontAwesome.PLUS);
	    newSessionButton.addStyleName("borderless-colored");
	    newSessionButton.addStyleName("small");
	    newSessionButton.addStyleName("icon-only");
	    newSessionButton.setDescription("create new Session");
	    
	    deleteButton = new Button("", this);
	    deleteButton.setIcon(FontAwesome.TRASH_O);
	    deleteButton.addStyleName("borderless-colored");
	    deleteButton.addStyleName("small");
	    deleteButton.addStyleName("icon-only");
	    deleteButton.setDescription("delete Session");
	    deleteButton.setEnabled(false);

	    
//	    MenuBar dropdown = new MenuBar();
//	    dropdown.addStyleName("borderless");
//	    dropdown.addStyleName("small");
//	    MenuItem addItem = dropdown.addItem("", FontAwesome.COG, null);
//	    addItem.setStyleName("icon-only");
//	    addItem.addItem("Settings", null);
//	    addItem.addItem("Preferences", null);
//	    addItem.addSeparator();
//	    addItem.addItem("Sign Out", null);

	    panelHeader.addComponent(label);
	    panelHeader.addComponents(searchField, editButton, cloneButton, newSessionButton, deleteButton);
//	    panelHeader.addComponent(dropdown);
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
		sessionsTable.addStyleName(ValoTheme.TABLE_SMALL);
		sessionsTable.setSizeFull();
		
		sessionsTable.setSelectable(true);
        sessionsTable.setImmediate(true);
        sessionsTable.setVisibleColumns("title");
//        sessionsTable.setColumnExpandRatio("title", 1);

        sessionsTable.addListener(new Property.ValueChangeListener() {
        	@Override
        	public void valueChange(
        			com.vaadin.data.Property.ValueChangeEvent event) {
        		setModificationsEnabled(event.getProperty().getValue() != null);
        		
        	}

        	private void setModificationsEnabled(boolean b) {
                editButton.setEnabled(b);
                cloneButton.setEnabled(b);
                deleteButton.setEnabled(b);
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
        this.setContent(layout);
        
 
//        return panel;
		
//		Component contentWrapper = createPanelWrapper(sessionsTable);
////	    contentWrapper.addStyleName("sessions-table");
//		
//		return contentWrapper;
	}

	
	public void buttonClick(ClickEvent event) {
        if (event.getButton() == newSessionButton) {
	        // open window to create item
	        UI.getCurrent().addWindow(
	        		new TestSessionEditor(tree, sessionsTable, testcase, true));	//testcases.getItem(parent).getEntity()
//	        		new TestSessionEditor(tree, testcase, true ));	//testcases.getItem(parent).getEntity()

        } else if (event.getButton() == editButton) {
			TestSession session = sessions.getItem(sessionsTable.getValue()).getEntity();	//.getBean();
	        UI.getCurrent().addWindow(new TestSessionEditor(
	        			tree, 
	        			session.getId(), 
	        			testcase,
        				sessionsTable)
	        );
        } else if (event.getButton() == cloneButton) {
			TestSession session = sessions.getItem(sessionsTable.getValue()).getEntity();	//.getBean();
	        UI.getCurrent().addWindow(new TestSessionEditor(
	        			tree, 
	        			session.getId(), 
	        			testcase,
        				sessionsTable,
        				true)
	        );

        } else if (event.getButton() == deleteButton) {
			TestSession session = sessions.getItem(sessionsTable.getValue()).getEntity();	//.getBean();
	        UI.getCurrent().addWindow(new ConfirmDeleteMenuItemWindow(tree, session.getId(), 
	        		"Are you sure you want to delete <b>" + session.getTitle() + "</b> ?<br /><br />"));

        }
    }
	
	
	
    private void setFilterByTestCase() {
    	sessions.removeAllContainerFilters();
//    	Equal ownerfilter = new Equal("parentcase", getTestCaseByTitle());//  ("parentcase", getTestCaseByTitle(), true, false);
    	Equal casefilter = new Equal("parentcase", testcase);//  ("parentcase", getTestCaseByTitle(), true, false);
    	
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
	
}

package com.aaron.mbpet.views;

import com.aaron.mbpet.MbpetUI;
import com.aaron.mbpet.domain.TestCase;
import com.aaron.mbpet.domain.TestSession;
import com.aaron.mbpet.domain.User;
import com.aaron.mbpet.ui.ConfirmDeleteMenuItemWindow;
import com.aaron.mbpet.views.cases.TestCaseEditor;
import com.aaron.mbpet.views.sessions.TestSessionEditor;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.data.Property;
import com.vaadin.data.util.filter.SimpleStringFilter;
import com.vaadin.data.util.filter.Compare.Equal;
import com.vaadin.event.FieldEvents.TextChangeEvent;
import com.vaadin.event.FieldEvents.TextChangeListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Tree;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.themes.ValoTheme;

public class SUTEditorTable extends Panel implements Button.ClickListener {

	Tree tree; 
	User currUser;
	
	public Table sutTable;
	private TextField searchField;
	private Button editButton;
	private Button newSUTButton;
	private Button deleteButton;
    private String sutFilter;

    private JPAContainer<TestCase> testcases;

	public SUTEditorTable(Tree tree, User user) {
		this.tree = tree;
		this.currUser = user;
		this.testcases = ((MbpetUI) UI.getCurrent()).getTestcases();
		
		buildSUTPanel();
	}
	

	@SuppressWarnings("deprecation")
	private void buildSUTPanel() {
        this.setHeight("325px");
        this.setWidth(25, Unit.EM);

		
		VerticalLayout layout = new VerticalLayout();
        layout.setSizeFull();       
	    
	    HorizontalLayout panelHeader = new HorizontalLayout();
	    panelHeader.addStyleName("panel-style-layout-header");
	    panelHeader.setWidth("100%");
	    
	    Label label = new Label(currUser.getFirstname() + "'s SUTs");
	    label.addStyleName(ValoTheme.LABEL_H4);
	    label.addStyleName(ValoTheme.LABEL_COLORED);
	    label.addStyleName(ValoTheme.LABEL_NO_MARGIN);
	    
        searchField = new TextField();
        searchField.addStyleName("tiny");
        searchField.setWidth(11, Unit.EM);
        searchField.setInputPrompt("Search by title");
        searchField.addTextChangeListener(new TextChangeListener() {
            @Override
            public void textChange(TextChangeEvent event) {
            	sutFilter = event.getText();
                updateFilters();
            }
        });
        
	    editButton = new Button("", this);
	    editButton.setIcon(FontAwesome.PENCIL);
	    editButton.addStyleName("borderless-colored");
	    editButton.addStyleName("small");
	    editButton.addStyleName("icon-only");
	    editButton.setDescription("edit selected SUT");
	    editButton.setEnabled(false);
	    
	    newSUTButton = new Button("", this);
	    newSUTButton.setIcon(FontAwesome.PLUS);
	    newSUTButton.addStyleName("borderless-colored");
	    newSUTButton.addStyleName("small");
	    newSUTButton.addStyleName("icon-only");
	    newSUTButton.setDescription("create new SUT");
	    
	    deleteButton = new Button("", this);
	    deleteButton.setIcon(FontAwesome.TRASH_O);
	    deleteButton.addStyleName("borderless-colored");
	    deleteButton.addStyleName("small");
	    deleteButton.addStyleName("icon-only");
	    deleteButton.setDescription("delete SUT");
	    deleteButton.setEnabled(false);
	    
	    panelHeader.addComponent(label);
	    panelHeader.addComponents(searchField, editButton, newSUTButton, deleteButton);
	    panelHeader.setComponentAlignment(label, Alignment.MIDDLE_LEFT);
	    panelHeader.setComponentAlignment(searchField, Alignment.MIDDLE_RIGHT);
	    panelHeader.setExpandRatio(label, 1);
	    
	    
	    // Table
		sutTable = new Table();
		sutTable.setContainerDataSource(testcases);
		setFilterByUser();
//		sessionsTable.setWidth("100%");
//		sessionsTable.setHeight("100%");
		sutTable.addStyleName(ValoTheme.TABLE_BORDERLESS);
		sutTable.addStyleName(ValoTheme.TABLE_NO_HEADER);
		sutTable.addStyleName(ValoTheme.TABLE_NO_HORIZONTAL_LINES);
		sutTable.addStyleName(ValoTheme.TABLE_SMALL);
		sutTable.setSizeFull();
		
		sutTable.setSelectable(true);
        sutTable.setImmediate(true);
        sutTable.setVisibleColumns("title");
//        sessionsTable.setColumnExpandRatio("title", 1);

        sutTable.addListener(new Property.ValueChangeListener() {
        	@Override
        	public void valueChange(
        			com.vaadin.data.Property.ValueChangeEvent event) {
        		setModificationsEnabled(event.getProperty().getValue() != null);
        		
        	}

        	private void setModificationsEnabled(boolean b) {
                editButton.setEnabled(b);
                deleteButton.setEnabled(b);
            }

        });
	    
//	    content.setSizeFull();
	    layout.addComponent(panelHeader);
        layout.addComponent(sutTable);
        layout.setExpandRatio(sutTable, 1);
        this.setContent(layout);
	}

	
	public void buttonClick(ClickEvent event) {
        if (event.getButton() == newSUTButton) {
	        // open window to create item
	        UI.getCurrent().addWindow(new TestCaseEditor(tree));	//testcases.getItem(parent).getEntity()
//	        		new TestSessionEditor(tree, testcase, true ));	//testcases.getItem(parent).getEntity()

        } else if (event.getButton() == editButton) {
			TestCase sut = testcases.getItem(sutTable.getValue()).getEntity();	//.getBean();
	        UI.getCurrent().addWindow(new TestCaseEditor(tree, sut.getId())); //currUser, sutTable));
        } else if (event.getButton() == deleteButton) {
			TestCase sut = testcases.getItem(sutTable.getValue()).getEntity();	//.getBean();
	        UI.getCurrent().addWindow(new ConfirmDeleteMenuItemWindow(tree, sut.getId(), 
	        		"Are you sure you want to delete <b>" + sut.getTitle() + "</b> ?<br /><br />"));

        }
    }
	
	
	
    private void setFilterByUser() {
    	testcases.removeAllContainerFilters();
    	Equal casefilter = new Equal("owner", currUser);//  ("parentcase", getTestCaseByTitle(), true, false);
    	
    	testcases.addContainerFilter(casefilter);
    }
	
	
    private void updateFilters() {
    	testcases.removeAllContainerFilters();
    	
    	setFilterByUser();
    	SimpleStringFilter filter = new SimpleStringFilter("title", sutFilter, true, false);
    	testcases.addContainerFilter(filter);
    }
	
}

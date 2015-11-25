/**
 * Panel table list of Models on SUT home page
 */
package com.aaron.mbpet.views.cases;

import java.util.List;

import org.eclipse.persistence.internal.sessions.factories.SessionsFactory;

import com.aaron.mbpet.domain.Model;
import com.aaron.mbpet.domain.TestCase;
import com.aaron.mbpet.domain.TestSession;
import com.aaron.mbpet.ui.ConfirmDeleteModelWindow;
import com.aaron.mbpet.views.MainView;
import com.aaron.mbpet.views.models.ModelEditor;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.data.Property;
import com.vaadin.data.util.GeneratedPropertyContainer;
import com.vaadin.data.util.filter.SimpleStringFilter;
import com.vaadin.data.util.filter.Compare.Equal;
import com.vaadin.event.FieldEvents.TextChangeEvent;
import com.vaadin.event.FieldEvents.TextChangeListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.shared.Position;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.Table.ColumnGenerator;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Tree;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.themes.ValoTheme;

public class ModelEditorTable extends Panel implements Button.ClickListener {

	
	Tree tree; 
	public static Table modelsTable;
	private TextField searchField;
	private Button editButton;
	private Button cloneButton;
	private Button newModelButton;
	private Button deleteButton;

    private String modelsFilter;

    JPAContainer<Model> models;
    JPAContainer<TestSession> sessions = MainView.sessions;
    TestCase parentcase;
    TestSession parentsession;
	private GeneratedPropertyContainer gpcontainer;

//	public ModelEditorTable(Tree tree) {
//		this.tree = tree;
//		this.models = MBPeTMenu.models;
//		
//		buildModelsPanel();
//	}
	
	public ModelEditorTable(Tree tree, TestCase parentcase) {
		this.tree = tree;
		this.parentcase = parentcase;
//		this.testsession = testsession;
		this.models = MainView.models;
		
		buildModelsPanel();
	}
	
//	public ModelEditorTable(Tree tree, TestSession testsession) {
//		this.tree = tree;
//		this.testsession = testsession;
//		this.models = MBPeTMenu.getModels();
//		
//		buildModelsPanel();
//	}
	

	@SuppressWarnings("deprecation")
	private void buildModelsPanel() {
//		final Panel panel = new Panel();
//		panel.addStyleName("panel-caption");
//		panel.setSizeFull();
//		this.addStyleName("dashboard-panel-table");
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
	    
	    Label label = new Label("Models");
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
            	modelsFilter = event.getText();
                updateFilters();
            }
        });
        
	    editButton = new Button("", this);
	    editButton.setIcon(FontAwesome.PENCIL);
	    editButton.addStyleName("borderless-colored");
	    editButton.addStyleName("small");
	    editButton.addStyleName("icon-only");
	    editButton.setDescription("edit selected Model");
	    editButton.setEnabled(false);

	    cloneButton = new Button("", this);
	    cloneButton.setIcon(FontAwesome.FILES_O);
	    cloneButton.addStyleName("borderless-colored");
	    cloneButton.addStyleName("small");
	    cloneButton.addStyleName("icon-only");
	    cloneButton.setDescription("clone Model");
	    cloneButton.setEnabled(false);
	    
	    newModelButton = new Button("", this);
	    newModelButton.setIcon(FontAwesome.PLUS);
	    newModelButton.addStyleName("borderless-colored");
	    newModelButton.addStyleName("small");
	    newModelButton.addStyleName("icon-only");
	    newModelButton.setDescription("create new Model");
	    
	    deleteButton = new Button("", this);
	    deleteButton.setIcon(FontAwesome.TRASH_O);
	    deleteButton.addStyleName("borderless-colored");
	    deleteButton.addStyleName("small");
	    deleteButton.addStyleName("icon-only");
	    deleteButton.setDescription("delete Model");
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
	    panelHeader.addComponents(searchField, editButton, cloneButton, newModelButton, deleteButton);
//	    panelHeader.addComponent(dropdown);
	    panelHeader.setComponentAlignment(label, Alignment.MIDDLE_LEFT);
	    panelHeader.setComponentAlignment(searchField, Alignment.MIDDLE_RIGHT);
	    panelHeader.setExpandRatio(label, 1);
	    
	    
	    // Table
		modelsTable = new Table();
		modelsTable.setContainerDataSource(models);	//(userSessionsContainer);
		setFilterByTestCase();
//		sessionsTable.setWidth("100%");
//		sessionsTable.setHeight("100%");
		modelsTable.addStyleName(ValoTheme.TABLE_BORDERLESS);
//		modelsTable.addStyleName(ValoTheme.TABLE_NO_HEADER);
		modelsTable.addStyleName(ValoTheme.TABLE_NO_HORIZONTAL_LINES);
		modelsTable.addStyleName(ValoTheme.TABLE_SMALL);
		modelsTable.addStyleName("background-white");
		modelsTable.setSizeFull();
		
		modelsTable.setSelectable(true);
        modelsTable.setImmediate(true);
        
        modelsTable.setVisibleColumns("title");
        modelsTable.setColumnHeaders(new String[] {"Model"});
//        modelsTable.setColumnExpandRatio("title", 1);
		
		// add generated column
		modelsTable.addGeneratedColumn("Parent Session", new ColumnGenerator() {
			
			@Override
			public Object generateCell(Table source, Object itemId, Object columnId) {
				// Get the value in the first column
//                int modelId = (Integer) source
//                    .getContainerProperty(itemId, "id").getValue();

                // get title of parent session
                TestSession session = (TestSession) source.getContainerProperty(itemId, "parentsession").getValue();
                
                Label label = new Label(sessions.getItem(session.getId()).getEntity().getTitle());
                
				return label;
			}
		});

		
		
        modelsTable.addListener(new Property.ValueChangeListener() {
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
        layout.addComponent(modelsTable);
        layout.setExpandRatio(modelsTable, 1);
        this.setContent(layout);
        
 
//        return panel;
		
//		Component contentWrapper = createPanelWrapper(sessionsTable);
////	    contentWrapper.addStyleName("sessions-table");
//		
//		return contentWrapper;
	}

	
	public void buttonClick(ClickEvent event) {
        if (event.getButton() == newModelButton) {
        	if (!(parentcase.getSessions().size() > 0)) {
    	    	Notification not = new Notification("", "Models must belong to a test Session.\n\nPlease create a Session first.", Type.TRAY_NOTIFICATION);
    	    	not.setPosition(Position.TOP_RIGHT);
    	    	not.setStyleName("failure");
    	    	not.show(Page.getCurrent());
    	    } else {
	//        	List<TestSession> slist = parentcase.getSessions().get(parentcase.getSessions().size()-1);
	        	TestSession newestsession = parentcase.getSessions().get(parentcase.getSessions().size()-1);	//slist.get(slist.size()-1);
		        // open window to create item
		        UI.getCurrent().addWindow(new ModelEditor(sessions.getItem(newestsession.getId()).getEntity(),
		        											parentcase, 
		        											true));	//testcases.getItem(parent).getEntity()
    	    }

        } else if (event.getButton() == editButton) {
			Model model = models.getItem(modelsTable.getValue()).getEntity();	//.getBean();
			TestSession parentsession = sessions.getItem(model.getParentsession().getId()).getEntity();
	        UI.getCurrent().addWindow(new ModelEditor(
	        			model.getId(),
	        			parentsession,	//model.getParentsession(),
	        			parentcase,
        				modelsTable)
	        );
        } else if (event.getButton() == cloneButton) {
			Model model = models.getItem(modelsTable.getValue()).getEntity();	//.getBean();
	        UI.getCurrent().addWindow(new ModelEditor(
	        			model.getId(),
	        			model.getParentsession(),
	        			parentcase,
        				modelsTable,
        				true)
	        );

        } else if (event.getButton() == deleteButton) {
			Model model = models.getItem(modelsTable.getValue()).getEntity();	//.getBean();
	        UI.getCurrent().addWindow(new ConfirmDeleteModelWindow(model, 
	        		"Are you sure you want to delete <b>" + model.getTitle() + "</b> ?<br /><br />", true));

        }
    }
	
	
	
    private void setFilterByTestCase() {
    	models.removeAllContainerFilters();
//    	Equal ownerfilter = new Equal("parentcase", getTestCaseByTitle());//  ("parentcase", getTestCaseByTitle(), true, false);
    	Equal casefilter = new Equal("parentsut", parentcase);//  ("parentcase", getTestCaseByTitle(), true, false);
    	
    	models.addContainerFilter(casefilter);
    }
	
	
    private void updateFilters() {
    	models.removeAllContainerFilters();
    	
    	setFilterByTestCase();
    	SimpleStringFilter filter = new SimpleStringFilter("title", modelsFilter, true, false);
    	models.addContainerFilter(filter);
    }
	
}

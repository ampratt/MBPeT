/**
 * Model Tab of Session view page
 * @author Aaron
 *
 */
package com.aaron.mbpet.views.tabs.modelstab;

import org.vaadin.aceeditor.AceEditor;

import com.aaron.mbpet.MbpetUI;
import com.aaron.mbpet.domain.Model;
import com.aaron.mbpet.domain.TestSession;
import com.aaron.mbpet.services.ModelUtils;
import com.aaron.mbpet.ui.ConfirmDeleteModelWindow;
import com.aaron.mbpet.views.sessions.SessionViewer;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.util.filter.SimpleStringFilter;
import com.vaadin.data.util.filter.Compare.Equal;
import com.vaadin.event.FieldEvents.TextChangeEvent;
import com.vaadin.event.FieldEvents.TextChangeListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.UI;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public class ModelTableAceTab extends Panel implements Button.ClickListener{

    JPAContainer<Model> models;
    TestSession currsession;
    
    HorizontalSplitPanel splitpanel;
    TabSheet modelsTabs;
    
    // table elements
    private TextField searchField;
    protected String modelsFilter;
    private Button cloneButton;
    private Button newModelButton;
    private Button deleteButton;
    Table modelsTable;
//    public static Table modelsTable;
    ModelUtils modelUtils = new ModelUtils();
    
    // Ace Editor elements
	AceEditor editor;	// = new AceEditor();
	ModelAceEditorLayout editorLayout;
	ModelDBuilderTab diagramtab;    

	
	public ModelTableAceTab(AceEditor editor, TabSheet modelsTabs, 
			ModelAceEditorLayout editorLayout, Table modelsTable, 
			ModelDBuilderTab diagramtab, TestSession currsession) {	//AceEditor editor TestSession currsession
		
		models = ((MbpetUI) UI.getCurrent()).getModels();
		this.currsession = currsession;
		this.editor = editor;	 //= new AceEditor()
		this.modelsTabs = modelsTabs;
		this.editorLayout = editorLayout;
		this.modelsTable = modelsTable;
		this.diagramtab = diagramtab;
		
//		panel = new Panel();
		setHeight("100%");
		setWidth("100%");
		addStyleName("borderless");
		
		VerticalLayout content = new VerticalLayout();
		content.setMargin(new MarginInfo(false, false, true, true));
//		content.setWidth("100%");
		

		splitpanel = new HorizontalSplitPanel();
		splitpanel.setSizeFull();
		splitpanel.setSplitPosition(30, Unit.PERCENTAGE);
//		setSpacing(true);
//		setMargin(true);
		splitpanel.setFirstComponent(buildLeftSide());
		splitpanel.setSecondComponent(buildRightSide());

		content.addComponent(splitpanel);

		setContent(content);
		
//		addComponent(buildLeftSide());
//		addComponent(buildRightSide());
		
//		addComponent(new AceEditorLayoutDirectory(editor));
	}

	
	private Component buildLeftSide() {
//        this.setHeight("325px");
		
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
	    
//	    Label label = new Label("Models");
//	    label.addStyleName(ValoTheme.LABEL_H4);
//	    label.addStyleName(ValoTheme.LABEL_COLORED);
//	    label.addStyleName(ValoTheme.LABEL_NO_MARGIN);
////	    content.setCaption(null);
	    
        searchField = new TextField();
        searchField.setWidth(11, Unit.EM);
        searchField.addStyleName("tiny");
        searchField.setInputPrompt("Search by title");
        searchField.addTextChangeListener(new TextChangeListener() {
            @Override
            public void textChange(TextChangeEvent event) {
            	modelsFilter = event.getText();
                updateFilters();
            }
        });
        
//	    editButton = new Button("", this);
//	    editButton.setIcon(FontAwesome.PENCIL);
//	    editButton.addStyleName("borderless-colored");
//	    editButton.addStyleName("small");
//	    editButton.addStyleName("icon-only");
//	    editButton.setDescription("edit selected Model");
//	    editButton.setEnabled(false);

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
	   
	    panelHeader.addComponents(searchField, cloneButton, newModelButton, deleteButton);	//editButton
//	    panelHeader.addComponent(dropdown);
	    panelHeader.setComponentAlignment(searchField, Alignment.MIDDLE_LEFT);
	    panelHeader.setComponentAlignment(cloneButton, Alignment.MIDDLE_RIGHT);
	    panelHeader.setExpandRatio(searchField, 1);
	    
	    
	    // Table
	    setFilterByTestCase();
//		modelsTable = new Table();
		modelsTable.setContainerDataSource(models);	//(userSessionsContainer);
		modelsTable.setSizeFull();
//		modelsTable.addStyleName(ValoTheme.TABLE_BORDERLESS);
//		modelsTable.addStyleName(ValoTheme.TABLE_NO_HEADER);
		modelsTable.addStyleName(ValoTheme.TABLE_NO_HORIZONTAL_LINES);
		modelsTable.addStyleName(ValoTheme.TABLE_SMALL);
//		modelsTable.addStyleName("background-white");
		modelsTable.setSelectable(true);
        modelsTable.setImmediate(true);
        modelsTable.setVisibleColumns("title");
        modelsTable.setColumnHeaders(new String[] {"Model"});
        modelsTable.addValueChangeListener(new ValueChangeListener() {
			@Override
			public void valueChange(ValueChangeEvent event) {
				if (modelsTable.getValue() != null) {
					
					// add model to editor view
					editorLayout.createNewModel(false);
					editorLayout.toggleEditorFields(true);
					editorLayout.setFieldsDataSource(models.getItem(event.getProperty().getValue()).getEntity());
					
					// add model to dbuilder
					diagramtab.setFieldsDataSource(models.getItem(event.getProperty().getValue()).getEntity());
					
//					models.getItem(modelsTable.getValue());					
					setModificationsEnabled(event.getProperty().getValue() != null);
	        	} else {
					setModificationsEnabled(false);
					// remove model to editor view
//					editorLayout.setFieldsDataSource(null);
//					editorLayout.toggleEditorFields(false);
	        	}
			} 	
				private void setModificationsEnabled(boolean b) {
	                cloneButton.setEnabled(b);
	                deleteButton.setEnabled(b);
	            }
		});
        
	    layout.addComponent(panelHeader);
        layout.addComponent(modelsTable);
        layout.setExpandRatio(modelsTable, 1);
        
		return layout;
	}

	
	private Component buildRightSide() {
		VerticalLayout rightlayout = new VerticalLayout();
		rightlayout.setSizeFull();
		
//		editorLayout = new ModelAceEditorLayout(editor, "dot", modelsTabs, diagramtab);
		editorLayout.toggleEditorFields(false);
		
//		dbuilderLayout = new ModelDBuilderNOTWINDOW(editor);
		
		rightlayout.addComponent(editorLayout);
		
		return rightlayout;	// editorLayout;
	}


	public void buttonClick(ClickEvent event) {
        if (event.getButton() == newModelButton) {
        	// enable editing
        	editorLayout.toggleEditorFields(true);
        	
        	editorLayout.createNewModel(true);
			editorLayout.setFieldsDataSource(new Model());

        } else if (event.getButton() == cloneButton) {
//			Model model = models.getItem(modelsTable.getValue()).getEntity();	//.getBean();
        	editorLayout.toggleEditorFields(true);
        	
        	Model selected = models.getItem(modelsTable.getValue()).getEntity();
        	Model m = new Model();
        	m.setTitle(selected.getTitle() + "_clone");
        	m.setDotschema(modelUtils.renameAceTitle(selected.getTitle() + "_clone", selected.getDotschema()));		//(selected.getDotschema());
        	m.setParentsession(selected.getParentsession());
        	m.setParentsut(selected.getParentsut());
        	
			editorLayout.setFieldsDataSource(m);
			editorLayout.cloneModel(m, true);

        } else if (event.getButton() == deleteButton) {
			Model model = models.getItem(modelsTable.getValue()).getEntity();	//.getBean();
	        UI.getCurrent().addWindow(new ConfirmDeleteModelWindow(model, 
	        		"Are you sure you want to delete <b>" + model.getTitle() + "</b> ?<br /><br />", false));

        }
    }


	
    private void setFilterByTestCase() {
    	models.removeAllContainerFilters();
//    	Equal ownerfilter = new Equal("parentcase", getTestCaseByTitle());//  ("parentcase", getTestCaseByTitle(), true, false);
    	Equal casefilter = new Equal("parentsession", currsession);//  ("parentcase", getTestCaseByTitle(), true, false);
    	
    	models.addContainerFilter(casefilter);
    }
	
    private void updateFilters() {
    	models.removeAllContainerFilters();
    	
    	setFilterByTestCase();
    	SimpleStringFilter filter = new SimpleStringFilter("title", modelsFilter, true, false);
    	models.addContainerFilter(filter);
    }
    
}

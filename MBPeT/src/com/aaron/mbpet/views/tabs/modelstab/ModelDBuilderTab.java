package com.aaron.mbpet.views.tabs.modelstab;

import java.util.List;

import org.vaadin.aceeditor.AceEditor;
import org.vaadin.diagrambuilder.DiagramBuilder;
import org.vaadin.diagrambuilder.DiagramStateEvent;
import org.vaadin.diagrambuilder.Node;
import org.vaadin.diagrambuilder.NodeType;
import com.aaron.mbpet.MbpetUI;
import com.aaron.mbpet.domain.Model;
import com.aaron.mbpet.domain.TestSession;
import com.aaron.mbpet.services.DBuilderUtils;
import com.aaron.mbpet.services.FileSystemUtils;
import com.aaron.mbpet.services.ModelUtils;
import com.aaron.mbpet.ui.ConfirmDeleteModelWindow;
import com.aaron.mbpet.views.tabs.parameterstab.ParametersFormAceView;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.filter.SimpleStringFilter;
import com.vaadin.data.util.filter.Compare.Equal;
import com.vaadin.event.FieldEvents.TextChangeEvent;
import com.vaadin.event.FieldEvents.TextChangeListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.themes.ValoTheme;

public class ModelDBuilderTab extends Panel implements Button.ClickListener {

	// Diagram elements
	final VerticalLayout diagramContainer = new VerticalLayout();
//    String outFileField = "C:/dev/git/alternate/mbpet/MBPeT/WebContent/META-INF/output/dot-output.dot";
    String outFileField = "C:/dev/git/alternate/mbpet/MBPeT/WebContent/META-INF/output/dot-output.dot";
    String inFileField = outFileField;
    
    // table elements
	private TextField searchField;
	private Button cloneButton;
	private Button newModelButton;
	private AbstractComponent deleteButton;
    protected String modelsFilter;
    Table dbuildModelsTable;
    Table aceModelsTable;
    
    //button toolbar
    TextField titleField;
    Button resetButton; 	//= new Button("Reset Diagram Builder");
    Button saveButton;	// = new Button("Save to .dot file");
    Button loadButton;	// = new Button("Load .dot file");
    Button renameNodesButton;	// = new Button("Rename Nodes");
    Button openAceButton;
    AceEditor editor;
    ModelAceEditorLayout editorLayout;
//    Button aceLoaderButton = new Button("Load dot from editor");
        
	
    DiagramBuilder diagramBuilder; 
    DBuilderUtils diagramUtils = new DBuilderUtils();
    TabSheet modelsTabs;
    
    private JPAContainer<Model> models = ((MbpetUI) UI.getCurrent()).getModels();
    private Model currmodel;
	private FieldGroup binder;
	private BeanItem<Model> modelBeanItem;
	TestSession currsession;
	private boolean createNewModel = false;


    
	public ModelDBuilderTab(AceEditor editor, TabSheet modelsTabs, 
			ModelAceEditorLayout editorLayout, Table modelsTable, 
			DiagramBuilder diagramBuilder, TestSession currsession) {	//Model currmodel, 
//		this.currmodel = models.getItem(currmodel.getId()).getEntity();
//    	toggleEditorFields(false);
		this.modelsTabs = modelsTabs;
		this.editor = editor;
		this.editorLayout = editorLayout;
		this.aceModelsTable = modelsTable;
		this.diagramBuilder = diagramBuilder;
		this.currsession = currsession;
		
//		setSizeFull();
//		setWidth("1600px");
//		setHeight("100%");
		setHeight("100%");
		setWidth("100%");
		addStyleName("borderless");
			
		VerticalLayout content = new VerticalLayout();
		content.setMargin(new MarginInfo(false, false, false, false));
		content.addStyleName("dbuilder-window");
//		content.setWidth("100%");

		Component toolbar = buildTableAndButtons();
		
		content.addComponent(toolbar);
		content.addComponent(buildDiagramBuilder());

		setContent(content);
		
		
		// Toolbar buttons
//		addComponent(toolbar);
////		main.setComponentAlignment(toolbar, Alignment.MIDDLE_RIGHT);
//    	
//		// Diagram Builder
//		addComponent(buildDiagramBuilder());
		
//		setFieldsDataSource(currmodel);
	}

	
	private Component buildTableAndButtons() {
		// toolbar buttons layout
        HorizontalLayout toolbar = new HorizontalLayout();
        toolbar.setWidth("100%");
//        toolbar.setMargin(new MarginInfo(false, true, true, false));
        toolbar.setSpacing(true);
        toolbar.addStyleName("toolbar-padding");
		
//        HorizontalLayout left = new HorizontalLayout();
//        left.setSpacing(true);
        
        renameNodesButton = new Button("Rename Nodes", this);
        renameNodesButton.setIcon(FontAwesome.SORT_NUMERIC_ASC);
        renameNodesButton.addStyleName("borderless");
        renameNodesButton.addStyleName("tiny");
        renameNodesButton.setDescription("Numerically rename nodes (e.g. 1->2->3...)");
//        renameNodesButton.setEnabled(false);
               
        resetButton = new Button("Reset", this);	//reset diagram
        resetButton.setIcon(FontAwesome.REFRESH);	//UNDO
        resetButton.addStyleName("borderless");
        resetButton.addStyleName("tiny");
        resetButton.setDescription("Reset Diagram");
//        resetDiagramButton.addStyleName("icon-only");
        
        
        HorizontalLayout right = new HorizontalLayout();
        right.setSpacing(true);
        right.setWidth("100%");
        
        // title editor
        titleField = new TextField("Model title:");
        titleField.setWidth(11, Unit.EM);
        titleField.addStyleName("tiny");
        titleField.focus();
        titleField.setInputPrompt("default_use_case");
        titleField.addTextChangeListener(new TextChangeListener() {	
			@Override
			public void textChange(TextChangeEvent event) {
//				saveButton.setEnabled(true);
			}
		});
        
        // buttons
        saveButton = new Button("Save", this);	// to .dot file
        saveButton.setIcon(FontAwesome.SAVE);
//        saveButton.addStyleName("borderless-colored");
        saveButton.addStyleName("tiny");
        saveButton.addStyleName("primary");
//        saveButton.setClickShortcut(KeyCode.ENTER);
//        saveButton.setDescription("clone Model");
//        saveButton.setEnabled(false);
        
        
		openAceButton = new Button("Source Code", this);
		openAceButton.setIcon(FontAwesome.CODE);
//		openAceButton.addStyleName("borderless");
		openAceButton.addStyleName("tiny");
//		openAceButton.addStyleName("icon-only");
		openAceButton.setDescription("Switch to code view");
		
//        left.addComponents(resetButton, renameNodesButton);
//        left.setComponentAlignment(resetButton, Alignment.BOTTOM_LEFT);
//        left.setComponentAlignment(renameNodesButton, Alignment.BOTTOM_LEFT);
        
//        right.addComponents(resetButton, renameNodesButton, titleField, saveButton, openAceButton);
//        right.setComponentAlignment(resetButton, Alignment.BOTTOM_RIGHT);
//        right.setComponentAlignment(renameNodesButton, Alignment.BOTTOM_RIGHT);
//        right.setComponentAlignment(titleField, Alignment.BOTTOM_RIGHT);
//        right.setComponentAlignment(saveButton, Alignment.BOTTOM_RIGHT);
//        right.setComponentAlignment(openAceButton, Alignment.BOTTOM_RIGHT);       
		
//		toolbar.addComponents(resetButton, renameNodesButton, titleField, saveButton, openAceButton);
		Component modelstable = buildModelsTable();
        toolbar.addComponent(modelstable);
		toolbar.addComponent(right);
		toolbar.setComponentAlignment(modelstable, Alignment.BOTTOM_LEFT);
		
        right.addComponents(resetButton, renameNodesButton, titleField, saveButton, openAceButton);
        right.setComponentAlignment(resetButton, Alignment.BOTTOM_LEFT);
        right.setComponentAlignment(renameNodesButton, Alignment.BOTTOM_LEFT);
        right.setComponentAlignment(titleField, Alignment.BOTTOM_LEFT);
        right.setComponentAlignment(saveButton, Alignment.BOTTOM_LEFT);
        right.setComponentAlignment(openAceButton, Alignment.BOTTOM_RIGHT); 
        right.setExpandRatio(openAceButton, 1);
        
		toolbar.setComponentAlignment(right, Alignment.BOTTOM_LEFT);
		toolbar.setExpandRatio(right, 1);
//		toolbar.setComponentAlignment(right, Alignment.BOTTOM_RIGHT);


//        aceLoaderButton.addClickListener(new Button.ClickListener() {
//        	@Override
//			public void buttonClick(ClickEvent event) {
//        		try {
//        			if (diagramBuilder != null) {
//    					//layout.
//        				removeComponent(diagramBuilder); 
//    				}
////        	        diagramBuilder = new DiagramBuilder();
//        	        diagramUtils.readFromDotSource(diagramBuilder = new DiagramBuilder(), editor.getValue());
//        	        diagramTitleField.setValue(diagramUtils.getGraphTitle());
//    		        //layout.
//        	        addComponent(diagramBuilder);    		        
//				} catch (FileNotFoundException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//					System.out.println(e.getClass());
//				}
//			}
//		});
       
		return toolbar;
	}


	
	private Component buildModelsTable() {
//      this.setHeight("325px");
		
		VerticalLayout layout = new VerticalLayout();
//		layout.setSizeFull();
		layout.setHeight("150px");
		layout.setWidth("280px");
//      layout.setSpacing(true);       
	    
	    HorizontalLayout panelHeader = new HorizontalLayout();
//	    panelCaption.addStyleName("v-panel-caption");
	    panelHeader.addStyleName("panel-style-layout-header");
	    panelHeader.setWidth("100%");
	    // panelCaption.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);
	    
	      searchField = new TextField();
	      searchField.setWidth(11, Unit.EM);
	      searchField.addStyleName("tiny");
	      searchField.setInputPrompt("Search Model by title");
	      searchField.addTextChangeListener(new TextChangeListener() {
	          @Override
	          public void textChange(TextChangeEvent event) {
	          	modelsFilter = event.getText();
	              updateFilters();
	          }
	      });

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
	    dbuildModelsTable = new Table();
	    dbuildModelsTable.setContainerDataSource(models);	//(userSessionsContainer);
	    dbuildModelsTable.setSizeFull();
//	    dbuildModelsTable.setPageLength(5);
//	    dbuildModelsTable.setWidth(11, Unit.EM);
//		modelsTable.addStyleName(ValoTheme.TABLE_BORDERLESS);
	    dbuildModelsTable.addStyleName(ValoTheme.TABLE_NO_HEADER);
	    dbuildModelsTable.addStyleName(ValoTheme.TABLE_NO_HORIZONTAL_LINES);
	    dbuildModelsTable.addStyleName(ValoTheme.TABLE_SMALL);
	    dbuildModelsTable.addStyleName(ValoTheme.TABLE_COMPACT);
//		modelsTable.addStyleName("background-white");
	    dbuildModelsTable.setSelectable(true);
	    dbuildModelsTable.setImmediate(true);
	    dbuildModelsTable.setVisibleColumns("title");
	    dbuildModelsTable.setColumnHeaders(new String[] {"Model"});
	    dbuildModelsTable.addValueChangeListener(new ValueChangeListener() {
			@Override
			public void valueChange(ValueChangeEvent event) {
				if (dbuildModelsTable.getValue() != null) {
					
					// add model to editor view
					editorLayout.createNewModel(false);
					editorLayout.toggleEditorFields(true);
					editorLayout.setFieldsDataSource(models.getItem(event.getProperty().getValue()).getEntity());
					
					// add model to dbuilder //TODO does this break something???
					setFieldsDataSource(models.getItem(event.getProperty().getValue()).getEntity());
					
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
      layout.addComponent(dbuildModelsTable);
      layout.setExpandRatio(dbuildModelsTable, 1);
      
		return layout;
	}
	

	/**
	 * Diagram Builder Component
	 */
    private Component buildDiagramBuilder() {
        diagramContainer.setMargin(new MarginInfo(false, false, true, false));
//    	addComponent(aceLoaderButton);
		
//        diagramBuilder = new DiagramBuilder();
        diagramBuilder.setImmediate(true);
        diagramBuilder.setAvailableFields(
		        new NodeType("diagram-node-start-icon", "Start", "start"),
		        new NodeType("diagram-node-state-icon", "State", "state"),
		        new NodeType("diagram-node-end-icon", "End", "end")
        );
//        diagramBuilder.setSizeFull();
        diagramBuilder.setWidth("100%");
        diagramBuilder.setHeight(1600, Unit.PIXELS);
        
//        diagramUtils.initDiagram(diagramBuilder);
        
        diagramContainer.addComponent(diagramBuilder);
        
        return diagramContainer;
    }
  
    
	public void buttonClick(ClickEvent event) {
		if (event.getButton() == resetButton) {
				if (diagramBuilder != null) {
 					//layout.
					diagramContainer.removeComponent(diagramBuilder); 
 				}
 				
 		        diagramBuilder = new DiagramBuilder();
 		        diagramUtils.initDiagram(diagramBuilder);
//     	        titleField.setValue(diagramUtils.getGraphTitle());
 		        diagramContainer.addComponent(diagramBuilder);
		
		} else if (event.getButton() == renameNodesButton) {
			renameNodes();
		
		} else if (event.getButton() == saveButton) {
			saveDiagram();
                
		} else if (event.getButton() == openAceButton) {
			//TODO SAVE FIRST
			currmodel = saveDiagram();
			
			
			modelsTabs.setSelectedTab(0);
			aceModelsTable.select(currmodel.getId());
			editorLayout.setFieldsDataSource(currmodel); //models.getItem(currmodel.getId()).getEntity());
//        	ModelsTab.acetab.setFieldsDataSource(models.getItem(currmodel.getId()).getEntity());

		} else if (event.getButton() == cloneButton) {
//			Model model = models.getItem(modelsTable.getValue()).getEntity();	//.getBean();
        	editorLayout.toggleEditorFields(true);
        	
        	ModelUtils modelUtils = new ModelUtils();
        	Model selected = models.getItem(dbuildModelsTable.getValue()).getEntity();
        	Model m = new Model();
        	m.setTitle(selected.getTitle() + "_clone");
        	m.setDotschema(modelUtils.renameAceTitle(selected.getTitle() + "_clone", selected.getDotschema()));		//(selected.getDotschema());
        	m.setParentsession(selected.getParentsession());
        	m.setParentsut(selected.getParentsut());
        	
			// actual SAVING to db
			currmodel = diagramUtils.commitNewModelToDB(m, m.getTitle(), m.getDotschema(), currsession);
//			currmodel = diagramUtils.commitToDB(currmodel, titleField.getValue(), dotSchema);
			
			// write model to disk
    		// 4.2 then write new file to disk
			FileSystemUtils fileUtils = new FileSystemUtils();
			fileUtils.writeModelToDisk(	//username, sut, session, settings_file)
					currmodel.getParentsut().getOwner().getUsername(),
					currmodel.getParentsut().getTitle(),
					currmodel.getParentsession().getTitle(),
					currmodel.getParentsession().getParameters().getModels_folder(),
					currmodel);
			
			
//			editorLayout.setFieldsDataSource(m);
			dbuildModelsTable.select(currmodel.getId());
//        	setFieldsDataSource(currmodel);
//			editorLayout.cloneModel(m, true);
        	
//    		// check editor title matches text field title
////    		ModelUtils modelUtils = new ModelUtils();
//    		Model editedmodel = modelUtils.createNewModel(m, currsession, binder); //currmodel.getParentsession(),
//    		createNewModel = false;
//    		
//    		try {
//    			if (editedmodel != null){
//    				dbuildModelsTable.select(models.getItem(editedmodel.getId()).getEntity().getId());
////    				toggleEditorFields(true);
//    				setFieldsDataSource(models.getItem(editedmodel.getId()).getEntity());	//(models.getItem(currmodel.getId()).getEntity());
//    				editor.focus();
////    				openDBuilderButton.setEnabled(true);
//    			}
//			} catch (NullPointerException e) {
//				e.printStackTrace();
//				createNewModel = true;
////				toggleEditorFields(false);
////				setFieldsDataSource(null);
//			}

        } else if (event.getButton() == newModelButton){
        	// enable editing
        	editorLayout.toggleEditorFields(true);
        	
        	// create empty model
//        	createNewModel(true);
        	
        	// set dbuilder fields source
			setFieldsDataSource(new Model());
			
			titleField.focus();
//			editorLayout.setFieldsDataSource(new Model());
		} else if (event.getButton() == deleteButton) {
			Model model = models.getItem(dbuildModelsTable.getValue()).getEntity();	//.getBean();
	        UI.getCurrent().addWindow(new ConfirmDeleteModelWindow(model, 
	        		"Are you sure you want to delete <b>" + model.getTitle() + "</b> ?<br /><br />", false));

	        titleField.setValue("");
        }
	
	}

	public void createNewModel(boolean b) {
		createNewModel  = b;
	}
	
	private void renameNodes() {
        // Using asynchronous API to lazily fetch the current state of the diagram.
        diagramBuilder.getDiagramState(new DiagramBuilder.StateCallback() {
            @Override
            public void onStateReceived(DiagramStateEvent event) {
            	if (diagramBuilder != null) {
					//layout.
            		diagramContainer.removeComponent(diagramBuilder); 
				}
            	List<Node> nodes = event.getNodes();
            	nodes = diagramUtils.renameNodes(nodes);	//event, diagramBuilder = new DiagramBuilder()
				diagramUtils.generateDiagramAfterRename(diagramBuilder = new DiagramBuilder(), nodes);
				//layout.
				diagramContainer.addComponent(diagramBuilder);
            }
        });
		
	}


	private Model saveDiagram() {
	    // Using asynchronous API to lazily fetch the current state of the diagram.
        diagramBuilder.getDiagramState(new DiagramBuilder.StateCallback() {
            @Override
            public void onStateReceived(DiagramStateEvent event) {
                // DO SOMETHING with received state information. e.g. parse data for .dot files
            	//diagramUtils.reportStateBack(event);
            	if (diagramBuilder != null) {
            		diagramContainer.removeComponent(diagramBuilder); 
				}
            	// PARSE data for DB (and file)
            	List<Node> nodes = event.getNodes();
				String dotSchema = diagramUtils.getGraphDataAsString(nodes, outFileField, titleField.getValue());	//outFileField.getValue()
            	
				// actual SAVING to db
				currmodel = diagramUtils.commitNewModelToDB(currmodel, titleField.getValue(), dotSchema, currsession);
//				currmodel = diagramUtils.commitToDB(currmodel, titleField.getValue(), dotSchema);
				
				// write model to disk
        		// 4.2 then write new file to disk
				FileSystemUtils fileUtils = new FileSystemUtils();
				fileUtils.writeModelToDisk(	//username, sut, session, settings_file)
						currmodel.getParentsut().getOwner().getUsername(),
						currmodel.getParentsut().getTitle(),
						currmodel.getParentsession().getTitle(),
						currmodel.getParentsession().getParameters().getModels_folder(),
						currmodel);
				
				// redraw diagram if nodes need to be renamed
//            	nodes = diagramUtils.renameNodes(nodes);
				diagramUtils.generateDiagramAfterRename(diagramBuilder = new DiagramBuilder(), nodes);
				diagramContainer.addComponent(diagramBuilder);
				
            }
        });
        return currmodel;
		
	}


	public void setFieldsDataSource(Model currmodel) {
		this.currmodel = currmodel;
		
		binder = new FieldGroup();
		modelBeanItem = new BeanItem<Model>(currmodel);		// takes item as argument
		binder.setItemDataSource(modelBeanItem);
		binder.bind(titleField, "title");
//		binder.bind(diagramBuilder, "dotschema");
		
//		saveButton.setEnabled(false);
		
		dbuildModelsTable.select(currmodel.getId());
		
		titleField.setNullRepresentation("");

		// load data to dbuilder
		if (diagramBuilder != null) {
			diagramContainer.removeComponent(diagramBuilder); 
		}
 	    diagramBuilder = new DiagramBuilder();
 	    String dotsource = currmodel.getDotschema();
 	    if (dotsource==null){
 	    	dotsource = "digraph " + currmodel.getTitle() + " {\n" +
					"\t//STATES\n"  +
					"\t1\n" + "\t2\n\n" +
					"\t//TRANSITIONS\n"  +
					"\t1 -> 2\n" +
					"}";
 	    }
        diagramUtils.readFromDotSource(diagramBuilder, dotsource);	// currmodel.getDotschema());	//inFileField inFileField.getValue()
        diagramContainer.addComponent(diagramBuilder);    		        
 		
 		// forcing reload slow loading graph
        renameNodes();
// 		renameNodesButton.click();
	}
	
//	public void toggleEditorFields(boolean b) {
////		saveButton.setEnabled(b);
////		renameNodesButton.setEnabled(b);
//		
////		titleField.focus();
//	}
	
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

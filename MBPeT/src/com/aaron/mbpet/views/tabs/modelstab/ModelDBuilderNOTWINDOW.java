package com.aaron.mbpet.views.tabs.modelstab;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import org.vaadin.aceeditor.AceEditor;
import org.vaadin.diagrambuilder.Connector;
import org.vaadin.diagrambuilder.DiagramBuilder;
import org.vaadin.diagrambuilder.DiagramStateEvent;
import org.vaadin.diagrambuilder.Node;
import org.vaadin.diagrambuilder.NodeType;
import org.vaadin.diagrambuilder.Transition;

import com.aaron.mbpet.components.diagrambuilder.DBuilderLayout;
import com.aaron.mbpet.domain.Model;
import com.aaron.mbpet.services.DBuilderUtils;
import com.aaron.mbpet.views.MBPeTMenu;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.util.BeanItem;
import com.vaadin.event.FieldEvents.TextChangeEvent;
import com.vaadin.event.FieldEvents.TextChangeListener;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;

public class ModelDBuilderNOTWINDOW extends VerticalLayout implements Button.ClickListener {

	final VerticalLayout main = new VerticalLayout();

	// Diagram elements
	final VerticalLayout diagramContainer = new VerticalLayout();
    String outFileField = "C:/dev/git/alternate/mbpet/MBPeT/WebContent/META-INF/output/dot-output.dot";
    String inFileField = outFileField;
	
    DiagramBuilder diagramBuilder; 
    DBuilderUtils diagramUtils = new DBuilderUtils();
//    List<String> nName = new ArrayList<String>();
//    List<String> nType = new ArrayList<String>();
//    List<int[]> nXy = new ArrayList<int[]>();
//    List<Transition> nTransitions = new ArrayList<Transition>();
//    List<Connector> nConnectorName = new ArrayList<Connector>();

    TabSheet modelsTabs;
    
    //toolbar
    TextField titleField;
    Button resetButton; 	//= new Button("Reset Diagram Builder");
    Button saveButton;	// = new Button("Save to .dot file");
    Button loadButton;	// = new Button("Load .dot file");
    Button renameNodesButton;	// = new Button("Rename Nodes");
    Button openAceButton;
    AceEditor editor;
//    Button aceLoaderButton = new Button("Load dot from editor");
        
    private JPAContainer<Model> models = MBPeTMenu.models;
    private Model currmodel;
	private FieldGroup binder;
	private BeanItem<Model> modelBeanItem;
    
    
	public ModelDBuilderNOTWINDOW(AceEditor editor, TabSheet modelsTabs) {	//Model currmodel, 
//		this.currmodel = models.getItem(currmodel.getId()).getEntity();
//    	toggleEditorFields(false);

		setSizeFull();
//		setWidth("1600px");
//		setHeight("100%");

		this.modelsTabs = modelsTabs;
		// AceEditor
		this.editor = editor;
		
		main.addStyleName("dbuilder-window");
		
		// Toolbar buttons
		Component toolbar = buildButtons();
		main.addComponent(toolbar);
//		main.setComponentAlignment(toolbar, Alignment.MIDDLE_RIGHT);
    	
		// Diagram Builder
		main.addComponent(buildDiagramBuilder());
		
//		setFieldsDataSource(currmodel);

		this.addComponent(main);
	}

	
	private Component buildButtons() {
		// toolbar buttons layout
        HorizontalLayout toolbar = new HorizontalLayout();
        toolbar.setWidth("100%");
//        toolbar.setMargin(new MarginInfo(false, true, true, false));
        toolbar.setSpacing(true);
        toolbar.addStyleName("toolbar-padding");
		
        HorizontalLayout left = new HorizontalLayout();
        left.setSpacing(true);
        
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
        
        // title editor
        titleField = new TextField("Model title:");
        titleField.setWidth("15em");
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
		
        left.addComponents(resetButton, renameNodesButton);
        left.setComponentAlignment(resetButton, Alignment.BOTTOM_LEFT);
        left.setComponentAlignment(renameNodesButton, Alignment.BOTTOM_LEFT);
        
        right.addComponents(resetButton, renameNodesButton, titleField, saveButton, openAceButton);
        right.setComponentAlignment(resetButton, Alignment.BOTTOM_RIGHT);
        right.setComponentAlignment(renameNodesButton, Alignment.BOTTOM_RIGHT);
        right.setComponentAlignment(titleField, Alignment.BOTTOM_RIGHT);
        right.setComponentAlignment(saveButton, Alignment.BOTTOM_RIGHT);
        right.setComponentAlignment(openAceButton, Alignment.BOTTOM_RIGHT);       
		
//		toolbar.addComponents(resetButton, renameNodesButton, titleField, saveButton, openAceButton);
		toolbar.addComponent(right);
//		toolbar.setExpandRatio(left, 1);
//		toolbar.setComponentAlignment(left, Alignment.BOTTOM_LEFT);
		toolbar.setComponentAlignment(right, Alignment.BOTTOM_RIGHT);


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



	/**
	 * Diagram Builder Component
	 */
    private Component buildDiagramBuilder() {
        diagramContainer.setMargin(new MarginInfo(false, false, true, false));
//    	addComponent(aceLoaderButton);
		
        diagramBuilder = new DiagramBuilder();
        diagramBuilder.setImmediate(true);
        diagramBuilder.setAvailableFields(
		        new NodeType("diagram-node-start-icon", "Start", "start"),
		        new NodeType("diagram-node-state-icon", "State", "state"),
		        new NodeType("diagram-node-end-icon", "End", "end")
        );
        diagramBuilder.setSizeFull();

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
			ModelTableAceView.modelsTable.select(currmodel.getId());
			ModelTableAceView.editorLayout.setFieldsDataSource(currmodel); //models.getItem(currmodel.getId()).getEntity());
//        	ModelsTab.acetab.setFieldsDataSource(models.getItem(currmodel.getId()).getEntity());

			
			
//			// call method for parsing .dot data
//     		try {
//     			if (diagramBuilder != null) {
// 					//layout.
//     				diagramContainer.removeComponent(diagramBuilder); 
// 				}
////     	        diagramBuilder = new DiagramBuilder();
//     	        diagramUtils.readFromDotSource(diagramBuilder = new DiagramBuilder(), inFileField);	//inFileField.getValue()
//     	        titleField.setValue(diagramUtils.getGraphTitle());
// 		        //layout.
//     	       diagramContainer.addComponent(diagramBuilder);    		        
//				} catch (FileNotFoundException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//					System.out.println(e.getClass());
//				}
		}
	
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
				currmodel = diagramUtils.commitToDB(currmodel, titleField.getValue(), dotSchema);
				
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
	
	public void toggleEditorFields(boolean b) {
//		saveButton.setEnabled(b);
//		renameNodesButton.setEnabled(b);
		
//		titleField.focus();
	}
	
  
}

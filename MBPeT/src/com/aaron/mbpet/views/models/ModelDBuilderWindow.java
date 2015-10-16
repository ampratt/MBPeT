package com.aaron.mbpet.views.models;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import org.vaadin.aceeditor.AceEditor;
import org.vaadin.diagrambuilder.Connector;
import org.vaadin.diagrambuilder.DiagramBuilder;
import org.vaadin.diagrambuilder.DiagramStateEvent;
import org.vaadin.diagrambuilder.Node;
import org.vaadin.diagrambuilder.Transition;

import com.aaron.mbpet.components.diagrambuilder.DBuilderLayout;
import com.aaron.mbpet.components.diagrambuilder.DBuilderUtils;
import com.aaron.mbpet.domain.Model;
import com.aaron.mbpet.views.MBPeTMenu;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.util.BeanItem;
import com.vaadin.event.FieldEvents.TextChangeEvent;
import com.vaadin.event.FieldEvents.TextChangeListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;

public class ModelDBuilderWindow extends Window implements Button.ClickListener {

	final VerticalLayout main = new VerticalLayout();

	// Diagram elements
	final VerticalLayout diagramContainer = new VerticalLayout();
    String outFileField = "C:/dev/git/alternate/mbpet/MBPeT/WebContent/META-INF/output/dot-output.dot";
    String inFileField = outFileField;
	
    TextField titleField;
    DiagramBuilder diagramBuilder; 
    
    //toolbar
    Button resetButton; 	//= new Button("Reset Diagram Builder");
    Button saveButton;	// = new Button("Save to .dot file");
    Button loadButton;	// = new Button("Load .dot file");
    Button renameNodesButton;	// = new Button("Rename Nodes");
//    Button aceLoaderButton = new Button("Load dot from editor");
    
    List<String> nName = new ArrayList<String>();
    List<String> nType = new ArrayList<String>();
    List<int[]> nXy = new ArrayList<int[]>();
    List<Transition> nTransitions = new ArrayList<Transition>();
    List<Connector> nConnectorName = new ArrayList<Connector>();
    DBuilderUtils diagramUtils = new DBuilderUtils();
    
    AceEditor editor;
    private JPAContainer<Model> models = MBPeTMenu.models;
    private Model currmodel;
	private FieldGroup binder;
	private BeanItem<Model> modelBeanItem;
    
    
	public ModelDBuilderWindow(Model currmodel, AceEditor editor) {
//		super("Create a new Test Case"); // Set window caption

		this.currmodel = models.getItem(currmodel.getId()).getEntity();

//    	toggleEditorFields(false);
		
		setCaptionAsHtml(true);
		setIcon(FontAwesome.SITEMAP);
		setCaption(" Edit Model: <b>" + this.currmodel.getTitle() + "</b>");
		center();
		setSizeFull();
		setResizable(true);
		setClosable(true);
		setDraggable(true);
		setModal(false);
		setPosition(-1, -1);
		setWidth(85, Unit.PERCENTAGE);
		setHeight(95, Unit.PERCENTAGE);
		
//		main.addComponent(new Label("<h3>Diagram Builder</h3>", ContentMode.HTML));

		// AceEditor
		this.editor = editor;
		main.addStyleName("dbuilder-window");
		
		// Toolbar buttons
		Component toolbar = buildButtons();
		main.addComponent(toolbar);
		main.setComponentAlignment(toolbar, Alignment.MIDDLE_RIGHT);
    	
		// Diagram Builder
		main.addComponent(buildDiagramBuilder());
//		initDiagramLayout(editor);
		
		setFieldsDataSource(currmodel);

		setContent(main);
	}

	
	private Component buildButtons() {
		// toolbar buttons layout
        HorizontalLayout toolbar = new HorizontalLayout();
//        toolbar.setWidth("100%");
        toolbar.setMargin(new MarginInfo(false, true, false, false));
        toolbar.setSpacing(true);
//        toolbar.addStyleName("toolbar-padding");
		
        // title editor
        titleField = new TextField("Model title:");
        titleField.setWidth("15em");
        titleField.addStyleName("tiny");
        titleField.focus();
        titleField.setInputPrompt("default_use_case");
        titleField.addTextChangeListener(new TextChangeListener() {	
			@Override
			public void textChange(TextChangeEvent event) {
				saveButton.setEnabled(true);
			}
		});
        
        // buttons
        saveButton = new Button("Save", this);	// to .dot file
        saveButton.setIcon(FontAwesome.SAVE);
        saveButton.addStyleName("borderless-colored");
        saveButton.addStyleName("tiny");
        saveButton.addStyleName("primary");
//        saveButton.setDescription("clone Model");
        saveButton.setEnabled(false);
        
        renameNodesButton = new Button("Rename Nodes", this);
        renameNodesButton.setIcon(FontAwesome.SORT_NUMERIC_ASC);
        renameNodesButton.addStyleName("borderless-colored");
        renameNodesButton.addStyleName("tiny");
        renameNodesButton.setDescription("Numerically rename nodes (e.g. 1->2->3...)");
//        renameNodesButton.setEnabled(false);
               
        resetButton = new Button("Reset", this);	//reset diagram
        resetButton.setIcon(FontAwesome.REFRESH);	//UNDO
        resetButton.addStyleName("borderless-colored");
        resetButton.addStyleName("tiny");
        resetButton.setDescription("Reset Diagram");
//        resetDiagramButton.addStyleName("icon-only");
        
        loadButton = new Button("Load .dot file", this);
        
//        HorizontalLayout saveLayout = new HorizontalLayout();
//        saveLayout.setSpacing(true);
//        outFileField.setWidth("15em");
//        outFileField.setInputPrompt("C:/dev/output/dot-output.dot");
//        outFileField.setValue("C:/dev/output/dot-output.dot");
//        saveLayout.addComponent(outFileField);
//        saveLayout.addComponent(saveButton);
//		
//        HorizontalLayout loadLayout = new HorizontalLayout();
//        loadLayout.setSpacing(true);
//        inFileField.setWidth("15em");
//        inFileField.setInputPrompt("C:/dev/output/dot-output.dot");
//        inFileField.setValue("C:/dev/output/dot-output.dot");
//        loadLayout.addComponent(inFileField);
//        loadLayout.addComponent(loadButton);
//        
//        VerticalLayout v = new VerticalLayout();
//        v.setSpacing(true);
//        v.addComponent(saveLayout);
//        v.addComponent(loadLayout);
////        v.setComponentAlignment(saveLayout, Alignment.BOTTOM_RIGHT);

        
        toolbar.addComponents(resetButton, renameNodesButton, saveButton, titleField);
      toolbar.setComponentAlignment(saveButton, Alignment.BOTTOM_LEFT);
      toolbar.setComponentAlignment(renameNodesButton, Alignment.BOTTOM_LEFT);
      toolbar.setComponentAlignment(resetButton, Alignment.BOTTOM_LEFT);
//        toolbar.addComponent(v);
//        toolbar.setComponentAlignment(v, Alignment.BOTTOM_RIGHT);
        


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
        
//        addComponents(resetDiagramButton,
////				saveLayout,
////				loadLayout, 
//				dFunctionsLayout);
//				//new HorizontalLayout(resetDiagramButton, saveLayout, loadLayout)); 
//        
		return toolbar;
	}
	
//	private Component buildButtonsOld() {
//        resetButton = new Button("Reset Diagram Builder", this);
//        saveButton = new Button("Save to .dot file", this);
//        loadButton = new Button("Load .dot file", this);
//        renameNodesButton = new Button("Rename Nodes", this);
//        
//        HorizontalLayout saveLayout = new HorizontalLayout();
//        saveLayout.setSpacing(true);
//        outFileField.setWidth("15em");
//        outFileField.setInputPrompt("C:/dev/output/dot-output.dot");
//        outFileField.setValue("C:/dev/output/dot-output.dot");
//        saveLayout.addComponent(outFileField);
//        saveLayout.addComponent(saveButton);
//		
//        HorizontalLayout loadLayout = new HorizontalLayout();
//        loadLayout.setSpacing(true);
//        inFileField.setWidth("15em");
//        inFileField.setInputPrompt("C:/dev/output/dot-output.dot");
//        inFileField.setValue("C:/dev/output/dot-output.dot");
//        loadLayout.addComponent(inFileField);
//        loadLayout.addComponent(loadButton);
//        
//        VerticalLayout v = new VerticalLayout();
//        v.setSpacing(true);
//        v.addComponent(saveLayout);
//        v.addComponent(loadLayout);
////        v.setComponentAlignment(saveLayout, Alignment.BOTTOM_RIGHT);
//
//        HorizontalLayout dFunctionsLayout = new HorizontalLayout();
//        dFunctionsLayout.setWidth("100%");
//        dFunctionsLayout.setSpacing(true);
//        diagramTitleField.setWidth("15em");
//        diagramTitleField.setCaption("Graph title");
//        diagramTitleField.setInputPrompt("default_use_case");
//        
//        dFunctionsLayout.addComponent(resetButton);
//        
//        dFunctionsLayout.addComponent(diagramTitleField);
//        dFunctionsLayout.addComponent(renameNodesButton);
//        dFunctionsLayout.addComponent(v);
//        dFunctionsLayout.setComponentAlignment(diagramTitleField, Alignment.BOTTOM_LEFT);
//        dFunctionsLayout.setComponentAlignment(renameNodesButton, Alignment.BOTTOM_LEFT);
//        dFunctionsLayout.setComponentAlignment(v, Alignment.BOTTOM_RIGHT);
//        
//
//
////        aceLoaderButton.addClickListener(new Button.ClickListener() {
////        	@Override
////			public void buttonClick(ClickEvent event) {
////        		try {
////        			if (diagramBuilder != null) {
////    					//layout.
////        				removeComponent(diagramBuilder); 
////    				}
//////        	        diagramBuilder = new DiagramBuilder();
////        	        diagramUtils.readFromDotSource(diagramBuilder = new DiagramBuilder(), editor.getValue());
////        	        diagramTitleField.setValue(diagramUtils.getGraphTitle());
////    		        //layout.
////        	        addComponent(diagramBuilder);    		        
////				} catch (FileNotFoundException e) {
////					// TODO Auto-generated catch block
////					e.printStackTrace();
////					System.out.println(e.getClass());
////				}
////			}
////		});
//        
////        addComponents(resetDiagramButton,
//////				saveLayout,
//////				loadLayout, 
////				dFunctionsLayout);
////				//new HorizontalLayout(resetDiagramButton, saveLayout, loadLayout)); 
////        
//		return dFunctionsLayout;
//	}



	/**
	 * Diagram Builder Component
	 */
    private Component buildDiagramBuilder() {
    	// set main content
        diagramContainer.setMargin(new MarginInfo(false, true, true, false));
//        diagramContainer.setSpacing(true);
//    	addComponent(aceLoaderButton);
                		

		
        diagramBuilder = new DiagramBuilder();
        diagramBuilder.setImmediate(true);
        diagramUtils.initDiagram(diagramBuilder);
        
        diagramContainer.addComponent(diagramBuilder);
        //initDiagram();
        
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
     	        titleField.setValue(diagramUtils.getGraphTitle());
 		        //layout.
     	       diagramContainer.addComponent(diagramBuilder);
		
		} else if (event.getButton() == renameNodesButton) {
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
		
		} else if (event.getButton() == saveButton) {
            // Using asynchronous API to lazily fetch the current state of the diagram.
            diagramBuilder.getDiagramState(new DiagramBuilder.StateCallback() {
                @Override
                public void onStateReceived(DiagramStateEvent event) {
                    // DO SOMETHING with received state information. e.g. parse data for .dot files
                	//diagramUtils.reportStateBack(event);
                	if (diagramBuilder != null) {
						//layout.
                		diagramContainer.removeComponent(diagramBuilder); 
					}
                	//save data to file
                	List<Node> nodes = event.getNodes();
					diagramUtils.saveToFile(nodes, outFileField, titleField.getValue());	//outFileField.getValue()
                	
					// redraw diagram if nodes need to be renamed
//                	nodes = diagramUtils.renameNodes(nodes);
					diagramUtils.generateDiagramAfterRename(diagramBuilder = new DiagramBuilder(), nodes);
					diagramContainer.addComponent(diagramBuilder);
                }
            });
                
		} else if (event.getButton() == loadButton) {
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

	
	public void setFieldsDataSource(Model currmodel) {
		this.currmodel = currmodel;
		
		binder = new FieldGroup();
		modelBeanItem = new BeanItem<Model>(currmodel);		// takes item as argument
		binder.setItemDataSource(modelBeanItem);
		binder.bind(titleField, "title");
//		binder.bind(diagramBuilder, "dotschema");
		
		saveButton.setEnabled(false);
		titleField.setNullRepresentation("");


		// call method for parsing .dot data
 		try {
 			if (diagramBuilder != null) {
					//layout.
 				diagramContainer.removeComponent(diagramBuilder); 
				}
// 	        diagramBuilder = new DiagramBuilder();
 	        diagramUtils.readFromDotSource(diagramBuilder = new DiagramBuilder(), currmodel.getDotschema());	//inFileField inFileField.getValue()
// 	        titleField.setValue(diagramUtils.getGraphTitle());
		        
 	        diagramContainer.addComponent(diagramBuilder);    		        
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println(e.getClass());
			}
	}
	
	public void toggleEditorFields(boolean b) {
		saveButton.setEnabled(b);
//		renameNodesButton.setEnabled(b);
		
//		titleField.focus();
	}
	
  
}

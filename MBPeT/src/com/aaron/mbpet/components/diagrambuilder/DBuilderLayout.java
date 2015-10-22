package com.aaron.mbpet.components.diagrambuilder;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import org.vaadin.diagrambuilder.Connector;
import org.vaadin.diagrambuilder.DiagramBuilder;
import org.vaadin.diagrambuilder.DiagramStateEvent;
import org.vaadin.diagrambuilder.Node;
import org.vaadin.diagrambuilder.Transition;

import com.aaron.mbpet.services.DBuilderUtils;
import com.vaadin.annotations.JavaScript;
import com.vaadin.annotations.StyleSheet;
import com.vaadin.server.VaadinRequest;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;

//@JavaScript({"http://cdn.alloyui.com/3.0.1/aui/aui-min.js",
//	"js/alloy-ui-master/.*",
////	"js/alloy-ui-master/cdn.alloyui.com_3.0.1_aui_aui-min.js",
////	"js/cdn.alloyui.com_2.5.0_aui_aui-min.js"
////	"js/aui-diagram-builder/js/aui-diagram-builder.js",
////	"js/aui-diagram-builder/js/aui-diagram-builder-connector.js",
////	"js/aui-diagram-builder/js/aui-diagram-node.js",
////	"js/aui-diagram-builder/js/aui-diagram-node-condition.js",
////	"js/aui-diagram-builder/js/aui-diagram-node-end.js",
////	"js/aui-diagram-builder/js/aui-diagram-node-fork.js",
////	"js/aui-diagram-builder/js/aui-diagram-node-join.js",
////	"js/aui-diagram-builder/js/aui-diagram-node-manager-base.js",
////	"js/aui-diagram-builder/js/aui-diagram-node-start.js",
////	"js/aui-diagram-builder/js/aui-diagram-node-state.js",
////	"js/aui-diagram-builder/js/aui-diagram-node-task.js",
//	})
//@StyleSheet("http://cdn.alloyui.com/3.0.1/aui-css/css/bootstrap.min.css")
@SuppressWarnings("serial")
public class DBuilderLayout extends VerticalLayout {

    final VerticalLayout layout = new VerticalLayout();
    final TextField outFileField = new TextField();
    final TextField inFileField = new TextField();
    final TextField diagramTitleField = new TextField();
    DiagramBuilder diagramBuilder; 
    Button aceLoaderButton = new Button("Load dot from editor");
    Button resetDiagramButton = new Button("Reset Diagram Builder");
    Button saveDiagramButton = new Button("Save to .dot file");
    Button loadDiagramButton = new Button("Load .dot file");
    Button renameNodesButton = new Button("Rename Nodes");
    
    List<String> nName = new ArrayList<String>();
    List<String> nType = new ArrayList<String>();
    List<int[]> nXy = new ArrayList<int[]>();
    List<Transition> nTransitions = new ArrayList<Transition>();
    List<Connector> nConnectorName = new ArrayList<Connector>();
    DBuilderUtils diagramUtils = new DBuilderUtils();
    
    
	public DBuilderLayout() {
    	// set main content
        //final VerticalLayout layout = new VerticalLayout();
        //layout.setMargin(true);
        //layout.setSpacing(true);
        //setContent(layout);
		setSizeFull();
		setMargin(true);
		setSpacing(true);

//        diagramBuilder = new DiagramBuilder();

		initLayout();
//        addComponent(new Label("<h3><i>Graphical editing of models with the DiagramBuilder will happen here</i></h3>", ContentMode.HTML));	//layout.

        //initDiagram();
    }
	
    public void initLayout() {
    	// set main content
        	//final VerticalLayout layout = new VerticalLayout();
//        layout.setMargin(true);
//        layout.setSpacing(true);
//        addComponent(layout);
    	addComponent(aceLoaderButton);
                		
        HorizontalLayout saveLayout = new HorizontalLayout();
        saveLayout.setSpacing(true);
        outFileField.setWidth("15em");
        outFileField.setInputPrompt("C:/dev/output/dot-output.dot");
        outFileField.setValue("C:/dev/output/dot-output.dot");
        saveLayout.addComponent(outFileField);
        saveLayout.addComponent(saveDiagramButton);
		
        HorizontalLayout loadLayout = new HorizontalLayout();
        loadLayout.setSpacing(true);
        inFileField.setWidth("15em");
        inFileField.setInputPrompt("C:/dev/output/dot-output.dot");
        inFileField.setValue("C:/dev/output/dot-output.dot");
        loadLayout.addComponent(inFileField);
        loadLayout.addComponent(loadDiagramButton);
        
        VerticalLayout v = new VerticalLayout();
        v.setSpacing(true);
        v.addComponent(saveLayout);
        v.addComponent(loadLayout);
//        v.setComponentAlignment(saveLayout, Alignment.BOTTOM_RIGHT);

        HorizontalLayout dFunctionsLayout = new HorizontalLayout();
        dFunctionsLayout.setWidth("100%");
        dFunctionsLayout.setSpacing(true);
        diagramTitleField.setWidth("15em");
        diagramTitleField.setCaption("Graph title");
        diagramTitleField.setInputPrompt("default_use_case");
        dFunctionsLayout.addComponent(diagramTitleField);
        dFunctionsLayout.addComponent(renameNodesButton);
        dFunctionsLayout.addComponent(v);
        dFunctionsLayout.setComponentAlignment(diagramTitleField, Alignment.BOTTOM_LEFT);
        dFunctionsLayout.setComponentAlignment(renameNodesButton, Alignment.BOTTOM_LEFT);
        dFunctionsLayout.setComponentAlignment(v, Alignment.BOTTOM_RIGHT);
		
        diagramBuilder = new DiagramBuilder();
        diagramBuilder.setImmediate(true);
        diagramUtils.initDiagram(diagramBuilder);

        //layout.
//        addComponent(new Label("<h1>Diagram Ace Combo example</h1>", ContentMode.HTML));
        addComponents(resetDiagramButton,
//        							saveLayout,
//        							loadLayout, 
        							dFunctionsLayout);
        							//new HorizontalLayout(resetDiagramButton, saveLayout, loadLayout));        
        addComponent(diagramBuilder);
        //initDiagram();

        
        aceLoaderButton.addClickListener(new Button.ClickListener() {
        	@Override
			public void buttonClick(ClickEvent event) {
//        		try {
        			if (diagramBuilder != null) {
    					//layout.
        				removeComponent(diagramBuilder); 
    				}
//        	        diagramBuilder = new DiagramBuilder();
        	        diagramUtils.readFromDotSource(diagramBuilder = new DiagramBuilder(), inFileField.getValue());
        	        diagramTitleField.setValue(diagramUtils.getGraphTitle());
    		        //layout.
        	        addComponent(diagramBuilder);    		        
//				} 
//        		catch (FileNotFoundException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//					System.out.println(e.getClass());
//				}
			}
		});
        
        
        
        resetDiagramButton.addClickListener(new Button.ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				if (diagramBuilder != null) {
					//layout.
					removeComponent(diagramBuilder); 
				}
				
		        diagramBuilder = new DiagramBuilder();
		        diagramUtils.initDiagram(diagramBuilder);
    	        diagramTitleField.setValue(diagramUtils.getGraphTitle());
		        //layout.
    	        addComponent(diagramBuilder);
			}
		});
        
        saveDiagramButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                // Using asynchronous API to lazily fetch the current state of the diagram.
                diagramBuilder.getDiagramState(new DiagramBuilder.StateCallback() {
                    @Override
                    public void onStateReceived(DiagramStateEvent event) {
                        // DO SOMETHING with received state information. e.g. parse data for .dot files
                    	//diagramUtils.reportStateBack(event);
                    	if (diagramBuilder != null) {
							//layout.
                    		removeComponent(diagramBuilder); 
						}
                    	//save data to file
                    	List<Node> nodes = event.getNodes();
						diagramUtils.getGraphDataAsString(nodes, outFileField.getValue(), diagramTitleField.getValue());
                    	
						// redraw diagram if nodes need to be renamed
//                    	nodes = diagramUtils.renameNodes(nodes);
						diagramUtils.generateDiagramAfterRename(diagramBuilder = new DiagramBuilder(), nodes);
						//layout.
						addComponent(diagramBuilder);
                    	
                    }
                });
            }
        });
        
        loadDiagramButton.addClickListener(new Button.ClickListener() {
        	@Override
			public void buttonClick(ClickEvent event) {
        		// call method for parsing .dot data
//        		try {
        			if (diagramBuilder != null) {
    					//layout.
        				removeComponent(diagramBuilder); 
    				}
//        	        diagramBuilder = new DiagramBuilder();
        	        diagramUtils.readFromDotSource(diagramBuilder = new DiagramBuilder(), inFileField.getValue());
        	        diagramTitleField.setValue(diagramUtils.getGraphTitle());
    		        //layout.
        	        addComponent(diagramBuilder);    		        
//				} catch (FileNotFoundException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//					System.out.println(e.getClass());
//				}
			}
		});
        
        
        renameNodesButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                // Using asynchronous API to lazily fetch the current state of the diagram.
                diagramBuilder.getDiagramState(new DiagramBuilder.StateCallback() {
                    @Override
                    public void onStateReceived(DiagramStateEvent event) {
                    	if (diagramBuilder != null) {
							//layout.
                    		removeComponent(diagramBuilder); 
						}
                    	List<Node> nodes = event.getNodes();
                    	nodes = diagramUtils.renameNodes(nodes);	//event, diagramBuilder = new DiagramBuilder()
						diagramUtils.generateDiagramAfterRename(diagramBuilder = new DiagramBuilder(), nodes);
						//layout.
						addComponent(diagramBuilder);
                    }
                });
            }
        });
        
    }
    
    
}

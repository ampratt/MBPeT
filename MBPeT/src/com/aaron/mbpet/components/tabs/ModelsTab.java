package com.aaron.mbpet.components.tabs;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.IOException;
import java.net.URL;

//import com.fasterxml.jackson.annotation.JsonInclude;
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.databind.SerializationFeature;
import com.vaadin.annotations.JavaScript;
import com.vaadin.annotations.PreserveOnRefresh;
import com.vaadin.annotations.StyleSheet;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.shared.ui.label.ContentMode;

import org.apache.tools.ant.taskdefs.Length;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

@JavaScript("http://cdn.alloyui.com/2.5.0/aui/aui-min.js")
@StyleSheet("http://cdn.alloyui.com/2.5.0/aui-css/css/bootstrap.min.css")
public class ModelsTab extends VerticalLayout {

    final VerticalLayout layout = new VerticalLayout();
//    private DiagramBuilder diagramBuilder = new DiagramBuilder();
    Button stateButton;
    List<String> nName = new ArrayList<String>();
	
	public ModelsTab() {
    	// set main content
        //final VerticalLayout layout = new VerticalLayout();
        //layout.setMargin(true);
        //layout.setSpacing(true);
        //setContent(layout);
		setSizeFull();
		setMargin(true);
		setSpacing(true);

        addComponent(new Label("<h3><i>Graphical editing of models with the DiagramBuilder will happen here</i></h3>", ContentMode.HTML));	//layout.

        //initDiagram();
    }

    
//    private void initDiagram()
//    {
//
//        
//        // Initialize diagram builder component
//        //diagramBuilder = new DiagramBuilder();
//        diagramBuilder.setAvailableFields(
//                new NodeType(
//                        "diagram-node-start-icon",
//                        "1",
//                        "start"
//                ),
//                new NodeType(
//                        "diagram-node-state-icon",
//                        "2",
//                        "state"
//                ),
///*                new NodeType(
//                        "diagram-node-task-icon",
//                        "Task",
//                        "task"
//                ),
//*/                new NodeType(
//                        "diagram-node-join-icon",
//                        "Join",
//                        "join"
//                ),
//                new NodeType(
//                        "diagram-node-fork-icon",
//                        "Fork",
//                        "fork"
//                ),
//                new NodeType(
//                        "diagram-node-condition-icon",
//                        "Condition",
//                        "condition"
//                ),
//                new NodeType(
//                        "diagram-node-end-icon",
//                        "End",
//                        "end"
//                ));
//        
//        diagramBuilder.setFields(
//        		new Node(
//        				"1", 
//        				"start", 
//    					10, 10
//				), 
//        		new Node("2",
//        				"state",
//        				200, 75
//				)
//		);
//
//        diagramBuilder.setTransitions(
//        			new Transition("1", "2", "0.60")
//		);
//
//        diagramBuilder.setSizeFull();
//
//        stateButton = new Button("Get state to server and report as JSON", new Button.ClickListener() {
//                    @Override
//                    public void buttonClick(Button.ClickEvent event) {
//                        /* Using asynchronous API to lazily fetch the current state
//                         * of the diagram.
//                         */
//                        diagramBuilder.getDiagramState(new DiagramBuilder.StateCallback() {
//                            @Override
//                            public void onStateReceived(DiagramStateEvent event) {
//                                /* Do something with received state information. e.g. parse data for .dot files.
//                                 */
//                            	reportStateBack(event);
//                            }
//                        });
//                    }
//                });
//
//        addComponent(new Label("<h3><i>Graphical editing of models will happen here</i></h3>", ContentMode.HTML));	//layout.
//        addComponent(stateButton);
//        addComponent(diagramBuilder);
//
//    }
//
//    @SuppressWarnings("null")
//	public void reportStateBack(DiagramStateEvent event) {
//        List<Node> nodes = event.getNodes();
//
//        // Normally you'd do something with the nodes, in this 
//        // demo, just report it back to browser
//        ObjectMapper mapper = new ObjectMapper();
//        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
//        mapper.enable(SerializationFeature.INDENT_OUTPUT);
//        try {
//            String writeValueAsString = mapper.writeValueAsString(nodes);	//event.getNodes()
//            
//            nName.clear();
//            for (int n=0; n < nodes.size(); n++) {
//            	// this String list can be used to pass node name info
//            	nName.add(nodes.get(n).getName());
//            }
//            String writeNodeNames = mapper.writeValueAsString(nName);
//            System.out.println(nName);
//            System.out.println(writeNodeNames);
//            Notification.show(
//                    "State reported: ",
//                    "Node Names:\n" + writeNodeNames +
//                    "\n\nStates:\n" + writeValueAsString,
//                    Notification.Type.ERROR_MESSAGE);
//        } catch (JsonProcessingException ex) {
//            Logger.getLogger(
//                    MbpetDemoUI.class.
//                    getName()).
//                    log(Level.SEVERE, null, ex);
//        }
//
//    }
}

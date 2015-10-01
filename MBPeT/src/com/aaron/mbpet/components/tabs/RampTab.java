package com.aaron.mbpet.components.tabs;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;

public class RampTab extends VerticalLayout {

	HorizontalLayout inputLayout = new HorizontalLayout();
	private TextField textualInput = new TextField();
	private Button generateButton = new Button("Submit Data to Graph");
	private String data = new String();
	//private FlotChart flot = new FlotChart();

	public RampTab() {
		//setSizeFull();
		setMargin(true);
		setSpacing(true);

        initRamp();

	}
	
	private void initRamp() {

        addComponent(new Label("<h3><i>This will have a graph for the ramp function</i></h3>", ContentMode.HTML));	//layout.
        addComponent(new Label("Give graph data in format: '[[0,0], [10,30], [20,50]]'"));
		
        // horizontal layout
        inputLayout.setWidth("100%");
        inputLayout.setSpacing(true);
        addComponent(inputLayout);
        
		// set input data
		textualInput.setWidth("100%");
		//textualInput.setCaption("Give graph data in format: '[[0,0], [10,30], [20,50]]'");
		textualInput.setValue("[[0,0], [10,30], [20,50],[50,70],[60,0]]");
		//flotInput.setInputPrompt("[[0,0], [10,30], [20,50],[50,70],[60,0]]");
		// options
		String options =
				"{" +
					"grid:{" +
						"backgroundColor:{" +
							"colors:["+
								"\"#fef\"," +
								"\"#eee\""+
							"]"+
						"}"+
					"}"+
				"}";
		//flot.setOptions(options);
		
		// lable to display graph data TESTING PURPOSES
		final Label current = new Label();
		
		// button to draw graph
		generateButton.addClickListener(new Button.ClickListener() {
			public void buttonClick(ClickEvent event) {
				current.setValue(textualInput.getValue());	//current.setValue("Graph data is: " + flot.getData());
			
				//flotData = flotInput.getValue();
				
				//buildFlotChart(textualInput.getValue());
				//addComponent(flot);
				
				// update label
				//current.setValue(formatDataFromGraph(flot.getData().toString()));	//current.setValue("Graph data is: " + flot.getData());
			}
		});
		inputLayout.addComponent(textualInput);
		inputLayout.addComponent(generateButton);
		addComponent(current);

		
		/*Button graphDataButton = new Button("Get Json data from graph");
		graphDataButton.addClickListener(new Button.ClickListener() {
			public void buttonClick(ClickEvent event) {
				layout.addComponent(new Label("Graph data is: " + flot.getData()));
				
			}
		});
		layout.addComponent(graphDataButton);			
		*/

		
	}
	
	public void buildFlotChart(String data) {
		/*flot.setWidth("600px");
		flot.setHeight("300px");
		flot.setData(formatDataForGraph(data));
		*/
		/*String data = "[" + //"[" +
					"[0, 5]," +
					"[2, 7]," +
					"[4, 8]," +
					"[10, 5]" +
					"]";// + "]";
		*/
	}
	
	
	public String formatDataForGraph(String input) {
		String formatted = new StringBuilder().append("[").append(input).append("]").toString();
		System.out.println(formatted);
		return formatted;
		
	}

	public String formatDataFromGraph(String graphData) {
		//String formatted = new StringBuilder().append("[").append(input).append("]").toString();	
		//String formatted = StringUtils.substringBetween(graphData, "[", "]");
		String formatted = graphData.substring(1, graphData.length() - 1);
		System.out.println(formatted);
		return formatted;	
	}
	
	
}

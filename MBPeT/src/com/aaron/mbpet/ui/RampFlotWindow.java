package com.aaron.mbpet.ui;

import com.aaron.mbpet.components.flot.FlotChart;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

public class RampFlotWindow extends Window {

	VerticalLayout layout = new VerticalLayout();
	private FlotChart chart;

	public RampFlotWindow(String rampValue) {
        super("Edit ramp value in chart"); // Set window caption
        center();
        setResizable(true);
        setClosable(true);

        setContent(buildWindowContent(rampValue));
        buildFlotChart(rampValue);
	}
	
	
	private Component buildWindowContent(String rampValue) {
    	// Some basic content for the window
//        VerticalLayout vc = new VerticalLayout();
//        vc.setMargin(true);
		
		layout.setMargin(true);
		layout.setSpacing(true);
//        setContent(layout);
        
        layout.addComponent(new Label("input ramp: " + rampValue));
        
        flotLayout();
      
//        // form
//        vc.addComponent(buildCreationForm(tree, parentCase));      
        
        return layout;
    }
	
	public VerticalLayout flotLayout() {
//		layout.setMargin(true);
//		layout.setSpacing(true);
		
		// FLOT CHART

		// set input data
		final TextField inputField = new TextField();
		inputField.setWidth("45%");
		inputField.setCaption("Give graph data in format: '[[0,0], [10,30], [20,50]]'");
		inputField.setValue("[[0,0],[19.33,19.93],[33.16,24.39],[49.66,102.45],[76.33,13.23]]");		//("[[0,0], [10,30], [20,50],[50,70],[60,0]]");
		//flotInput.setInputPrompt("[[0,0], [10,30], [20,50],[50,70],[60,0]]");
		layout.addComponent(inputField);
		
		// lable to display graph data TESTING PURPOSES
		final Label currentData = new Label();
		layout.addComponent(currentData);
		

		// button to draw graph
		Button button = new Button("Submit Data to Graph");
		button.addClickListener(new Button.ClickListener() {
			public void buttonClick(ClickEvent event) {
				//flotData = flotInput.getValue();
				
				if (chart != null) {
					layout.removeComponent(chart); 
	//				try {
	//					Thread.sleep(500);
	//				} catch (InterruptedException e) {
	//					e.printStackTrace();
	//				}				
				}

				buildFlotChart(inputField.getValue());
				layout.addComponent(chart);
//						layout.addComponent(new Label("this is the chart options JSON: " + chart.getOptions().toString()));
				// update label
				currentData.setValue("Data from chart State:\n" + chart.getData().toJson());	//toString());	//current.setValue("Graph data is: " + flot.getData());				
			}
		});
		layout.addComponent(button);

		
		// button to get graph data
		Button dataButton = new Button("get current data from chart");
		dataButton.addClickListener(new Button.ClickListener() {
			public void buttonClick(ClickEvent event) {			
//						layout.addComponent(new Label("this is the chart options JSON: " + chart.getOptions().toString()));
				// update label
//						currentData.setValue(formatDataFromGraph("Data from chart State: " + chart.getData().toString()));	//current.setValue("Graph data is: " + flot.getData());
				currentData.setValue("Data from chart State:\n" + chart.getData().toJson());	//current.setValue("Graph data is: " + flot.getData());
//						Notification.show(formatDataFromGraph("this is the data now in the chart: " + chart.getData().toString()));
			}
		});
		layout.addComponent(dataButton);
		
		return layout;
				
//				buildFlotChart("[[0,0], [10,30], [20,50]]");
//				layout.addComponent(chart);
//				FlotChart flot2 = new FlotChart();
//				flot2.setWidth("600px");
//				flot2.setHeight("300px");
//				flot2.addSeries(1, 2, 4, 8, 16);
//				layout.addComponent(flot2);
	}
	
	
	public void buildFlotChart(String data) {
		chart = new FlotChart();
		chart.setWidth("95%");
		chart.setHeight("300px");
		
//		JsonFactory factory = new JreJsonFactory();
//		JsonString jsString = factory.create(data);
//		System.out.println("JSSTRING: " + jsString.asString());
		
//		String data1 = formatDataForGraph(data);		//"[{ data:" + data + "\", lines:{show:true}\", points:{show:true} }]";
		String d = "[[0,0],[5,5],[10,10],[20,20],[25,25],[30,40],[32,44],[37,50],[40,100],[45,110],[50,110],[55,100],[60,50],[70,20],[80,10]]";
		String data1 = d + ", \"label\": \"server data\", \"lines\": {\"show\":\"true\", \"fill\":\"true\"}, \"points\":{\"show\":\"true\"}, \"clickable\":\"true\", \"hoverable\":\"true\", \"editable\":\"false\"";	//lines:{show:true, fill:true}, points:{show:true}, 	//formatDataForGraph("[[0,0], [10,30], [20,50]]");
		String data2 = data + ", \"label\": \"ramp function\", \"lines\":{\"show\":\"true\", \"fill\":\"false\"}, \"points\":{\"show\":\"true\"}, \"clickable\":\"true\", \"hoverable\":\"true\", \"editable\":\"true\"";
		
		chart.setData("[{ \"data\": " + data2 + " }]");	//(formatDataForGraph(data));
//		chart.setData("[{ \"data\": " + data1 + "}, { \"data\": " + data2 + " }]");	//("[{ data:[[0,0], [10,30], [20,50]], lines:{show:true}, points:{show:true}, hoverable:true, clickable:true }]");	//(formatDataForGraph(data));
		// options
		String options =
				"{" + 
					//"series: { lines: {show: true}, points: {show: true} }" +
//					"crosshair: {mode: x}, " +
					"\"legend\": { \"position\": \"nw\" }, " +
					"\"xaxes\": [{ \"axisLabel\": \"x label\", }], " +
					"\"yaxes\": [{ \"position\": \"left\", \"axisLabel\": \"y label\", \"tickFormatter\": \"ms\"}], " +	//'ms'}], " +
					"\"grid\": { " +
						"\"aboveData\": \"false\"," +
						"\"clickable\": \"true\"," +
						"\"hoverable\": \"true\"," +
						"\"editable\":\"true\"," +
//						"backgroundColor:{" +
//							"colors:[ \"#fef\", \"#eee\" ]" +
//						"}" +
					"}" +
				"}";
		chart.setOptions(options);
		/*String data = "[" + //"[" +
					"[0, 5]," +
					"[2, 7]," +
					"[4, 8]," +
					"[10, 5]" +
					"]";// + "]";
		*/
	}
	

	
}

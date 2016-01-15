package com.aaron.mbpet.ui;

import com.aaron.mbpet.components.flot.FlotChart;
import com.aaron.mbpet.services.FlotUtils;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

import elemental.json.JsonArray;
import elemental.json.JsonFactory;
import elemental.json.JsonObject;
import elemental.json.impl.JreJsonFactory;

public class RampFlotWindow extends Window {

	VerticalLayout layout = new VerticalLayout();
	HorizontalLayout hl = new HorizontalLayout();
	final Label currentData = new Label();
	final static TextField inputField = new TextField();
	private static FlotChart chart;
	String rampValue;

	public RampFlotWindow(String rampValue) {
        super("Edit ramp value in chart"); // Set window caption
        center();
        setResizable(true);
        setDraggable(true);
        setClosable(true);
        setPosition(-1, -1);
        setWidth(30, Unit.EM);		//(33, Unit.PERCENTAGE);

        this.rampValue = rampValue;
        
        setContent(buildWindowContent());
        
        buildFlotChart(rampValue);
//        hl.setWidth("100%");
//        hl.addComponent(chart);
//        layout.addComponent(chart);
//				layout.addComponent(new Label("this is the chart options JSON: " + chart.getOptions().toString()));
		// update label
		currentData.setValue("Data from chart State:\n" + chart.getData().toJson());
	}
	
	
	private Component buildWindowContent() {
    	// Some basic content for the window
//        VerticalLayout vc = new VerticalLayout();
//        vc.setMargin(true);
		
		layout.setMargin(true);
		layout.setSpacing(true);
//        setContent(layout);
        
        layout.addComponent(new Label("input ramp: " + rampValue));
        
		// set input data
//		inputField = new TextField();
		inputField.addStyleName("small");
		inputField.setWidth("70%");
		inputField.setCaption("Give graph data in format: '[[0,0], [10,30], [20,50]]'");

//		String formatted = FlotUtils.formatRampToFlot(rampValue);
		inputField.setValue(FlotUtils.formatFlotToRamp(rampValue));		//("[[0,0],[19.33,19.93],[33.16,24.39],[49.66,102.45],[76.33,13.23]]");		//("[[0,0], [10,30], [20,50],[50,70],[60,0]]");
		//flotInput.setInputPrompt("[[0,0], [10,30], [20,50],[50,70],[60,0]]");
		layout.addComponent(inputField);
		
		// lable to display graph data TESTING PURPOSES
		layout.addComponent(currentData);
		
		// button to draw graph
		Button button = new Button("Submit Data to Graph");
		button.addStyleName("small");
		button.addClickListener(new Button.ClickListener() {
			public void buttonClick(ClickEvent event) {
				buttonAction(inputField.getValue());
			}
		});
		layout.addComponent(button);

		
		// button to get graph data
		Button dataButton = new Button("get current data from chart");
		dataButton.addStyleName("small");
		dataButton.addClickListener(new Button.ClickListener() {
			public void buttonClick(ClickEvent event) {			
//						layout.addComponent(new Label("this is the chart options JSON: " + chart.getOptions().toString()));
				// update label
//						currentData.setValue(formatDataFromGraph("Data from chart State: " + chart.getData().toString()));	//current.setValue("Graph data is: " + flot.getData());
				currentData.setValue("Data from chart State:\n" + chart.getData().toJson());	//current.setValue("Graph data is: " + flot.getData());
//						Notification.show(formatDataFromGraph("this is the data now in the chart: " + chart.getData().toString()));
				
//				JsonFactory factory = new JreJsonFactory();
//				JsonArray jarray = factory.parse(chart.getData().toJson());
//				System.out.println(jarray + "-" + jarray.toJson());
//				JsonObject obj = jarray.get(0);
//				System.out.println("data->" + obj.get("data").toJson());
				
				JsonFactory factory = new JreJsonFactory();
				JsonArray jarray = factory.parse(chart.getData().toJson());
				System.out.println(jarray + "-" + jarray.toJson());
				JsonObject obj = jarray.get(0);
				JsonArray dataArray = factory.parse(obj.get("data").toJson());
				System.out.println("data->" + dataArray.toJson());
//				for (int i = 0; i < dataArray.length(); i++) {
//					JsonArray array = dataArray.get(i);
//					System.out.println(array.toJson());
//					
//					int x = (int) Math.round(array.get(0).asNumber());
//					int y = (int) Math.round(array.get(1).asNumber());
//					System.out.println(x + "," + y);
//
//					((JsonArray) dataArray.get(i)).set(0,x); 	//(int) Math.round(array.get(0)));
//					((JsonArray) dataArray.get(i)).set(1,y); 	//(int) Math.round(array.get(0)));
////					System.out.println(dataArray.get(i).);
//				}			
//				System.out.println("data->" + dataArray.toJson());
//				System.out.println(jarray + "-" + jarray.toJson());
//				
////				buildFlotChart(dataArray.toJson());
//				buttonAction(dataArray.toJson());

				inputField.setValue(FlotUtils.formatFlotToRamp(dataArray.toJson()));	//(obj.get("data").toJson()));		//("[[0,0],[19.33,19.93],[33.16,24.39],[49.66,102.45],[76.33,13.23]]");		//("[[0,0], [10,30], [20,50],[50,70],[60,0]]");

			}
		});
		layout.addComponent(dataButton);
		

		hl.setSpacing(false);
		hl.setWidth("100%");
//		hl.setSizeUndefined();
		layout.addComponent(hl);
		
		Label xLabel = new Label("Users");
		xLabel.addStyleName("tiny");
		xLabel.setWidth(4.0f, Unit.EM);
		hl.addComponent(xLabel);
		hl.setComponentAlignment(xLabel, Alignment.MIDDLE_LEFT);
//		hl.setExpandRatio(xLabel, 0);
		
		firstbuttonAction();

//		buildFlotChart(rampValue);
//		hl.addComponent(chart);
//		hl.setExpandRatio(chart, 1);
		
		Label yLabel = new Label("Seconds");
		yLabel.addStyleName("tiny");
		yLabel.setSizeUndefined();
		layout.addComponent(yLabel);
		layout.setComponentAlignment(yLabel, Alignment.TOP_CENTER);
		
		return layout;

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
//		String data1 = d + ", \"label\": \"server data\", \"lines\": {\"show\":\"true\", \"fill\":\"true\"}, \"points\":{\"show\":\"true\"}, \"clickable\":\"true\", \"hoverable\":\"true\", \"editable\":\"false\"";	//lines:{show:true, fill:true}, points:{show:true}, 	//formatDataForGraph("[[0,0], [10,30], [20,50]]");
		String data2 = data + ", \"label\": \"ramp function\", \"lines\":{\"show\":\"true\"}, \"points\":{\"show\":\"true\"}, \"clickable\":\"true\", \"hoverable\":\"true\", \"editable\":\"true\"";
		//, \"fill\":\"false\"   \"clickable\":\"true\",
		
		chart.setData("[{ \"data\": " + data2 + " }]");	//(formatDataForGraph(data));
//		chart.setData("[{ \"data\": " + data1 + "}, { \"data\": " + data2 + " }]");	//("[{ data:[[0,0], [10,30], [20,50]], lines:{show:true}, points:{show:true}, hoverable:true, clickable:true }]");	//(formatDataForGraph(data));
		// options
		String options =
				"{" + 
					//"series: { lines: {show: true}, points: {show: true} }" +
//					"\"crosshair\": {\"mode\": \"x\"}, " +
					"\"legend\": { \"position\": \"nw\" }, " +
					"\"xaxis\": { \"position\": \"bottom\", \"min\":0, \"tickDecimals\": \"0\"}, " +	//, \"axisLabel\": \"x label\"}], " +
					"\"yaxis\": { \"position\": \"left\", \"min\":0, \"tickDecimals\": \"0\"}, " +	//\"axisLabel\": \"y label\", \"position\": \"left\",  'ms'}], " +
					"\"grid\": { " +
//						"\"margin\": { \"top\":\"5\", \"left\":\"10\", \"bottom\":\"15\", \"right\":\"20\" }," +
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
	}
	
	
//	public void buttonClick( ClickEvent event ) {
//		  if( event.getButton() == button ) {
//		    buttonAction();
//		  }
//		}

	public void firstbuttonAction() {
		//flotData = flotInput.getValue();
			
		buildFlotChart(FlotUtils.formatRampToFlot(rampValue));
		hl.addComponent(chart);
		hl.setExpandRatio(chart, 1);
//				layout.addComponent(new Label("this is the chart options JSON: " + chart.getOptions().toString()));
		// update label
		currentData.setValue("Data from chart State:\n" + chart.getData().toJson());	//toString());	//current.setValue("Graph data is: " + flot.getData());
		System.out.println("Data from chart State:\n" + chart.getData().toJson());
	}
		public void buttonAction(String inputvalue) {
			//flotData = flotInput.getValue();
			
			if (chart != null){
				hl.removeComponent(hl.getComponent(1));
//				hl.removeComponent(chart);
			}
			
			rampValue = inputvalue;	//inputField.getValue();
			buildFlotChart(FlotUtils.formatRampToFlot(rampValue));
			hl.addComponent(chart);
			hl.setExpandRatio(chart, 1);
//					layout.addComponent(new Label("this is the chart options JSON: " + chart.getOptions().toString()));
			// update label
			currentData.setValue("Data from chart State:\n" + chart.getData().toJson());	//toString());	//current.setValue("Graph data is: " + flot.getData());
			System.out.println("Data from chart State:\n" + chart.getData().toJson());
		}
		
		
		
		public static void updateDataInField() {
			JsonFactory factory = new JreJsonFactory();
			JsonArray jarray = factory.parse(chart.getData().toJson());
			System.out.println(jarray + "-" + jarray.toJson());
			JsonObject obj = jarray.get(0);
			JsonArray dataArray = factory.parse(obj.get("data").toJson());
			System.out.println("DROP TRIGGERED UPDATE->" + dataArray.toJson());
			
			inputField.setValue(dataArray.toJson()); 
			
		}

	
}

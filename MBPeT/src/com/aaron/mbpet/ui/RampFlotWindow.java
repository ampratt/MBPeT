package com.aaron.mbpet.ui;

import com.aaron.mbpet.MbpetUI;
import com.aaron.mbpet.components.flot.FlotChart;
import com.aaron.mbpet.domain.Parameters;
import com.aaron.mbpet.domain.TRT;
import com.aaron.mbpet.domain.TestSession;
import com.aaron.mbpet.services.FileSystemUtils;
import com.aaron.mbpet.services.FlotUtils;
import com.aaron.mbpet.services.ParametersUtils;
import com.aaron.mbpet.views.tabs.parameterstab.ParametersAceEditorLayout;
import com.aaron.mbpet.views.tabs.parameterstab.ParametersFormAceView;
import com.kbdunn.vaadin.addons.fontawesome.FontAwesome;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.Validator.InvalidValueException;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.data.util.BeanItem;
import com.vaadin.event.FieldEvents.TextChangeEvent;
import com.vaadin.event.FieldEvents.TextChangeListener;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.server.Page;
import com.vaadin.server.Resource;
import com.vaadin.shared.Position;
import com.vaadin.shared.ui.MarginInfo;
//import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

import elemental.json.JsonArray;
import elemental.json.JsonFactory;
import elemental.json.JsonObject;
import elemental.json.impl.JreJsonFactory;

public class RampFlotWindow extends Window implements Button.ClickListener {

	private JPAContainer<Parameters> parameterscontainer;
	private Parameters currParameters;
	private TestSession currSession;
	FieldGroup binder;
	BeanItem<Parameters> beanItem;
	
	VerticalLayout layout = new VerticalLayout();
	HorizontalLayout hl = new HorizontalLayout();
	final static Label currentData = new Label();
	final static TextField inputField = new TextField();
	private Button drawButton;
	private Button pointButton;
	private Button saveButton;
	private static FlotChart chart;
	static String rampValue;
	static JsonFactory factory = new JreJsonFactory();

	ParametersAceEditorLayout editorLayout;
	ParametersFormAceView formAceView;
	
	public RampFlotWindow(TestSession currSession, Parameters currParameters, String rampValue, 
			ParametersAceEditorLayout editorLayout, ParametersFormAceView formAceView) {
        super("Edit ramp value in chart"); // Set window caption
        center();
        setResizable(true);
        setDraggable(true);
        setClosable(true);
        setPosition(-1, -1);
        setWidth(40, Unit.EM);		//(33, Unit.PERCENTAGE);

        this.currSession = currSession;
        this.currParameters = currParameters;
        this.rampValue = rampValue;
        this.editorLayout = editorLayout;
        this.formAceView = formAceView;
        this.parameterscontainer = ((MbpetUI) UI.getCurrent()).getParameterscontainer();

        setContent(buildWindowContent());
        bindFields();
        
//        buildFlotChart(rampValue);
        
//        hl.setWidth("100%");
//        hl.addComponent(chart);
//        layout.addComponent(chart);
//				layout.addComponent(new Label("this is the chart options JSON: " + chart.getOptions().toString()));
		// update label
		currentData.setValue("Data from chart State:\n" + chart.getData().toJson());
	}
	
	
	private void bindFields() {
//		Person person = new Person();
//		currParameters.setRamp_list(rampValue);

		
		binder = new FieldGroup();
		beanItem = new BeanItem<Parameters>(currParameters);		// takes item as argument
		binder.setItemDataSource(beanItem); 	// link to data model to binder
//		binder.bindMemberFields(form);	// link to layout

		// GENERATE FIELDS
		// using bind() to determine what type of field is created yourself...
//		TextField select = new TextField();
		binder.bind(inputField, "ramp_list");
//		layout.addComponent(select);
		
//		for (Object propertyId : item.getItemPropertyIds()) {
//			if(!"address".equals(propertyId)) {
//				Field field = binder.buildAndBind(propertyId);
//				layout.addComponent(field);							
//			}
//		}
		
//		// using buildAndBind()
//		Field field = binder.buildAndBind("firstname");
//		layout.addComponent(field);
	}


	private Component buildWindowContent() {
    	// Some basic content for the window
//        VerticalLayout vc = new VerticalLayout();
//        vc.setMargin(true);
		
		layout.setMargin(true);
		layout.setSpacing(true);
//        setContent(layout);
        
//        layout.addComponent(new Label("input ramp: " + rampValue));
        
		HorizontalLayout buttons = new HorizontalLayout();
		buttons.setWidth("100%");
		buttons.setMargin(new MarginInfo(false, true, false, false));
		buttons.setSpacing(true);
		layout.addComponent(buttons);

		HorizontalLayout fieldButtons = new HorizontalLayout();
		fieldButtons.setSpacing(false);
		buttons.addComponent(fieldButtons);
		
		// set input data
//		inputField = new TextField();
		inputField.addStyleName("tiny");
		inputField.setWidth(25, Unit.EM);		//("70%");
		inputField.setCaption("Ramp value");		//("Give graph data in format: '[[0,0], [10,30], [20,50]]'");
//		inputField.setValue(FlotUtils.formatFlotToRamp(rampValue));		//("[[0,0],[19.33,19.93],[33.16,24.39],[49.66,102.45],[76.33,13.23]]");		//("[[0,0], [10,30], [20,50],[50,70],[60,0]]");
		//inputField.setInputPrompt("[[0,0], [10,30], [20,50],[50,70],[60,0]]");
		inputField.addTextChangeListener(myTextChangeListener);
		inputField.addValueChangeListener(myValueChangeListener);
		fieldButtons.addComponent(inputField);
		
		// button to draw graph
		drawButton = new Button("Draw", this);
		drawButton.addStyleName("tiny");
//		drawButton.setIcon(FontAwesome.LINE_CHART);
//		drawButton.setDescription("Draw Graph");
		drawButton.setClickShortcut(KeyCode.ENTER);
		fieldButtons.addComponent(drawButton);
		fieldButtons.setComponentAlignment(drawButton, Alignment.BOTTOM_LEFT);

		//button to add new point to graph
		pointButton = new Button("", this);
		pointButton.addStyleName("tiny");
		pointButton.setIcon(FontAwesome.PLUS);
		pointButton.setDescription("Add data point");

		saveButton = new Button("Save", this);
		saveButton.addStyleName("tiny");
		saveButton.addStyleName("primary");
		saveButton.setIcon(FontAwesome.SAVE);
		saveButton.setEnabled(false);
		
		buttons.addComponents(pointButton, saveButton);
		buttons.setComponentAlignment(pointButton, Alignment.BOTTOM_LEFT);
		buttons.setComponentAlignment(saveButton, Alignment.BOTTOM_RIGHT);
		buttons.setExpandRatio(saveButton, 1);
		buttons.setExpandRatio(pointButton, 0);	//fieldButtons, 1
		
		// lable to display graph data TESTING PURPOSES
//		layout.addComponent(currentData);

		
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
//		layout.addComponent(dataButton);
		

		hl.setSpacing(false);
		hl.setWidth("100%");
//		hl.setSizeUndefined();
		layout.addComponent(hl);
		
		Label yLabel = new Label("Users");
		yLabel.addStyleName("tiny");
		yLabel.setWidth(4.0f, Unit.EM);
		hl.addComponent(yLabel);
		hl.setComponentAlignment(yLabel, Alignment.MIDDLE_LEFT);
//		hl.setExpandRatio(xLabel, 0);
		
		firstbuttonAction();	//first



//		buildFlotChart(rampValue);
//		hl.addComponent(chart);
//		hl.setExpandRatio(chart, 1);
		
		Label xLabel = new Label("Time (Seconds)");
		xLabel.addStyleName("tiny");
		xLabel.setSizeUndefined();
		layout.addComponent(xLabel);
		layout.setComponentAlignment(xLabel, Alignment.TOP_CENTER);
		
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
//		String data2 = data + ", \"label\": \"ramp function\", \"lines\":{\"show\":\"true\"}, \"points\":{\"show\":\"true\"}, \"clickable\":\"true\", \"hoverable\":\"true\", \"editable\":\"true\"";
		//, \"fill\":\"false\"   \"clickable\":\"true\",
		String data2 = data + ", \"label\": \"ramp function\", \"lines\":{\"show\":\"true\"}, \"points\":{\"show\":\"true\"}, \"clickable\":\"true\", \"hoverable\":\"true\", \"editable\":\"true\"";

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
	
	

	@Override
	public void buttonClick(ClickEvent event) {
		if (event.getButton() == drawButton) {
			buttonAction(inputField.getValue());

		} else if (event.getButton() == saveButton) {
			saveParameters();
			saveButton.setEnabled(false);
			
		} else if (event.getButton() == pointButton) {
			//get current data
			JsonArray jarray = factory.parse(chart.getData().toJson());
			JsonObject obj = jarray.get(0);
			JsonArray dataArray = factory.parse(obj.get("data").toJson());
				System.out.println("current data->" + dataArray.toJson());
			
			// get last data point array
			JsonArray lastSet = dataArray.getArray(dataArray.length()-1);
			System.out.println("last data point->" + lastSet.toJson());

			String arrayString = dataArray.toJson();
			System.out.println("array string->" + arrayString);
			
			int x = (int) lastSet.getNumber(0) +10;
			int y = (int) lastSet.getNumber(1) +5;
					
			String newRamp = arrayString.substring(0, arrayString.length()-1) +
							",[" + x + "," + y + "]]";
				System.out.println("new ramp string->" + newRamp);

//			JsonArray newPoint = factory.createArray();
//			JsonArray newRamp = dataArray. set(dataArray.length(), );	//dataArray.getArray(dataArray.length()-1);
			buttonAction(FlotUtils.formatFlotToRamp(newRamp));
		}
		
	}
	

	public void firstbuttonAction() {
		//flotData = flotInput.getValue();
		buildFlotChart(FlotUtils.formatRampToFlot(rampValue));
		hl.addComponent(chart);
		hl.setExpandRatio(chart, 1);
//				layout.addComponent(new Label("this is the chart options JSON: " + chart.getOptions().toString()));
		// update label
		currentData.setValue("Data from chart State:\n" + chart.getData().toJson());	//toString());	//current.setValue("Graph data is: " + flot.getData());
		System.out.println("Data from chart State:\n" + chart.getData().toJson());
		rampValue = FlotUtils.formatFlotToRamp(chart.getData().toJson());
	}
	
	public void buttonAction(String inputvalue) {
		//flotData = flotInput.getValue();
		if (chart != null){
			hl.removeComponent(hl.getComponent(1));
		}
		rampValue = inputvalue;	//inputField.getValue();
		buildFlotChart(FlotUtils.formatRampToFlot(rampValue));
		hl.addComponent(chart);
		hl.setExpandRatio(chart, 1);
//					layout.addComponent(new Label("this is the chart options JSON: " + chart.getOptions().toString()));
		// update label
		inputField.setValue(rampValue);	//(obj.get("data").toJson()));		//("[[0,0],[19.33,19.93],[33.16,24.39],[49.66,102.45],[76.33,13.23]]");		//("[[0,0], [10,30], [20,50],[50,70],[60,0]]");
		currentData.setValue("Data from chart State:\n" + chart.getData().toJson());	//toString());	//current.setValue("Graph data is: " + flot.getData());
		System.out.println("Data from chart State:\n" + chart.getData().toJson());
	}
	
	
	
	public static void updateDataInField() {
		JsonArray jarray = factory.parse(chart.getData().toJson());
			System.out.println(jarray + "-" + jarray.toJson());
		JsonObject obj = jarray.get(0);
		JsonArray dataArray = factory.parse(obj.get("data").toJson());
			System.out.println("DROP TRIGGERED UPDATE->" + dataArray.toJson());
		
//			inputField.setValue(dataArray.toJson()); 
		rampValue = FlotUtils.formatFlotToRamp(dataArray.toJson());
		inputField.setValue(FlotUtils.formatFlotToRamp(dataArray.toJson()));	//(obj.get("data").toJson()));		//("[[0,0],[19.33,19.93],[33.16,24.39],[49.66,102.45],[76.33,13.23]]");		//("[[0,0], [10,30], [20,50],[50,70],[60,0]]");
		currentData.setValue("Data from chart State:\n" + chart.getData().toJson());	//current.setValue("Graph data is: " + flot.getData());
	}

	
    public void saveParameters() {
	    try {
			binder.commit();
			
			// 1 UPDATE container
			parameterscontainer.addEntity(beanItem.getBean());
			currParameters = parameterscontainer.getItem(beanItem.getBean().getId()).getEntity();
//			System.out.println("Parameters are now: " + currentparams.getId() 
//								+ " " + currentparams.getSettings_file());
			
			// 2 UPDATE parentcase reference
			currSession.setParameters(parameterscontainer.getItem(currParameters.getId()).getEntity());		
//			System.out.println("Session's Params are now: " + currsession.getParameters().getId() + " " + currsession.getParameters().getSettings_file());
			

			// insert form data into settings file
			String settings = ParametersUtils.insertFormDataToAce(currParameters, editorLayout.getEditorValue());
			currParameters.setSettings_file(settings);
			parameterscontainer.addEntity(currParameters);

			// add settings to editor view
			editorLayout.toggleEditorFields(true);
			editorLayout.setEditorValue(settings);
			
			// write settings file to disk
      	  	FileSystemUtils fileUtils = new FileSystemUtils();
			fileUtils.writeSettingsToDisk(	//username, sut, session, settings_file)
					currSession.getParentcase().getOwner().getUsername(),
					currSession.getParentcase().getTitle(), 
					currSession.getTitle(), 
					currParameters.getSettings_file());
			
			// update Form view
			formAceView.bindFormtoBean(currParameters);
			
//			// write confirmation message
//	        Notification notification = new Notification("Parameters",Type.TRAY_NOTIFICATION);
//	        notification.setDescription("were edited");
//	        notification.setStyleName("dark small");	//tray  closable login-help
//	        notification.setPosition(Position.BOTTOM_RIGHT);
//	        notification.setDelayMsec(500);
//	        notification.show(Page.getCurrent());
        
		} catch (CommitException  | InvalidValueException e) {
			e.printStackTrace();
	        Notification notification = new Notification("Heads Up!");
	        notification.setDescription("Some fields had improper values");
	        notification.setStyleName("failure");	//tray  closable login-help
//	        notification.setPosition(Position.BOTTOM_RIGHT);
//	        notification.setDelayMsec(500);
	        notification.show(Page.getCurrent());
		} 
    }
	
    TextChangeListener myTextChangeListener = new TextChangeListener() {
        @Override
        public void textChange(TextChangeEvent event) {
        	saveButton.setEnabled(true);
//            AbstractTextField source = (AbstractTextField) event.getSource();
//            System.err.println(event.getText() + " - " + source.getValue());
//            doValidation();
        }
    };
    
    ValueChangeListener myValueChangeListener = new ValueChangeListener() {
		@Override
		public void valueChange(ValueChangeEvent event) {
        	saveButton.setEnabled(true);
		}
	};
    
}

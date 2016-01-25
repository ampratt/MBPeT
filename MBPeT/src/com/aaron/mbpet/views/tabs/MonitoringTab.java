package com.aaron.mbpet.views.tabs;

import com.aaron.mbpet.components.flot.FlotChart;
import com.vaadin.annotations.JavaScript;
import com.vaadin.annotations.StyleSheet;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
//import com.vaadin.tests.themes.valo.components.TestIcon;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;

@JavaScript("http://cdn.alloyui.com/2.5.0/aui/aui-min.js")
@StyleSheet("http://cdn.alloyui.com/2.5.0/aui-css/css/bootstrap.min.css")
public class MonitoringTab extends Panel {

	VerticalLayout vert = new VerticalLayout();
	private VerticalLayout slavevert;
	public Label sentdata;
	public Label receiveddata;
	public Label throughput;
	public Label success;
	public Label error_count;
	public Label targetuser;
	public Label averageresponse;
	public Label minmaxresponse;
	public Label slave;
	public Label slavelabel;
	public FlotChart usersChart;
	String dataOptions = ", \"label\": \"active users\", \"lines\":{\"show\":\"true\"}, \"points\":{\"show\":\"true\"}, \"hoverable\":\"true\" ";

	ReportsTab reportsTab;
//	public static ProgressBar progressbar;
//	public static Label progressstatus;
	
    public MonitoringTab() {	//ReportsTab reportsTab
    	//setHeight(100.0f, Unit.PERCENTAGE);
        setSizeFull();
		setContent(vert);
        
        vert.setMargin(true);
        vert.setSpacing(true);
		
//        this.reportsTab = reportsTab; 
        
//        // Create the indicator, disabled until progress is started
//        progressbar = new ProgressBar(new Float(0.0));
//        progressbar.setEnabled(false);
//        progressbar.setVisible(false);
//        progressbar.setWidth("250px");
//        vert.addComponent(progressbar);
//        
//        progressstatus = new Label("not running");
//        progressstatus.setVisible(false);
//        vert.addComponent(progressstatus);

        
        vert.addComponent(buildPanels());
//        vert.addComponent(vert);
//        vert.setExpandRatio(vert, 1);
        
//        vert.addComponent(usersChart());
       usersChart();
       
        vert.addComponent(new Label("<h2>Here below will be whatever graphs are desired...</h2>", ContentMode.HTML));

    }
    
    
    public HorizontalLayout buildPanels() {
    	HorizontalLayout row = new HorizontalLayout();
        //row.setStyleName("monitor-panels");
        row.setMargin(new MarginInfo(true, true, false, true));
        row.setSpacing(true);
    	row.setWidth("100%");
//    	addComponent(row);
        //TestIcon testIcon = new TestIcon(60);

        Panel panel = new Panel("Response Times");
        //panel.setIcon(testIcon.get());
        panel.setContent(responseTimes());
        row.addComponent(panel);

        panel = new Panel("Response Counts");
        panel.setContent(responseCounts());
        panel.setSizeFull();
        row.addComponent(panel);
        
        panel = new Panel("Bandwidth");
        panel.setContent(bandwidthContent());
        row.addComponent(panel);
        
        panel = new Panel("Slaves");
        panel.setContent(slavePanelContent());
        row.addComponent(panel);

//        vert.setComponentAlignment(row, Alignment.TOP_CENTER);	//this
        return row;
    }
    
    public void usersChart() {
//    	VerticalLayout v = new VerticalLayout();
    	//TESTING
		// update Button
		Button updateButton = new Button("get Data Updates");
		updateButton.addClickListener(new Button.ClickListener() {
			public void buttonClick(ClickEvent event) {		
				
				Notification.show("you asked for updates...", Type.HUMANIZED_MESSAGE);

				fetchNewData(20);
//				for (int i=0; i<25; i++) { 
//					updatesChart.update();
//				}
				usersChart.getCurrentData();
				
				// update label
//				currentData.setValue("Data from chart State:\n" + updatesChart.getData().toJson());	//current.setValue("Graph data is: " + flot.getData());
			}
		});
		vert.addComponent(updateButton);
		
		
    	HorizontalLayout hl = new HorizontalLayout();
		hl.setSpacing(false);
		hl.setWidth("100%");
//		hl.setSizeUndefined();
		vert.addComponent(hl);
//		vert.setComponentAlignment(hl, Alignment.TOP_CENTER);
		
		// chart axis label
		Label yLabel = new Label("Users");
		yLabel.addStyleName("tiny");
		yLabel.setWidth(4.0f, Unit.EM);
		hl.addComponent(yLabel);
		hl.setComponentAlignment(yLabel, Alignment.MIDDLE_RIGHT);
		
		// flot chart
//		firstbuttonAction();
		buildFlotChart("[[0,0]]");	//(FlotUtils.formatRampToFlot(rampValue));
		hl.addComponent(usersChart);
		hl.setComponentAlignment(usersChart, Alignment.MIDDLE_LEFT);
		hl.setExpandRatio(usersChart, 1);
		System.out.println("Data from chart State:\n" + usersChart.getData().toJson());
//		currentData.setValue("Data from chart State:\n" + chart.getData().toJson());	//toString());	//current.setValue("Graph data is: " + flot.getData());
//		rampValue = FlotUtils.formatFlotToRamp(chart.getData().toJson());

		
		// chart axis label
		Label xLabel = new Label("Time (Seconds)");
		xLabel.addStyleName("tiny");
		xLabel.setSizeUndefined();
		vert.addComponent(xLabel);
		vert.setComponentAlignment(xLabel, Alignment.TOP_CENTER);
		
//		return v;
    }
    
	public void buildFlotChart(String data) {
		usersChart = new FlotChart();
		usersChart.setWidth("90%");
		usersChart.setHeight("250px");
		
//		JsonFactory factory = new JreJsonFactory();
//		JsonString jsString = factory.create(data);
//		System.out.println("JSSTRING: " + jsString.asString());
		
//		String data1 = formatDataForGraph(data);		//"[{ data:" + data + "\", lines:{show:true}\", points:{show:true} }]";
//		String d = "[[0,0],[5,5],[10,10],[20,20],[25,25],[30,40],[32,44],[37,50],[40,100],[45,110],[50,110],[55,100],[60,50],[70,20],[80,10]]";
//		String data1 = d + ", \"label\": \"server data\", \"lines\": {\"show\":\"true\", \"fill\":\"true\"}, \"points\":{\"show\":\"true\"}, \"clickable\":\"true\", \"hoverable\":\"true\", \"editable\":\"false\"";	//lines:{show:true, fill:true}, points:{show:true}, 	//formatDataForGraph("[[0,0], [10,30], [20,50]]");
//		String data2 = data + ", \"label\": \"ramp function\", \"lines\":{\"show\":\"true\"}, \"points\":{\"show\":\"true\"}, \"clickable\":\"true\", \"hoverable\":\"true\", \"editable\":\"true\"";
		
		String data2 = data + dataOptions;//", \"label\": \"active users\", \"lines\":{\"show\":\"true\"}, \"points\":{\"show\":\"true\"}, \"hoverable\":\"true\" ";
//				"\"clickable\":\"true\", \"editable\":\"true\""

		usersChart.setData("[{ \"data\": " + data2 + " }]");	//(formatDataForGraph(data));
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
						"\"clickable\": \"false\"," +
						"\"hoverable\": \"true\"," +
						"\"editable\":\"false\"," +
//						"backgroundColor:{" +
//							"colors:[ \"#fef\", \"#eee\" ]" +
//						"}" +
					"}" +
				"}";
		usersChart.setOptions(options);
	}
	
	public void fetchNewData(int rounds) {
		int y;
		for (int i=0; i<rounds; i++) {
			if (prevY <= 150) {
				y = prevY + 10; 
			} else {
				y = prevY - 75; 
			}
			if (x<1)
				usersChart.update(0, 0);			// update the js code to effect the chart 

			x += 1; 	

			usersChart.addNewData(x, y);	// update the server side data
			usersChart.update(x, y);			// update the js code to effect the chart 
//			System.out.println(updatesChart.getData().toJson());
	//		data.push([x, y]);
			prevY = y;
//			prevX = x;
		}
	}
    
	
    private Component responseTimes() {
        VerticalLayout vert = new VerticalLayout();
        //vert.setSizeFull();
        vert.setMargin(true);
        vert.setHeight("6em");
        //vert.setWidth("8em");
             
        // panels for KPI updates
        HorizontalLayout row = new HorizontalLayout();
        row.setWidth("100%");
        //layout.setSpacing(true);
        //Label content = new Label("Suspendisse dictum feugiat nisl ut dapibus. Mauris iaculis porttitor posuere. Praesent id metus massa, ut blandit odio.");
        
        Label responseTimesLabel = new Label("Average");
        responseTimesLabel.addStyleName("small");
        
        averageresponse = new Label();
        averageresponse.setSizeUndefined();
        averageresponse.addStyleName("bold");
        averageresponse.addStyleName("small");
        
        row.addComponents(responseTimesLabel, averageresponse);
        row.setComponentAlignment(responseTimesLabel, Alignment.MIDDLE_LEFT);
        row.setComponentAlignment(averageresponse, Alignment.MIDDLE_RIGHT);
//        row.setExpandRatio(responseTimesLabel, 1);
//        row.setExpandRatio(averageresponse, 2);
        vert.addComponent(row);

        row = new HorizontalLayout();
        row.setWidth("100%");
        
        responseTimesLabel = new Label("Min/Max");
        responseTimesLabel.addStyleName("small");
        
        minmaxresponse = new Label();
        minmaxresponse.setSizeUndefined();
        minmaxresponse.addStyleName("bold");
        minmaxresponse.addStyleName("small");
        
        row.addComponents(responseTimesLabel, minmaxresponse);
        row.setComponentAlignment(responseTimesLabel, Alignment.MIDDLE_LEFT);
        row.setComponentAlignment(minmaxresponse, Alignment.MIDDLE_RIGHT);
//        row.setExpandRatio(responseTimesLabel, 1);
//        row.setExpandRatio(minmaxresponse, 2);
        vert.addComponent(row);
        
        return vert;
    }
    
    private Component responseCounts() {
//        GridLayout grid = new GridLayout(2,2);
//        //vert.setSizeFull();
//        grid.setMargin(true);
//        grid.setHeight("6em");        
//
//        Label responseCountsLabel = new Label("Throughput");
//        responseCountsLabel.addStyleName("small");
//        grid.addComponent(responseCountsLabel, 0,0);
//        
//        throughput = new Label();
//        throughput.addStyleName("bold");
//        grid.addComponent(throughput, 0,1);
//       

        VerticalLayout vert = new VerticalLayout();
        vert.addStyleName("response-counts-layout-padding");
        //vert.setSizeFull();
        vert.setMargin(true);
        vert.setHeight("100%");
        //vert.setWidth("7em");
        
        HorizontalLayout row = new HorizontalLayout();
        row.setWidth("100%");

        Label responseCountsLabel = new Label("Throughput");
        responseCountsLabel.addStyleName("small");
        
        throughput = new Label();
        throughput.setSizeUndefined();
        throughput.addStyleName("bold");
        throughput.addStyleName("small");
        
        row.addComponents(responseCountsLabel, throughput);
        row.setComponentAlignment(responseCountsLabel, Alignment.MIDDLE_LEFT);
        row.setComponentAlignment(throughput, Alignment.MIDDLE_RIGHT);
//        row.setExpandRatio(responseCountsLabel, 1);
//        row.setExpandRatio(throughput, 2);
        vert.addComponent(row);

        row = new HorizontalLayout();
        row.setWidth("100%");
        
        responseCountsLabel = new Label("Success");
        responseCountsLabel.addStyleName("small");
        
        success = new Label();
        success.setSizeUndefined();
        success.addStyleName("bold");
        success.addStyleName("small");
        
        row.addComponents(responseCountsLabel, success);
        row.setComponentAlignment(responseCountsLabel, Alignment.MIDDLE_LEFT);
        row.setComponentAlignment(success, Alignment.MIDDLE_RIGHT);
//        row.setExpandRatio(responseCountsLabel, 2);
//        row.setExpandRatio(success, 1);
        vert.addComponent(row);
        
        
        row = new HorizontalLayout();
        row.setWidth("100%");
        
        responseCountsLabel = new Label("Error");
        responseCountsLabel.addStyleName("small");
        
        error_count = new Label();
        error_count.setSizeUndefined();
        error_count.addStyleName("bold");
        error_count.addStyleName("small");
        
        row.addComponents(responseCountsLabel, error_count);
        row.setComponentAlignment(responseCountsLabel, Alignment.MIDDLE_LEFT);
        row.setComponentAlignment(error_count, Alignment.MIDDLE_RIGHT);
        vert.addComponent(row);
        
        
        row = new HorizontalLayout();
        row.setWidth("100%");
        
        responseCountsLabel = new Label("No. Users");
        responseCountsLabel.addStyleName("small");
        
        targetuser = new Label();
        targetuser.setSizeUndefined();
        targetuser.addStyleName("bold");
        targetuser.addStyleName("small");
        
        row.addComponents(responseCountsLabel, targetuser);
        row.setComponentAlignment(responseCountsLabel, Alignment.MIDDLE_LEFT);
        row.setComponentAlignment(targetuser, Alignment.MIDDLE_RIGHT);
        vert.addComponent(row);
        
        return vert;
    }
    
    private Component bandwidthContent() {
        VerticalLayout vert = new VerticalLayout();
        vert.setMargin(true);
        vert.setHeight("6em");
        
        HorizontalLayout row = new HorizontalLayout();
        row.setWidth("100%");

        Label bandwidthlabel = new Label("Sent");
        bandwidthlabel.addStyleName("small");
        
        sentdata = new Label();
        sentdata.setSizeUndefined();
        sentdata.addStyleName("bold");
        sentdata.addStyleName("small");
        
        row.addComponents(bandwidthlabel, sentdata);
        row.setComponentAlignment(bandwidthlabel, Alignment.MIDDLE_LEFT);
        row.setComponentAlignment(sentdata, Alignment.MIDDLE_RIGHT);
//        row.setExpandRatio(bandwidthlabel, 1);
//        row.setExpandRatio(sentdata, 2);
        vert.addComponent(row);

        row = new HorizontalLayout();
        row.setWidth("100%");
        
        bandwidthlabel = new Label("Received");
        bandwidthlabel.addStyleName("small");
        
        receiveddata = new Label();
        receiveddata.setSizeUndefined();
        receiveddata.addStyleName("bold");
        receiveddata.addStyleName("small");
        
        row.addComponents(bandwidthlabel, receiveddata);
        row.setComponentAlignment(bandwidthlabel, Alignment.MIDDLE_LEFT);
        row.setComponentAlignment(receiveddata, Alignment.MIDDLE_RIGHT);
//        row.setExpandRatio(bandwidthlabel, 1);
//        row.setExpandRatio(receiveddata, 2);
        vert.addComponent(row);
        
        return vert;
    }
    
    
    
    private Component slavePanelContent() {
        slavevert = new VerticalLayout();
        slavevert.setMargin(true);
        slavevert.setHeight("6em");
        
//        HorizontalLayout row = new HorizontalLayout();
//        row.setWidth("100%");
//       
//        slavelabel = new Label("Slave - ");
//        slavelabel.addStyleName("small");
//        
//        slave = new Label();
//        slave.addStyleName("small");
//        slave.addStyleName("bold");
//
//        row.addComponents(slavelabel, slave);
//        vert.addComponent(row);  
        
        return slavevert;     
    }
    

    
    public void generateSlaveMonitoringInfo(int numslaves, String status) {
    	String[] statuses = {"Connected", "Generating", "Saturated"};
    	slavevert.removeAllComponents();
    	
    	for (int i=1; i<numslaves+1; i++) {
    		Label slave = new Label("Slave " + i + " - <b>" + status + "</b>", ContentMode.HTML);	//statuses[i]);  
    		slave.addStyleName("small");
    		slavevert.addComponent(slave);        	
      	}    
    }
    
    public void updateSlaveMonitoringInfo(int numslaves, String[] slaveresults) {
    	String[] statuses = {"Connected", "Generating", "Saturated"};
    	slavevert.removeAllComponents();
    	
    	for (int i=0; i<numslaves; i++) {
//    		HorizontalLayout row = new HorizontalLayout();
//    		row.setWidth("100%");
    		
    		Label slave;
    		if (slaveresults[i].contains(String.valueOf(i+1)))
    			slave = new Label(slaveresults[i] + " - <b>Generating</b>", ContentMode.HTML);	//statuses[i]);  "Slave " + (i) + " - <b>" + slaveresults[i] + "</b>"
			else 
				slave = new Label("Slave " + (i+1) + " - <b>Connected</b>", ContentMode.HTML);	//statuses[i]);
    		slave.addStyleName("small");
    		slavevert.addComponent(slave);        	
      	}    
    }
    
    
    public void updateFields(String[] fields) {
    //(String averageresponse, String minmaxresponse, String sent, String received, String throughput, String targetuser, String slave ) {
    	
    	this.averageresponse.setValue(fields[0]);
    	this.minmaxresponse.setValue(fields[1]);
    	this.throughput.setValue(fields[2]);
    	this.success.setValue(fields[3]);
    	this.error_count.setValue(fields[4]);
    	this.sentdata.setValue(fields[5]);
    	this.receiveddata.setValue(fields[6]); 
    	this.targetuser.setValue(fields[7]);
//    	this.slave.setValue(fields[7]);
    }
    
    int y, x, prevY = 0;
	public void updateChart(int users) {
		if (x<1)	//draw point (0,0) on graph
			usersChart.update(0,0);			// update the js code to effect the chart 

		x += 1; 	
		y = users;
		System.out.println("sent int:" + users);
		System.out.println("New point:" + x + "," + users);

		//TODO this first command is causing memory overload!
		usersChart.addNewData(x, y);	// update the server side data
		usersChart.update(x, y);			// update the js code to effect the chart 
		System.out.println(usersChart.getData().toJson());
//			data.push([x, y]);
//			prevY = y;
	}
	
    public void addNewMessageComponent(String string) {
    	vert.addComponent(new Label(string));
    }
}

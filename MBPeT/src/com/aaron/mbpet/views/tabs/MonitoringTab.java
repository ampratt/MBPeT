package com.aaron.mbpet.views.tabs;

import com.vaadin.annotations.JavaScript;
import com.vaadin.annotations.StyleSheet;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.shared.ui.label.ContentMode;
//import com.vaadin.tests.themes.valo.components.TestIcon;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.ProgressBar;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

@JavaScript("http://cdn.alloyui.com/2.5.0/aui/aui-min.js")
@StyleSheet("http://cdn.alloyui.com/2.5.0/aui-css/css/bootstrap.min.css")
public class MonitoringTab extends Panel {

	VerticalLayout vert = new VerticalLayout();
	public Label sentdata;
	public Label receiveddata;
	public Label throughput;
	private Label success;
	private Label error_count;
	public Label targetuser;
	public Label averageresponse;
	public Label minmaxresponse;
	public Label slave;
	private Label slavelabel;
	private static VerticalLayout slavevert;
	
//	public static ProgressBar progressbar;
//	public static Label progressstatus;
	
    public MonitoringTab() {
    	//setHeight(100.0f, Unit.PERCENTAGE);
        setSizeFull();
		setContent(vert);
        
        vert.setMargin(true);
        vert.setSpacing(true);
		
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
       
        vert.addComponent(new Label("<h2>Here below will be whatever graphs are desired...</h2>", ContentMode.HTML));

    }
    
    
    public HorizontalLayout buildPanels() {
    	HorizontalLayout row = new HorizontalLayout();
        //row.setStyleName("monitor-panels");
        row.setMargin(true);
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
    
    public static void updateSlaveMonitoringInfo(int numslaves, String[] slaveresults) {
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
    
    public static void generateSlaveMonitoringInfo(int numslaves, String status) {
    	String[] statuses = {"Connected", "Generating", "Saturated"};
    	slavevert.removeAllComponents();
    	
    	for (int i=1; i<numslaves+1; i++) {
    		Label slave = new Label("Slave " + i + " - <b>" + status + "</b>", ContentMode.HTML);	//statuses[i]);  
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
    
    public void addNewMessageComponent(String string) {
    	vert.addComponent(new Label(string));
    }
}

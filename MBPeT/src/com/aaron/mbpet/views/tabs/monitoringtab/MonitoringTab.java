package com.aaron.mbpet.views.tabs.monitoringtab;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import org.vaadin.aceeditor.AceEditor;
import org.vaadin.aceeditor.AceMode;
import org.vaadin.aceeditor.AceTheme;

import com.aaron.mbpet.MbpetUI;
import com.aaron.mbpet.components.flot.FlotChart;
import com.aaron.mbpet.domain.ActionResponse;
import com.aaron.mbpet.domain.TRT;
import com.aaron.mbpet.services.AceUtils;
import com.aaron.mbpet.views.tabs.reportstab.ReportsTab;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.annotations.JavaScript;
import com.vaadin.annotations.StyleSheet;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.combobox.FilteringMode;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
//import com.vaadin.tests.themes.valo.components.TestIcon;

@JavaScript("http://cdn.alloyui.com/2.5.0/aui/aui-min.js")
@StyleSheet("http://cdn.alloyui.com/2.5.0/aui-css/css/bootstrap.min.css")
public class MonitoringTab extends Panel {

	HorizontalLayout firstLayoutHL = new HorizontalLayout();
	VerticalLayout vert = new VerticalLayout();
	VerticalLayout rightvert = new VerticalLayout();
	public VerticalLayout responsePanelVert;
	private VerticalLayout slavevert;
	public Label sentdata;
	public Label receiveddata;
	public Label throughput;
	public Label success;
	public Label error_count;
	public Label targetuser;
	public Label aggregatedResponse;
	public Label minmaxresponse;
	public Label slave;
	public Label slavelabel;
	HorizontalLayout flotRampLayout;
	HorizontalLayout flotAggregatedLayout;
	HorizontalLayout flotIndividualLayout;
	public FlotChart monRampChart;
	public FlotChart monAggChart;
	public FlotChart monIndChart;
	String rampDataOptions = ", \"color\": \"rgb(77, 167, 77)\", \"label\": \"virtual users\", \"lines\":{\"show\":\"true\", \"fill\":\"true\"}, \"points\":{\"show\":\"true\"}, \"hoverable\":\"true\" ";
	String aggDataOptions = ", \"label\": \"aggregated response times\", \"lines\":{\"show\":\"true\", \"fill\":\"true\"}, \"points\":{\"show\":\"true\"}, \"hoverable\":\"true\" ";
	String indDataOptions = ", \"label\": \"individual response times\", \"lines\":{\"show\":\"true\"}, \"points\":{\"show\":\"true\"}, \"hoverable\":\"true\" ";

	JPAContainer<TRT> trtcontainer = ((MbpetUI) UI.getCurrent()).getTrtcontainer();

	VerticalLayout terminallayout;
	AceEditor editor;
	private ComboBox themeBox;	
	List<String> themeList;
	String[] themes = {"twilight", "terminal", "ambiance", "chrome", "clouds", "cobalt", "dreamweaver", "eclipse", "github", "xcode"};

	
	ReportsTab reportsTab;
	private Panel panel;
//	public static ProgressBar progressbar;
//	public static Label progressstatus;
	private VerticalLayout leftCharts;
//	private HorizontalLayout chartsHL;
	
    public MonitoringTab() {	//ReportsTab reportsTab
    	//setHeight(100.0f, Unit.PERCENTAGE);
        setSizeFull();
		setContent(firstLayoutHL);	//(vert);
		firstLayoutHL.setWidth("100%");
		firstLayoutHL.addComponent(vert);
		firstLayoutHL.addComponent(rightvert); //right side action panel and charts 
		firstLayoutHL.setExpandRatio(vert, 7);
		firstLayoutHL.setExpandRatio(rightvert, 3);
		
		rightvert.setMargin(new MarginInfo(true, true, false, false));
		rightvert.setSpacing(true);
		
        vert.setMargin(new MarginInfo(true, false, true, true));	//	(true);
        vert.setSpacing(true);  
        vert.addComponent(buildPanels());

//        vert.addComponent(usersChart());
        buildMonitoringChartsLayouts();
//        buildAggregatedChart();
//        buildIndividualChart();
    }
    
    
    public HorizontalLayout buildPanels() {
    	HorizontalLayout row = new HorizontalLayout();
        //row.setStyleName("monitor-panels");
        row.setMargin(false);	//(new MarginInfo(false, false, false, false));
        row.setSpacing(false);
    	row.setWidth("100%");
//    	addComponent(row);
        //TestIcon testIcon = new TestIcon(60);

    	Panel panel = new Panel("Slaves");
        panel.setContent(slavePanelContent());
        row.addComponent(panel);
        
        
        panel = new Panel("Bandwidth");
        panel.setContent(bandwidthContent());
        row.addComponent(panel);

        panel = new Panel("Response Counts");
        panel.setContent(responseCounts());
        panel.setSizeFull();
        row.addComponent(panel);
        
        panel = new Panel("Average Response Times");
        //panel.setIcon(testIcon.get());
//        panel.addStyleName("color1");
        responsePanelVert = new VerticalLayout();
        responsePanelVert.setMargin(true);
        panel.setContent(responsePanelVert);	//responseTimes());
        rightvert.addComponent(panel);

        return row;
    }
    
    public void buildMonitoringChartsLayouts() {
    	
//    	chartsHL = new HorizontalLayout();
//    	chartsHL.setMargin(false);
//    	chartsHL.setSpacing(false);
//    	chartsHL.setWidth("100%");
//    	vert.addComponent(chartsHL);
    	
    	leftCharts = new VerticalLayout();
    	leftCharts.setMargin(false);
    	leftCharts.setSpacing(false);
    	leftCharts.setWidth("100%");
    	vert.addComponent(leftCharts);
    	vert.setExpandRatio(leftCharts, 2);
    	
	/**
	 * RAMP CHART
	 */
		flotRampLayout = new HorizontalLayout();
		flotRampLayout.setSpacing(false);
		flotRampLayout.setWidth("100%");
		leftCharts.addComponent(flotRampLayout);
		
		// chart axis label
		Label yLabel = new Label("Users");
		yLabel.addStyleName("tiny");
		yLabel.setWidth(4.5f, Unit.EM);
		flotRampLayout.addComponent(yLabel);
		flotRampLayout.setComponentAlignment(yLabel, Alignment.MIDDLE_RIGHT);
		
		// flot chart
		buildRampChart("[[0,0]]");	//(FlotUtils.formatRampToFlot(rampValue));
	
		// chart axis label
		Label xLabel = new Label("Time (Seconds)");
		xLabel.addStyleName("tiny");
		xLabel.setSizeUndefined();
		leftCharts.addComponent(xLabel);
		leftCharts.setComponentAlignment(xLabel, Alignment.TOP_CENTER);
		
//		firstbuttonAction();
//		flotLayout.addComponent(usersChart);
//		flotLayout.setComponentAlignment(usersChart, Alignment.MIDDLE_LEFT);
//		flotLayout.setExpandRatio(usersChart, 1);
//		System.out.println("Data from chart State:\n" + usersChart.getData().toJson());
		
//		currentData.setValue("Data from chart State:\n" + chart.getData().toJson());	//toString());	//current.setValue("Graph data is: " + flot.getData());
//		rampValue = FlotUtils.formatFlotToRamp(chart.getData().toJson());
//		return v;

	/**
	 * AGGEGATED RESPONSE CHART
	 */
		flotAggregatedLayout = new HorizontalLayout();
		flotAggregatedLayout.setSpacing(false);
		flotAggregatedLayout.setWidth("100%");
		leftCharts.addComponent(flotAggregatedLayout);
		
		// chart axis label
		yLabel = new Label("Aggregated<br>Response Times", ContentMode.HTML);
		yLabel.addStyleName("tiny");
		yLabel.setWidth(4.5f, Unit.EM);
		flotAggregatedLayout.addComponent(yLabel);
		flotAggregatedLayout.setComponentAlignment(yLabel, Alignment.MIDDLE_RIGHT);
		
		// flot chart
		buildAggChart("[[0,0]]");	//(FlotUtils.formatRampToFlot(rampValue));
	
//		// chart axis label
//		xLabel = new Label("Time (Seconds)");
//		xLabel.addStyleName("tiny");
//		xLabel.setSizeUndefined();
//		leftCharts.addComponent(xLabel);
//		leftCharts.setComponentAlignment(xLabel, Alignment.TOP_CENTER);

//	/**
//	 * INDIVIDUAL RESPONSE CHART
//	 */
//		flotIndividualLayout = new HorizontalLayout();
//		flotIndividualLayout.setSpacing(false);
//		flotIndividualLayout.setWidth("100%");
//		vert.addComponent(flotIndividualLayout);
//		
//		// chart axis label
//		yLabel = new Label("Individual<br>Response Times", ContentMode.HTML);
//		yLabel.addStyleName("tiny");
//		yLabel.setWidth(4.5f, Unit.EM);
//		flotIndividualLayout.addComponent(yLabel);
//		flotIndividualLayout.setComponentAlignment(yLabel, Alignment.MIDDLE_RIGHT);
//		
//		// flot chart
//		buildIndChart("[[0,0]]");	//(FlotUtils.formatRampToFlot(rampValue));
//	
//		// chart axis label
//		xLabel = new Label("Time (Seconds)");
//		xLabel.addStyleName("tiny");
//		xLabel.setSizeUndefined();
//		vert.addComponent(xLabel);
//		vert.setComponentAlignment(xLabel, Alignment.TOP_CENTER);
    }

    
    List<Component> actionChartLayouts = new ArrayList<Component>();
    List<Component> actionPanelLayouts = new ArrayList<Component>();
    LabelDataPair aggLabelDataPair;
	private VerticalLayout rightCharts;
	public VerticalLayout panelIndResponses;
	boolean firstrun=true;
    public void buildIndActionChartAndPanelLayout(List<TRT> actionsSelected){ 	//List<Integer>
    	if(!firstrun){
    		// charts ind actions
    		actionChartLayouts.clear();
    		rightvert.removeComponent(rightCharts); //chartsHL
    		
    		// panel ind actions
    		actionPanelLayouts.clear();
    		responsePanelVert.removeAllComponents();
//    		responsePanelVert.removeComponent(aggLabelDataPair);
//            responsePanelVert.removeComponent(panelIndResponses);
    	}
    	//charts
    	rightCharts = new VerticalLayout();
    	rightCharts.setMargin(false);
    	rightCharts.setSpacing(false);
    	rightCharts.setWidth("100%");
    	rightvert.addComponent(rightCharts);
    	rightvert.setExpandRatio(rightCharts, 1);

    	//panel
    	panelIndResponses = new VerticalLayout();
//    	panelIndResponses.setMargin(false);
//    	panelIndResponses.setSpacing(false);
//    	panelIndResponses.setWidth("100%");
    	aggLabelDataPair = new LabelDataPair("Aggregated");
    	responsePanelVert.addComponent(aggLabelDataPair);
    	responsePanelVert.addComponent(panelIndResponses);
    	responsePanelVert.setExpandRatio(panelIndResponses, 1);
        actionPanelLayouts.add(aggLabelDataPair);
    	
    	IndividualActionChartLayout indActionLayout;
    	LabelDataPair lableDataPair;
    	if (actionsSelected!=null){
    		for(TRT action : actionsSelected){	//for (int i=0; i<actionsSelected; i++){
    			System.out.println("current action is:" + action);
    			//chart
    			indActionLayout = new IndividualActionChartLayout(action.getAction());	//(Integer.toString(i+1));
    			actionChartLayouts.add(indActionLayout);
    			rightCharts.addComponent(indActionLayout);
    			
    			//panel
    			lableDataPair = new LabelDataPair(action.getAction());
    			actionPanelLayouts.add(lableDataPair);
    			panelIndResponses.addComponent(lableDataPair);
    		}    		
    	}
//    	IndividualActionChartLayout indActionLayout;
//    	if (actionIDsSelected!=null){
//    		for(int id : actionIDsSelected){	//for (int i=0; i<actionsSelected; i++){
//    			System.out.println("current action is:" + id);
//    			TRT trt = trtcontainer.getItem(id).getEntity();
//    			indActionLayout = new IndividualActionChartLayout(trt.getAction());	//(Integer.toString(i+1));
//    			actionLayouts.add(indActionLayout);
//    			rightCharts.addComponent(indActionLayout);
//    		}    		
//    	}
    	firstrun=false;
    }

    
	public void buildRampChart(String chartdata) {
		monRampChart = new FlotChart("Virtual Users");
		monRampChart.setWidth("95%");
		monRampChart.setHeight("250px");
		
//		JsonFactory factory = new JreJsonFactory();
//		JsonString jsString = factory.create(data);
//		System.out.println("JSSTRING: " + jsString.asString());
		
//		String data1 = formatDataForGraph(data);		//"[{ data:" + data + "\", lines:{show:true}\", points:{show:true} }]";
//		String d = "[[0,0],[5,5],[10,10],[20,20],[25,25],[30,40],[32,44],[37,50],[40,100],[45,110],[50,110],[55,100],[60,50],[70,20],[80,10]]";
//		String data1 = d + ", \"label\": \"server data\", \"lines\": {\"show\":\"true\", \"fill\":\"true\"}, \"points\":{\"show\":\"true\"}, \"clickable\":\"true\", \"hoverable\":\"true\", \"editable\":\"false\"";	//lines:{show:true, fill:true}, points:{show:true}, 	//formatDataForGraph("[[0,0], [10,30], [20,50]]");
//		String data2 = data + ", \"label\": \"ramp function\", \"lines\":{\"show\":\"true\"}, \"points\":{\"show\":\"true\"}, \"clickable\":\"true\", \"hoverable\":\"true\", \"editable\":\"true\"";
		
		String data = chartdata + rampDataOptions;
		//", \"label\": \"active users\", \"lines\":{\"show\":\"true\"}, \"points\":{\"show\":\"true\"}, \"hoverable\":\"true\" ";
//		"\"clickable\":\"true\", \"editable\":\"true\""
		
		monRampChart.setData("[{ \"data\": " + data + " }]");	//(formatDataForGraph(data));
//		chart.setData("[{ \"data\": " + data1 + "}, { \"data\": " + data2 + " }]");	//("[{ data:[[0,0], [10,30], [20,50]], lines:{show:true}, points:{show:true}, hoverable:true, clickable:true }]");	//(formatDataForGraph(data));
		// options
		String options =
				"{" + 
					"\"series\": { lines: {\"fill\": \"true\", \"fillColor\": \"rgb(77, 167, 77)\"}},"	+	//, points: {show: true} }" +
					"\"crosshair\": {\"mode\": \"x\"}, " +
//					"\"legend\": { \"position\": \"nw\" }, " +
					"\"xaxis\": { \"position\": \"bottom\", \"color\": \"rgb(77, 167, 77)\", \"min\":0, \"tickDecimals\": \"0\"}, " +	//, \"axisLabel\": \"x label\"}], " +
					"\"yaxis\": { \"position\": \"left\", \"color\": \"rgb(77, 167, 77)\", \"min\":0, \"tickDecimals\": \"0\"}, " +	//\"axisLabel\": \"y label\", \"position\": \"left\",  'ms'}], " +
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
		monRampChart.setOptions(options);
		
		flotRampLayout.addComponent(monRampChart);
		flotRampLayout.setComponentAlignment(monRampChart, Alignment.MIDDLE_LEFT);
		flotRampLayout.setExpandRatio(monRampChart, 1);
		System.out.println("Data from chart State:\n" + monRampChart.getData().toJson());
	}
	public void buildAggChart(String chartdata) {
		monAggChart = new FlotChart("Aggregated Response Time");
		monAggChart.setWidth("95%");
		monAggChart.setHeight("225px");
		String data = chartdata + aggDataOptions;
		monAggChart.setData("[{ \"data\": " + data + " }]");	//(formatDataForGraph(data));		
		// options
		String options =
				"{" + 
					"\"series\": { lines: {\"fill\": \"true\"}},"	+	//, points: {show: true} }" +
					"\"crosshair\": {\"mode\": \"x\"}, " +
//					"\"legend\": { \"position\": \"nw\" }, " +
					"\"xaxis\": { \"position\": \"bottom\", \"min\":0, \"tickDecimals\": \"0\"}, " +	//, \"axisLabel\": \"x label\"}], " +
					"\"yaxis\": { \"position\": \"left\", \"min\":0, \"minTickSize\": \"0.2\", \"tickDecimals\": \"1\", \"tickFormatter\": \"secondFormatter\"}, " +	//, \"tickDecimals\": \"0\" \"axisLabel\": \"y label\", \"position\": \"left\",  'ms'}], " +
					"\"grid\": { " +
						"\"clickable\": \"false\"," +
						"\"hoverable\": \"true\"," +
						"\"editable\":\"false\"," +

					"}" +
				"}";
		monAggChart.setOptions(options);
		
		flotAggregatedLayout.addComponent(monAggChart);
		flotAggregatedLayout.setComponentAlignment(monAggChart, Alignment.MIDDLE_LEFT);
		flotAggregatedLayout.setExpandRatio(monAggChart, 1);
		System.out.println("Data from chart State:\n" + monAggChart.getData().toJson());
	}
	public void buildIndChart(String chartdata) {
		monIndChart = new FlotChart("Individual Action Response");
		monIndChart.setWidth("90%");
		monIndChart.setHeight("250px");
		String data = chartdata + indDataOptions;
		monIndChart.setData("[{ \"data\": " + data + " }]");	//(formatDataForGraph(data));		
		// options
		String options =
				"{" + 
					"\"crosshair\": {\"mode\": \"x\"}, " +
					"\"legend\": { \"position\": \"nw\" }, " +
					"\"xaxis\": { \"position\": \"bottom\", \"min\":0, \"tickDecimals\": \"0\"}, " +	//, \"axisLabel\": \"x label\"}], " +
					"\"yaxis\": { \"position\": \"left\", \"min\":0, \"tickDecimals\": \"0\"}, " +	//\"axisLabel\": \"y label\", \"position\": \"left\",  'ms'}], " +
					"\"grid\": { " +
						"\"clickable\": \"false\"," +
						"\"hoverable\": \"true\"," +
						"\"editable\":\"false\"," +

					"}" +
				"}";
		monIndChart.setOptions(options);
		
		flotIndividualLayout.addComponent(monIndChart);
		flotIndividualLayout.setComponentAlignment(monIndChart, Alignment.MIDDLE_LEFT);
		flotIndividualLayout.setExpandRatio(monIndChart, 1);
		System.out.println("Data from chart State:\n" + monIndChart.getData().toJson());
	}

    
	public void buildMbpetTerminalOutput(){
        panel = new Panel("Terminal style");
        panel.setHeight("325px");
//        panel.setIcon(testIcon.get());
        panel.addStyleName("terminalcolor");
//        panel.addStyleName("well");
//        panel.setContent(panelContent());
        
        vert.addComponent(panel);
		
		terminallayout = new VerticalLayout();
		terminallayout.setMargin(false);
		terminallayout.setSpacing(false);
//        terminallayout.setSizeFull();
        terminallayout.addStyleName("terminal-background");
        
        Label label = new Label("mbpet>...");
        label.addStyleName("tiny");
        label.addStyleName("terminal-font");
        terminallayout.addComponent(label);
        
        panel.setContent(terminallayout);
        
	}
	
	public void buildMbpetTerminal(){
		// theme button
		CssLayout css = new CssLayout();
		themeList = Arrays.asList(themes);
        themeBox = new ComboBox("", themeList);
//        themeBox.setContainerDataSource(themeList);
//        modeBox.setWidth(100.0f, Unit.PERCENTAGE);
        themeBox.setWidth(9, Unit.EM);
        themeBox.addStyleName("tiny");
//        themeBox.setInputPrompt("No style selected");
        themeBox.setFilteringMode(FilteringMode.CONTAINS);
        themeBox.setImmediate(true);
        themeBox.setNullSelectionAllowed(false);
        themeBox.setValue(themeList.get(0));        
        themeBox.addValueChangeListener(new ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
            	AceUtils.setAceTheme(editor, event.getProperty().getValue().toString());
            }
        });
        css.addComponent(themeBox);
		vert.addComponent(css);	//themeBox
		vert.setComponentAlignment(css, Alignment.TOP_LEFT);
		
		
		// Ace Editor
		editor = new AceEditor();		
		// use static hosted files for theme, mode, worker
//					editor.setThemePath("/static/ace");
//					editor.setModePath("/static/ace");
//					editor.setWorkerPath("/static/ace");
		editor.setWidth(25, Unit.EM);
		editor.setHeight("225px");
//		editor.setReadOnly(true); 
		editor.setMode(AceMode.python);
		editor.setTheme(AceTheme.twilight);	
		editor.setWordWrap(true);
//		Collection<String> editorSource = new ArrayList<String>();
//		editor.setPropertyDataSource(editorSource);
//				setEditorMode(fileFormat);
//				editor.setUseWorker(true);
//				editor.setWordWrap(false);
//				editor.setShowInvisibles(false);
//				System.out.println(editor.getValue());
		vert.addComponent(editor);
		vert.setExpandRatio(editor, 1);
	}
	
    private Component responseTimes() {
        responsePanelVert = new VerticalLayout();
        //vert.setSizeFull();
        responsePanelVert.setMargin(true);
//        vert.setHeight("6em");
        //vert.setWidth("8em");
//        LabelDataPair aggLabelDataPair = new LabelDataPair("Aggregated");
//        responsePanelVert.addComponent(aggLabelDataPair);
//        actionPanelLayouts.add(aggLabelDataPair);
        
//        Label header = new Label("<b>Response Times</b>", ContentMode.HTML);
////        header.addStyleName("monitoringpanel-header");
//        vert.addComponent(header);
//        vert.setComponentAlignment(header, Alignment.TOP_CENTER);
             
        // aggregated
//        HorizontalLayout row = new HorizontalLayout();
//        row.setWidth("100%");
//        //layout.setSpacing(true);
//        //Label content = new Label("Suspendisse dictum feugiat nisl ut dapibus. Mauris iaculis porttitor posuere. Praesent id metus massa, ut blandit odio.");
//        
//        Label responseTimesLabel = new Label("Aggregated");
//        responseTimesLabel.addStyleName("small");
//        
//        aggregatedResponse = new Label();
//        aggregatedResponse.setSizeUndefined();
//        aggregatedResponse.addStyleName("bold");
//        aggregatedResponse.addStyleName("small");
//        
//        row.addComponents(responseTimesLabel, aggregatedResponse);
//        row.setComponentAlignment(responseTimesLabel, Alignment.MIDDLE_LEFT);
//        row.setComponentAlignment(aggregatedResponse, Alignment.MIDDLE_RIGHT);
////        row.setExpandRatio(responseTimesLabel, 1);
////        row.setExpandRatio(averageresponse, 2);
//        vert.addComponent(new LabelDataPair("Aggregated"));

        
//        row = new HorizontalLayout();
//        row.setWidth("100%");
//        
//        responseTimesLabel = new Label("Min/Max");
//        responseTimesLabel.addStyleName("small");
//        
//        minmaxresponse = new Label();
//        minmaxresponse.setSizeUndefined();
//        minmaxresponse.addStyleName("bold");
//        minmaxresponse.addStyleName("small");
//        
//        row.addComponents(responseTimesLabel, minmaxresponse);
//        row.setComponentAlignment(responseTimesLabel, Alignment.MIDDLE_LEFT);
//        row.setComponentAlignment(minmaxresponse, Alignment.MIDDLE_RIGHT);
////        row.setExpandRatio(responseTimesLabel, 1);
////        row.setExpandRatio(minmaxresponse, 2);
//        vert.addComponent(row);
        
        return responsePanelVert;
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
    		Label slave = new Label("Slave" + i + " - <b>" + status + "</b>", ContentMode.HTML);	//statuses[i]);  
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
    		
    		Label slave = null;
    		if (slaveresults[i].contains(String.valueOf(i+1)))
    			slave = new Label(slaveresults[i] + " - <b>Generating</b>", ContentMode.HTML);	//statuses[i]);  "Slave " + (i) + " - <b>" + slaveresults[i] + "</b>"
//			else 
//				slave = new Label("Slave" + (i+1) + " - <b>Connected</b>", ContentMode.HTML);	//statuses[i]);
    		slave.addStyleName("small");
    		slavevert.addComponent(slave);        	
      	}    
    }
    public void disconnectSlaveMonitoringInfo(int numslaves) {
    	slavevert.removeAllComponents();
    	
    	for (int i=1; i<numslaves+1; i++) {
    		Label slave = new Label("Slave" + i + " - <b>" + "Disconnected" + "</b>", ContentMode.HTML);	//statuses[i]);  
    		slave.addStyleName("small");
    		slavevert.addComponent(slave);        	
      	}    
    }
    
    public void updateFields(String[] fields) {
    //(String averageresponse, String minmaxresponse, String sent, String received, String throughput, String targetuser, String slave ) {
    	
//    	this.aggregatedResponse.setValue(fields[0]);
//    	this.minmaxresponse.setValue(fields[1]);
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


    StringBuilder sb = new StringBuilder();
	public void insertDataToEditor(String message) {
        Label label = new Label("mbpet>" + message);
        label.addStyleName("tiny");
        label.addStyleName("terminal-font");
        terminallayout.addComponent(label);

//		vert.addComponent(new Label(message));
//		sb.append("\n");
//		sb.append(message);
//		editor.setValue(sb.toString());
	}

	
//    int yRamp, x, prevYRamp = 0;
//    int yAgg, prevYAgg = 0;
//    int yInd, prevYInd = 0;
	int x = 0;
	DecimalFormat df = new DecimalFormat("#.##");
	public void updateChartsAndPanels(int users, HashSet<ActionResponse> responseset) {	//,int aggResp, int indResp
		if (x<1){	//draw point (0,0) on graph
			monRampChart.update(0,0);			// update the js code to effect the chart 
			monAggChart.update(0,0);
//			monIndChart.update(0,0);
			for(Component c : actionChartLayouts) {
				((IndividualActionChartLayout) c).getChart().update(0, 0);	//monIndChart
			}
		}
		x += 1; 	//increase time count by 1 second
		

		// update RAMP
		monRampChart.setY(users);	//yRamp
		System.out.println("sent int:" + users);
		System.out.println("New point:" + x + "," + users);
		monRampChart.addNewData(x, monRampChart.getY());		// update the server side data	- this first command WAS causing memory overload!
		monRampChart.update(x, monRampChart.getY());			// update the js code to effect the chart 
		System.out.println(monRampChart.getData().toJson());
		
		
		// update AGG and IND responses
		System.out.println("HashSet of Responses is:" + responseset);
		for(ActionResponse act : responseset){
			if(act.getTitle().contains("Aggregated Response Time")){
				if(act.getTotalResponseTime() == 0){
					// update AGG
					monAggChart.setY(0.0);	//yAgg
					monAggChart.addNewData(x, monAggChart.getY());		// update the server side data	- this first command WAS causing memory overload!
					monAggChart.update(x, monAggChart.getY());			// update the js code to effect the chart 
					//panel
//					this.aggregatedResponse.setValue( String.valueOf(monAggChart.getY()) + " s" );
					for (Component c : actionPanelLayouts){
						System.out.println("trying to match aggregated:" + ((LabelDataPair) c).getName() + " - " + act.getTitle());
						if(((LabelDataPair) c).getName().contains("Aggregated")){
							((LabelDataPair) c).setDataValue(String.valueOf(monAggChart.getY()) + " s" );	//monIndChart						
							break;
						}		
					}
				} else {
					monAggChart.setY(Double.parseDouble(df.format(act.getAverage())) );	//yAgg
					monAggChart.addNewData(x, monAggChart.getY());		// update the server side data	- this first command WAS causing memory overload!
					monAggChart.update(x, monAggChart.getY());			// update the js code to effect the chart 
					//panel
//					this.aggregatedResponse.setValue( String.valueOf(monAggChart.getY()) + " s" );
					// update panel labels
					for (Component c : actionPanelLayouts){
						System.out.println("trying to match aggregated:" + ((LabelDataPair) c).getName() + " - " + act.getTitle());
						if(((LabelDataPair) c).getName().contains("Aggregated")){
							((LabelDataPair) c).setDataValue(String.valueOf(monAggChart.getY()) + " s" );	//monIndChart						
							break;
						}		
					}
				}
			} else {
				// update IND action response charts
				for(Component c : actionChartLayouts) {
//					IndividualActionChartLayout cl = (IndividualActionChartLayout) c;
					if(((IndividualActionChartLayout) c).getTitle().equals(act.getTitle())){
						((IndividualActionChartLayout) c).updateChart(x, act.getCurrentResponseTime());	//monIndChart						
					}
//					if(((IndividualActionChartLayout) c).getTitle().equals(act.getTitle())){
//						((IndividualActionChartLayout) c).updateChart(x, act.getCurrentResponseTime());	//monIndChart						
//					}
//					if(((LabelDataPair) c).getName().equals(act.getTitle())){
//						((LabelDataPair) c).setDataValue(String.valueOf(act.getAverage()) + " s" );	//monIndChart						
//					}
				}
				// update panel labels
				for (Component c : actionPanelLayouts){
					System.out.println("Panel action names:" + ((LabelDataPair) c).getName() + " - " + act.getTitle());
					if(((LabelDataPair) c).getName().equals(act.getTitle())){
						((LabelDataPair) c).setDataValue(df.format(act.getAverage()) + " s" );		//String.valueOf(act.getAverage()) + " s" );	//monIndChart						
					}		
//					if (){
//						this.aggregatedResponse.setValue( String.valueOf(monAggChart.getY()) + " s" );
//					}
				}
			}
		}
//		// update AGG
//		monAggChart.setY(users);	//yAgg
//		monAggChart.addNewData(x, monAggChart.getY());		// update the server side data	- this first command WAS causing memory overload!
//		monAggChart.update(x, monAggChart.getY());			// update the js code to effect the chart 
//		
//		// update IND
//		for(Component v : actionLayouts) {
//			((IndividualActionChartLayout) v).updateChart(x, users);	//monIndChart
//		}
//		yInd = users;
//		monIndChart.addNewData(x, yInd);		// update the server side data	- this first command WAS causing memory overload!
//		monIndChart.update(x, yInd);			// update the js code to effect the chart 
////			data.push([x, y]);
////			prevY = y;
	}
	
	
	public void refreshCharts(){		//FlotChart newChart
		
		// RAMP chart
		flotRampLayout.removeComponent(monRampChart);
		monRampChart.setY(0); x=0; monRampChart.setPrevY(0);	//yRamp=0; x=0; prevYRamp=0;
		buildRampChart("[[0,0]]");
		
		// AGG chart
		flotAggregatedLayout.removeComponent(monAggChart);
		monAggChart.setY(0); x=0; monAggChart.setPrevY(0);	//yAgg=0; x=0; prevYAgg=0;
		buildAggChart("[[0,0]]");
		
		// IND chart
		for(Component v : actionChartLayouts) {
			((IndividualActionChartLayout) v).rebuildChart();	//(monIndChart);
		}
//		flotIndividualLayout.removeComponent(monIndChart);
//		yInd=0; x=0; prevYInd=0;
//		buildIndChart("[[0,0]]");
		
		
//		usersChart = new FlotChart();
//		initChartData("[[0,0]]");
//		usersChart.setData("[{ \"data\": " + "[[0,0]]" + dataOptions + " }]"); // update server side	
//		usersChart.reset("[{ \"data\": " + "[[0,0]]" + dataOptions + " }]");	// update the js code to effect the chart 
////				usersChart.addNewData(x, y);	// update the server side data
////				usersChart.update(x, y);		// update the js code to effect the chart 
//		System.out.println(usersChart.getData().toJson());
	}
	
	
	
	
	
//	public void resetChart() {
//		monRampChart.setData("[{ \"data\": " + "[[0,0]]" + dataOptions + " }]"); // update server side	
////
//		yRamp = 0;
//		x = 0 ;
//		prevYRamp = 0;
//		
//		monRampChart.reset("[{ \"data\": " + "[[0,0]]" + dataOptions + " }]");	// update the js code to effect the chart 
////		usersChart.addNewData(x, y);	// update the server side data
////		usersChart.update(x, y);		// update the js code to effect the chart 
//		System.out.println(monRampChart.getData().toJson());
//		
////		System.err.println("usersChart.getData()>" + usersChart.getData().toJson());
////		if (!usersChart.getData().toJson().equals("[{ \"data\": " + "[[0,0]]" + dataOptions + " }]")) {
////			System.err.println("RESETTING FLOTCHART");
////			FlotChart newchart = new FlotChart();
////			flotLayout.replaceComponent(usersChart, newchart);			
////			newchart = usersChart;
////		}
//		
////        currentReportsComponent = newReportsComponent;
////        
////		// flot chart
//////		firstbuttonAction();
////		buildFlotChart("[[0,0]]");	//(FlotUtils.formatRampToFlot(rampValue));
////		flotLayout.addComponent(usersChart);
////		flotLayout.setComponentAlignment(usersChart, Alignment.MIDDLE_LEFT);
////		flotLayout.setExpandRatio(usersChart, 1);
////		System.out.println("Data from chart State:\n" + usersChart.getData().toJson());
//		
////		buildFlotChart("[[0,0]]");
////		usersChart.setData("[{ \"data\": " + "[[0,0]]" + dataOptions + " }]");		
//	}
	
	
	
//	public void fetchNewData(int rounds) {
//	int y;
//	for (int i=0; i<rounds; i++) {
//		if (prevY <= 150) {
//			y = prevY + 10; 
//		} else {
//			y = prevY - 75; 
//		}
//		if (x<1)
//			monRampChart.update(0, 0);			// update the js code to effect the chart 
//
//		x += 1; 	
//
//		monRampChart.addNewData(x, y);	// update the server side data
//		monRampChart.update(x, y);			// update the js code to effect the chart 
////		System.out.println(updatesChart.getData().toJson());
////		data.push([x, y]);
//		prevY = y;
////		prevX = x;
//	}
//}
	
}

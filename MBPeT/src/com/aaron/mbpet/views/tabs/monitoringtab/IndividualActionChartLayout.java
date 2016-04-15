package com.aaron.mbpet.views.tabs.monitoringtab;

import com.aaron.mbpet.components.flot.FlotChart;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

public class IndividualActionChartLayout extends VerticalLayout {

	public String title;
	private HorizontalLayout flotIndividualLayout;
	private FlotChart monIndChart;
	String indDataOptions;	// = ", \"label\": \"individual response times\", \"lines\":{\"show\":\"true\"}, \"points\":{\"show\":\"true\"}, \"hoverable\":\"true\" ";
	private Label chartTitle;

	public IndividualActionChartLayout(String title){
		this.title = title;
		indDataOptions = ", \"color\": \"rgb(203, 75, 75)\", \"label\": \"" + title + "\", \"lines\":{\"show\":\"true\", \"fill\":\"true\"}, \"hoverable\":\"true\" ";

		initLayout();
	}
	
	public void initLayout(){
		/**
		 * INDIVIDUAL RESPONSE CHART
		 */
//		flotIndividualLayout = new HorizontalLayout();
//		flotIndividualLayout.setSpacing(false);
//		flotIndividualLayout.setWidth("100%");
		
		setSpacing(false);
		setWidth("100%");
		
		chartTitle = new Label("<b>" + title + "</b>", ContentMode.HTML);
		chartTitle.addStyleName("tiny");
		addComponent(chartTitle);
		setComponentAlignment(chartTitle, Alignment.BOTTOM_CENTER);
		
		flotIndividualLayout = new HorizontalLayout();
		flotIndividualLayout.setWidth("100%");
		flotIndividualLayout.setSpacing(false);
		addComponent(flotIndividualLayout);
		
		// flot chart
		buildIndChart("[[0,0]]");	//(FlotUtils.formatRampToFlot(rampValue));

		// chart axis label
		Label yLabel = new Label(" (sec)");	//(title.replaceAll("_", " "), ContentMode.HTML);
		yLabel.addStyleName("tiny");
		yLabel.setWidth(4.5f, Unit.EM);
		flotIndividualLayout.addComponent(yLabel);
		flotIndividualLayout.setComponentAlignment(yLabel, Alignment.MIDDLE_RIGHT);
		
		
//		// chart axis label
//		Label xLabel = new Label("Time (Seconds)");
//		xLabel.addStyleName("tiny");
//		xLabel.setSizeUndefined();
		
//		addComponent(flotIndividualLayout);
//		addComponent(xLabel);
//		setComponentAlignment(xLabel, Alignment.TOP_CENTER);
	}

	public void buildIndChart(String chartdata) {
		monIndChart = new FlotChart(title);
		monIndChart.setWidth("100%");
		monIndChart.setHeight("150px");
		String data = chartdata + indDataOptions;
		monIndChart.setData("[{ \"data\": " + data + " }]");	//(formatDataForGraph(data));		
		// options
		String options =
			"{" +
				"\"series\": { lines:{\"fill\":\"true\", \"fillColor\":\"rgb(203, 75, 75)\"}},"	+	//, points: {show: true} }" +
					"\"crosshair\": {\"mode\": \"x\"}, " +
					"\"legend\": { \"show\":\"true\", \"position\": \"nw\" }, " +
					"\"xaxis\": { \"position\": \"bottom\", \"min\":0, \"tickDecimals\": \"0\"}, " +	//, \"axisLabel\": \"x label\"}], " +
					"\"yaxis\": { \"position\": \"right\", \"min\":0, \"color\": \"rgb(203, 75, 75)\", \"minTickSize\": \"0.2\", \"tickDecimals\": \"1\"}, " +	//\"axisLabel\": \"y label\", \"position\": \"left\",  'ms'}], " +
					"\"grid\": { " +
						"\"clickable\": \"false\"," +
						"\"hoverable\": \"true\"," +
						"\"editable\":\"false\"," +

					"}" +
			"}";
		monIndChart.setOptions(options);
		
		flotIndividualLayout.addComponentAsFirst(monIndChart);
		flotIndividualLayout.setComponentAlignment(monIndChart, Alignment.MIDDLE_LEFT);
		flotIndividualLayout.setExpandRatio(monIndChart, 1);
		//System.out.println("Data from chart State:\n" + monIndChart.getData().toJson());
		
//		// chart axis label
//		Label yLabel = new Label(title + " search google(car)", ContentMode.HTML);
//		yLabel.addStyleName("tiny");
//		yLabel.setWidth(4.5f, Unit.EM);
//		addComponent(yLabel);
//		setComponentAlignment(yLabel, Alignment.MIDDLE_RIGHT);
	}
	
	public FlotChart getIndActionChart(){
		return monIndChart;
	}
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}

	public void rebuildChart(){	//(FlotChart monIndChart) {
		flotIndividualLayout.removeComponent(monIndChart);
		monIndChart.setY(0); monIndChart.setX(0); monIndChart.setPrevY(0);	//yInd=0; x=0; prevYInd=0;
		buildIndChart("[[0,0]]");		
	}

	public void updateChart(int x, double y) {	//(FlotChart monIndChart) {
		monIndChart.setY(y);	//yInd = users;
		monIndChart.addNewData(x, y);		// update the server side data	- this first command WAS causing memory overload!
		monIndChart.update(x, y);		
	}
	
	public FlotChart getChart(){
		return monIndChart;
	}
}

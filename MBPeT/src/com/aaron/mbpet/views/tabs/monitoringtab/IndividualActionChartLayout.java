package com.aaron.mbpet.views.tabs.monitoringtab;

import com.aaron.mbpet.components.flot.FlotChart;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

public class IndividualActionChartLayout extends HorizontalLayout {

	public String title;
	private HorizontalLayout flotIndividualLayout;
	private FlotChart monIndChart;
	String indDataOptions;	// = ", \"label\": \"individual response times\", \"lines\":{\"show\":\"true\"}, \"points\":{\"show\":\"true\"}, \"hoverable\":\"true\" ";

	public IndividualActionChartLayout(String title){
		this.title = title;
		indDataOptions = ", \"color\": \"rgb(203, 75, 75)\", \"label\": \"" + title + "\", \"lines\":{\"show\":\"true\"}, \"points\":{\"show\":\"true\"}, \"hoverable\":\"true\" ";

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
		
		// flot chart
		buildIndChart("[[0,0]]");	//(FlotUtils.formatRampToFlot(rampValue));

		// chart axis label
		Label yLabel = new Label(title.replaceAll("_", " "), ContentMode.HTML);
		yLabel.addStyleName("tiny");
		yLabel.setWidth(4.5f, Unit.EM);
		addComponent(yLabel);
		setComponentAlignment(yLabel, Alignment.MIDDLE_RIGHT);
		
		
		// chart axis label
		Label xLabel = new Label("Time (Seconds)");
		xLabel.addStyleName("tiny");
		xLabel.setSizeUndefined();
		
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
				"\"series\": { lines: {\"fill\": \"true\", \"fillColor\": \"rgb(203, 75, 75)\"}},"	+	//, points: {show: true} }" +
					"\"crosshair\": {\"mode\": \"x\"}, " +
					"\"legend\": { \"position\": \"nw\" }, " +
					"\"xaxis\": { \"position\": \"bottom\", \"min\":0, \"tickDecimals\": \"0\"}, " +	//, \"axisLabel\": \"x label\"}], " +
					"\"yaxis\": { \"position\": \"left\", \"min\":0, \"color\": \"rgb(203, 75, 75)\", \"minTickSize\": \"0.5\", \"tickDecimals\": \"1\"}, " +	//\"axisLabel\": \"y label\", \"position\": \"left\",  'ms'}], " +
					"\"grid\": { " +
						"\"clickable\": \"false\"," +
						"\"hoverable\": \"true\"," +
						"\"editable\":\"false\"," +

					"}" +
			"}";
		monIndChart.setOptions(options);
		
		addComponentAsFirst(monIndChart);
		setComponentAlignment(monIndChart, Alignment.MIDDLE_LEFT);
		setExpandRatio(monIndChart, 1);
		System.out.println("Data from chart State:\n" + monIndChart.getData().toJson());
		
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
		removeComponent(monIndChart);
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

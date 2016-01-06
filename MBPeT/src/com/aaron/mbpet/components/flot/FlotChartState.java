/**
 * State Class. Represents state of JsComponent (Java to Javascript)
 */
package com.aaron.mbpet.components.flot;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray; 
import org.json.JSONObject;

import com.vaadin.shared.ui.JavaScriptComponentState;


public class FlotChartState extends JavaScriptComponentState {
	//public List<List<List<Double>>> series = new ArrayList<List<List<Double>>>();

	private JSONArray data;
	private JSONArray seriesOptions;
	private JSONObject options;

	
	public JSONArray getData() {
		return data;
	}
	
	public void setData(JSONArray data) {
		this.data = data;
	}

	public JSONObject getOptions() {
		return options;
	}

	public void setOptions(JSONObject data) {
		this.options = data;
	}
	
	public JSONArray getSeriesOptions() {
		return seriesOptions;
	}
	
	public void setSeriesOptions(JSONArray options) {
		this.seriesOptions = options;
	}
}
/**
 * State Class. Represents state of JsComponent (Java to Javascript)
 */
package com.aaron.mbpet.components.flot;

import java.util.ArrayList;
import java.util.List;

import elemental.json.JsonArray;
import elemental.json.JsonObject;
//import org.json.JSONArray; 
//import org.json.JSONObject;

import com.vaadin.shared.ui.JavaScriptComponentState;


public class FlotChartState extends JavaScriptComponentState {
	//public List<List<List<Double>>> series = new ArrayList<List<List<Double>>>();

	private JsonArray data;
	private JsonArray seriesOptions;
	private JsonObject options;
	
//	private JSONArray data;
//	private JSONArray seriesOptions;
//	private JSONObject options;

	
	public JsonArray getData() {
		return data;
	}
	
	public void setData(JsonArray data) {
		this.data = data;
	}

	public JsonObject getOptions() {
		return options;
	}

	public void setOptions(JsonObject data) {
		this.options = data;
	}
	
	public JsonArray getSeriesOptions() {
		return seriesOptions;
	}
	
	public void setSeriesOptions(JsonArray options) {
		this.seriesOptions = options;
	}
}
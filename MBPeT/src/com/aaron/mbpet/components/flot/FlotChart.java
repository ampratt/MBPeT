/**
 * Server-side component. provide API for interaction with JS component
 */
package com.aaron.mbpet.components.flot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List; 

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.vaadin.annotations.JavaScript;
import com.vaadin.ui.AbstractJavaScriptComponent;
import com.vaadin.ui.JavaScriptFunction;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;

import elemental.json.JsonArray;

@JavaScript({//"https://ajax.googleapis.com/ajax/libs/jquery/1.7.2/jquery.min.js",
	"js/jquery.min.js",
	"js/jquery.flot.js",
	"js/flot_connector.js",
	"js/jquery.flot.crosshair.min.js",
	"js/jquery.flot.mouse.js",
	"js/jquery.flot.JUMlib.js",
	"js/jquery.flot.resize.js",
//	"http://jumjum123.github.io/JUMFlot/javascripts/jquery.flot.JUMlib.js" 
	})
public class FlotChart extends AbstractJavaScriptComponent {

	private static final long serialVersionUID = 2824489559972070152L;
	private JSONArray data;
	private JSONObject options;
	private JSONArray seriesOptions;
	
	@SuppressWarnings("serial")
	public FlotChart() {
        registerRpc(new FlotClickRpc() {
        	@Override
            public void onPlotClick(int seriesIndex, int dataIndex) {
                Notification.show("Clicked on [seriesIndex,dataIndex]: [" + seriesIndex + ", " + dataIndex + "]", Notification.Type.TRAY_NOTIFICATION);
            }
        });
                
        registerRpc(new FlotDropRpc() {
            public void onDataDrop(int seriesIndex, int dataIndex) {    
            }
			@Override
			public void onDataDrop(int seriesIndex, int dataIndex, JSONArray dataPoint, double x1, double y1, JSONArray newData) {
//				Notification.show("Datapoint: [" + seriesIndex + ", " + dataIndex + "] was at location " + dataPoint + 
//		    					"\nWas moved to the new datapoints:  [" + x1 + ", " + y1 + "]" + 
//								"\nnewData is: " + newData, Notification.Type.HUMANIZED_MESSAGE);
//			    getPosAfterDrop();
			    
				// IMPORTANT. Commit new data to state!
				setDataJSON(newData);
				
			    //FOR TESTING - update chart state
			    System.out.println("newData: " + newData);				
			    System.out.println("newData from getData(Flot): " + getData());				
			}
        });
        
        registerRpc(new FlotUpdateRpc() {
			@Override
			public void onDataUpdate(int seriesIndex, int dataIndex) {			
//				Notification.show("you asked for updates...", Type.HUMANIZED_MESSAGE);
			}
			@Override
			public void onDataUpdate(JSONArray newData) {
				setDataJSON(newData);
				
				Notification.show("from registerRpc");

			    //FOR TESTING - update chart state
			    System.out.println("newData: " + newData);				
			    System.out.println("newData from getData(Flot): " + getData());				}
		});
        
        addFunction("onDataUpdate", new JavaScriptFunction() {
            //@Override
            public void call(JsonArray arguments) {
                int seriesIndex = (int) arguments.getNumber(0);
                int dataIndex = (int) arguments.getNumber(1);
                double newData = (double) arguments.getNumber(2);
            }
//			@Override
			public void call(JSONArray arguments) throws JSONException {
			}
        });
        
        
        addFunction("onDataDrop", new JavaScriptFunction() {
            //@Override
            public void call(JsonArray arguments) {
                int seriesIndex = (int) arguments.getNumber(0);
                int dataIndex = (int) arguments.getNumber(1);
                double dataPoint = (double) arguments.getNumber(2);
                double x1 = (double) arguments.getNumber(3);
                double y1 = (double) arguments.getNumber(4);
                double newData = (double) arguments.getNumber(5);
//                Notification.show("Dropped on [" + seriesIndex + ", " + dataIndex + "]");
            }
//			@Override
			public void call(JSONArray arguments) throws JSONException {
				// TODO Auto-generated method stub
			}
        });
        
        
        addFunction("onPlotClick", new JavaScriptFunction() {
            //@Override
            public void call(JsonArray arguments) {
                int seriesIndex = (int) arguments.getNumber(0);
                int dataIndex = (int) arguments.getNumber(1);
                double dataPoint = (double) arguments.getNumber(2);
                double x1 = (double) arguments.getNumber(3);
                double y1 = (double) arguments.getNumber(4);
                double newData = (double) arguments.getNumber(5);
//                Notification.show("Clicked on [" + seriesIndex + ", " + dataIndex + "]");
            }
//			@Override
			public void call(JSONArray arguments) throws JSONException {
				// TODO Auto-generated method stub
			}

        });
    }
	
	
//	public void update() {
//		callFunction("update");
//	}
	public void update(int x, int y) {
		callFunction("update", x, y);
	}
	
	public void getCurrentData() {
		callFunction("getCurrentData");
//		setDataJSON(callFunction("getCurrentData"));
//		Notification.show("from getCurrentData");
	}
	
	
	
//	public void getPosAfterDrop() {
//		callFunction("getPosAfterDrop");
//	}
	
//	public void getSeriesData() {
//		callFunction("getSeriesData");
//		//System.out.println( );
//	}
	
//    public void highlight(int seriesIndex, int dataIndex) {
//        getRpcProxy(FlotHighlightRpc.class).highlight(seriesIndex, dataIndex);
//        callFunction("highlight", seriesIndex, dataIndex);
//    }
	
    @Override
    public FlotChartState getState() {
    	return (FlotChartState) super.getState();
    }
    
    /*
     * DATA
     */
	public void setData(String source) {
		try {
			data = new JSONArray(source);
			getState().setData(data);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	public JSONArray getData(){
		data = getState().getData();
		return data;
	}
	
	public void setDataJSON(JSONArray source) {
		data = source;
		getState().setData(data);
	}
	
	public void addNewData(String source) {
		data = getData();
		data.put(source);
		setData(data);
	}
	public void addNewData(int x, int y) {
		String newData = "[" + x +"," + y + "]";
		System.out.println("data before update: " + data);
		
		// JSON data must be manually formatted to add the new data inside the correct brackets
		setData( putNewData(newData) );		// data.put(newData);
		System.out.println("data after update:  " + data);
		
		// call js connector update function
//		update(x, y);
	}
	
    /*
     * OPTIONS
     */
	public void setOptions(String opt) {
		try {
			options = new JSONObject(opt);
			getState().setOptions(options);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	public JSONObject getOptions(){
		return options;	
	}
	
	
	/*
	 * SERIES OPTIONS
	 */
	public void setSeriesOptions(String source) {
		try {
			seriesOptions = new JSONArray(source);
			getState().setData(seriesOptions);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	public JSONArray getSeriesOptions(){
		return seriesOptions;
	}
	
	
	public String putNewData(String newData) {
		// get current data
		String d = data.toString();
//		System.out.println("this is data.toString(): " + d);

		// remove outer right brackets - ']}]'
		String deFormatted = d.substring(0, d.length() - 3);
		System.out.println("deFormatted: " + deFormatted);
		
		// add newData and reformat finished data
		String formatted = new StringBuilder().append(deFormatted).append(","+ newData).append("]}]").toString();
		System.out.println("formatted: " + formatted);

		return formatted;
	}
	
//    public void addSeries(double... points) {
//        List<List<Double>> pointList = new ArrayList<List<Double>>();
//        for (int i = 0; i < points.length; i++) {
//            pointList.add(Arrays.asList(Double.valueOf(i),
//                    Double.valueOf(points[i])));
//        }
//
//        getState().series.add(pointList);
//    }
	

}

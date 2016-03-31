package com.aaron.mbpet.services;

import java.io.FileReader;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
//import org.json.simple.JSONArray;
//import org.json.simple.JSONObject;
//import org.json.simple.parser.JSONParser;
//import org.json.simple.parser.ParseException;


public class JsonDecoderMbpet {

	public JsonDecoderMbpet() {
		// TODO Auto-generated constructor stub
	}
	
	
	public JsonDecoderMbpet(String update){
//		  JSONParser parser = new JSONParser();
//		  
//		    try{
////		    	Object obj = parser.parse(new FileReader(
////	            		"C:\\dev\\workspaceAlternate\\json-demos\\src\\mbpet-message.json"));
//		       Object obj = parser.parse(update);
//		       JSONObject jsonObject = (JSONObject)obj;
////		       JSONArray array = (JSONArray)obj;
//				
//
//		       // full JSON object
//		       double timestamp = (Double) jsonObject.get("timestamp");
//		       JSONObject summaryObject = (JSONObject) jsonObject.get("summary");
//		       JSONObject valuesObject = (JSONObject) jsonObject.get("values");
//	           Long target_user = (Long) jsonObject.get("target_user");
//	           String slave_name = (String) jsonObject.get("slave_name");
//	           
//	           // decompose internal JSON objects
//	           double net_recv_total = (Double) summaryObject.get("net_recv_total");
//	           double net_send_total = (Double) summaryObject.get("net_send_total");
//	           
//
//		       System.out.println("\n--NEW UPDATE FROM MASTER--");
//
//		       System.out.println("\nThe length of the json object is: " + jsonObject.size());
//		       System.out.println("Full json: " + jsonObject);
//
//	           System.out.println("\nTimestamp: " + timestamp);
//	           System.out.println("Summary: " + summaryObject);
//		           System.out.println("\tReceived total: " + summaryObject.get("net_recv_total"));
//		           System.out.println("\tSent total: " + summaryObject.get("net_send_total"));
//	           System.out.println("Values: " + valuesObject);
//		           System.out.println("\tresp: " + valuesObject.get("resp"));
//		           System.out.println("\tnet_recv: " + valuesObject.get("net_recv"));
//		           System.out.println("\tnet_send: " + valuesObject.get("net_send"));
//		           System.out.println("\terror: " + valuesObject.get("error"));
//	           System.out.println("Target_user: " + target_user);
//	           System.out.println("Slave_name: " + slave_name);
//		
// 
//		    } catch(ParseException pe){
//				
//			       System.out.println("position: " + pe.getPosition());
//			       System.out.println(pe);
//			} 
	}

	public String getMessage(String update) {
//		  JSONParser parser = new JSONParser();
		JSONObject jsonObject = null;
		    try{
//		    	Object obj = parser.parse(new FileReader(
//	            		"C:\\dev\\workspaceAlternate\\json-demos\\src\\mbpet-message.json"));
//		       Object obj = parser.parse(update);
//		    	jsonObject = (JSONObject)obj;
		       JSONTokener tokener = new JSONTokener(update);
		    	jsonObject = new JSONObject(tokener);
		
		    } catch	(JSONException pe) {	//(ParseException pe){
//			       System.out.println("position: " + pe.getPosition());
			       System.out.println(pe);
			} 
		return jsonObject.toString();
	}

	
	String[] keyvalues = new String[8];
	double resp_avg = 0;
	int error_count = 0;
	int throughput = 0;
	int success = 0;
	double net_send_total = 0;
	double net_recv_total = 0;
	String slave_name;
	int target_user = 0;

	public String[] getKeyValues(String update) {
		
//		JSONParser parser = new JSONParser(); 
		JSONTokener tokener; //= new JSONTokener(update);
	    JSONObject jsonObject;// = null;
		List<Double> resplist = new ArrayList<Double>();
//		double resplist[];
		DecimalFormat df = new DecimalFormat("#.##");
		try{
//	       	Object obj = parser.parse(update);
//	       	jsonObject = (JSONObject)obj;
			tokener = new JSONTokener(update);
			jsonObject = new JSONObject(tokener);
			
	       	// full JSON object
//	       	double timestamp = (Double) jsonObject.get("timestamp");
	       	JSONObject summaryObject = (JSONObject) jsonObject.get("summary");
	       	JSONObject valuesObject = (JSONObject) jsonObject.get("values");
	       	target_user = jsonObject.getInt("target_user");
	       	slave_name = (String) jsonObject.get("slave_name");
          
	       	// decompose internal JSON objects
	       	
		       	// "Summary" object
	       		// RESP_AVG
	       		if (summaryObject.has("resp_avg") ) {
	       			resp_avg = (Double) summaryObject.get("resp_avg");
//	       			new DecimalFormat("#.##").format(resp_avg); 
	       		}
	       		// ERROR_COUNT
	       		if (summaryObject.has("error_count") ) 
	       			error_count += summaryObject.getInt("error_count");
	       		
	       		// THROUGHPUT
	       		if (summaryObject.has("throughput") ) 
	       			throughput += summaryObject.getInt("throughput");
	       		
	       		// SUCCESS
	       		success = throughput - error_count;
	       				
	       		// SENT TOTAL
	       		if (summaryObject.has("net_send_total") ) {
	       			net_send_total += (Double) summaryObject.get("net_send_total");
//	       			net_send_total = df.format(net_send_total);
//	       			new DecimalFormat("#.##").format(net_send_total); 
	       		}
	       		// RECEIVE TOTAL
	       		if (summaryObject.has("net_recv_total") ) { 
	       			net_recv_total += (Double) summaryObject.get("net_recv_total");
//	       			new DecimalFormat("#.##").format(net_recv_total); 
				}

//		       	// "Values" object
//		       	JSONArray respArray = (JSONArray) valuesObject.get("resp");
//		       	JSONArray net_recvArray = (JSONArray) valuesObject.get("net_recv");
//		       	JSONArray net_sendArray = (JSONArray) valuesObject.get("net_send");
//		       	JSONArray error = (JSONArray) valuesObject.get("error");
//		       		
//		       		// value "resp" objects
//		       		for (int i=0; i<respArray.length(); i++){	//((Object) resp).getValuesAs(JsonObject.class)) {
//		       			JSONObject action = (JSONObject) respArray.get(i);
////		       			double resp = (Double) action.get("val");
//		       			resplist.add((Double) action.get("val"));
//		       		}
//		       		String minmax = getMinMaxResponse(resplist);
//		       		
//		       		// current update send / receive bandwidth
//		       		JSONObject rObject = (JSONObject) net_recvArray.get(0);
//		       		Double net_recv = (Double) rObject.get("val");
//		       		
//		       		JSONObject sObject = (JSONObject) net_sendArray.get(0);
//		       		Double net_send = (Double) sObject.get("val");
		       		
		       		
		       		
     		keyvalues[0] = String.valueOf(df.format(resp_avg)) + " ms";	//(String)summaryObject.get("resp_avg");
     		keyvalues[1] = "0 ms";	//(String)summaryObject.get("minmax");
     		keyvalues[2] = String.valueOf(throughput);		//(String)summaryObject.get("throughput");
     		keyvalues[3] = String.valueOf(success);		//(String)summaryObject.get("throughput");
     		keyvalues[4] = String.valueOf(error_count);	//(String)summaryObject.get("throughput");
     		if (net_send_total < 1048576) {	// 1024B in K; orig>10000
     			keyvalues[5] = String.valueOf(df.format(net_send_total/1024) + " KB");
     		} else {	//1MB = 1,048,576 bytes
     			keyvalues[5] = String.valueOf(df.format(net_send_total/1048576) + " MB");
 			}
     		if (net_recv_total < 1048576) {
     			keyvalues[6] = String.valueOf(df.format(net_recv_total/1024) + " KB");
     		} else {
     			keyvalues[6] = String.valueOf(df.format(net_recv_total/1048576) + " MB");
 			}
			keyvalues[7] = String.valueOf(target_user);
//     		keyvalues[7] = slave_name;

//	       	try {
//			} catch (NullPointerException e) {
//				keyvalues[0] = "0";
//				keyvalues[1] = "0";	//(String)summaryObject.get("minmax");
//				keyvalues[2] = String.valueOf(net_send_total);
//				keyvalues[3] = String.valueOf(net_recv_total);
//				keyvalues[4] = (String)summaryObject.get("throughput");
//				keyvalues[5] = String.valueOf(target_user);
//				keyvalues[6] = slave_name;
//				e.printStackTrace();
//			}
	       	
		} catch (JSONException e) {
          e.printStackTrace();
      }

		return keyvalues;
	}

	
	String[] slavearray;
	public String[] getSlaves(String update, int numslaves) {
		
		JSONTokener tokener; //= new JSONTokener(update);
	    JSONObject jsonObject;// = null;
       	slavearray = new String[numslaves];

		try{
//	       	Object obj = parser.parse(update);
//	       	jsonObject = (JSONObject)obj;
			tokener = new JSONTokener(update);
			jsonObject = new JSONObject(tokener);
			
	       	// full JSON object
	       	slave_name = "";
       		if (jsonObject.has("slave_name") ) 
       			slave_name = (String) jsonObject.get("slave_name");
       		for (int i=0; i<slavearray.length; i++){
       			slavearray[i] = slave_name;       			
       			System.out.println("SLAVE ARRAY " + (i+1) + ": " + slavearray[i]);
       		}
//	       	slavearray[1] = "";
//	       	for (int i=0; i<numslaves; i++) {
//	       		System.out.println("SLAVE ARRAY 2: " + slavearray[i]);	       		
//	       	}
	       	
		} catch (JSONException e) {
          e.printStackTrace();
      }

		return slavearray;
	}
	
	

	public String getMinMaxResponse(List list) {
//	    int a[] = {2,5,3,7,8};
//	    Arrays.sort(a);
//	    int min =a[0];
//	    System.out.println(min);
//	    int max= a[a.length-1];
//	    System.out.println(max);

	    String min = Collections.min(list);
      String max = Collections.max(list);
      	    
	    return min+"/"+max;
	}
	
	
//	public JsonDecoderMbpet(String update){
//		  JSONParser parser = new JSONParser();
//		  
//		    try{
////		    	Object obj = parser.parse(new FileReader(
////	            		"C:\\dev\\workspaceAlternate\\json-demos\\src\\mbpet-message.json"));
//		       Object obj = parser.parse(update);
//		       JSONObject jsonObject = (JSONObject)obj;
////		       JSONArray array = (JSONArray)obj;
//				
//
//		       // full JSON object
//		       double timestamp = (Double) jsonObject.get("timestamp");
//		       JSONObject summaryObject = (JSONObject) jsonObject.get("summary");
//		       JSONObject valuesObject = (JSONObject) jsonObject.get("values");
//	           Long target_user = (Long) jsonObject.get("target_user");
//	           String slave_name = (String) jsonObject.get("slave_name");
//	           
//	           // decompose internal JSON objects
//	           double net_recv_total = (Double) summaryObject.get("net_recv_total");
//	           double net_send_total = (Double) summaryObject.get("net_send_total");
//	           
//
//		       System.out.println("\n--NEW UPDATE FROM MASTER--");
//
//		       System.out.println("\nThe length of the json object is: " + jsonObject.size());
//		       System.out.println("Full json: " + jsonObject);
//
//	           System.out.println("\nTimestamp: " + timestamp);
//	           System.out.println("Summary: " + summaryObject);
//		           System.out.println("\tReceived total: " + summaryObject.get("net_recv_total"));
//		           System.out.println("\tSent total: " + summaryObject.get("net_send_total"));
//	           System.out.println("Values: " + valuesObject);
//		           System.out.println("\tresp: " + valuesObject.get("resp"));
//		           System.out.println("\tnet_recv: " + valuesObject.get("net_recv"));
//		           System.out.println("\tnet_send: " + valuesObject.get("net_send"));
//		           System.out.println("\terror: " + valuesObject.get("error"));
//	           System.out.println("Target_user: " + target_user);
//	           System.out.println("Slave_name: " + slave_name);
//		
//   
//		    } catch(ParseException pe){
//				
//			       System.out.println("position: " + pe.getPosition());
//			       System.out.println(pe);
//			} 
//	}
//
//	public String getMessage(String update) {
//		  JSONParser parser = new JSONParser();
//		  JSONObject jsonObject = null;
//		    try{
////		    	Object obj = parser.parse(new FileReader(
////	            		"C:\\dev\\workspaceAlternate\\json-demos\\src\\mbpet-message.json"));
//		       Object obj = parser.parse(update);
//		       jsonObject = (JSONObject)obj;
//		
//		    } catch(ParseException pe){
//				
//			       System.out.println("position: " + pe.getPosition());
//			       System.out.println(pe);
//			} 
//		return jsonObject.toString();
//	}
//
//	public String[] getKeyValues(String update) {
//		String[] keyvalues = new String[7];
//		
//		JSONParser parser = new JSONParser();
//		JSONObject jsonObject = null;
//		List<Double> resplist = new ArrayList<Double>();
////		double resplist[];
//
//		try{
//	       	Object obj = parser.parse(update);
//	       	jsonObject = (JSONObject)obj;
//	       	
//	       	// full JSON object
//	       	double timestamp = (Double) jsonObject.get("timestamp");
//	       	JSONObject summaryObject = (JSONObject) jsonObject.get("summary");
//	       	JSONObject valuesObject = (JSONObject) jsonObject.get("values");
//	       	Long target_user = (Long) jsonObject.get("target_user");
//	       	String slave_name = (String) jsonObject.get("slave_name");
//           
//	       	// decompose internal JSON objects
//	       	
//		       	// "Summary" object
//		       	double net_send_total = (Double) summaryObject.get("net_send_total");
//		       	double net_recv_total = (Double) summaryObject.get("net_recv_total");
//		       	Long throughput = (Long) summaryObject.get("throughput");
//		       	double resp_avg = (Double) summaryObject.get("resp_avg");
//
//		       	// "Values" object
//		       	JSONArray respArray = (JSONArray) valuesObject.get("resp");
//		       	JSONArray net_recvArray = (JSONArray) valuesObject.get("net_recv");
//		       	JSONArray net_sendArray = (JSONArray) valuesObject.get("net_send");
//		       	JSONArray error = (JSONArray) valuesObject.get("error ");
//		       		
//		       		// value "resp" objects
//		       		for (int i=0; i<respArray.size(); i++){	//((Object) resp).getValuesAs(JsonObject.class)) {
//		       			JSONObject action = (JSONObject) respArray.get(i);
////		       			double resp = (Double) action.get("val");
//		       			resplist.add((Double) action.get("val"));
//		       		}
//		       		String minmax = getMinMaxResponse(resplist);
//		       		
//		       		// current update send / receive bandwidth
//		       		JSONObject rObject = (JSONObject) net_recvArray.get(0);
//		       		Double net_recv = (Double) rObject.get("val");
//		       		
//		       		JSONObject sObject = (JSONObject) net_sendArray.get(0);
//		       		Double net_send = (Double) sObject.get("val");
//		       		
//		       		
//		       		
//	       	try {
//				keyvalues[0] = (String)summaryObject.get("resp_avg");
//				keyvalues[1] = "0";	//(String)summaryObject.get("minmax");
//				keyvalues[2] = String.valueOf(net_send_total);
//				keyvalues[3] = String.valueOf(net_recv_total);
//				keyvalues[4] = (String)summaryObject.get("throughput");
//				keyvalues[5] = String.valueOf(target_user);
//				keyvalues[6] = slave_name;
//			} catch (NullPointerException e) {
//				keyvalues[0] = "0";
//				keyvalues[1] = "0";	//(String)summaryObject.get("minmax");
//				keyvalues[2] = String.valueOf(net_send_total);
//				keyvalues[3] = String.valueOf(net_recv_total);
//				keyvalues[4] = (String)summaryObject.get("throughput");
//				keyvalues[5] = String.valueOf(target_user);
//				keyvalues[6] = slave_name;
//				e.printStackTrace();
//			}
//	       	
//		} catch(ParseException pe){
//			System.out.println("position: " + pe.getPosition());
//			System.out.println(pe);
//		} 
//
//		return keyvalues;
//	}
//
//	public String getMinMaxResponse(List list) {
////	    int a[] = {2,5,3,7,8};
////	    Arrays.sort(a);
////	    int min =a[0];
////	    System.out.println(min);
////	    int max= a[a.length-1];
////	    System.out.println(max);
//
//	    String min = Collections.min(list);
//        String max = Collections.max(list);
//        	    
//	    return min+"/"+max;
//	}

}

package com.aaron.mbpet.services;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.StringTokenizer;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import javax.persistence.Query;

import com.aaron.mbpet.MbpetUI;
import com.aaron.mbpet.domain.Parameters;
import com.aaron.mbpet.domain.TRT;
import com.aaron.mbpet.views.MainView;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.server.Page;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;

public class ParametersUtils {

	static String newline = System.getProperty("line.separator");
	static String space = " ";
	static StringBuilder builder;
	
	static Parameters currParams;
	static JPAContainer<Parameters> parameterscontainer = ((MbpetUI) UI.getCurrent()).getParameterscontainer();
	static JPAContainer<TRT> trtcontainer = ((MbpetUI) UI.getCurrent()).getTrtcontainer();

	public static String insertFormDataToAce(Parameters currparams, String editor){
		
		currParams = currparams;
		builder = new StringBuilder();
		Scanner sc = new Scanner(editor);
		System.out.println("==SCANNING SETTINGS==");

		while (sc.hasNextLine()) {
			String line = sc.nextLine();
			
			if (line.contains("dstat_mode") && line.contains("=")) {
				writeDstatInfo(line);
				
			} else if (line.contains("host") && line.contains("=")) {
				writeQuotedDataInfo(line, "\"", currParams.getHost());		// writeHostInfo(line);
				
			} else if (line.contains("username") && line.contains("=")) {
				writeQuotedDataInfo(line, "'", currParams.getUsername());
				
			} else if (line.contains("password") && line.contains("=")) {
				writeQuotedDataInfo(line, "'", currParams.getPassword());
				
			} else if (line.contains("user_types") && line.contains("=")) {
				writeQuotedDataInfo(line, "\"", currParams.getUser_types());
				
			} else if (line.contains("models_folder") && line.contains("=")) {
				writeQuotedDataInfo(line, "\"", currParams.getModels_folder());
				
			} else if (line.contains("test_report_folder") && line.contains("=")) {
				writeQuotedDataInfo(line, "\"", currParams.getTest_report_folder());

			} else if (line.contains("ip") && line.contains("=")) {
				System.out.println("## handling IP now ##");
				writeQuotedDataInfo(line, "\"", currParams.getIp());
				
			} else if ( (line.contains("test_duration") && line.contains("=")) && !(line.contains("E.g."))  ) {
				writeIntegerInfo(line, currParams.getTest_duration());
				
			} else if ( line.contains("ramp_list") && line.contains("=") && !(line.contains("#ramp_list")) ) {
				writeRampListInfo(line, currParams.getRamp_list());	
				
			} else if ( (line.contains("interval") && line.contains("=")) && !(line.contains("E.g.")) && !(line.startsWith("#")) ) {
				writeIntegerInfo(line, currParams.getMonitoring_interval());
				
			} else if ( (line.contains("mean_user_think_time") && line.contains("=")) && !(line.contains("E.g."))  ) {
				writeIntegerInfo(line, currParams.getMean_user_think_time());
				
			} else if ( (line.contains("standard_deviation") && line.contains("=")) && !(line.contains("E.g."))  ) {
				writeDoubleInfo(line, currParams.getStandard_deviation());
				
			} else if ( (line.contains("TargetResponseTime") && line.contains("=")) && !(line.contains("E.g."))  ) {
				String rtimes = writeTRTListToString(currParams.getTarget_response_times());
				builder.append("TargetResponseTime = " + rtimes).append(newline);
				System.out.println(rtimes);
				
			} else {
				builder.append(line).append(newline);				
			}
		}
		sc.close();
		
		return builder.toString();
	}

	
	public static Parameters commitAceParamData(Parameters currparams, String editor){
		currParams = currparams;
		Scanner sc = new Scanner(editor);
		System.out.println("==Getting Ace Parameter Data==");

		while (sc.hasNextLine()) {
			String line = sc.nextLine();
			
			if (line.contains("dstat_mode") && line.contains("=") && !(line.startsWith("#")) ) {
				if (line.startsWith("#"))
					currparams.setDstat_mode(null);
				else if(line.startsWith("dstat_mode"))
					currparams.setDstat_mode(getDstatInfo(line));
				
			} else if (line.contains("host") && line.contains("=") && !(line.startsWith("#"))) {
				if (line.startsWith("#"))
					currparams.setHost(null);
				else if(line.startsWith("host"))
					currparams.setHost(getQuotedDataInfo(line, "\"", "host"));
				
			} else if (line.contains("username") && line.contains("=") && !(line.startsWith("#"))) {
				if (line.startsWith("#"))
					currparams.setUsername(null);
				else if(line.startsWith("username"))
					currparams.setUsername(getQuotedDataInfo(line, "'", "username"));
				
			} else if (line.contains("password") && line.contains("=") && !(line.startsWith("#"))) {
				if (line.startsWith("#"))
					currparams.setPassword(null);
				else if(line.startsWith("password"))
					currparams.setPassword(getQuotedDataInfo(line, "'", "password"));
				
			} else if (line.contains("user_types") && line.contains("=") && !(line.startsWith("#"))) {
				if (line.startsWith("#"))
					currparams.setUser_types(null);
				else if(line.startsWith("user_types"))
					currparams.setUser_types(getQuotedDataInfo(line, "\"", "user_types"));
				
			} else if (line.contains("models_folder") && line.contains("=") && !(line.startsWith("#"))) {
				if (line.startsWith("#"))
					currparams.setModels_folder(null);
				else if(line.startsWith("models_folder"))
					currparams.setModels_folder(getQuotedDataInfo(line, "\"", "models_folder"));
				
			} else if (line.contains("test_report_folder") && line.contains("=") && !(line.startsWith("#"))) {
				if (line.startsWith("#"))
					currparams.setTest_report_folder(null);
				else if(line.startsWith("test_report_folder"))
					currparams.setTest_report_folder(getQuotedDataInfo(line, "\"", "test_report_folder"));
				
			} else if (line.contains("ip") && line.contains("=") && !(line.startsWith("#"))) {
				if (line.startsWith("#"))
					currparams.setIp(null);
				else if(line.startsWith("ip")){
					System.out.println("## handling IP now ##");
					currparams.setIp(getQuotedDataInfo(line, "\"", "ip"));
				}
								
			} else if ( (line.contains("test_duration") && line.contains("=") && !(line.startsWith("#"))) && !(line.contains("E.g."))  ) {
				if (line.startsWith("#"))
					currparams.setTest_duration(0);
				else if(line.startsWith("test_duration"))
					currparams.setTest_duration(getIntegerInfo(line));
								
			} else if ( line.contains("ramp_list") && line.contains("=") && !(line.contains("#ramp_list")) ) {
				if (line.startsWith("#"))
					currparams.setRamp_list("[(0, 0)]");
				else if(line.startsWith("ramp_list"))
					currparams.setRamp_list(getRampListInfo(line));
				
			} else if ( (line.contains("interval") && line.contains("=")) && !(line.contains("E.g.")) && !(line.startsWith("#")) ) {
				if (line.startsWith("#"))
					currparams.setMonitoring_interval(0);
				else if(line.startsWith("interval"))
					currparams.setMonitoring_interval(getIntegerInfo(line));
				
			} else if ( (line.contains("mean_user_think_time") && line.contains("=")) && !(line.contains("E.g."))  ) {
				if (line.startsWith("#"))
					currparams.setMean_user_think_time(0);
				else if(line.startsWith("mean_user_think_time"))
					currparams.setMean_user_think_time(getIntegerInfo(line));
								
			} else if ( (line.contains("standard_deviation") && line.contains("=")) && !(line.contains("E.g."))  ) {
				if (line.startsWith("#"))
					currparams.setStandard_deviation(0.0);
				else if(line.startsWith("standard_deviation"))
					currparams.setStandard_deviation(getDoubleInfo(line));
												
			} else if ( line.startsWith("TargetResponseTime") ){	//(line.contains("TargetResponseTime") && line.contains("=")) && !(line.startsWith("E.g."))  ) {
//				if (line.startsWith("#")){}
////					currparams.setTarget_response_times(null);
//				else if(line.startsWith("TargetResponseTime"))
					getTRTListFromString(line);

			} else {
			}
		}
		sc.close();
		parameterscontainer.addEntity(currParams);
		
		return parameterscontainer.getItem(currParams.getId()).getEntity();
	}


	



	/**
	 * WRITE DATA TO ACE EDITOR
	 */
	private static void writeQuotedDataInfo(String line, String quoteType, String formData) {
		System.out.println("the current line is: " + line);
		StringTokenizer stk = new StringTokenizer(line, "=");
		String pre = stk.nextToken();
		if(!(formData==null)){
			if(!formData.isEmpty() && pre.startsWith("#"))  
				pre = pre.substring(1);
		} else 
			formData="";
		builder.append(pre + "= ");
		String afterequals = stk.nextToken();
		System.out.println("afterequals: " + afterequals);

		Scanner lineScanner = new Scanner(afterequals); //(line)
		while (lineScanner.hasNext()) {
			String next = lineScanner.next();
			System.out.println("linescanner.next: " + next);

			stk = new StringTokenizer(next, quoteType);		//			Scanner hostsc = new Scanner(line).useDelimiter("'[^']*'");
			if (line.contains(quoteType)) {
				if (stk.hasMoreTokens()) {
					String nt = stk.nextToken();
					System.out.println("next token: " + nt);
					
//					String str = stk.nextToken();
					System.out.println(next + " stk-> " + nt);						
					if ( next.equals(quoteType + nt + quoteType) ){
						builder.append(quoteType + formData + quoteType).append(space);						
					} else 
						builder.append(next).append(space);
				} else
					builder.append(quoteType + formData + quoteType).append(space);						

			}
		}
		lineScanner.close();
		builder.append(newline);
	}
	

	private static void writeIntegerInfo(String line, int formData) {
		StringTokenizer stk = new StringTokenizer(line, "=");
		builder.append(stk.nextToken() + "= ");
		String afterequals = stk.nextToken();
		
		Scanner lineScanner = new Scanner(afterequals); //(line)
		if (lineScanner.hasNextInt()) {
			builder.append(String.valueOf(formData)).append(space);						
		}
		while (lineScanner.hasNext()) {
			if (lineScanner.hasNextInt())	
				lineScanner.next();	// skip original int
			else 
				builder.append(lineScanner.next()).append(space);
		}
		lineScanner.close();
		builder.append(newline);
	}
	
	private static void writeDoubleInfo(String line, double formData) {
		StringTokenizer stk = new StringTokenizer(line, "=");
		builder.append(stk.nextToken() + "= ");
		String afterequals = stk.nextToken();
		
		Scanner lineScanner = new Scanner(afterequals); //(line)
		if (lineScanner.hasNextDouble()) {
			builder.append(String.valueOf(formData)).append(space);						
		}
		while (lineScanner.hasNext()) {
			if (lineScanner.hasNextDouble())	
				lineScanner.next();	// skip original int
			else 
				builder.append(lineScanner.next()).append(space);
		}
		lineScanner.close();
		builder.append(newline);
	}
	
	private static void writeDstatInfo(String line) {
		StringTokenizer stk = new StringTokenizer(line, "=");
		builder.append(stk.nextToken() + "= ");
		String afterequals = stk.nextToken();
		
		Scanner lineScanner = new Scanner(afterequals);//line	.useDelimiter("[^0-9]+");
		while (lineScanner.hasNext()) {
			String next = lineScanner.next();
			if ( next.equals("None") || next.equals("\"file\"") || next.equals("\"ssh\"") ){
				builder.append(currParams.getDstat_mode()).append(space);						
			} else 
				builder.append(next).append(space);
		}
		lineScanner.close();
		builder.append(newline);		
	}

	
	private static void writeRampListInfo(String line, String formData) {
		StringTokenizer stk = new StringTokenizer(line, "=");
		builder.append(stk.nextToken() + "= ");
		String afterequals = stk.nextToken();
		
		if (afterequals.contains("null") || afterequals == null) {
			builder.append(String.valueOf(formData)).append(space);	
		} else {
//			String str = afterequals.split("[\\[\\]]")[1];
			builder.append(String.valueOf(formData)).append(space);	
			
			Scanner lineScanner = new Scanner(afterequals).useDelimiter("]"); //(line)
			if (lineScanner.hasNext())
				lineScanner.next();
			lineScanner.useDelimiter("\\s");
			while(lineScanner.hasNext()) {
				String next = lineScanner.next();
				if (!next.equals("]"))
					builder.append(next);
			}			
			lineScanner.close();
		}
		builder.append(newline);
	}
	
	
	public static String writeTRTListToString(List<TRT> list){	//(Parameters currparams) {
		StringBuilder builder = new StringBuilder();
		
		// open list of response times
		builder.append("{");
		for (int i=0; i<list.size(); i++) {	//(TRT trt : currparams.getTarget_response_times()) {
			TRT trt = list.get(i);
//			TRT trt = trtcontainer.getItem(id).getEntity();
			builder.append(writeTRTToString(trt));
			if (i != list.size()-1 )
				builder.append(", ");
		}
		builder.append("}");
		
		return builder.toString();
	}
	
	public static String writeTRTToString(TRT trt) {
		String str = "";
		if (trt!=null) {
			str = "'" + trt.getAction() + "': " +
					"{'average':" + trt.getAverage() +
					",'max':" + trt.getMax() + "}";
		}
		return str;
	}
	
	
	
	/**
	 * READ DATA FROM ACE EDITOR
	 */
	public static String getDstatInfo(String line) {
		String result = "";
		StringTokenizer stk = new StringTokenizer(line, "=");
		stk.nextToken();	// token before '='
		String afterequals = stk.nextToken();
		
		if (afterequals == null) {
			result = "";
		} else {
			Scanner lineScanner = new Scanner(afterequals);//line	.useDelimiter("[^0-9]+");
			if (lineScanner.hasNext()) {
				String next = lineScanner.next();
				if ( next.equals("None") || next.equals("\"file\"") || next.equals("\"ssh\"") )			
					result = next;
			}
			lineScanner.close();			
		}
		System.out.println("RESULT -> " + result);
		return result;
	}
	
	
	private static String getQuotedDataInfo(String line, String quoteType, String variable) {
		String result = "";
		StringTokenizer stk = new StringTokenizer(line, "=");
		stk.nextToken();	// token before '='
		String afterequals = stk.nextToken().trim();
		System.out.println("afterequals -> " + afterequals);

//		if (afterequals == null) {
//		} else if (afterequals.contains(quoteType)) {
		if ( !(afterequals == null) && afterequals.contains(quoteType) ) {
			stk = new StringTokenizer(afterequals, quoteType);		//			Scanner hostsc = new Scanner(line).useDelimiter("'[^']*'");
			try {
				String str = stk.nextToken();
				System.out.println("first token -> -" + str + "-");
				if (!str.equals(" ")) {
					String s = str.trim();
					if (!s.startsWith("#")){
						result = s;
						System.out.println("first Token - result -> -" + result + "-");						
					} else {
						result = "";
						System.out.println("first Token - result -> -" + result + "-");												
					}
//					result = str;	//stk.nextToken();
				} else if (str.equals("") || str.equals(" ")) {
					result = "";
					System.out.println("result -> " + result);
				} else {
					result = stk.nextToken();
					System.out.println("second Token - result -> " + result);
				}
			} catch (NoSuchElementException e) {
				e.printStackTrace();
				Notification not = new Notification("Check your formatting on '" + variable + "'");
				not.setStyleName("failure");
		        not.show(Page.getCurrent());

			}
		}
		System.out.println("RESULT -> " + result);
		
		return result;
	}
	
	
	private static int getIntegerInfo(String line) {
		int result = 0;
		StringTokenizer stk = new StringTokenizer(line, "=");
		stk.nextToken();	// token before '='
		String afterequals = stk.nextToken();
		
		Scanner lineScanner = new Scanner(afterequals); //(line)
		if (lineScanner.hasNextInt()) {
			result = lineScanner.nextInt();
		}
		lineScanner.close();
		System.out.println("RESULT -> " + result);

		return result;
	}
	
	private static double getDoubleInfo(String line) {
		double result = 0;
		StringTokenizer stk = new StringTokenizer(line, "=");
		stk.nextToken();	// token before '='
		String afterequals = stk.nextToken();
		
		Scanner lineScanner = new Scanner(afterequals); //(line)
		if (lineScanner.hasNextDouble()) {
			result = lineScanner.nextDouble();
		}
		lineScanner.close();
		System.out.println("RESULT -> " + result);

		return result;
	}
	
	private static String getRampListInfo(String line) {
		String result = "";
		StringTokenizer stk = new StringTokenizer(line, "=");
		stk.nextToken();	// token before '='
		String afterequals = stk.nextToken();
		
		if ( !(afterequals == null) && afterequals.contains("]") ) {
			String regex = afterequals.split("[\\[\\]]")[1];
			result = "[" + regex + "]";
//			stk = new StringTokenizer(afterequals, "]");		//			Scanner hostsc = new Scanner(line).useDelimiter("'[^']*'");
//			result = stk.nextToken() + "]";
		}
		System.out.println("RESULT -> " + result);

		return result;
	}
	
	/*
	 * 1 get first '{'
	 * 2 get all text till next '}'
	 *   	a. get single trt info
	 * 3 if (next is ',') then : step 2
	 * 	 else (next is '}') then : finish read
	 */
	public static void getTRTListFromString(String line){	//List<TRT> list  (Parameters currparams) {
		System.out.println("#Getting TRTs");
		//		String result = "";
		StringTokenizer stk = new StringTokenizer(line, "=");
		stk.nextToken();	// token before '='
		String afterequals = stk.nextToken();
		List<TRT> newtrtlist = new ArrayList<TRT>();
		
		List<TRT> prevtrtlist = new ArrayList<TRT>();
		for ( TRT t : currParams.getTarget_response_times()){
			prevtrtlist.add(t);
		}
		
		
		// get new trt's from editor
		if ( !(afterequals == null) && afterequals.contains("{") ) {
			stk = new StringTokenizer(afterequals, "}");		//			Scanner hostsc = new Scanner(line).useDelimiter("'[^']*'");
			try {
//				stk.nextToken(); // before 
				while (stk.hasMoreTokens()) {
					String singleResult = stk.nextToken() + "}";
					if (!singleResult.contains("'")) {
						System.out.println("singleResult - > " + singleResult);						
						break;
					}
					singleResult = singleResult.substring(singleResult.indexOf("'"));
					System.out.println("first token - > " + singleResult);
					TRT trt = getTRTFromString(singleResult);
					
					// write the trt to db
					trt = commitTRTtoDB(trt);					
					
					// add to list of trt's
					newtrtlist.add(trt);
				}
				
				// remove any leftover trt's that weren't in the ace editor
						// TESTING
						System.out.println("WHAT IS PREV LIST OF TRTs: "
						+ currParams.getTarget_response_times()); // testing purposes
						for (TRT s : currParams.getTarget_response_times()) {
							System.out.println(s.getId() + " - " + s.getAction()); // testing purposes	            		
						}

				  // Get an iterator.
				  Iterator<TRT> ite = prevtrtlist.iterator();
				  boolean tExists = false;
				  while(ite.hasNext()) {
				       TRT value = ite.next();		// if ( !(newtrtlist.contains(value)) ) {
				       for (TRT insideT : newtrtlist) {
				    	   if (value.getAction().equals(insideT.getAction())) {
				    		   tExists = true; }
				       }
				       if (tExists == false) {
				       		// remove from parent
							currParams.removeTRT(value); 	
							parameterscontainer.addEntity(currParams);
							
							// delete item
							TRT t = trtcontainer.getItem(value.getId()).getEntity();
							trtcontainer.removeItem(t.getId());		
						}
				  }
				  		// TESTING
						System.out.println("WHAT IS NEW LIST OF TRTs: "
								+ currParams.getTarget_response_times()); // testing purposes
						for (TRT s : currParams.getTarget_response_times()) {
							System.out.println(s.getId() + " - " + s.getAction()); // testing purposes	            		
						}
				
			} catch (NoSuchElementException e) {
				e.printStackTrace();
				Notification not = new Notification("Check your formatting on 'TargetResposeTimes'");
				not.setStyleName("failure");
		        not.show(Page.getCurrent());

			}
		}
//		System.out.println("RESULT -> " + result);
		
//		return result;
	}

	public static TRT getTRTFromString(String singleTRT) {
		TRT trt = new TRT();

		// get Action
		Scanner scan = new Scanner(singleTRT).useDelimiter("'");
		String result = scan.next();
		System.out.println("action scan result - >" + result);
		trt.setAction(result);
		
		// get Average
		String restofline = scan.nextLine();
//		System.out.println(restofline.split("[\\:\\,]")[1]);
		String regex = restofline.split("[\\:\\,]")[2];
		double dub = Double.parseDouble(regex);
		trt.setAverage(dub);			
		System.out.println("average scan result - >" + String.valueOf(dub));

		// get Max
//		System.out.println(restofline.split("[\\:\\}]")[1]);
//		System.out.println(restofline.split("[\\:\\}]")[2]);
		regex = restofline.split("[\\:\\}]")[3];
		dub = Double.parseDouble(regex);
		trt.setMax(dub);			
		System.out.println("max scan result - >" + String.valueOf(dub));

		// 1. set parent params
		trt.setParentparameter(currParams);
		
		return trt;
	}
	
	private static TRT commitTRTtoDB(TRT trt) {
		EntityManager em = Persistence.createEntityManagerFactory("mbpet")
				.createEntityManager();
		Query query = em.createQuery("SELECT OBJECT(t) FROM TRT t WHERE t.action = :action AND t.parentparameter = :parentparameter");
		//		            query.setParameter("title", newsession.getTitle());
		query.setParameter("action", trt.getAction());
		query.setParameter("parentparameter", trt.getParentparameter());
		TRT queriedTRT = null;
		
		try {		
			// == check if action already exists ==
			// retrieving db generated id
			queriedTRT = (TRT) query.getSingleResult();
//			List<TRT> results = query.getResultList(); // getSingleResult();	//(TRT) 
//			System.out.println("result list size: " + results.size());
//			queriedTRT = results.get(results.size()-1);
//			System.out.println("the generated id is: " + queriedTRT.getId());
			
//			trt = trtcontainer.getItem(queriedTRT.getId()).getEntity();
			System.out.println("the generated id is: " + queriedTRT.getId());

//			return queriedTRT;
	
		} catch (NoResultException e) {
			e.printStackTrace();
			
			// == else, add new TRT to db ==
			
			// 1. add to container
			trtcontainer.addEntity(trt);

			// 2. retrieving db generated id
			List<TRT> results = query.getResultList(); // getSingleResult();	//(TRT) 
			System.out.println("result list size: " + results.size());
			queriedTRT = results.get(results.size()-1);
			System.out.println("the generated id is: " + queriedTRT.getId());
			int id = queriedTRT.getId(); // here is the id we need for tree
			
			// 3. update parent Parameters to add TRT to List<TRT> trt
			currParams.addTRT(queriedTRT);
			parameterscontainer.addEntity(currParams);
						
			System.out.println("WHAT IS NEW LIST OF TRTs: "
							+ currParams.getTarget_response_times()); // testing purposes
			for (TRT s : currParams.getTarget_response_times()) {
				System.out.println(s.getId() + " - "
						+ s.getAction()); // testing purposes	            		
			}
//			return queriedTRT;

			
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
		return queriedTRT;

	}
	
}

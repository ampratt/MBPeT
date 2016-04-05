package com.aaron.mbpet.services;

import java.io.*;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.aaron.mbpet.MbpetUI;
import com.aaron.mbpet.ui.MasterTerminalWindow;
import com.aaron.mbpet.views.sessions.SessionViewer;
import com.vaadin.ui.UI;

public class StreamGobbler2 {	//extends Thread {
	
    InputStream is;
    String type;
//    final PushMasterUpdater updater;
    PushMasterUpdater updater;		//PushLabelUpdater 
    MasterTerminalWindow masterTerminalWindow;
    SessionViewer sessionViewer;
    
    StreamGobbler2(InputStream is, String type, final MbpetUI mbpetUI, 
    		MasterTerminalWindow masterTerminalWindow, SessionViewer sessionViewer) {
    	this.is = is;
        this.type = type;
        this.updater = mbpetUI;
//        this.sessionViewer = sessionViewer;
        this.masterTerminalWindow = masterTerminalWindow; 
        this.sessionViewer = sessionViewer;
    }
    
    StreamGobbler2(InputStream is, String type) {
    	this.is = is;
        this.type = type;
    }
    
    public void startMasterOutputGobbler() {
    	new Thread() {
            @Override
            public void run() {
		    	try {
		             InputStreamReader isr = new InputStreamReader(is);
		             BufferedReader br = new BufferedReader(isr);
		             String line=null;
		             boolean firstMessage = true;
//		             Scanner sc;
		             int timestamp;
		             while ( (line = br.readLine()) != null) {
		            	 final String output = line;
		            	 
	            		 System.out.println("searching for timestamp in..."+line);
	            		 Scanner sc = new Scanner(line);
	            		 String one = "", two = "";
	            		 if (sc.hasNext()) one = sc.next();
	            		 if (sc.hasNext()) {two = sc.next();
	            		 System.out.println("first two words: " + one + " " + two); }
	            		 
	            		 if ((one.equals("Time")) && (two.equals("Left:")) ){
//		            	 if(containsExactPhrase(line, "Left:")){
		            		 System.out.println("line contained 'Time Left:'");
//		            		 timestamp = sc.nextInt();
		            	 
				            	 // start progressbar
				            	 if (firstMessage) {
			      		        	   // Update the UI thread-safely
			    		               UI.getCurrent().access(new Runnable() {
			    		                   @Override
			    		                   public void run() {
			    		                	   sessionViewer.displayProgressBar(true);	//TODO this causes java.lang.IllegalStateException: A connector should not be marked as dirty while a response is being written.
//			    		                	   sessionViewer.updateProgressBar();
		    		      		           }
			    		               });
			    		               firstMessage=false;
				            	 } else {
				            		 sessionViewer.updateProgressBar();
				            	 }
      		           
		            	 }
//	            		 sc.close();

		                 // Update the UI thread-safely
		                 UI.getCurrent().access(new Runnable() {
		                     @Override
		                     public void run() {
		                    	 String prefix = "mbpet>";
//		                    	 if (type.equals("ERROR")) prefix = "mbpet-ERROR>";
		                    	 masterTerminalWindow.insertDataToEditor(new StringBuilder(prefix + output));
		                     }
		                 });
		                 
//		                 if (sc.hasNextInt()){
//		                	 if (sc.nextInt() < 1) {
//		                		 
//		                	 }
//		                 }
//		                 String nosummary = "Message doesn't have summary field.";
		                 
//		             	updater.printNextMasterOutput("mbpet>" + line, masterTerminalWindow);	//masterTerminalWindow);	
		//                 System.out.println(type + ">" + line);
		             }
		             // I COULD REROUTE TO REPORTS TAB FROM HERE IN ADDITION TO FROM UDP
		         } catch (IOException ioe) {
		         	ioe.printStackTrace();  
		         }
            }
        }.start();
    }
    
    public void startMasterErrorGobbler() {
    	new Thread() {
            @Override
            public void run() {
		    	try {
		             InputStreamReader isr = new InputStreamReader(is);
		             BufferedReader br = new BufferedReader(isr);
		             String line=null;
		             while ( (line = br.readLine()) != null) {
		                 // Update the UI thread-safely
		            	 final String output = line;
		                 UI.getCurrent().access(new Runnable() {
		                     @Override
		                     public void run() {
		                    	 String prefix = "mbpet>";
		                    	 if (type.equals("ERROR")) prefix = "mbpet-ERROR>";
		                    	 masterTerminalWindow.insertDataToEditor(new StringBuilder(prefix + output));
		                     }
		                 });
//		             	updater.printNextMasterOutput("mbpet>" + line, masterTerminalWindow);	//masterTerminalWindow);	
		//                 System.out.println(type + ">" + line);
		             }
		         } catch (IOException ioe) {
		         	ioe.printStackTrace();  
		         }
            };
        }.start();
    }
    
    public void startSlaveGobbler() {
    	new Thread() {
            @Override
            public void run() {
		    	try {
		             InputStreamReader isr = new InputStreamReader(is);
		             BufferedReader br = new BufferedReader(isr);
		             String line=null;
		             while ( (line = br.readLine()) != null) {
//		                 // Update the UI thread-safely
//		            	 final String output = line;
//		                 UI.getCurrent().access(new Runnable() {
//		                     @Override
//		                     public void run() {
//		                    	 masterTerminalWindow.insertDataToEditor("mbpet>" + output);
//		                     }
//		                 });
		                 System.out.println("SLAVE" + type + ">" + line);
//		             	updater.printNextMasterOutput("mbpet>" + line, masterTerminalWindow);	//masterTerminalWindow);	
		//                 System.out.println(type + ">" + line);
		             }
		         } catch (IOException ioe) {
		         	ioe.printStackTrace();  
		         }
            };
        }.start();
    }
    
    Pattern p;
    Matcher m;
	private boolean containsExactPhrase(String fullString, String word){
		System.out.println("line searched: " + fullString);

	    String pattern = ".*\\b"+word+"\\b.*";
	    p = Pattern.compile(pattern);
	    m = p.matcher(fullString);
	    
	    System.out.println("found Left:? >" + m.find());

	    return m.find();
	}
    
    
}
package com.aaron.mbpet.services;

import java.io.*;

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
//    SessionViewer sessionViewer;
    
    StreamGobbler2(InputStream is, String type, final MbpetUI mbpetUI, MasterTerminalWindow masterTerminalWindow) {
    	this.is = is;
        this.type = type;
        this.updater = mbpetUI;
//        this.sessionViewer = sessionViewer;
        this.masterTerminalWindow = masterTerminalWindow; 
    }
    
    public void startStreamGobbler() {
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
		                    	 masterTerminalWindow.insertDataToEditor("mbpet>" + output);
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
    
    
}
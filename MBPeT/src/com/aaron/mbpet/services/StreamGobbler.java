package com.aaron.mbpet.services;

import java.io.*;

import com.aaron.mbpet.MbpetUI;
import com.aaron.mbpet.ui.MasterTerminalWindow;
import com.aaron.mbpet.views.sessions.SessionViewer;

public class StreamGobbler {	//extends Thread {
	
    InputStream is;
    String type;
//    final PushMasterUpdater updater;
    PushMasterUpdater updater;		//PushLabelUpdater 
    MasterTerminalWindow masterTerminalWindow;
//    SessionViewer sessionViewer;
    
    StreamGobbler(InputStream is, String type){ //, final MbpetUI mbpetUI, MasterTerminalWindow masterTerminalWindow) {
    	this.is = is;
        this.type = type;
//        this.updater = mbpetUI;
////        this.sessionViewer = sessionViewer;
//        this.masterTerminalWindow = masterTerminalWindow; 
    }
    
    public void startPdfGobbler() {
    	new Thread() {
            @Override
            public void run() {
		    	try {
		             InputStreamReader isr = new InputStreamReader(is);
		             BufferedReader br = new BufferedReader(isr);
		             String line=null;
		             while ( (line = br.readLine()) != null) {
		                 System.out.println("wkhtmltopdf" + type + ">" + line);
		             }
		         } catch (IOException ioe) {
		         	ioe.printStackTrace();  
		         }
            };
        }.start();
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
//		            	 updater.printNextInput(line, masterTerminalWindow);	//masterTerminalWindow);	
		             	updater.printNextMasterOutput(new StringBuilder("mbpet>" + line), masterTerminalWindow);	//masterTerminalWindow);	
		//                 System.out.println(type + ">" + line);
		             }
		         } catch (IOException ioe) {
		         	ioe.printStackTrace();  
		         }
            };
        }.start();
    }
    
    
    public void getNetstatOutput(final MasterUtils mu) {
    	final boolean portfound=false;
    	new Thread() {
            @Override
            public void run() {
		    	try {
		             InputStreamReader isr = new InputStreamReader(is);
		             BufferedReader br = new BufferedReader(isr);
		             String line=null;
		             StringBuilder sb = new StringBuilder();
		             while ( (line = br.readLine()) != null) {
		            	 sb.append(line).append("\n");
		             }
        			 mu.setNetstatOutput(sb);
		         } catch (IOException ioe) {
		         	ioe.printStackTrace();  
		         }
            };
        }.start();
    }
    public void startNetstatGobbler(final MasterUtils mu, final int port) {
    	final boolean portfound=false;
    	new Thread() {
            @Override
            public void run() {
		    	try {
		             InputStreamReader isr = new InputStreamReader(is);
		             BufferedReader br = new BufferedReader(isr);
		             String line=null;
		             StringBuilder sb = new StringBuilder();
//		             mu.setPortInUse(false);
		             while ( (line = br.readLine()) != null) {
//	            		 Scanner sc = new Scanner(line);
	            		 if (line.contains("0.0.0.0:"+port)){
	            			 System.out.println("port already in use: " + line + "\ntry another port");
//	            			 portfound = true;
	            			 mu.setPortInUse(true);
	            			 break;
	            		 }
		             }
		         } catch (IOException ioe) {
		         	ioe.printStackTrace();  
		         }
            };
        }.start();
//        return portfound;
    }

    
//    @Override
//    public void run() {
////        try {
////            InputStreamReader isr = new InputStreamReader(is);
////            BufferedReader br = new BufferedReader(isr);
////            String line=null;
////            while ( (line = br.readLine()) != null) {
//////            	updater.printNextInput("mbpet>" + line, masterTerminalWindow);	
//////                System.out.println(type + ">" + line);
////            }
////        } catch (IOException ioe) {
////        	ioe.printStackTrace();  
////        }
//    }
    
    
}
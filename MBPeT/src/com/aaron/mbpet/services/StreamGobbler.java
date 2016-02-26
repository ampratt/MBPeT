package com.aaron.mbpet.services;

import java.io.*;

import com.aaron.mbpet.MbpetUI;
import com.aaron.mbpet.ui.MasterTerminalWindow;

class StreamGobbler extends Thread {
	
    InputStream is;
    String type;
    final PushMasterUpdater updater;
    MasterTerminalWindow masterTerminalWindow;
    
    StreamGobbler(InputStream is, String type, final MbpetUI mbpetUI, MasterTerminalWindow masterTerminalWindow) {
    	this.is = is;
        this.type = type;
        this.updater = mbpetUI;
        this.masterTerminalWindow = masterTerminalWindow; 
    }
    
    @Override
    public void run() {
        try {
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String line=null;
            while ( (line = br.readLine()) != null) {
            	updater.printNextInput("mbpet>" + line, masterTerminalWindow);	
//                System.out.println(type + ">" + line);
            }
        } catch (IOException ioe) {
        	ioe.printStackTrace();  
        }
    }
}
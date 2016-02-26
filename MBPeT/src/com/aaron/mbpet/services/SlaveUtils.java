package com.aaron.mbpet.services;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;

import com.aaron.mbpet.views.sessions.SessionViewer;
import com.vaadin.ui.UIDetachedException;

public class SlaveUtils implements Runnable {

	String command;
	public int masterport;

	public SlaveUtils(){		//(String command) {
//			this.command = command;
	}
	
	
	public void startSlave(final int masterport) {		//(final String command) {	//"mbpet_cli.exe test_project -b localhost:9999 -s"
//	    	new Thread() {
		new Thread(new Runnable() {
            @Override
            public void run() {
            	Process p;
        		try {
        			command = 
        					"./mbpet_slave " + 
							"127.0.0.1 -p " + 
							masterport;
        	        ProcessBuilder pb = new ProcessBuilder(
//        	        		"cmd.exe", "/c", command);
        	        		
//	        	        		"mbpet_cli.exe test_project -b localhost:9999 -s");	//c:\\dev\\mbpet\\mbpet_cli.exe c:\\dev\\mbpet\\test_project -b localhost:9999
//	        	        		"mbpet_cli.exe", "test_project", "-b", "localhost:9999");	//c:\\dev\\mbpet\\mbpet_cli.exe c:\\dev\\mbpet\\test_project -b localhost:9999
//	        	        		"dir & echo example of & echo working dir");
//        	        			"echo", "./mbpet_slave", "127.0.0.1");
    	        			"echo", command);
        	        pb.directory(new File("C:\\dev\\mbpet\\slave"));
        	
        			System.out.println("### Running slave command: " + command);
//	        			pb.redirectErrorStream(true);
        			p = pb.start();
//        			p.getInputStream().close(); 
//        			p.getOutputStream().close(); 
//        			p.getErrorStream().close();

	                int errCode = p.waitFor();
	                System.out.println("Echo command executed, any errors? " + (errCode == 0 ? "No" : "Yes"));

	        		StringBuilder output = new StringBuilder();	    	
        		
        			output.append("<OUTPUT> \n");
//        			System.out.println("<OUTPUT> \n");
        			BufferedReader bri = new BufferedReader(new InputStreamReader(p.getInputStream()));
        	        String line = null;
        			while ((line = bri.readLine()) != null) {
        				output.append(line + "\n");
//        				System.out.println(line);
        				line = bri.readLine();
        			}
        			bri.close();
        			output.append("</OUTPUT> \n");
        
        			
        			output.append("<ERROR> \n");
        			BufferedReader bre = new BufferedReader(new InputStreamReader(p.getErrorStream()));
        			line = null;
        			while ((line = bre.readLine()) != null) {
        				  output.append(line + "\n");	
        				  //System.out.println(line);
        			}
        			bre.close();
        			output.append("</ERROR> \n");
        			
        			System.out.println(output.toString());
        			System.out.println("the slave should be running now...");
        
//        			//get enter command to start process
//        			BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
//        			String amount = inFromUser.readLine();
        			
        			
        			try {
        				int exitVal = p.waitFor();            
        				System.err.println("Slave Process exitValue: " + exitVal);
        			} catch (InterruptedException e) {
        				e.printStackTrace();
        			}

        		} catch (IOException | InterruptedException e) {
        			e.printStackTrace();
        		}
            };
        }).start();
     
	}

	public String getCommand() {
		return command;
	}
	public void setCommand(String command) {
		this.command = command;
	}
	
	@Override
	public void run() {
		Process p;
		try {
	        ProcessBuilder pb = new ProcessBuilder(
//	        			"cmd.exe", "/c", command);
	        		
//		        		"mbpet_cli.exe test_project -b localhost:9999 -s");	//c:\\dev\\mbpet\\mbpet_cli.exe c:\\dev\\mbpet\\test_project -b localhost:9999
//		        		"mbpet_cli.exe", "test_project", "-b", "localhost:9999");	//c:\\dev\\mbpet\\mbpet_cli.exe c:\\dev\\mbpet\\test_project -b localhost:9999
		        		"dir & echo example of & echo working dir");
	        pb.directory(new File("C:\\dev\\mbpet\\slave"));
	
			System.out.println("Run echo command");
//				pb.redirectErrorStream(true);
			p = pb.start();

//				try {
//					int exitVal = p.waitFor();            
//					System.out.println("Process exitValue: " + exitVal);
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//				}

		} catch (IOException e) {
			e.printStackTrace();
		}		
	}
}

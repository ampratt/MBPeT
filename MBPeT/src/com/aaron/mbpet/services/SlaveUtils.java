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

import com.aaron.mbpet.MbpetUI;
import com.aaron.mbpet.views.sessions.SessionViewer;
import com.vaadin.ui.UI;
import com.vaadin.ui.UIDetachedException;

public class SlaveUtils implements Runnable {

	String command;
//	public int masterport;
//	String usersBasepath = ((MbpetUI) UI.getCurrent()).getUsersBasepath();	//"C:\\dev\\mbpet\\users\\";
	String mbpetBasepath = ((MbpetUI) UI.getCurrent()).getMbpetBasepath();	//"C:\\dev\\mbpet\\users\\";

	public SlaveUtils(){		//(String command) {
//			this.command = command;
	}
	
	
	public void startSlave(final int masterport, final String slaveOptions) {		//(final String command) {	//"mbpet_cli.exe test_project -b localhost:9999 -s"
//	    	new Thread() {
		new Thread(new Runnable() {
            @Override
            public void run() {
            	Process p;
        		try {
        			command = "./mbpet_slave " + 
							"127.0.0.1 -p " + 
							masterport +
							" " + slaveOptions;
        	        ProcessBuilder pb = new ProcessBuilder(
//        	        		"cmd.exe", "/c", command);	//Windows command
        	        		"/bin/bash", "-c", command);		//Unix command
//		        	        "/bin/bash", "-c", "echo", command);
        	        		
//	        	        		"mbpet_cli.exe test_project -b localhost:9999 -s");	//c:\\dev\\mbpet\\mbpet_cli.exe c:\\dev\\mbpet\\test_project -b localhost:9999
//	        	        		"mbpet_cli.exe", "test_project", "-b", "localhost:9999");	//c:\\dev\\mbpet\\mbpet_cli.exe c:\\dev\\mbpet\\test_project -b localhost:9999
//	        	        		"dir & echo example of & echo working dir");
//        	        			"echo", "./mbpet_slave", "127.0.0.1");
        	        pb.directory(new File(mbpetBasepath + "/slave"));	//("C:\\dev\\mbpet\\slave"));
        	
//	        			pb.redirectErrorStream(true);
        			p = pb.start();
//        			p.getInputStream().close(); 
//        			p.getOutputStream().close(); 
//        			p.getErrorStream().close();

        			//get output from slave
        			System.out.println("### Running slave command: " + command);
        			System.out.println("### Running slave command...from dir>" + pb.directory());
//					System.out.println("### Running master command...from dir> \\" + username + "\\master");
										
//					//update UI thread-safely
//	                 UI.getCurrent().access(new Runnable() {
//	                     @Override
//	                     public void run() {
//	                    	 masterTerminalWindow.insertDataToEditor("mbpet>" + command + "\n");
//	                     }
//	                 });
	                 
//					final PushMasterUpdater updater = mbpetUI;
					// any error message?
		            StreamGobbler2 errorGobbler = new 
		                StreamGobbler2(p.getErrorStream(), "ERROR");//, mbpetUI, masterTerminalWindow);	//masterTerminalWindow);            
		            
		            // any output?
		            StreamGobbler2 outputGobbler = new 
		                StreamGobbler2(p.getInputStream(), "OUTPUT");//, mbpetUI, masterTerminalWindow);	//masterTerminalWindow);
		                
		            // kick them off
		            outputGobbler.startSlaveGobbler();
		            errorGobbler.startSlaveGobbler();                        
		            
//					getPid(p);

		            // any error???
	                int errCode = p.waitFor();
	                System.out.println("Echo command executed, any errors? " + (errCode == 0 ? "No" : "Yes"));

//	        		StringBuilder output = new StringBuilder();	    	
//        		
//        			output.append("<OUTPUT> \n");
////        			System.out.println("<OUTPUT> \n");
//        			BufferedReader bri = new BufferedReader(new InputStreamReader(p.getInputStream()));
//        	        String line = null;
//        			while ((line = bri.readLine()) != null) {
//        				output.append(line + "\n");
////        				System.out.println(line);
//        				line = bri.readLine();
//        			}
//        			bri.close();
//        			output.append("</OUTPUT> \n");
//        
//        			
//        			output.append("<ERROR> \n");
//        			BufferedReader bre = new BufferedReader(new InputStreamReader(p.getErrorStream()));
//        			line = null;
//        			while ((line = bre.readLine()) != null) {
//        				  output.append(line + "\n");	
//        				  //System.out.println(line);
//        			}
//        			bre.close();
//        			output.append("</ERROR> \n");
//        			
//        			System.out.println(output.toString());
//        			System.out.println("the slave should be running now...");
//        
////        			//get enter command to start process
////        			BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
////        			String amount = inFromUser.readLine();
//        			
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
	        pb.directory(new File(mbpetBasepath + "/slave"));	//("C:\\dev\\mbpet\\slave"));
	
			//System.out.println("Run echo command");
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

package com.aaron.mbpet.services;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.SocketException;
import java.net.SocketTimeoutException;

import com.aaron.mbpet.views.sessions.SessionViewer;
import com.vaadin.ui.UIDetachedException;

public class MasterUtils implements Runnable {

	String command;
	public int masterPort;
	
	public MasterUtils(){		//(String command) {

//		this.command = command;
		
		
//		MasterUtils obj = new MasterUtils();
//		String domainName = "google.com";
		
		//in mac oxs
//		String command = "ping -c 3 " + domainName;
		
		//in windows
//		String command = "ping -n 3 " + domainName;
		
		// Runtime.exec()
//		String initiateMaster = "cmd /c c:\\dev\\mbpet\\mbpet_cli.exe c:\\dev\\mbpet\\test_project -b localhost:9999";
//		String output = executeCommand(initiateMaster);	//obj 	command
//		System.out.println(output);

		// ProcessBuilder
//		try {
////			startMaster(command);
////			initProcessBuilder();
//		} catch (IOException | InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
	}
	

	@Override
	public void run() {
		Process p;
		try {
	        ProcessBuilder pb = new ProcessBuilder("cmd.exe", "/c",
	        		command);
//	        		"mbpet_cli.exe test_project -b localhost:9999 -s");	//c:\\dev\\mbpet\\mbpet_cli.exe c:\\dev\\mbpet\\test_project -b localhost:9999
//	        		"mbpet_cli.exe", "test_project", "-b", "localhost:9999");	//c:\\dev\\mbpet\\mbpet_cli.exe c:\\dev\\mbpet\\test_project -b localhost:9999
//	        		"dir & echo example of & echo working dir");
	        pb.directory(new File("C:\\dev\\mbpet"));
	
			System.out.println("Run echo command");
//			pb.redirectErrorStream(true);
			p = pb.start();

//			try {
//				int exitVal = p.waitFor();            
//				System.out.println("Process exitValue: " + exitVal);
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}

		} catch (IOException e) {
			e.printStackTrace();
		}		
	}
	
	
	
	public void startMaster(final String numSlaves, final int udpPort, final String username) {		//(final String command) {	//"mbpet_cli.exe test_project -b localhost:9999 -s"
//    	new Thread() {
		new Thread(new Runnable() {
            @Override
            public void run() {
            	Process p;
        		try {
        			command = "mbpet_cli.exe " +
						"test_project " +
						numSlaves + 
						" -p " + getAvailablePort() +
						" -b localhost:" + udpPort + 
						" -s";
        	        ProcessBuilder pb = new ProcessBuilder("cmd.exe", "/c", command);
//        	        		"mbpet_cli.exe " +
//        					"test_project " +
//        					numSlaves + 
//        					" -p " + udpPort +
//        					" -b localhost:" + getAvailablePort() + 
//        					" -s");
//        	        		"mbpet_cli.exe test_project -b localhost:9999 -s");	//c:\\dev\\mbpet\\mbpet_cli.exe c:\\dev\\mbpet\\test_project -b localhost:9999
//        	        		"mbpet_cli.exe", "test_project", "-b", "localhost:9999");	//c:\\dev\\mbpet\\mbpet_cli.exe c:\\dev\\mbpet\\test_project -b localhost:9999
//        	        		"dir & echo example of & echo working dir");
        	        
        	        pb.directory(new File("C:\\dev\\mbpet\\users\\" + username + "\\master"));		//("C:\\dev\\mbpet"));
        	
        			System.out.println("### Running master command: " + command);
//        			pb.redirectErrorStream(true);
        			p = pb.start();
        			p.getInputStream().close(); 
        			p.getOutputStream().close(); 
        			p.getErrorStream().close();

        			try {
        				int exitVal = p.waitFor();            
        				System.err.println("Master Process exitValue: " + exitVal);
        			} catch (InterruptedException e) {
        				e.printStackTrace();
        			}

        		} catch (IOException e) {
        			e.printStackTrace();
        		}
            };
        }).start();
        
        
//		Process p;
//		try {
//	        ProcessBuilder pb = new ProcessBuilder("cmd.exe", "/c",
//	        		command);
////	        		"mbpet_cli.exe test_project -b localhost:9999 -s");	//c:\\dev\\mbpet\\mbpet_cli.exe c:\\dev\\mbpet\\test_project -b localhost:9999
////	        		"mbpet_cli.exe", "test_project", "-b", "localhost:9999");	//c:\\dev\\mbpet\\mbpet_cli.exe c:\\dev\\mbpet\\test_project -b localhost:9999
////	        		"dir & echo example of & echo working dir");
//	        pb.directory(new File("C:\\dev\\mbpet"));
//	
//			System.out.println("Run echo command");
//			pb.redirectErrorStream(true);
//			p = pb.start();
//
//        
////        int errCode = p.waitFor();
////        System.out.println("Echo command executed, any errors? " + (errCode == 0 ? "No" : "Yes"));
//        
////		StringBuilder output = new StringBuilder();	    	
////		
////		
////			output.append("<OUTPUT> \n");
////			System.out.println("<OUTPUT> \n");
////			BufferedReader bri = new BufferedReader(new InputStreamReader(p.getInputStream()));
////	        String line = null;
////	        		//			System.out.println(line = bri.readLine());
////			//			int iin;
////			//			while ((iin = bri.read()) != -1)
////			//			{
////			//			   if (iin > 0)
////			//			   {
////			//			      char c = (char) iin;
////			//			      // Do something with the character
////			//			      output.append(c);
////			//			      System.out.println(c);
////			//			   }
////			//			}
////			
////			while ((line = bri.readLine()) != null) {
////				output.append(line + "\n");
////				System.out.println(line);
////				line = bri.readLine();
////				
////			}
////			bri.close();
////			output.append("</OUTPUT> \n");
////
////			
////			output.append("<ERROR> \n");
////			BufferedReader bre = new BufferedReader(new InputStreamReader(p.getErrorStream()));
////			line = null;
////			while ((line = bre.readLine()) != null) {
////				  output.append(line + "\n");	
////				  //System.out.println(line);
////			}
////			bre.close();
////			output.append("</ERROR> \n");
////			
////
////			System.out.println(output.toString());
//			
//			System.out.println("the master should be running");
//
////			//get enter command to start process
////			BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
////			String amount = inFromUser.readLine();
//
//			
//			try {
//				int exitVal = p.waitFor();            
//				System.out.println("Process exitValue: " + exitVal);
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
//
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
	}
	
	
	
	public int getAvailablePort() {
		int openport = 0;
	    for (int port=6000; port<7000; port++) {		//(int port : ports)
			try {
				System.out.println("Trying port: " + port);
				ServerSocket srv = new ServerSocket(port);	//System.out.println("socket open on port " + port);
				srv.close();		//System.out.println(srv.getLocalPort());
				srv = null;			//System.out.println("socket closed on port " + port);
				openport = port;
//				setMasterPort(port);
				break;
//				return true;
			} catch (IOException e) {
				System.out.println(e);
				continue;	//return false;
			}
	    }
		System.out.println("Returning port [" + openport + "] for master use");
		setMasterPort(openport);
	    return openport;
	}

	public void setMasterPort(int port) {
		this.masterPort = port;
		System.out.println("Master port set to: " + masterPort);

	}
	public int getMasterPort(){
		return masterPort;
	}

	public String getCommand() {
		return command;
	}
	public void setCommand(String command) {
		this.command = command;
	}
	
	
//	private void initProcessBuilder() throws IOException, InterruptedException {
//
//        ProcessBuilder pb = new ProcessBuilder("cmd.exe",
//                "/C dir & echo example of & echo working dir");	//("echo", "This is ProcessBuilder Example from JCG");
//       
//        System.out.println("Run echo command");
//        Process process = pb.start();
//        int errCode = process.waitFor();
//        System.out.println("Echo command executed, any errors? " + (errCode == 0 ? "No" : "Yes"));
//        System.out.println("Echo Output:\n" + output(process.getInputStream()));  		
//	}
//
//	private String output(InputStream inputStream) throws IOException {
//         StringBuilder sb = new StringBuilder();
//         BufferedReader br = null;
//         try {
//             br = new BufferedReader(new InputStreamReader(inputStream));
//             String line = null;
//             while ((line = br.readLine()) != null) {
//                 sb.append(line + System.getProperty("line.separator"));
//             }
//         } finally {
//             br.close();
//         }
//
//         return sb.toString();
//
//	}

	 
	 
	private String executeRuntimeCommand(String command) {

		StringBuffer output = new StringBuffer();

		Process p;
		try {
			Runtime rt = Runtime.getRuntime();
			p = rt.exec(command);
			p.waitFor();
			BufferedReader reader = new BufferedReader(
					new InputStreamReader(p.getInputStream()));

            String line = "";			
			while ((line = reader.readLine())!= null) {
				output.append(line + "\n");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return output.toString();

	}


}

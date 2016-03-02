package com.aaron.mbpet.services;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.SocketException;
import java.net.SocketTimeoutException;

import com.aaron.mbpet.MbpetUI;
import com.aaron.mbpet.domain.TestSession;
import com.aaron.mbpet.ui.MasterTerminalWindow;
import com.aaron.mbpet.views.sessions.SessionViewer;
import com.vaadin.annotations.Push;
import com.vaadin.ui.UI;
import com.vaadin.ui.UIDetachedException;

public class MasterUtils implements Runnable {

	String command;
	public int masterPort;
	String usersBasepath = ((MbpetUI) UI.getCurrent()).getUsersBasepath();	//"C:\\dev\\mbpet\\users\\";
	
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
	
	public void startMasterStreamGobbler(final MbpetUI mbpetUI, final MasterTerminalWindow masterTerminalWindow, final SessionViewer sessionViewer,
										final String numSlaves, final int udpPort, 
										final String username, final TestSession currsession) {		//(final String command) {
		new Thread(new Runnable(){
			@Override
			public void run() {
				try {
//					String testpath = usersBasepath + "apratt\\yaas\\y1";
//					String mastercommand = "mbpet_cli.exe " +
//							testpath + " " + 1 + " -p " + masterport + " -b localhost:" + udpPort + " -s";
					String c = "mbpet_cli.exe" +
		    				" " + getTestDir(currsession) +		//"test_project " +
							" " + numSlaves + 
							" -p " + getAvailablePort() +
							" -b localhost:" + udpPort + 
							" -s";
					setCommand(c);
//					ProcessBuilder pb = new ProcessBuilder(command);
        			System.out.println("master command: " + command);
        	        ProcessBuilder pb = new ProcessBuilder("cmd.exe", "/c", command);
//        	        		"mbpet_cli.exe test_project -b localhost:9999 -s");	//c:\\dev\\mbpet\\mbpet_cli.exe c:\\dev\\mbpet\\test_project -b localhost:9999
//        	        		"mbpet_cli.exe", "test_project", "-b", "localhost:9999");	//c:\\dev\\mbpet\\mbpet_cli.exe c:\\dev\\mbpet\\test_project -b localhost:9999
//        	        		"dir & echo example of & echo working dir");
//        	        pb.directory(new File("C:\\dev\\mbpet"));
        	        pb.directory(new File(usersBasepath + username + "\\master"));		//("C:\\dev\\mbpet"));

        	        pb.redirectErrorStream(true);
					
        	        //close socket to free up port #
    				ss.close();		//System.out.println(srv.getLocalPort());
    				ss = null;
    				
        			final Process p = pb.start();
					System.out.println("### Running master command...from dir>" + pb.directory());
//					System.out.println("### Running master command...from dir> \\" + username + "\\master");
										
					//update UI thread-safely
	                 UI.getCurrent().access(new Runnable() {
	                     @Override
	                     public void run() {
	                    	 masterTerminalWindow.insertDataToEditor("mbpet>" + command + "\n");
	                     }
	                 });
	                 
//					final PushMasterUpdater updater = mbpetUI;
					// any error message?
		            StreamGobbler2 errorGobbler = new 
		                StreamGobbler2(p.getErrorStream(), "ERROR", mbpetUI, masterTerminalWindow);	//masterTerminalWindow);            
		            
		            // any output?
		            StreamGobbler2 outputGobbler = new 
		                StreamGobbler2(p.getInputStream(), "OUTPUT", mbpetUI, masterTerminalWindow);	//masterTerminalWindow);
		                
		            // kick them off
//		            errorGobbler.start();
//		            outputGobbler.start();
		            outputGobbler.startStreamGobbler();
		            errorGobbler.startStreamGobbler();                        
		            
					getPid(p);

		            // any error???
		            int exitVal = p.waitFor();
		            System.out.println("ExitValue: " + exitVal);        
		            
				} catch (IOException | InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		}).start();
	}
	
	
    private String getTestDir(TestSession currSession) {	//(String username, TestSession currSession){
    	String path =  //"..\\" +		//usersBasepath + username +
	    			"..\\" + currSession.getParentcase().getTitle() +
	    			"\\" + currSession.getTitle();
    	return path;
    }
	
	ServerSocket ss = null;
	public int getAvailablePort() {
		int openport = 0;
	    for (int port=6000; port<7000; port++) {		//(int port : ports)
			try {
				System.out.println("\nTrying port: " + port);
				ss = new ServerSocket(port);	//System.out.println("socket open on port " + port);
//				ss.close();		//System.out.println(srv.getLocalPort());
//				ss = null;			//System.out.println("socket closed on port " + port);
//				openport = port;
//				setMasterPort(port);
				break;
//				return true;
			} catch (IOException e) {
				System.out.println(e);
				continue;	//return false;
			}
	    }
		System.out.println("\nReturning port [" + ss.getLocalPort() + "] for master use");
		setMasterPort(ss.getLocalPort());
	    return ss.getLocalPort();
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
	
	
	
	
	public void startMaster(final String numSlaves, final int udpPort, final String username, final TestSession currsession) {		//(final String command) {	//"mbpet_cli.exe test_project -b localhost:9999 -s"
//    	new Thread() {
		new Thread(new Runnable() {
            @Override
            public void run() {
            	Process p;
        		try {
        			command = "mbpet_cli.exe" +
        				" " + getTestDir(currsession) +	//(username, currsession) +		//"test_project " +
						" " + numSlaves + 
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
        	        
        	        pb.directory(new File(usersBasepath + username + "\\master"));		//("C:\\dev\\mbpet"));
        	
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

	
//	private String executeRuntimeCommand(String command) {
//
//		StringBuffer output = new StringBuffer();
//
//		Process p;
//		try {
//			Runtime rt = Runtime.getRuntime();
//			p = rt.exec(command);
//			p.waitFor();
//			BufferedReader reader = new BufferedReader(
//					new InputStreamReader(p.getInputStream()));
//
//            String line = "";			
//			while ((line = reader.readLine())!= null) {
//				output.append(line + "\n");
//			}
//
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return output.toString();
//	}

	
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

	
	public static int getPid(Process process) {
	    try {
	        Class<?> cProcessImpl = process.getClass();
	        Field fPid = cProcessImpl.getDeclaredField("PID");
	        if (!fPid.isAccessible()) {
	            fPid.setAccessible(true);
	        }
	        System.out.println("!!! this process' pid is:" + fPid.getInt(process));
	        return fPid.getInt(process);
	    } catch (Exception e) {
	    	System.err.println(e);
	        return -1;
	    }
	}
	
	//Unix
	 public static long getPidOfProcess(Process p) {
		    long pid = -1;

		    try {
		      if (p.getClass().getName().equals("java.lang.UNIXProcess")) {
		        Field f = p.getClass().getDeclaredField("pid");
		        f.setAccessible(true);
		        pid = f.getLong(p);
		        f.setAccessible(false);
		      }
		    } catch (Exception e) {
		      pid = -1;
		    }
		    return pid;
		  }
	
	
}

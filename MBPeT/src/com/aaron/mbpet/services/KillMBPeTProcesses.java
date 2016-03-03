package com.aaron.mbpet.services;

import java.io.BufferedReader;
import java.io.InputStreamReader;


public class KillMBPeTProcesses {


	
	public void KillMBPeTProcesses() {
		
	}
	
	public void killWindowsProcess(int port){
		String TASKLIST = "tasklist";
		String KILL = "taskkill /F /IM ";	
		
		 String masterProcessName = "mbpet_cli.exe";	//"./mbpet_cli"
		 String slaveProcessName = "mbpet_slave";	//"./mbpet_slave"
//		 String udpProcessName = "python.exe";
		 try {
			if (isProcessRunning(TASKLIST, masterProcessName)) {
				System.out.print(masterProcessName + " is being killed\n");
				killProcess(KILL, masterProcessName);
			 }
			if (isProcessRunning(TASKLIST, slaveProcessName)) {
				System.out.print(slaveProcessName + " is being killed\n");
				killProcess(KILL, slaveProcessName);
			 }
//			if (isProcessRunning(udpProcessName)) {
//				System.out.print(udpProcessName + " is being killed\n");
//				killProcess(udpProcessName);
//			 }
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

	public void killLinuxProcess(int port){
		String TASKLISTtcp = "fuser " + port + "/tcp";	//ps -ef	//netstat -tulpn | grep ./mbpet_cli
		String TASKLISTudp = "fuser " + port + "/udp";	//ps -ef	//netstat -tulpn | grep ./mbpet_cli
		String KILLtcp = "fuser -k " + port + "/tcp";
		String KILLudp = "fuser -k " + port + "/udp";
		
		 String masterProcessName = "./mbpet_cli";
//		 String slaveProcessName = "./mbpet_slave";
		 try {
			if (isProcessRunningLinux(TASKLISTtcp, String.valueOf(port))){	//masterProcessName)) {
				System.out.print(masterProcessName + "/tcp is being killed\n");
				killProcess(KILLtcp, masterProcessName);
			 }
			if (isProcessRunningLinux(TASKLISTudp, String.valueOf(port))){	//masterProcessName)) {
				System.out.print(masterProcessName + "/udp is being killed\n");
				killProcess(KILLudp, masterProcessName);
			 }

		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}
	

	
	public static boolean isProcessRunningLinux(String TASKLIST, String port) throws Exception {

	 Process p = Runtime.getRuntime().exec(TASKLIST);
	 BufferedReader reader = new BufferedReader(new InputStreamReader(
	   p.getInputStream()));
	 String line;
	 while ((line = reader.readLine()) != null) {
		  if (line.contains(port)) {
			  System.out.println(line);
			  return true;
		  }
	 }
	 return false;
	}
	
	public static boolean isProcessRunning(String TASKLIST, String serviceName) throws Exception {

		 Process p = Runtime.getRuntime().exec(TASKLIST);
		 BufferedReader reader = new BufferedReader(new InputStreamReader(
		   p.getInputStream()));
		 String line;
		 while ((line = reader.readLine()) != null) {
			  if (line.contains(serviceName)) {
				  System.out.println(line);
				  return true;
			  }
		 }
		 return false;
	}
	
	
	public static void killProcessByPID(String KILL) throws Exception {
		//kill it
		Runtime.getRuntime().exec(KILL);
	}
	
	public static void killProcess(String KILL, String serviceName) throws Exception {
		//get pid of running service
//		Runtime.getRuntime().exec(pidof"+ serviceName);
		
		//kill it
		Runtime.getRuntime().exec(KILL + serviceName);
	 }
	
	
	
//	public static void main(String[] args) {
//		 String masterProcessName = "mbpet_cli.exe";	//"./mbpet_cli"
//		 String slaveProcessName = "mbpet_slave";	//"./mbpet_slave"
////		 String udpProcessName = "python.exe";
//		 try {
//			if (isProcessRunning(masterProcessName)) {
//				System.out.print(masterProcessName + " is being killed\n");
//				killProcess(masterProcessName);
//			 }
//			if (isProcessRunning(slaveProcessName)) {
//				System.out.print(slaveProcessName + " is being killed\n");
//				killProcess(slaveProcessName);
//			 }
////			if (isProcessRunning(udpProcessName)) {
////				System.out.print(udpProcessName + " is being killed\n");
////				killProcess(udpProcessName);
////			 }
//		} catch (Exception e1) {
//			e1.printStackTrace();
//		}
//		 
//	}


//	public KillMBPeTProcesses() {
//		 String masterProcessName = "mbpet_cli.exe";
//		 String slaveProcessName = "mbpet_slave";
//		 String udpProcessName = "python.exe";
//		 try {
//			if (isProcessRunning(masterProcessName)) {
//				System.out.print(masterProcessName + " is being killed\n");
//				killProcess(masterProcessName);
//			 }
//			if (isProcessRunning(slaveProcessName)) {
//				System.out.print(slaveProcessName + " is being killed\n");
//				killProcess(slaveProcessName);
//			 }
//			if (isProcessRunning(udpProcessName)) {
//				System.out.print(udpProcessName + " is being killed\n");
//				killProcess(udpProcessName);
//			 }
//		} catch (Exception e1) {
//			e1.printStackTrace();
//		}
//		 
//	}

}

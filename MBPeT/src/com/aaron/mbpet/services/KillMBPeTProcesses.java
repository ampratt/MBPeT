package com.aaron.mbpet.services;

import java.io.BufferedReader;
import java.io.InputStreamReader;


public class KillMBPeTProcesses {

//	public static void main(String[] args) {
	public KillMBPeTProcesses() {
		 String masterProcessName = "mbpet_cli.exe";
		 String slaveProcessName = "mbpet_slave";
		 String udpProcessName = "python.exe";
		 try {
			if (isProcessRunning(masterProcessName)) {
				System.out.print(masterProcessName + " is being killed");
				killProcess(masterProcessName);
			 }
			if (isProcessRunning(slaveProcessName)) {
				System.out.print(slaveProcessName + " is being killed");
				killProcess(slaveProcessName);
			 }
			if (isProcessRunning(udpProcessName)) {
				System.out.print(udpProcessName + " is being killed");
				killProcess(udpProcessName);
			 }
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		 
	}
	
	
	private static final String TASKLIST = "tasklist";
	private static final String KILL = "taskkill /F /IM ";

	public static boolean isProcessRunning(String serviceName) throws Exception {

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

	public static void killProcess(String serviceName) throws Exception {

		//get pid of running service
//		Runtime.getRuntime().exec(pidof"+ serviceName);
		
		//kill it
		Runtime.getRuntime().exec(KILL + serviceName);

	 }
}

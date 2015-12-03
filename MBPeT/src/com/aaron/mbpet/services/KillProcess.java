package com.aaron.mbpet.services;

import java.io.BufferedReader;
import java.io.InputStreamReader;


public class KillProcess {

	public static void main(String[] args) {
		 String processName = "mbpet_cli.exe";
		 try {
			if (isProcessRunning(processName)) {
				System.out.print(processName + " is being killed");
				killProcess(processName);
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

	  Runtime.getRuntime().exec(KILL + serviceName);

	 }
}

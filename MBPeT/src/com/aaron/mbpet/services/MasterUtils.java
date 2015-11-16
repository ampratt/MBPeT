package com.aaron.mbpet.services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class MasterUtils {

	
	public MasterUtils() {

//		MasterUtils obj = new MasterUtils();

		String domainName = "google.com";
		
		//in mac oxs
//		String command = "ping -c 3 " + domainName;
		
		//in windows
//		String command = "ping -n 3 " + domainName;
		
		// Runtime.exec()
//		String initiateMaster = "cmd /c c:\\dev\\mbpet\\mbpet_cli.exe c:\\dev\\mbpet\\test_project -b localhost:9999";
//		String output = executeCommand(initiateMaster);	//obj 	command
//		System.out.println(output);

		// ProcessBuilder
		try {
			initProcessBuilder();
		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	private void initProcessBuilder() throws IOException, InterruptedException {

        ProcessBuilder pb = new ProcessBuilder("cmd.exe",
                "/C dir & echo example of & echo working dir");	//("echo", "This is ProcessBuilder Example from JCG");
       
        System.out.println("Run echo command");
        Process process = pb.start();
        int errCode = process.waitFor();
        System.out.println("Echo command executed, any errors? " + (errCode == 0 ? "No" : "Yes"));
        System.out.println("Echo Output:\n" + output(process.getInputStream()));  		
	}

	 private String output(InputStream inputStream) throws IOException {
		         StringBuilder sb = new StringBuilder();
		         BufferedReader br = null;
		         try {
		             br = new BufferedReader(new InputStreamReader(inputStream));
		             String line = null;
		             while ((line = br.readLine()) != null) {
		                 sb.append(line + System.getProperty("line.separator"));
		             }
		         } finally {
		             br.close();
		         }
	
		         return sb.toString();

		     }

	 
	 
	private String executeCommand(String command) {

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

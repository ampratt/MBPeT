package com.aaron.mbpet.services;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.eclipse.persistence.internal.sessions.remote.SequencingFunctionCall.GetNextValue;

import com.aaron.mbpet.MbpetUI;
import com.aaron.mbpet.domain.Model;
import com.vaadin.ui.UI;

public class FileSystemUtils {

	String usersBasepath = ((MbpetUI) UI.getCurrent()).getUsersBasepath();	//"/dev/mbpet/users/"	"C:\\dev\\mbpet\\users\\";
	String mbpetBasepath = ((MbpetUI) UI.getCurrent()).getMbpetBasepath();	//"/dev/mbpet/" "C:\\dev\\mbpet\\users\\";
//	String webContent = ((MbpetUI) UI.getCurrent()).getWebContent();	//"/dev/mbpet/" "C:\\dev\\mbpet\\users\\";
//	String usersBasepath = "/dev/mbpet/users/";		//"/dev/mbpet_projects/";
//	String mbpetBasepath = "/dev/mbpet/";		//"/dev/mbpet_projects/";

	public FileSystemUtils(){
//		createUserDir("javacreatedme");
	}
	
	public void createUserDir(String username) {
		// Create one directory
		File file;
		boolean success = (file = new File(usersBasepath + username)).mkdir();
//		File file = new File(usersBasepath + username);
		if (file.exists()){	//if (success) {
			grantPermissions(file);
			System.out.println("Directory: \"" + username + "\" created");
			System.out.println("Directory: " + file.getAbsolutePath());
		} else
			System.err.println("user directory not created!");
	}
	
//	public void createUserReportDir(String username) {
//		File file;
//		boolean success = (file = new File(webContent + "/VAADIN/users_reports/" + username)).mkdir();
////		File file = new File(webContent + "/VAADIN/users_reports/" + username );
//		if (file.exists()){	//if (success) {
//			grantPermissions(file);
//			System.out.println("Directory: \"" + username + "\" created");
//			System.out.println("Directory: " + file.getAbsolutePath());
//		} else
//			System.err.println("user reports directory not created!");		// TODO Auto-generated method stub
//		
//	}
	
	public void createSUTDir(String username, String sut) {
		// Create one directory
		File file;
		boolean success = (file = new File(usersBasepath + username + "/" + sut)).mkdir();
		if (success) {
			grantPermissions(file);
			System.out.println("Directory: " + file.getAbsolutePath());
		}
	}

	public void createSessionTestDir(String username, String sut, String testSession_dir) {
		// Create one directory
		File file;
		boolean success = (
				file = new File(usersBasepath + username + "/" + sut + "/" + testSession_dir)).mkdir();
		if (success) {
			grantPermissions(file);
			System.out.println("Directory: " + file.getAbsolutePath());
		}
	}
	
	public void createModelsDir(String username, String sut, String session, String models_dir) {
		File file;
		boolean success = (
				file = new File(usersBasepath + username + "/" + sut + "/" + session + "/" + models_dir)).mkdir();
		if (success) {
			grantPermissions(file);
			System.out.println("Directory: " + file.getAbsolutePath());
		}
	}
	
	public void createReportsDir(String username, String sut, String session, String reports_dir) {
		File file;
		boolean success = (
				file = new File(usersBasepath + username + "/" + sut + "/" + session + "/" + reports_dir)).mkdir();
		if (success) {
			grantPermissions(file);
			System.out.println("Reports Directory: " + file.getAbsolutePath());
			
			//create pdf dir
			createPDFReportsDir(file.getAbsolutePath());
		}
	}
	public void createPDFReportsDir(String reports_dir_path) {
		if (!(new File(reports_dir_path + "/pdf").exists()) ) {
			System.out.println("NO PDF DIR EXISTS...creating one");
			File file;
			boolean success = (
					file = new File(reports_dir_path + "/pdf")).mkdir();
			if (success) {
				grantPermissions(file);
				System.out.println("PDF Reports Directory: " + file.getAbsolutePath());
			}			
		}else System.out.println("PDF DIR ALREADY EXISTS");

	}
	
	public File generatePdfReport(String reportsFolder, File html) {
		File pdf = null;

		try {
			System.out.println("ATTEMPTING TO CREATE pdf report of: " + html.getName());
			
			String command = "wkhtmltopdf " + 
					html.getAbsolutePath() + " " + 
					reportsFolder + "/pdf/" + FilenameUtils.removeExtension(html.getName()) + ".pdf";
			//create pdf
//			Runtime.getRuntime().exec(new String[] { //"cmd.exe", "/c", "echo windows doesn't make pdfs"});
//							"/bin/bash", "-c", command });
			System.out.println("ATTEMPTING TO CREATE pdf report of: " + html.getName());

	        ProcessBuilder pb = new ProcessBuilder(
	        		"/bin/bash", "-c", command); //Unix commands
//	        pb.directory(new File(usersBasepath + username + "/master"));		//("C:\\dev\\mbpet"));
	        pb.redirectErrorStream(true);
			final Process p = pb.start();				
             
			// any error our output
            StreamGobbler errorGobbler = new StreamGobbler(p.getErrorStream(), "ERROR");
            StreamGobbler outputGobbler = new StreamGobbler(p.getInputStream(), "OUTPUT");
            
            // kick them off
            outputGobbler.startPdfGobbler();
            errorGobbler.startPdfGobbler();                        

            // any error???
            int exitVal = 0;
			try {
				exitVal = p.waitFor();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}System.out.println("ExitValue: " + exitVal);  
			
			
			
			
			
			//get pdf
			pdf = new File(reportsFolder + "/pdf/" + 
					FilenameUtils.removeExtension(html.getName()) + ".pdf" );
			if (pdf.exists()) {
				grantPermissions(pdf);
				System.out.println("pdf report created successfully");
			} else {
				System.out.println("No pdf. return html instead");
				pdf = html;
			}
		} catch (IOException e) {			
			System.out.println(e + "\nno pdf. trying to create one...");
		}
		
		return pdf;
	}
	
	/**
	 * RENAMING
	 */
	public void renameSUTDir(String username, String prevSutName, String newSutName) {
		File dir = new File(usersBasepath + username + "/" + prevSutName);
		if (!dir.isDirectory()) {
			System.err.println("There is no directory @ given path");
			createSUTDir(username, newSutName);
		}
		File newDir = new File(dir.getParent() + "/" + newSutName);
		dir.renameTo(newDir);
	}
	
	public void renameSessionDir(String username, String sut, String prevSessionName, String newSessionName) {
		File dir = new File(usersBasepath + username + "/" + sut + "/" + prevSessionName);
		if (!dir.isDirectory()) {
			System.err.println("There is no directory @ given path");
			createSessionTestDir(username, sut, newSessionName);
		}
		File newDir = new File(dir.getParent() + "/" + newSessionName);
		dir.renameTo(newDir);
	}

	public void renameModelsDir(String username, String sut, String session, 
			String prevModelsDir, String newModelsDir) {
		File dir = new File(usersBasepath + username + "/" + sut + "/" + session + "/" + prevModelsDir);
		if (!dir.isDirectory()) {
			System.err.println("There is no directory @ given path");
//			createModelsDir(username, sut, session, newModelsDir);
		}
		File newDir = new File(dir.getParent() + "/" + newModelsDir);
		dir.renameTo(newDir);
	}
	
	public void renameReportsDir(String username, String sut, String session, 
			String prevReportsDir, String newReportsDir) {
		File dir = new File(usersBasepath + username + "/" + sut + "/" + session + "/" + prevReportsDir);
		if (!dir.isDirectory()) {
			System.err.println("There is no directory @ given path");
//			createModelsDir(username, sut, session, newModelsDir);
		}
		File newDir = new File(dir.getParent() + "/" + newReportsDir);
		dir.renameTo(newDir);
	}


	/**
	 * WRITING FILES TO DISK
	 */
	public void writeSettingsToDisk(String username, String sut, String session, String settings_file) {
		try {
			File file;
			FileUtils.writeStringToFile(
					file = new File(usersBasepath + username + "/" + sut + "/" + session + "/settings.py"), 
					settings_file);
			grantPermissions(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void writeAdapterToDisk(String username, String sut, String session, 
			String adapter_file, String fileName) {
		//fileType = A) adapter.py, B) adapter.xml, C) petadapter.py 
//					String ftype=".py";
//					if (fileName.equals("python")) ftype = ".py";
//					if (fileName.equals("xml")) ftype = ".xml";
		try {
			File file;
			FileUtils.writeStringToFile(
					file = new File(usersBasepath + username + "/" + sut + "/" + session + "/" + fileName),		//"/adapter" + ftype), 
					adapter_file);
			grantPermissions(file);			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void writeModelToDisk(String username, String sut, String session, 
			String models_folder, Model m) {
		try {
			File file;
			FileUtils.writeStringToFile(
					file = new File(usersBasepath + username + "/" + sut + "/" + session + "/" + 
							models_folder + "/" + m.getTitle() + ".gv"), 
							m.getDotschema());
			grantPermissions(file);
			System.out.println("WRITE THIS DOTSCHEMA TO FILE:\n" + m.getDotschema());
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}
	
	public void writeModelsToDisk(String username, String sut, String session, 
			String models_folder, List<Model> mlist) {
		try {
			for (Model m : mlist) {
				File file;
				FileUtils.writeStringToFile(
						file = new File(usersBasepath + username + "/" + sut + "/" + session + "/" + 
								models_folder + "/" + m.getTitle() + ".gv"), 
						m.getDotschema());
				grantPermissions(file);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * DELETE dir or files
	 */
	public void deleteModelFromDisk(String username, String sut, String session, 
			String models_folder, String previousTitle) {
		// TODO delete single file from disk
		try {    		
    		File file = new File(usersBasepath + username + "/" + sut + "/" + session + "/" + models_folder +
    				"/" + previousTitle + ".gv");
    		if(file.delete()){
    			System.out.println(file.getName() + " is deleted!");
    		}else{
    			System.out.println("Delete operation is failed.");
    		}
    	}catch(Exception e){    		
    		e.printStackTrace();
    	}
	}
	
	public void cleanModelsDirectory(String username, String sut, String session, String models_folder) {
		try {
			FileUtils.cleanDirectory(
					new File(usersBasepath + username + "/" + sut + "/" + session + "/" + models_folder));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void deleteSessionDirectory(String username, String sut, String session) {
		try {
			FileUtils.deleteDirectory(	//cleanDirectory(
					new File(usersBasepath + username + "/" + sut + "/" + session));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void deleteSUTDirectory(String username, String sut) {
		try {
			FileUtils.deleteDirectory(	//cleanDirectory(
					new File(usersBasepath + username + "/" + sut));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	
	/**
	 * COPY master dir to local dir
	 * @param username
	 */
	public void copyMasterToUserDir(String username) {
		try {
			// cp -rp mbpetBasepath+"/master" usersBasepath+"/"+username+"/master"
			String cp = "cp -rp "+mbpetBasepath+"/master " + usersBasepath+"/"+username+"/master";
//			File src = new
//			FileUtils.copyDirectory(
//					new File(mbpetBasepath + "/master"), 
//					file);	//new File(usersBasepath + "/" + username + "/master"));
			Runtime.getRuntime().exec(new String[] { "/bin/bash", "-c", cp});
			File file = new File(usersBasepath + "/" + username + "/master");

			if (file.exists()) {
				// grant permissions to dir and all sub dirs
//				grantPermissions(file);
//				grantPermissionsRecursive(file);
				System.out.println("copied master to user dir");
			}
			else
				System.err.println("failed to copy master to user dir");
		} catch (IOException e) {
			e.printStackTrace();
		}			
	}


	public void grantPermissions(File file) {
		//chmod -R <permissionsettings> <dirname>
		try {
			System.out.println("ATTEMPTING TO CHANGE FILE PERMISSIONS of file: " + file.getAbsolutePath());
			Runtime.getRuntime().exec(new String[] { "/bin/bash", "-c", "chmod 777 " + file.getAbsolutePath() });
		} catch (IOException e) {
			e.printStackTrace();
		}

//		file.setReadable(true, true);
//		file.setWritable(true, true);
//		file.setExecutable(true, false);
	}
	public void grantPermissionsRecursive(File file) {
		try {
			System.out.println("ATTEMPTING TO CHANGE FILE PERMISSIONS of subdirs of dir: " + file.getAbsolutePath());
			Runtime.getRuntime().exec(new String[] { "/bin/bash", "-c", "chmod -Rf 777 " + file.getAbsolutePath() + "/*" });
		} catch (IOException e) {
			e.printStackTrace();
		}
	}







//	public File generatePdfReport(String reportsFolder, File html) {
//		File pdf = null;
//
//		try {
//			System.out.println("ATTEMPTING TO CREATE pdf report of: " + html.getName());
//			
//			String command = "wkhtmltopdf " + 
//					html.getAbsolutePath() + " " + 
//					reportsFolder + "/pdf/" + FilenameUtils.removeExtension(html.getName()) + ".pdf";
//			//create pdf
//			Runtime.getRuntime().exec(new String[] { //"cmd.exe", "/c", "echo windows doesn't make pdfs"});
//							"/bin/bash", "-c", command });
//			System.out.println("ATTEMPTING TO CREATE pdf report of: " + html.getName());
//			
//			try {
//				Thread.sleep(3000);
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
//			
//			//get pdf
//			pdf = new File(reportsFolder + "/pdf/" + 
//					FilenameUtils.removeExtension(html.getName()) + ".pdf" );
//			if (pdf.exists()) {
//				grantPermissions(pdf);
//				System.out.println("pdf report created successfully");
//			} else {
//				System.out.println("No pdf. return html instead");
//				pdf = html;
//			}
//		} catch (IOException e) {			
//			System.out.println(e + "\nno pdf. trying to create one...");
//		}
//		
//		return pdf;
//	}
	
	
//	File dir = new File(basepath + username + "/");
//	if (!dir.isDirectory()) {
//		System.err.println("There is no directory @ given path");
//		System.exit(0);
//	}
	
//	 File dir = new File("testDir");
//     File newName = new File(sut);
//     if ( dir.isDirectory() ) {
//             dir.renameTo(newName);
//     } else {
//             dir.mkdir();
//             dir.renameTo(newName);
//     }	
	
	
//	  String strManyDirectories="dir1/dir2/dir3";

//	  // Create multiple directories
//	  success = (new File(strManyDirectories)).mkdirs();
//	  if (success) {
//	  System.out.println("Directories: " 
//	   + strManyDirectories + " created");
//	  }
	
}

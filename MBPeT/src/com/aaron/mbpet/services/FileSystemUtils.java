package com.aaron.mbpet.services;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

import org.apache.commons.io.FileUtils;

import com.aaron.mbpet.domain.Model;

public class FileSystemUtils {

	String basepath = "/dev/mbpet_projects/";
	
	public FileSystemUtils(){
//		createUserDir("javacreatedme");
	}
	
	public void createUserDir(String username) {
		// Create one directory
		File file;
		boolean success = (file = new File(basepath + username)).mkdir();
		if (success) {
			System.out.println("Directory: \"" + username + "\" created");
			System.out.println("Directory: " + file.getAbsolutePath());
		}  
	}
	
	public void createSUTDir(String username, String sut) {
		// Create one directory
		File file;
		boolean success = (file = new File(basepath + username + "/" + sut)).mkdir();
		if (success)
			System.out.println("Directory: " + file.getAbsolutePath());
	}

	public void createSessionTestDir(String username, String sut, String testSession_dir) {
		// Create one directory
		File file;
		boolean success = (
				file = new File(basepath + username + "/" + sut + "/" + testSession_dir)).mkdir();
		if (success)
			System.out.println("Directory: " + file.getAbsolutePath());
	}
	
	public void createModelsDir(String username, String sut, String session, String models_dir) {
		File file;
		boolean success = (
				file = new File(basepath + username + "/" + sut + "/" + session + "/" + models_dir)).mkdir();
		if (success)
			System.out.println("Directory: " + file.getAbsolutePath());
	}
	
	public void createReportsDir(String username, String sut, String session, String reports_dir) {
		File file;
		boolean success = (
				file = new File(basepath + username + "/" + sut + "/" + session + "/" + reports_dir)).mkdir();
		if (success)
			System.out.println("Directory: " + file.getAbsolutePath());
	}

	
	/**
	 * RENAMING
	 */
	public void renameSUTDir(String username, String prevSutName, String newSutName) {
		File dir = new File(basepath + username + "/" + prevSutName);
		if (!dir.isDirectory()) {
			System.err.println("There is no directory @ given path");
			createSUTDir(username, newSutName);
		}
		File newDir = new File(dir.getParent() + "/" + newSutName);
		dir.renameTo(newDir);
	}
	
	public void renameSessionDir(String username, String sut, String prevSessionName, String newSessionName) {
		File dir = new File(basepath + username + "/" + sut + "/" + prevSessionName);
		if (!dir.isDirectory()) {
			System.err.println("There is no directory @ given path");
			createSessionTestDir(username, sut, newSessionName);
		}
		File newDir = new File(dir.getParent() + "/" + newSessionName);
		dir.renameTo(newDir);
	}

	public void renameModelsDir(String username, String sut, String session, 
			String prevModelsDir, String newModelsDir) {
		File dir = new File(basepath + username + "/" + sut + "/" + session + "/" + prevModelsDir);
		if (!dir.isDirectory()) {
			System.err.println("There is no directory @ given path");
//			createModelsDir(username, sut, session, newModelsDir);
		}
		File newDir = new File(dir.getParent() + "/" + newModelsDir);
		dir.renameTo(newDir);
	}
	
	public void renameReportsDir(String username, String sut, String session, 
			String prevReportsDir, String newReportsDir) {
		File dir = new File(basepath + username + "/" + sut + "/" + session + "/" + prevReportsDir);
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
			FileUtils.writeStringToFile(
					new File(basepath + username + "/" + sut + "/" + session + "/settings.py"), 
					settings_file);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void writeModelsToDisk(String username, String sut, String session, 
			String models_folder, List<Model> mlist) {
		try {
			for (Model m : mlist) {
				FileUtils.writeStringToFile(
						new File(basepath + username + "/" + sut + "/" + session + "/" + 
								models_folder + "/" + m.getTitle() + ".gv"), 
						m.getDotschema());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void cleanModelsDirectory(String username, String sut, String session, String models_folder) {
		try {
			FileUtils.cleanDirectory(
					new File(basepath + username + "/" + sut + "/" + session + "/" + models_folder));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void deleteSessionDirectory(String username, String sut, String session) {
		try {
			FileUtils.deleteDirectory(	//cleanDirectory(
					new File(basepath + username + "/" + sut + "/" + session));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void deleteSUTDirectory(String username, String sut) {
		try {
			FileUtils.deleteDirectory(	//cleanDirectory(
					new File(basepath + username + "/" + sut));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
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

package com.aaron.mbpet.services;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

public class Unzip {
    List<String> fileList;
    private String INPUT_ZIP_FILE;	// = "C:\\myzip.zip";
    private String OUTPUT_FOLDER;	// = "C:\\outputzip";
	private String parentSutPath;
	ZipFile zipFile;
	File filefile;
	
    public Unzip(File zfile){		//public static void main( String[] args ){
//    	Unzip unZip = new Unzip();
//    	unZip.
    	try {
    		this.filefile = zfile;
			zipFile = new ZipFile(zfile);

	    	this.parentSutPath = parentSutPath;
	    	OUTPUT_FOLDER = parentSutPath + "upload/";
	//    	unZipIt(zipfileUploaded);	//(INPUT_ZIP_FILE,OUTPUT_FOLDER);
//	    	unzipFileIntoDirectory(zipFile, zfile);
	    	
//	        //DELETE zip folder
//	        zipFile.close();
//			if(zfile.delete()){
//				System.out.println(zfile.getName() + " is deleted!");
//			}else{
//				System.out.println("Delete operation is failed.");
//			}
		} catch (ZipException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    
    /**
     * @param zipFile
     * @param jiniHomeParentDir
     */
    public File unzipFileIntoDirectory() {
      Enumeration files = zipFile.entries();
      File f = null;
      File newHomeDir = null;
      FileOutputStream fos = null;
      
      String absolutePath = filefile.getAbsolutePath();
      String filePath = null;
      boolean firstdir = true;
      while (files.hasMoreElements()) {
        try {
          ZipEntry entry = (ZipEntry) files.nextElement();
          InputStream eis = zipFile.getInputStream(entry);
          byte[] buffer = new byte[1024];
          int bytesRead = 0;
          
          System.out.println("zipfile path>" + filefile.getAbsolutePath());
          filePath = absolutePath.substring(0,absolutePath.lastIndexOf(File.separator));	//File.separator
          System.out.println("trying to create file at dir>" + filePath);
          f = new File(filePath + File.separator + entry.getName());
          
          if (entry.isDirectory()) {
            f.mkdirs();
            	System.out.println("created directory:" + f + " which is a directory");
		    	if (firstdir){
		    		newHomeDir = new File(f.getAbsolutePath());        		  
		        	System.out.println("SET PROJECT DIR UNZIPPED TO:" + newHomeDir);
		    		firstdir = false;
		    	}
            continue;
          } else {
            f.getParentFile().mkdirs();
            f.createNewFile();
          }
          if(f.isFile())
        	  System.out.println("created file:" + f + " which is a file");
          
          fos = new FileOutputStream(f);
    
          while ((bytesRead = eis.read(buffer)) != -1) {
            fos.write(buffer, 0, bytesRead);
          }
        } catch (IOException e) {
          e.printStackTrace();
          continue;
        } finally {
          if (fos != null) {
            try {
              fos.close();
            } catch (IOException e) {
              // ignore
            }
          }
        }
      }
      
      //DELETE zip folder
      try {
    	  	zipFile.close();

			if(filefile.delete()){
				System.out.println(filefile.getName() + " is deleted!");
			}else{
				System.out.println("Delete operation is failed.");
			}
	  	} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
      
      return newHomeDir;
    }

    
    
    
    /**
     * @param input zip file
     * @param output zip file output folder
     */
    public void unZipIt(File zipFile){

     byte[] buffer = new byte[1024];
    	
     try{
    		
    	//create output directory is not exists
    	File folder = new File(zipFile.getAbsolutePath() + "/" + zipFile.getName());	//(OUTPUT_FOLDER);
    	if(!folder.exists()){
    		folder.mkdir();
    	}
    		
    	//get the zip file content
    	ZipInputStream zis = 
    		new ZipInputStream(new FileInputStream(zipFile.getAbsolutePath()));	//zipFile));
    	//get the zipped file list entry
    	ZipEntry ze = zis.getNextEntry();
    		
    	while(ze!=null){
    			
    	   String fileName = ze.getName();
           File newFile = new File(OUTPUT_FOLDER + File.separator + fileName);
                
           System.out.println("file unzip : "+ newFile.getAbsoluteFile());
                
            //create all non exists folders
            //else you will hit FileNotFoundException for compressed folder
            new File(newFile.getParent()).mkdirs();
              
            FileOutputStream fos = new FileOutputStream(newFile);             

            int len;
            while ((len = zis.read(buffer)) > 0) {
       		fos.write(buffer, 0, len);
            }
        		
            fos.close();   
            ze = zis.getNextEntry();
    	}
    	
        zis.closeEntry();
    	zis.close();
    		
    	System.out.println("Done");
    		
    }catch(IOException ex){
       ex.printStackTrace(); 
    }
   }    
}
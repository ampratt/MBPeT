package com.aaron.mbpet.services;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import com.aaron.mbpet.MbpetUI;
import com.aaron.mbpet.domain.TestCase;
import com.aaron.mbpet.domain.User;
import com.vaadin.server.Page;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import com.vaadin.ui.Upload.Receiver;
import com.vaadin.ui.Upload.SucceededEvent;
import com.vaadin.ui.Upload.SucceededListener;

// Implement both receiver that saves upload in a file and
// listener for successful upload
public class ZipFileUploader implements Receiver, SucceededListener {
    public File file;
	public User curruser;
	public TestCase parentSUT;
	String usersBasePath = ((MbpetUI) UI.getCurrent()).getUsersBasepath();
    
	public ZipFileUploader(User curruser, TestCase parentSUT){
		this.curruser = curruser;
		this.parentSUT = parentSUT;
	}


	// upload the file to destination of username/selected SUT
    public OutputStream receiveUpload(String filename, String mimeType) {
        // Create upload stream
        FileOutputStream fos = null; // Output stream to write to
        
//        // get selected SUT
//		Object capid = sutCombobox.getValue();// ic.getItem(binder.getField("parentsession").getValue());
//		Item item = ic.getItem(capid);
//		System.out.println("sutCombobox prop id(sut): " + item.getItemProperty("id").getValue().toString());
//		parentSUT = sutcontainer.getItem(item.getItemProperty("id").getValue()).getEntity();
		
        try {
            // Open the file (destination uploading to) for writing.
            file = new File(usersBasePath + 
            				curruser.getUsername() + "/" +
            				parentSUT.getTitle() + "/"
            				+ filename);	//"C:/dev/mbpet/zip/"
            fos = new FileOutputStream(file);
        } catch (final java.io.FileNotFoundException e) {
            Notification.show(
                    "Could not open file<br/>", e.getMessage(),
                    Notification.TYPE_ERROR_MESSAGE);
            return null;
        }
        return fos; // Return the output stream to write to
    }

    // create the session - together with all the enclosed files
    public void uploadSucceeded(SucceededEvent event) {
        // Show the uploaded file in the image viewer
    	System.out.println("UPLOAD COMPLETED SUCCESSFULLY");
    	Notification not = new Notification("Zip uploaded<br/>" +
    			"Generating the Test Session Configurations from the enclosed files.",
    			Notification.Type.ASSISTIVE_NOTIFICATION);
        not.show(Page.getCurrent());

//        image.setVisible(true);
//        image.setSource(new FileResource(file));	//, getApplication()));
    }
    
    
	public User getCurruser() {
		return curruser;
	}
	public void setCurruser(User curruser) {
		this.curruser = curruser;
	}

	public TestCase getParentSUT() {
		return parentSUT;
	}
	public void setParentSUT(TestCase parentSUT) {
		this.parentSUT = parentSUT;
	}
}

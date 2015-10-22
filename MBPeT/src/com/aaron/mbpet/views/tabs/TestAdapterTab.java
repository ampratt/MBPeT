package com.aaron.mbpet.views.tabs;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import org.apache.tools.ant.types.FileList.FileName;
import org.vaadin.aceeditor.AceEditor;


import com.aaron.mbpet.components.aceeditor.AceEditorLayoutDirectory;
import com.vaadin.event.FieldEvents.TextChangeEvent;
import com.vaadin.event.FieldEvents.TextChangeListener;
import com.vaadin.server.FileResource;
import com.vaadin.server.Page;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Layout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.Upload;
import com.vaadin.ui.Upload.FinishedEvent;
import com.vaadin.ui.Upload.Receiver;
import com.vaadin.ui.Upload.StartedListener;
import com.vaadin.ui.Upload.SucceededEvent;
import com.vaadin.ui.Upload.SucceededListener;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Notification.Type;

public class TestAdapterTab extends VerticalLayout {

	AceEditor editor;
	private TextField textualInput = new TextField();
	private Button generateButton = new Button("Submit Data to Graph");
	
	public TestAdapterTab() {
		setMargin(true);
		setSpacing(true);
	
	    addComponent(new Label("<h3><i>Upload file or write code below to send adapter settings to master</i></h3>", ContentMode.HTML));	//layout.
		browseForFile();
	    
		editor = new AceEditor();
		addComponent(new AceEditorLayoutDirectory(editor));

	    //initDiagram();
	}
	
	private void buildAceEditor(){
		editor = new AceEditor();
		AceEditorLayoutDirectory acelayout = new AceEditorLayoutDirectory(editor);
	}
	
//	private void buildAceEditor() {
//		// Ace Editor
//		editor.setValue("Hello world!\nif:\n\tthen \ndo that\n...");
//		editor.setWidth("70%");		
//		//editor.setWordWrap(false);
//		editor.setReadOnly(false);
//		//editor.setShowInvisibles(false);
//		editor.setMode(AceMode.python);
//		//editor.setTheme(AceTheme.twilight);	
//
//		// Use worker (if available for the current mode)
//		//editor.setUseWorker(true);
//		editor.addTextChangeListener(new TextChangeListener() {
//		    @Override
//		    public void textChange(TextChangeEvent event) {
//		        Notification.show("Text: " + event.getText());
//		    }
//		});
//		
//		editor.addSelectionChangeListener(new SelectionChangeListener() {
//		    @Override
//		    public void selectionChanged(SelectionChangeEvent e) {
//		        int cursor = e.getSelection().getCursorPosition();
//		        //Notification.show("Cursor at: " + cursor);
//		    }
//		});
//		addComponent(editor);
//
//		//new SuggestionExtension(new MySuggester()).extend(editor);
//
//		Button button = new Button("Get Current Code");
//		button.addClickListener(new Button.ClickListener() {
//			public void buttonClick(ClickEvent event) {
//				String s = editor.getValue();
//				Label label = new Label(s);
//				//layout.addComponent(label);
//				//testing purposes
//				Notification.show(editor.getValue(), Type.WARNING_MESSAGE);
//			}
//		});
//		addComponent(button);
//
//		new SuggestionExtension(new MySuggester()).extend(editor);		
//	}
	
	
	
		public void browseForFile() {
		// horizontal layout
        final VerticalLayout vert = new VerticalLayout();
        final Label confirm = new Label("Your file was successfully uploaded to: D:\\");
    	final HorizontalLayout hor = new HorizontalLayout();
		hor.setWidth("100%");
		hor.setSpacing(true);
        addComponent(hor);
               
        /**
         * Upload Example
         */
        // Show uploaded file in this placeholder
        final Embedded embedded = new Embedded("Uploaded File");
        embedded.setVisible(false);
        
        // Implement both receiver that saves upload in a file and
        // listener for successful upload
        class ImageUploader implements Receiver, SucceededListener {
            String fName = null;
            String dir = "D:\\";
        	public File file;
            
            public OutputStream receiveUpload(String filename, String mimeType) {
            	fName = filename;
            	//vert.removeComponent(confirm);
                // Create upload stream
                FileOutputStream fos = null; // Stream to write to
                try {
                    // Open the file for writing.
                    file = new File(dir + filename);	// /tmp/uploads/
                    fos = new FileOutputStream(file);
                } catch (final java.io.FileNotFoundException e) {
                    new Notification("Could not open file<br/>",
                                     e.getMessage(),
                                     Notification.Type.ERROR_MESSAGE)
                        .show(Page.getCurrent());
                    return null;
                }
                return fos; // Return the output stream to write to
            }

            public void uploadSucceeded(SucceededEvent event) {
                // Show the uploaded file in the image viewer
            	//confirm.setValue("Your file was successfully uploaded to: D:\\");
            	//vert.addComponent(confirm);
                embedded.setVisible(true);
                embedded.setSource(new FileResource(file));
                Notification.show("Success!", "Your file was successfully uploaded to: " + dir + fName, Type.TRAY_NOTIFICATION);
            }
        };
        ImageUploader receiver = new ImageUploader(); 

        // Create the upload with a caption and set receiver later
        Upload upload = new Upload("Upload File Here:", receiver);
        upload.setButtonCaption("Start Upload");
        upload.addSucceededListener(receiver);
                
        // Put the components in a panel
        //Panel panel = new Panel("Cool Image Storage");
        //Layout panelContent = new VerticalLayout();
        //panelContent.addComponents(upload, embedded);
        //panel.setContent(panelContent);
        vert.addComponent(embedded);
        hor.addComponents(upload, vert);
		
	}


}

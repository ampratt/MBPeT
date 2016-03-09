package com.aaron.mbpet.views.tabs;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import org.vaadin.aceeditor.AceEditor;
import org.vaadin.aceeditor.AceMode;


import com.aaron.mbpet.MbpetUI;
import com.aaron.mbpet.domain.Adapter;
import com.aaron.mbpet.domain.TestSession;
import com.aaron.mbpet.services.AceUtils;
import com.aaron.mbpet.services.FileSystemUtils;
import com.aaron.mbpet.views.adapters.AdapterEditor;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.util.BeanItem;
import com.vaadin.event.FieldEvents.TextChangeEvent;
import com.vaadin.event.FieldEvents.TextChangeListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.combobox.FilteringMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;

public class AdapterTab extends Panel implements Button.ClickListener {

	AceEditor editor;
	private TextField textualInput = new TextField();
	private Button generateButton = new Button("Submit Data to Graph");
	private TestSession currsession;
	private ComboBox themeBox;
	private Button saveButton;

	List<String> themeList;
	String[] themes = {"ambiance", "chrome", "clouds", "cobalt", "dreamweaver", "eclipse", "github", "terminal", "twilight", "xcode"};
    String basepath = "C:/dev/git/alternate/mbpet/MBPeT/WebContent";	//VaadinService.getCurrent().getBaseDirectory().getAbsolutePath();
    String webContent = ((MbpetUI) UI.getCurrent()).getWebContent();
    String defaultsettingsfile = webContent + "/WEB-INF/tmp/adapter_default.py";
    
	JPAContainer<Adapter> adapterscontainer = ((MbpetUI) UI.getCurrent()).getAdapterscontainer();
	Adapter currentAdapter;
	BeanItem<Adapter> beanItem;


	public AdapterTab(TestSession currsession) {		//TestSession currsession
//		setSizeFull();
//		setMargin(true);
//		setSpacing(true);
	
		editor = new AceEditor();
		this.currsession = currsession;
		this.currentAdapter = adapterscontainer.getItem(currsession.getAdapter().getId()).getEntity();
//		this.beanItem = new BeanItem<Adapter>(bean)

				
		setHeight("100%");
		setWidth("100%");
		addStyleName("borderless");
			
		VerticalLayout content = new VerticalLayout();
		content.setMargin(new MarginInfo(false, false, true, true));
//		content.setWidth("100%");
		content.addComponent(buildButtons());	
		content.addComponent(buildAceEditor());

		setContent(content);
		

//	    addComponent(new Label("<h3><i>Upload file or write code below to send adapter settings to master</i></h3>", ContentMode.HTML));	//layout.
//		browseForFile();
//	    
//		editor = new AceEditor();
//		addComponent(new AceEditorLayoutDirectory(editor));

	    //initDiagram();
	}
	
//	private void buildAceEditor(){
//		editor = new AceEditor();
//		AceEditorLayoutDirectory acelayout = new AceEditorLayoutDirectory(editor);
//	}
	
	private Component buildButtons() {
        // Horizontal Layout
        HorizontalLayout h = new HorizontalLayout(); 
        h.setWidth("100%");
        h.setSpacing(true);    

        themeList = Arrays.asList(themes);
        themeBox = new ComboBox("", themeList);
//        modeBox.setContainerDataSource(modeList);
//        modeBox.setWidth(100.0f, Unit.PERCENTAGE);
        themeBox.setWidth(9, Unit.EM);
        themeBox.addStyleName("tiny");
//        themeBox.setInputPrompt("No style selected");
        themeBox.setFilteringMode(FilteringMode.CONTAINS);
        themeBox.setImmediate(true);
        themeBox.setNullSelectionAllowed(false);
        themeBox.setValue(themeList.get(1));        
        themeBox.addValueChangeListener(new ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
            	AceUtils.setAceTheme(editor, event.getProperty().getValue().toString());
            }
        });
        
        
        saveButton = new Button("Save", this);
        saveButton.setIcon(FontAwesome.SAVE);
//        saveButton.addStyleName("borderless-colored");	//borderless-
        saveButton.addStyleName("tiny");
		saveButton.addStyleName("primary");
//        saveButton.addStyleName("icon-only");
        saveButton.setDescription("save adapter");
        saveButton.setEnabled(false);
	    
		
		h.addComponents(saveButton, themeBox);	//themeBox
		h.setComponentAlignment(saveButton, Alignment.BOTTOM_RIGHT);
		h.setComponentAlignment(themeBox, Alignment.BOTTOM_LEFT);
		h.setExpandRatio(themeBox, 1);

		return h;
		
	}
	
	
	private AceEditor buildAceEditor() {
//		System.out.println("ADAPTER FILE : " + defaultsettingsfile);
		// Ace Editor
		try {
			if (currentAdapter.getAdapter_file() == null) {
				loadExampleSettings();
//				editor.setValue("Fill in parameters for Test Session '" + currsession.getTitle() + "'");	//("Hello world!\nif:\n\tthen \ndo that\n...");			
			} else {
				editor.setValue(currentAdapter.getAdapter_file());
			}
		} catch (NullPointerException e1) {
			e1.printStackTrace();
			loadExampleSettings();
//			editor.setValue("Fill in parameters for Test Session '" + currsession.getTitle() + "'");	//("Hello world!\nif:\n\tthen \ndo that\n...");			
		} 
		
			// use static hosted files for theme, mode, worker
//			editor.setThemePath("/static/ace");
//			editor.setModePath("/static/ace");
//			editor.setWorkerPath("/static/ace");
		editor.setWidth("90%");
		editor.setHeight("425px");
		editor.setReadOnly(false); 
		editor.setMode(AceMode.python);
//		setEditorMode(fileFormat);
//		editor.setUseWorker(true);
//		editor.setTheme(AceTheme.twilight);	
//		editor.setWordWrap(false);
//		editor.setShowInvisibles(false);
//		System.out.println(editor.getValue());
		
		// Use worker (if available for the current mode)
		//editor.setUseWorker(true);
		editor.addTextChangeListener(new TextChangeListener() {
		    @Override
		    public void textChange(TextChangeEvent event) {
//		        Notification.show("Text: " + event.getText());
		        saveButton.setEnabled(true);
		    }
		});
//		new SuggestionExtension(new MySuggester()).extend(editor);		
		
		return editor;
	}
	
	
    public void buttonClick(ClickEvent event) {
        if (event.getButton() == saveButton) {
			String s = editor.getValue();
//			saveToFile(s, testDir);	//+aceOutFileField.getValue());
			
			// commit settings file from Ace
			new AdapterEditor(currentAdapter, currsession, s);
			currentAdapter = adapterscontainer.getItem(currentAdapter.getId()).getEntity();
			
//			// update Form view
//			formAceView.bindFormtoBean(currentAdapter);
////			for (Object pid : beanItem.getItemPropertyIds()) {beanItem.getItemProperty(pid).setValue(currParameters)}	        
			
			saveButton.setEnabled(false);

			// edit models directory name
//      	  	FileSystemUtils fileUtils = new FileSystemUtils();
//			// write settings file to disk
//			fileUtils.writeAdapterToDisk(	//username, sut, session, settings_file)
//					currsession.getParentcase().getOwner().getUsername(),
//					currsession.getParentcase().getTitle(), 
//					currsession.getTitle(), 
//					currentAdapter.getAdapter_file());
			
//			Notification.show(s, Type.WARNING_MESSAGE);
        }

    }
    
    
    
    private void loadExampleSettings() {
		System.out.println("SETTINGS FILE : " + defaultsettingsfile);

		StringBuilder builder = new StringBuilder();
		Scanner scan = null;
		try {
			scan = new Scanner(new FileReader(defaultsettingsfile));
			while (scan.hasNextLine()) {		
				builder.append(scan.nextLine()).append(System.getProperty("line.separator"));
			}	
			System.out.println(builder.toString());
			editor.setValue(builder.toString());
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		
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
//	
//	
//	
//		public void browseForFile() {
//		// horizontal layout
//        final VerticalLayout vert = new VerticalLayout();
//        final Label confirm = new Label("Your file was successfully uploaded to: D:\\");
//    	final HorizontalLayout hor = new HorizontalLayout();
//		hor.setWidth("100%");
//		hor.setSpacing(true);
//        addComponent(hor);
//               
//        /**
//         * Upload Example
//         */
//        // Show uploaded file in this placeholder
//        final Embedded embedded = new Embedded("Uploaded File");
//        embedded.setVisible(false);
//        
//        // Implement both receiver that saves upload in a file and
//        // listener for successful upload
//        class ImageUploader implements Receiver, SucceededListener {
//            String fName = null;
//            String dir = "D:\\";
//        	public File file;
//            
//            public OutputStream receiveUpload(String filename, String mimeType) {
//            	fName = filename;
//            	//vert.removeComponent(confirm);
//                // Create upload stream
//                FileOutputStream fos = null; // Stream to write to
//                try {
//                    // Open the file for writing.
//                    file = new File(dir + filename);	// /tmp/uploads/
//                    fos = new FileOutputStream(file);
//                } catch (final java.io.FileNotFoundException e) {
//                    new Notification("Could not open file<br/>",
//                                     e.getMessage(),
//                                     Notification.Type.ERROR_MESSAGE)
//                        .show(Page.getCurrent());
//                    return null;
//                }
//                return fos; // Return the output stream to write to
//            }
//
//            public void uploadSucceeded(SucceededEvent event) {
//                // Show the uploaded file in the image viewer
//            	//confirm.setValue("Your file was successfully uploaded to: D:\\");
//            	//vert.addComponent(confirm);
//                embedded.setVisible(true);
//                embedded.setSource(new FileResource(file));
//                Notification.show("Success!", "Your file was successfully uploaded to: " + dir + fName, Type.TRAY_NOTIFICATION);
//            }
//        };
//        ImageUploader receiver = new ImageUploader(); 
//
//        // Create the upload with a caption and set receiver later
//        Upload upload = new Upload("Upload File Here:", receiver);
//        upload.setButtonCaption("Start Upload");
//        upload.addSucceededListener(receiver);
//                
//        // Put the components in a panel
//        //Panel panel = new Panel("Cool Image Storage");
//        //Layout panelContent = new VerticalLayout();
//        //panelContent.addComponents(upload, embedded);
//        //panel.setContent(panelContent);
//        vert.addComponent(embedded);
//        hor.addComponents(upload, vert);
//		
//	}


}

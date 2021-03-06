package com.aaron.mbpet.views.tabs.adapterstabs;

import com.aaron.mbpet.domain.TestSession;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TabSheet.SelectedTabChangeEvent;
import com.vaadin.ui.themes.ValoTheme;

public class AdaptersTab extends TabSheet {   

	AdapterPythonTab pythonTab;
	AdapterXMLTab xmlTab;
	
	TestSession currsession;
	
    public AdaptersTab() {
    }

    @SuppressWarnings("deprecation")
	public AdaptersTab(final TestSession currsession) {	//TestSession currsession
//        setSizeFull();

        this.currsession = currsession;
        
        setHeight(100.0f, Unit.PERCENTAGE);
        addStyleName(ValoTheme.TABSHEET_PADDED_TABBAR);
//        addStyleName(ValoTheme.TABSHEET_FRAMED);
//        addStyleName(ValoTheme.TABSHEET_EQUAL_WIDTH_TABS);

        pythonTab = new AdapterPythonTab(currsession);
        xmlTab = new AdapterXMLTab(currsession);
        
        addTab(pythonTab, "Python Adapter");
        addTab(xmlTab, "XML Adapter");

        addListener(new TabSheet.SelectedTabChangeListener() {
            @Override
            public void selectedTabChange(SelectedTabChangeEvent event) {
                if (event.getTabSheet().getSelectedTab() == pythonTab) {
                	//focus on editor
                	pythonTab.editor.focus();
                	pythonTab.editor.setCursorPosition(0);
                }
                if (event.getTabSheet().getSelectedTab() == xmlTab) {
                	xmlTab.editor.focus();
                	xmlTab.editor.setCursorPosition(0);                
                }
            }
        });
//        VerticalLayout configTab = new VerticalLayout();
//        configTab.setHeight("100%");
////        configTab.addComponent(buildConfigTabs());
        
        
    }

	
}





//extends Panel implements Button.ClickListener {
//
//	AceEditor editor;
//	private TestSession currsession;
//	private ComboBox modeBox;
//	private ComboBox themeBox;
//	private Button saveButton;
//
//    List<String> modeList;
//    String[] modes = {"python", "xml"};
//	List<String> themeList;
//	String[] themes = {"ambiance", "chrome", "clouds", "cobalt", "dreamweaver", "eclipse", "github", "terminal", "twilight", "xcode"};
//    String basepath = "C:/dev/git/alternate/mbpet/MBPeT/WebContent";	//VaadinService.getCurrent().getBaseDirectory().getAbsolutePath();
//    String webContent = ((MbpetUI) UI.getCurrent()).getWebContent();
//    String defaultsettingsfile = webContent + "/WEB-INF/tmp/adapter_default.py";
//    
//	JPAContainer<Adapter> adapterscontainer = ((MbpetUI) UI.getCurrent()).getAdapterscontainer();
//	Adapter currentAdapter;
//	BeanItem<Adapter> beanItem;
//
//
//	public AdapterTab(TestSession currsession) {		//TestSession currsession
////		setSizeFull();
////		setMargin(true);
////		setSpacing(true);
//	
//		editor = new AceEditor();
//		this.currsession = currsession;
//		this.currentAdapter = adapterscontainer.getItem(currsession.getAdapter().getId()).getEntity();
////		this.beanItem = new BeanItem<Adapter>(bean)
//
//				
//		setHeight("100%");
//		setWidth("100%");
//		addStyleName("borderless");
//			
//		VerticalLayout content = new VerticalLayout();
//		content.setMargin(new MarginInfo(false, false, true, true));
////		content.setWidth("100%");
//		content.addComponent(buildButtons());	
//		content.addComponent(buildAceEditor());
//
//		setContent(content);
//		
//
////	    addComponent(new Label("<h3><i>Upload file or write code below to send adapter settings to master</i></h3>", ContentMode.HTML));	//layout.
////		browseForFile();
////	    
////		editor = new AceEditor();
////		addComponent(new AceEditorLayoutDirectory(editor));
//
//	    //initDiagram();
//	}
//	
////	private void buildAceEditor(){
////		editor = new AceEditor();
////		AceEditorLayoutDirectory acelayout = new AceEditorLayoutDirectory(editor);
////	}
//	
//	private Component buildButtons() {
//        // Horizontal Layout
//        HorizontalLayout h = new HorizontalLayout(); 
//        h.setWidth("100%");
//        h.setSpacing(true);    
//
//        themeList = Arrays.asList(themes);
//        themeBox = new ComboBox("theme", themeList);
////        modeBox.setContainerDataSource(modeList);
////        modeBox.setWidth(100.0f, Unit.PERCENTAGE);
//        themeBox.setWidth(9, Unit.EM);
//        themeBox.addStyleName("tiny");
////        themeBox.setInputPrompt("No style selected");
//        themeBox.setFilteringMode(FilteringMode.CONTAINS);
//        themeBox.setImmediate(true);
//        themeBox.setNullSelectionAllowed(false);
//        themeBox.setValue(themeList.get(1));        
//        themeBox.addValueChangeListener(new ValueChangeListener() {
//            @Override
//            public void valueChange(Property.ValueChangeEvent event) {
//            	AceUtils.setAceTheme(editor, event.getProperty().getValue().toString());
//            }
//        });
//        
//        modeList = Arrays.asList(modes);
//        modeBox = new ComboBox("code style:", modeList);
////        modeBox.setContainerDataSource(modes);
////        modeBox.setWidth(100.0f, Unit.PERCENTAGE);
//        modeBox.setWidth(7, Unit.EM);
//        modeBox.addStyleName("tiny");
//        modeBox.setInputPrompt("No style selected");
//        modeBox.setFilteringMode(FilteringMode.CONTAINS);
//        modeBox.setImmediate(true);
//        modeBox.setNullSelectionAllowed(false);
//        modeBox.setValue(modeList.get(0));        
//        modeBox.addValueChangeListener(new ValueChangeListener() {
//            @Override
//            public void valueChange(Property.ValueChangeEvent event) {
////                Notification.show("mode changed to: " + event.getProperty().getValue().toString());
//                setEditorMode(event.getProperty().getValue().toString());
//            }
//        });
//        
//        saveButton = new Button("Save", this);
//        saveButton.setIcon(FontAwesome.SAVE);
////        saveButton.addStyleName("borderless-colored");	//borderless-
//        saveButton.addStyleName("tiny");
//		saveButton.addStyleName("primary");
////        saveButton.addStyleName("icon-only");
//        saveButton.setDescription("save adapter");
//        saveButton.setEnabled(false);
//	    
//		
//		h.addComponents(themeBox, modeBox, saveButton);	//themeBox
//		h.setComponentAlignment(themeBox, Alignment.BOTTOM_LEFT);
//		h.setComponentAlignment(modeBox, Alignment.BOTTOM_LEFT);
//		h.setComponentAlignment(saveButton, Alignment.BOTTOM_LEFT);	//BOTTOM_RIGHT);
//		h.setExpandRatio(saveButton, 1);
//
//		return h;
//		
//	}
//	
//	
//	private AceEditor buildAceEditor() {
////		System.out.println("ADAPTER FILE : " + defaultsettingsfile);
//		// Ace Editor
//		try {
//			if (currentAdapter.getAdapter_file() == null) {
//				loadExampleSettings();
////				editor.setValue("Fill in parameters for Test Session '" + currsession.getTitle() + "'");	//("Hello world!\nif:\n\tthen \ndo that\n...");			
//			} else {
//				editor.setValue(currentAdapter.getAdapter_file());
//			}
//		} catch (NullPointerException e1) {
//			e1.printStackTrace();
//			loadExampleSettings();
////			editor.setValue("Fill in parameters for Test Session '" + currsession.getTitle() + "'");	//("Hello world!\nif:\n\tthen \ndo that\n...");			
//		} 
//		
//			// use static hosted files for theme, mode, worker
////			editor.setThemePath("/static/ace");
////			editor.setModePath("/static/ace");
////			editor.setWorkerPath("/static/ace");
//		editor.setWidth("90%");
//		editor.setHeight("425px");
//		editor.setReadOnly(false); 
//		editor.setMode(AceMode.python);
////		setEditorMode(fileFormat);
////		editor.setUseWorker(true);
////		editor.setTheme(AceTheme.twilight);	
////		editor.setWordWrap(false);
////		editor.setShowInvisibles(false);
////		System.out.println(editor.getValue());
//		
//		// Use worker (if available for the current mode)
//		//editor.setUseWorker(true);
//		editor.addTextChangeListener(new TextChangeListener() {
//		    @Override
//		    public void textChange(TextChangeEvent event) {
////		        Notification.show("Text: " + event.getText());
//		        saveButton.setEnabled(true);
//		    }
//		});
////		new SuggestionExtension(new MySuggester()).extend(editor);		
//		
//		return editor;
//	}
//	
//	
//    public void buttonClick(ClickEvent event) {
//        if (event.getButton() == saveButton) {
////			String adapterString = editor.getValue();
////			saveToFile(s, testDir);	//+aceOutFileField.getValue());
//			
//        	System.out.println("editor mode:" +modeBox.getValue());
//			// commit settings file from Ace
//			new AdapterEditor(currentAdapter, currsession, editor.getValue(), modeBox.getValue().toString());
//			currentAdapter = adapterscontainer.getItem(currentAdapter.getId()).getEntity();
//			
////			// update Form view
////			formAceView.bindFormtoBean(currentAdapter);
//////			for (Object pid : beanItem.getItemPropertyIds()) {beanItem.getItemProperty(pid).setValue(currParameters)}	        
//			
//			saveButton.setEnabled(false);
//
//			// edit models directory name
////      	  	FileSystemUtils fileUtils = new FileSystemUtils();
////			// write settings file to disk
////			fileUtils.writeAdapterToDisk(	//username, sut, session, settings_file)
////					currsession.getParentcase().getOwner().getUsername(),
////					currsession.getParentcase().getTitle(), 
////					currsession.getTitle(), 
////					currentAdapter.getAdapter_file());
//			
////			Notification.show(s, Type.WARNING_MESSAGE);
//        }
//
//    }
//    
//	public void setEditorMode(String mode) {
//		if (mode.equals("py") || mode.equals("python")) {
//			editor.setMode(AceMode.python);	
//			modeBox.setValue(modeList.get(0));
////			System.out.println("Mode changed to python");
//		} else if (mode.equals("xml")) {
//			editor.setMode(AceMode.xml);	
//			modeBox.setValue(modeList.get(1));
////			System.out.println("Mode changed to xml");
//		} 
//	}
//    
//    private void loadExampleSettings() {
//		System.out.println("SETTINGS FILE : " + defaultsettingsfile);
//
//		StringBuilder builder = new StringBuilder();
//		Scanner scan = null;
//		try {
//			scan = new Scanner(new FileReader(defaultsettingsfile));
//			while (scan.hasNextLine()) {		
//				builder.append(scan.nextLine()).append(System.getProperty("line.separator"));
//			}	
//			System.out.println(builder.toString());
//			editor.setValue(builder.toString());
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}		
//		
//	}
//    
////	private void buildAceEditor() {
////		// Ace Editor
////		editor.setValue("Hello world!\nif:\n\tthen \ndo that\n...");
////		editor.setWidth("70%");		
////		//editor.setWordWrap(false);
////		editor.setReadOnly(false);
////		//editor.setShowInvisibles(false);
////		editor.setMode(AceMode.python);
////		//editor.setTheme(AceTheme.twilight);	
////
////		// Use worker (if available for the current mode)
////		//editor.setUseWorker(true);
////		editor.addTextChangeListener(new TextChangeListener() {
////		    @Override
////		    public void textChange(TextChangeEvent event) {
////		        Notification.show("Text: " + event.getText());
////		    }
////		});
////		
////		editor.addSelectionChangeListener(new SelectionChangeListener() {
////		    @Override
////		    public void selectionChanged(SelectionChangeEvent e) {
////		        int cursor = e.getSelection().getCursorPosition();
////		        //Notification.show("Cursor at: " + cursor);
////		    }
////		});
////		addComponent(editor);
////
////		//new SuggestionExtension(new MySuggester()).extend(editor);
////
////		Button button = new Button("Get Current Code");
////		button.addClickListener(new Button.ClickListener() {
////			public void buttonClick(ClickEvent event) {
////				String s = editor.getValue();
////				Label label = new Label(s);
////				//layout.addComponent(label);
////				//testing purposes
////				Notification.show(editor.getValue(), Type.WARNING_MESSAGE);
////			}
////		});
////		addComponent(button);
////
////		new SuggestionExtension(new MySuggester()).extend(editor);		
////	}
////	
////	
////	
////		public void browseForFile() {
////		// horizontal layout
////        final VerticalLayout vert = new VerticalLayout();
////        final Label confirm = new Label("Your file was successfully uploaded to: D:\\");
////    	final HorizontalLayout hor = new HorizontalLayout();
////		hor.setWidth("100%");
////		hor.setSpacing(true);
////        addComponent(hor);
////               
////        /**
////         * Upload Example
////         */
////        // Show uploaded file in this placeholder
////        final Embedded embedded = new Embedded("Uploaded File");
////        embedded.setVisible(false);
////        
////        // Implement both receiver that saves upload in a file and
////        // listener for successful upload
////        class ImageUploader implements Receiver, SucceededListener {
////            String fName = null;
////            String dir = "D:\\";
////        	public File file;
////            
////            public OutputStream receiveUpload(String filename, String mimeType) {
////            	fName = filename;
////            	//vert.removeComponent(confirm);
////                // Create upload stream
////                FileOutputStream fos = null; // Stream to write to
////                try {
////                    // Open the file for writing.
////                    file = new File(dir + filename);	// /tmp/uploads/
////                    fos = new FileOutputStream(file);
////                } catch (final java.io.FileNotFoundException e) {
////                    new Notification("Could not open file<br/>",
////                                     e.getMessage(),
////                                     Notification.Type.ERROR_MESSAGE)
////                        .show(Page.getCurrent());
////                    return null;
////                }
////                return fos; // Return the output stream to write to
////            }
////
////            public void uploadSucceeded(SucceededEvent event) {
////                // Show the uploaded file in the image viewer
////            	//confirm.setValue("Your file was successfully uploaded to: D:\\");
////            	//vert.addComponent(confirm);
////                embedded.setVisible(true);
////                embedded.setSource(new FileResource(file));
////                Notification.show("Success!", "Your file was successfully uploaded to: " + dir + fName, Type.TRAY_NOTIFICATION);
////            }
////        };
////        ImageUploader receiver = new ImageUploader(); 
////
////        // Create the upload with a caption and set receiver later
////        Upload upload = new Upload("Upload File Here:", receiver);
////        upload.setButtonCaption("Start Upload");
////        upload.addSucceededListener(receiver);
////                
////        // Put the components in a panel
////        //Panel panel = new Panel("Cool Image Storage");
////        //Layout panelContent = new VerticalLayout();
////        //panelContent.addComponents(upload, embedded);
////        //panel.setContent(panelContent);
////        vert.addComponent(embedded);
////        hor.addComponents(upload, vert);
////		
////	}
//
//
//}

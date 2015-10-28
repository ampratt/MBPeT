package com.aaron.mbpet.components.aceeditor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import org.vaadin.aceeditor.AceEditor;
import org.vaadin.aceeditor.AceMode;
import org.vaadin.aceeditor.AceEditor.SelectionChangeEvent;
import org.vaadin.aceeditor.AceEditor.SelectionChangeListener;

import com.aaron.mbpet.domain.DbUtils;
import com.aaron.mbpet.domain.Parameters;
import com.aaron.mbpet.domain.TestSession;
import com.aaron.mbpet.views.MBPeTMenu;
import com.aaron.mbpet.views.parameters.ParametersEditor;
import com.aaron.mbpet.views.sessions.SessionViewer;
import com.google.gwt.thirdparty.guava.common.io.Files;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ReadOnlyException;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.util.converter.Converter.ConversionException;
import com.vaadin.event.FieldEvents.TextChangeEvent;
import com.vaadin.event.FieldEvents.TextChangeListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.VaadinService;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.combobox.FilteringMode;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Notification.Type;

@SuppressWarnings("serial")
public class ParametersAceEditorLayout extends VerticalLayout implements Button.ClickListener {

	AceEditor editor;// = new AceEditor();
	ComboBox modeBox;
	Button saveButton;
	Button loadButton;

    List<String> modeList;
    String fileFormat = "dot";
    String[] modes = {"python", "dot", "gv"};
    String testDir = "C:/dev/git/alternate/mbpet/MBPeT/WebContent/META-INF/output/settings.py";

    TestSession currsession;
    Parameters currParameters;
    JPAContainer<Parameters> parameters = MBPeTMenu.parameters;
    
    String basepath = "C:/dev/git/alternate/mbpet/MBPeT/WebContent";	//VaadinService.getCurrent().getBaseDirectory().getAbsolutePath();
    String defaultsettingsfile = basepath + "/WEB-INF/tmp/settings.py";
    
	public ParametersAceEditorLayout(AceEditor editor, String fileFormat) {	// TestSession currsession
		setSizeFull();
		setMargin(new MarginInfo(false, true, true, true));
//		setMargin(true);
//		setSpacing(true);
		
		this.editor = editor; //= new AceEditor()
		this.fileFormat = fileFormat;
		this.currsession = SessionViewer.currsession;
		this.currParameters = currsession.getParameters();//parameters.getItem(currsession.getParameters().getId()).getEntity(); //currsession.getParameters();
//		System.out.println("Current parameters from session is ->" + currsession.getParameters().getId() +
//							" - owned by session ->" + currsession.getId() + "-" + currsession.getTitle());
//		currParameters.setSettings_file((String) DbUtils.readFromDb(currParameters.getId()));
		
//        addComponent(new Label("<h3>Give Test Parameters in settings.py file</h3>", ContentMode.HTML));
		addComponent(buildButtons());	
		addComponent(buildAceEditor());
	}

	private Component buildButtons() {
        // Horizontal Layout
        HorizontalLayout h = new HorizontalLayout();        
        h.setSpacing(true);    

        modeList = Arrays.asList(modes);
        modeBox = new ComboBox("code style", modeList);
//        modeBox.setContainerDataSource(modes);
        modeBox.setWidth(8, Unit.EM);
        modeBox.addStyleName("tiny");
        modeBox.setInputPrompt("No style selected");
        modeBox.setFilteringMode(FilteringMode.CONTAINS);
        modeBox.setImmediate(true);
        modeBox.setNullSelectionAllowed(false);
        modeBox.setValue(modeList.get(0));        
        modeBox.addValueChangeListener(new ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
//                Notification.show("mode changed to: " + event.getProperty().getValue().toString());
                setEditorMode(event.getProperty().getValue().toString());
            }
        });

//		aceOutFileField.setCaption("Give file name");
//		aceOutFileField.setWidth("20em");
//        aceOutFileField.setInputPrompt("settings.py");	//("C:/dev/output/ace-editor-output.dot");
//        aceOutFileField.setValue("settings.py");
//        h.addComponent(aceOutFileField);
	
//		aceInFileField.setCaption("Give file name");
//		aceInFileField.setWidth("20em");
//		aceInFileField.setInputPrompt("settings.py");	//("C:/dev/output/ace-editor-output.dot");
//		aceInFileField.setValue("settings.py");
//		h.addComponent(aceInFileField);
        
        saveButton = new Button("Save", this);
        saveButton.setIcon(FontAwesome.SAVE);
        saveButton.addStyleName("tiny");
		saveButton.addStyleName("primary");
//        saveButton.addStyleName("icon-only");
//        saveButton.removeStyleName("borderless-colored");
        saveButton.setDescription("save Parameters");
        saveButton.setEnabled(false);
	    
		loadButton = new Button("Load", this);
		loadButton.setIcon(FontAwesome.CLIPBOARD);
//		loadButton.addStyleName("colored");	//borderless-
		loadButton.addStyleName("small");
		loadButton.addStyleName("icon-only");
		loadButton.setDescription("load parameters");
        
		h.addComponent(modeBox);
		h.addComponent(saveButton);
//		h.addComponent(loadButton);
		h.setComponentAlignment(saveButton, Alignment.BOTTOM_LEFT);
//		h.setComponentAlignment(loadButton, Alignment.BOTTOM_LEFT);
		
		return h;
		
	}
	
	

	private AceEditor buildAceEditor() {
		
		System.out.println("SETTINS FILE : " + defaultsettingsfile);
		// Ace Editor
		try {
			if (currParameters.getSettings_file() == null) {
				loadExampleSettings();
//				editor.setValue("Fill in parameters for Test Session '" + currsession.getTitle() + "'");	//("Hello world!\nif:\n\tthen \ndo that\n...");			
			} else {
				editor.setValue(currParameters.getSettings_file());
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
		editor.setWidth("100%");
		editor.setHeight("400px");
		editor.setReadOnly(false); 
		setEditorMode(fileFormat);
//		editor.setMode(AceMode.python);
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
		
		editor.addSelectionChangeListener(new SelectionChangeListener() {
		    @Override
		    public void selectionChanged(SelectionChangeEvent e) {
		        int cursor = e.getSelection().getCursorPosition();
//		        Notification.show("Cursor at: " + cursor);
		    }
		});
		
		return editor;

//		new SuggestionExtension(new MySuggester()).extend(editor);		
	}
	
	
	

	public void buttonClick(ClickEvent event) {
        if (event.getButton() == saveButton) {
			String s = editor.getValue();
//			saveToFile(s, testDir);	//+aceOutFileField.getValue());
			new ParametersEditor(currParameters, currsession, s);
			currParameters = parameters.getItem(currParameters.getId()).getEntity();
	        saveButton.setEnabled(false);

//			Notification.show(s, Type.WARNING_MESSAGE);
        }
//         else if (event.getButton() == loadButton) {
//			// load file to editor
////        	String settings = (String) DbUtils.readFromDb(currParameters.getId());
//			editor.setValue(currParameters.getSettings_file());	//settings currParameters.getSettings_file()
////			editor.setValue( loadFile(testDir)); //+aceInFileField.getValue()) );
//			
//			// set code style mode to match file type
////			setEditorMode(Files.getFileExtension(modeBox.getValue().toString()));
////			String extension = Files.getFileExtension(aceInFileField.getValue());	//FilenameUtils.getExtension(filename);
////			setEditorMode(Files.getFileExtension(aceInFileField.getValue()));
//        }

    }

	
	public void saveToFile(String output, String fileName) {
        // create file
//		String fileName = "C:/dev/output/ace-editor-output.dot";
        File file = new File(fileName);
        PrintWriter writer = null;
		try {
			writer = new PrintWriter(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
        writer.println( output );
        writer.close();
	
        //show confirmation to user
        Notification.show("dot file was saved at: " + fileName, Notification.Type.TRAY_NOTIFICATION);;
	}
	
	public void setEditorMode(String mode) {
//		String file = filename.replace("C:/", "");
//		file = file.replace("/", "\\");
		if (mode.equals("dot")) {
			editor.setMode(AceMode.dot);
			modeBox.setValue(modeList.get(1));
//			System.out.println("Mode changed to dot");
		} else if (mode.equals("gv")){
			editor.setMode(AceMode.dot);
			modeBox.setValue(modeList.get(2));
//			System.out.println("Mode changed to dot");
		} else if (mode.equals("py") || mode.equals("python")) {
			editor.setMode(AceMode.python);	
			modeBox.setValue(modeList.get(0));
//			System.out.println("Mode changed to python");
		} 
	}
	
	public String loadFile(String filename) {
		
		String input = "";
		BufferedReader br;
		try {
			Scanner sc = new Scanner(new FileReader(filename));
			String line = null;
			while (sc.hasNextLine()) {
				line = sc.nextLine();
				System.out.println(line);
				input = input.concat(line);
				if(sc.hasNextLine()) {
					input = input.concat("\n");
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return input;
	}


    private void loadExampleSettings() {
		System.out.println("SETTINS FILE : " + defaultsettingsfile);

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
    
    
}

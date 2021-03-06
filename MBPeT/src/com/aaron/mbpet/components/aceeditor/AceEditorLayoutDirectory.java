//package com.aaron.mbpet.components.aceeditor;
//
//import java.io.BufferedReader;
//import java.io.File;
//import java.io.FileNotFoundException;
//import java.io.FileReader;
//import java.io.IOException;
//import java.io.PrintWriter;
//import java.util.Arrays;
//import java.util.List;
//import java.util.Scanner;
//
//import org.vaadin.aceeditor.AceEditor;
//import org.vaadin.aceeditor.AceMode;
//import org.vaadin.aceeditor.AceEditor.SelectionChangeEvent;
//import org.vaadin.aceeditor.AceEditor.SelectionChangeListener;
//
//import com.google.gwt.thirdparty.guava.common.io.Files;
//import com.vaadin.data.Property;
//import com.vaadin.data.Property.ValueChangeListener;
//import com.vaadin.event.FieldEvents.TextChangeEvent;
//import com.vaadin.event.FieldEvents.TextChangeListener;
//import com.vaadin.shared.ui.combobox.FilteringMode;
//import com.vaadin.shared.ui.label.ContentMode;
//import com.vaadin.ui.Button;
//import com.vaadin.ui.ComboBox;
//import com.vaadin.ui.Component;
//import com.vaadin.ui.HorizontalLayout;
//import com.vaadin.ui.Label;
//import com.vaadin.ui.Notification;
//import com.vaadin.ui.TextField;
//import com.vaadin.ui.VerticalLayout;
//import com.vaadin.ui.Button.ClickEvent;
//import com.vaadin.ui.Notification.Type;
//
//@SuppressWarnings("serial")
//public class AceEditorLayoutDirectory extends VerticalLayout {
//
////	final VerticalLayout layout = new VerticalLayout();
//	AceEditor editor;// = new AceEditor();
//    final TextField aceOutFileField = new TextField();
//    final TextField aceInFileField = new TextField();
//    ComboBox modeBox;
//    List<String> modeList;
//    String testDir = "C:/dev/git/alternate/mbpet/MBPeT/WebContent/META-INF/output/";
//    
//	public AceEditorLayoutDirectory(AceEditor editor) {
//		setSizeFull();
//		setMargin(true);
//		setSpacing(true);
//		this.editor = editor; //= new AceEditor()
//		initLayout();	
//	}
//
//	private void initLayout() {
//		// set main content
////		layout.setMargin(true);
////		layout.setSpacing(true);
//////		layout.setSizeFull();
////		setContent(layout);
//        
//        //layout.
//        addComponent(new Label("<h2>Diagram Ace Combo example</h2>", ContentMode.HTML));
//
//        String[] modes = {"dot", "python", "gv"};
//        modeList = Arrays.asList(modes);
//        modeBox = new ComboBox("Select source code style", modeList);
////        modeBox.setContainerDataSource(modes);
////        modeBox.setWidth(100.0f, Unit.PERCENTAGE);
//        modeBox.addStyleName("tiny");
//        modeBox.setInputPrompt("No style selected");
//        modeBox.setFilteringMode(FilteringMode.CONTAINS);
//        modeBox.setImmediate(true);
//        modeBox.setNullSelectionAllowed(false);
//        modeBox.setValue(modeList.get(0));        
//        modeBox.addValueChangeListener(new ValueChangeListener() {
//            @Override
//            public void valueChange(Property.ValueChangeEvent event) {
//                Notification.show("mode changed to: " + event.getProperty().getValue().toString());
//                setEditorMode(event.getProperty().getValue().toString(), editor);
//            }
//        });
//        //layout.
//        addComponent(modeBox);
//        
//        // Horizontal Layout
//        HorizontalLayout h = new HorizontalLayout();
//        addComponent(h);
//        VerticalLayout v = new VerticalLayout();
//        h.setWidth("100%");
//        h.setSpacing(true);
//        h.addComponent(buildAceEditor());
//        h.addComponent(v);
//        h.setExpandRatio(editor, 2);
//        h.setExpandRatio(v, 1);
//		
//		aceOutFileField.setCaption("Give file name");
//		aceOutFileField.setWidth("20em");
//        aceOutFileField.setInputPrompt("settings.py");	//("C:/dev/output/ace-editor-output.dot");
//        aceOutFileField.setValue("settings.py");
//        
//        //layout.
//        v.addComponent(aceOutFileField);
//
//		Button saveButton = new Button("Save");
//		saveButton.addClickListener(new Button.ClickListener() {
//			public void buttonClick(ClickEvent event) {
//				String s = editor.getValue();
////				Label label = new Label(s);
//				//layout.addComponent(label);
//				//testing purposes
//				Notification.show(s, Type.WARNING_MESSAGE);
//				saveToFile(s, testDir+aceOutFileField.getValue());
//			}
//		});
//		//layout.
//		v.addComponent(saveButton);
//		
//		aceInFileField.setCaption("Give file name");
//		aceInFileField.setWidth("20em");
//		aceInFileField.setInputPrompt("settings.py");	//("C:/dev/output/ace-editor-output.dot");
//		aceInFileField.setValue("settings.py");
//        //layout.
//		v.addComponent(aceInFileField);
//        
//		Button loadButton = new Button("Load");
//		loadButton.addClickListener(new Button.ClickListener() {
//			public void buttonClick(ClickEvent event) {
//				// load file to editor
//				editor.setValue( loadFile(testDir+aceInFileField.getValue()) );
//				
//				// set code style mode to match file type
////				System.out.println(inFileField.getValue());
//				String extension = Files.getFileExtension(aceInFileField.getValue());	//FilenameUtils.getExtension(filename);
////				System.out.println("extension is " + extension);
//				setEditorMode(extension, editor);
//			}
//		});
//		//layout.
//		v.addComponent(loadButton);
//		
//	}
//	
//	
//
//	private AceEditor buildAceEditor() {
//		// Ace Editor
//		editor.setValue("Hello world!\nif:\n\tthen \ndo that\n...");
//			// use static hosted files for theme, mode, worker
////			editor.setThemePath("/static/ace");
////			editor.setModePath("/static/ace");
////			editor.setWorkerPath("/static/ace");
//		editor.setWidth("100%");		
//		editor.setReadOnly(false);
//		editor.setMode(AceMode.python);
////		editor.setUseWorker(true);
////		editor.setTheme(AceTheme.twilight);	
////		editor.setWordWrap(false);
////		editor.setReadOnly(false);
////		editor.setShowInvisibles(false);
////		System.out.println(editor.getValue());
//		
//		//layout.
////		addComponent(editor);
//
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
//		return editor;
//
////		new SuggestionExtension(new MySuggester()).extend(editor);		
//	}
//	
//	
//	
//	public void saveToFile(String output, String fileName) {
//        // create file
////		String fileName = "C:/dev/output/ace-editor-output.dot";
//        File file = new File(fileName);
//        PrintWriter writer = null;
//		try {
//			writer = new PrintWriter(file);
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//		}
//        writer.println( output );
//        writer.close();
//	
//        //show confirmation to user
//        Notification.show("dot file was saved at: " + fileName, Notification.Type.TRAY_NOTIFICATION);;
//	}
//	
//	public void setEditorMode(String mode, AceEditor editor) {
////		String file = filename.replace("C:/", "");
////		file = file.replace("/", "\\");
//		if (mode.equals("dot")) {
//			editor.setMode(AceMode.dot);
////			modeBox.setValue("dot");
//			modeBox.setValue(modeList.get(0));
//			System.out.println("Mode changed to dot");
//		} else if (mode.equals("gv")){
//			editor.setMode(AceMode.dot);
//			modeBox.setValue(modeList.get(2));
//			System.out.println("Mode changed to dot");
//		} else if (mode.equals("py") || mode.equals("python")) {
//			editor.setMode(AceMode.python);	
//			modeBox.setValue(modeList.get(1));
//			System.out.println("Mode changed to python");
//		} 
//	}
//	
//	public String loadFile(String filename) {
//		
//		String input = "";
//		BufferedReader br;
//		try {
//			Scanner sc = new Scanner(new FileReader(filename));
//			String line = null;
//			while (sc.hasNextLine()) {
//				line = sc.nextLine();
//				System.out.println(line);
//				input = input.concat(line);
//				if(sc.hasNextLine()) {
//					input = input.concat("\n");
//				}
//			}
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		return input;
//	}
//
//
//}

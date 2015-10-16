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

import com.aaron.mbpet.domain.Model;
import com.aaron.mbpet.domain.Parameters;
import com.aaron.mbpet.domain.TestSession;
import com.aaron.mbpet.views.MBPeTMenu;
import com.aaron.mbpet.views.models.ModelDBuilderWindow;
import com.aaron.mbpet.views.models.ModelTableAceView;
import com.aaron.mbpet.views.models.ModelUtils;
import com.aaron.mbpet.views.parameters.ParametersEditor;
import com.aaron.mbpet.views.sessions.SessionViewer;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.util.BeanItem;
import com.vaadin.event.FieldEvents.TextChangeEvent;
import com.vaadin.event.FieldEvents.TextChangeListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.shared.Position;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.combobox.FilteringMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Notification.Type;

public class ModelAceEditorLayout extends VerticalLayout implements Button.ClickListener{

	AceEditor editor;// = new AceEditor();
	TextField titleField;
	ComboBox modeBox;
	Button saveButton;
	Button launchDBuilderButton;
//    final TextField aceOutFileField = new TextField();
//    final TextField aceInFileField = new TextField();
    
    String fileFormat = "dot";
    List<String> modeList;
    String[] modes = {"dot", "gv", "python"};
    String testDir = "C:/dev/git/alternate/mbpet/MBPeT/WebContent/META-INF/output/settings.py";
    
    JPAContainer<Model> models = MBPeTMenu.models;
    Model currmodel;
    TestSession currsession;
	FieldGroup binder;
	BeanItem<Model> modelBeanItem;
	
	private boolean createNewModel = false;

	public ModelAceEditorLayout(AceEditor editor, String fileFormat) {	// TestSession currsession
		setSizeFull();
		setMargin(new MarginInfo(false, true, false, true));
//		setMargin(true);
//		setSpacing(true);
		
		this.editor = editor; //= new AceEditor()
		this.fileFormat = fileFormat;
		this.currsession = SessionViewer.currsession;
		
//        addComponent(new Label("<h3>Give Test Parameters in settings.py file</h3>", ContentMode.HTML));
		addComponent(buildButtons());	
		addComponent(buildAceEditor());
	}

	private Component buildButtons() {
        // Horizontal Layout
        HorizontalLayout h = new HorizontalLayout(); 
        h.setWidth("100%");
        h.setSpacing(true);    

        titleField = new TextField("Title:");
        titleField.addStyleName("tiny");
        titleField.setImmediate(true);
        titleField.addTextChangeListener(new TextChangeListener() {	
			@Override
			public void textChange(TextChangeEvent event) {
				saveButton.setEnabled(true);
				launchDBuilderButton.setEnabled(false);
			}
		});
        
        modeList = Arrays.asList(modes);
        modeBox = new ComboBox("code style:", modeList);
//        modeBox.setContainerDataSource(modes);
//        modeBox.setWidth(100.0f, Unit.PERCENTAGE);
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
        
        saveButton = new Button("Save", this);
        saveButton.setIcon(FontAwesome.SAVE);
//        saveButton.addStyleName("borderless-colored");	//borderless-
        saveButton.addStyleName("tiny");
		saveButton.addStyleName("primary");
//        saveButton.addStyleName("icon-only");
//        saveButton.removeStyleName("borderless-colored");
        saveButton.setDescription("save model");
        saveButton.setEnabled(false);
	    
		launchDBuilderButton = new Button("Draw Model", this);
		launchDBuilderButton.setIcon(FontAwesome.EXTERNAL_LINK);
		launchDBuilderButton.addStyleName("tiny");
//		loadButton.addStyleName("colored");	//borderless-
//		launchDBuilderButton.addStyleName("icon-only");
		launchDBuilderButton.setDescription("load parameters");
		launchDBuilderButton.setEnabled(false);
		
		h.addComponents(titleField, modeBox, saveButton, launchDBuilderButton);
		h.setComponentAlignment(titleField, Alignment.BOTTOM_LEFT);
		h.setComponentAlignment(modeBox, Alignment.BOTTOM_LEFT);
		h.setComponentAlignment(saveButton, Alignment.BOTTOM_LEFT);
		h.setComponentAlignment(launchDBuilderButton, Alignment.BOTTOM_RIGHT);
		h.setExpandRatio(saveButton, 1);
//		h.setExpandRatio(launchDBuilderButton, 0);
		
		return h;
		
	}
	
	

	private AceEditor buildAceEditor() {
			// use static hosted files for theme, mode, worker
//			editor.setThemePath("/static/ace");
//			editor.setModePath("/static/ace");
//			editor.setWorkerPath("/static/ace");
		editor.setWidth("100%");
		editor.setHeight("425px");
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
				launchDBuilderButton.setEnabled(false);
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
			
			Model editedmodel = null;
			if ( createNewModel == true ) {
				editedmodel = ModelUtils.createNewModel(currmodel, currsession, binder); //currmodel.getParentsession(),
				createNewModel = false;
			} else {
				editedmodel = ModelUtils.editModel(currmodel, currsession, binder); //currmodel.getParentsession(),				
			}
			
			
			ModelTableAceView.modelsTable.select(models.getItem(editedmodel.getId()).getEntity().getId());
			toggleEditorFields(true);
			setFieldsDataSource(models.getItem(editedmodel.getId()).getEntity());	//(models.getItem(currmodel.getId()).getEntity());
			editor.focus();

			launchDBuilderButton.setEnabled(true);

//	        saveButton.setEnabled(false);

        } else if (event.getButton() == launchDBuilderButton) {
        	// open diagram builder window
//        	UI.getCurrent().addWindow(new ModelDBuilderWindow(currmodel, editor));
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
			modeBox.setValue(modeList.get(0));
//			System.out.println("Mode changed to dot");
		} else if (mode.equals("gv")){
			editor.setMode(AceMode.dot);
			modeBox.setValue(modeList.get(2));
//			System.out.println("Mode changed to dot");
		} else if (mode.equals("py") || mode.equals("python")) {
			editor.setMode(AceMode.python);	
			modeBox.setValue(modeList.get(1));
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

	
	public void setFieldsDataSource(Model currmodel) {
		this.currmodel = currmodel;
		
		binder = new FieldGroup();
		modelBeanItem = new BeanItem<Model>(currmodel);		// takes item as argument
		binder.setItemDataSource(modelBeanItem);
		binder.bind(titleField, "title");
		binder.bind(editor, "dotschema");
		
		titleField.setNullRepresentation("");
		
//		titleField.setValue(currmodel.getTitle());
//		editor.setValue(currmodel.getDotschema());
		
		saveButton.setEnabled(false);
//		titleField.setPropertyDataSource(this.currmodel.getTitle());
//		editor.setPropertyDataSource(this.currmodel.getDotschema());
	}
	
	public void toggleEditorFields(boolean b) {
		titleField.setEnabled(b);
		modeBox.setEnabled(b);
		editor.setEnabled(b);	
		launchDBuilderButton.setEnabled(b);

		titleField.focus();
	}
	
	public void createNewModel(boolean b) {
		createNewModel = b;
	}

	public void cloneModel(boolean b) {
		createNewModel = b;
		Notification not = new Notification("Click 'Save' to store cloned model!", Type.WARNING_MESSAGE);
		not.setPosition(Position.MIDDLE_RIGHT);
		not.setStyleName("success");
		not.show(Page.getCurrent());
		saveButton.setEnabled(true);
		saveButton.focus();
	}

}

package com.aaron.mbpet.views.tabs.modelstab;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;
import org.vaadin.aceeditor.AceEditor;
import org.vaadin.aceeditor.AceMode;
import org.vaadin.aceeditor.AceEditor.SelectionChangeEvent;
import org.vaadin.aceeditor.AceEditor.SelectionChangeListener;

import com.aaron.mbpet.MbpetUI;
import com.aaron.mbpet.domain.Model;
import com.aaron.mbpet.domain.TestSession;
import com.aaron.mbpet.services.ModelUtils;
import com.aaron.mbpet.views.MainView;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.fieldgroup.FieldGroup;
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
import com.vaadin.ui.Notification;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;

public class ModelAceEditorLayout extends VerticalLayout implements Button.ClickListener{

	AceEditor editor;// = new AceEditor();
	ModelDBuilderTab diagramtab;
	
	TabSheet modelsTabs;
	Table modelsTable;
	TextField titleField;
	ComboBox modeBox;
	Button saveButton;
	Button openDBuilderButton;
    
    String fileFormat = "dot";
    List<String> modeList;
    String[] modes = {"dot", "gv", "python"};
//    String testDir = "C:/dev/git/alternate/mbpet/MBPeT/WebContent/META-INF/output/settings.py";
    
    JPAContainer<Model> models = ((MbpetUI) UI.getCurrent()).getModels();
    Model currmodel;
    TestSession currsession;
	FieldGroup binder;
	BeanItem<Model> modelBeanItem;
	
	private boolean createNewModel = false;

	public ModelAceEditorLayout(AceEditor editor, String fileFormat, 
			TabSheet modelsTabs, Table modelsTable, TestSession currsession) {	// TestSession currsession
		setSizeFull();
		setMargin(new MarginInfo(false, true, false, true));
//		setMargin(true);
//		setSpacing(true);
		
		this.modelsTabs = modelsTabs;
		this.editor = editor; //= new AceEditor()
		this.modelsTable = modelsTable;
		this.fileFormat = fileFormat;
		this.currsession = currsession;
//		this.diagramtab = diagramtab;
		
//        addComponent(new Label("<h3>Give Test Parameters in settings.py file</h3>", ContentMode.HTML));
		addComponent(buildButtons());	
		addComponent(buildAceEditor());
		setExpandRatio(editor, 1);
	}

	private Component buildButtons() {
        // Horizontal Layout
        HorizontalLayout h = new HorizontalLayout(); 
        h.setWidth("100%");
        h.setSpacing(true);    

        titleField = new TextField("Title:");
        titleField.setWidth(11, Unit.EM);
        titleField.addStyleName("tiny");
        titleField.setImmediate(true);
        titleField.addTextChangeListener(new TextChangeListener() {	
			@Override
			public void textChange(TextChangeEvent event) {
				saveButton.setEnabled(true);
				openDBuilderButton.setEnabled(false);
			}
		});
        
        modeList = Arrays.asList(modes);
        modeBox = new ComboBox("code style:", modeList);
//        modeBox.setContainerDataSource(modes);
//        modeBox.setWidth(100.0f, Unit.PERCENTAGE);
        modeBox.setWidth(7, Unit.EM);
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
        saveButton.setDescription("save model");
        saveButton.setEnabled(false);
	    
		openDBuilderButton = new Button("Draw Model", this);
		openDBuilderButton.setIcon(FontAwesome.EXTERNAL_LINK);
//		openDBuilderButton.addStyleName("borderless");
		openDBuilderButton.addStyleName("tiny");
//		launchDBuilderButton.addStyleName("icon-only");
		openDBuilderButton.setDescription("Switch to drag and drop builder");
		openDBuilderButton.setEnabled(false);
		
		h.addComponents(titleField, saveButton, openDBuilderButton);	//modeBox
		h.setComponentAlignment(titleField, Alignment.BOTTOM_LEFT);
//		h.setComponentAlignment(modeBox, Alignment.BOTTOM_LEFT);
		h.setComponentAlignment(saveButton, Alignment.BOTTOM_LEFT);
		h.setComponentAlignment(openDBuilderButton, Alignment.BOTTOM_RIGHT);
		h.setExpandRatio(openDBuilderButton, 1);
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
//		setEditorMode(fileFormat);
		editor.setMode(AceMode.dot);
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
				openDBuilderButton.setEnabled(false);
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
			// check editor title matches text field title
//			if (editor.getValue() != null)
//				corrected = ModelUtils.compareTitles(titleField.getValue(), editor.getValue());
//			String corrected = ModelUtils.renameAceTitle(titleField.getValue(), editor.getValue());		//(selected.getDotschema());
//			editor.setValue(corrected);
			
			ModelUtils modelUtils = new ModelUtils();
			if ( createNewModel == true ) {
//				editor.setValue(corrected);
				editedmodel = modelUtils.createNewModel(currmodel, currsession, binder); //currmodel.getParentsession(),
				createNewModel = false;
			} else {
//				editor.setValue(corrected);
				//save
				editedmodel = modelUtils.editModel(currmodel, currsession, binder); //currmodel.getParentsession(),				
			}
			
			
			try {
				if (editedmodel != null){
					modelsTable.select(models.getItem(editedmodel.getId()).getEntity().getId());
					toggleEditorFields(true);
					setFieldsDataSource(models.getItem(editedmodel.getId()).getEntity());	//(models.getItem(currmodel.getId()).getEntity());
					editor.focus();
					openDBuilderButton.setEnabled(true);					
				} else {
					createNewModel = true;
					toggleEditorFields(false);
					setFieldsDataSource(null);
					UI.getCurrent().getNavigator()
							.navigateTo(MainView.NAME + "/" + 
								currsession.getParentcase().getTitle() + "/" + 
									currsession.getTitle() + "-id=" + currsession.getId());
				}
			} catch (NullPointerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				createNewModel = true;
				toggleEditorFields(false);
				setFieldsDataSource(null);
//				saveButton.click();
			}


//	        saveButton.setEnabled(false);

        } else if (event.getButton() == openDBuilderButton) {       	
        	modelsTabs.setSelectedTab(1);
        	diagramtab.setFieldsDataSource(currmodel);

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
			modeBox.setValue(modeList.get(1));
//			System.out.println("Mode changed to dot");
		} else if (mode.equals("py") || mode.equals("python")) {
			editor.setMode(AceMode.python);	
			modeBox.setValue(modeList.get(2));
//			System.out.println("Mode changed to python");
		} 
	}

	
	
	public void toggleEditorFields(boolean b) {
		titleField.setEnabled(b);
		modeBox.setEnabled(b);
		editor.setEnabled(b);	
		openDBuilderButton.setEnabled(b);

		titleField.focus();
	}
	
	
	
	public void setFieldsDataSource(Model currmodel) {
		if (currmodel==null) {
			binder.clear();
//			titleField.setValue("");
			editor.setValue("");
		} else {
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
	}
	

	
	public void createNewModel(boolean b) {
		createNewModel = b;
	}

	public void cloneModel(Model model, boolean b) {
		createNewModel = b;

		// check editor title matches text field title
		ModelUtils modelUtils = new ModelUtils();
		Model editedmodel = modelUtils.createNewModel(model, currsession, binder); //currmodel.getParentsession(),
		createNewModel = false;
		
		try {
			if (editedmodel != null){
				modelsTable.select(models.getItem(editedmodel.getId()).getEntity().getId());
				toggleEditorFields(true);
				setFieldsDataSource(models.getItem(editedmodel.getId()).getEntity());	//(models.getItem(currmodel.getId()).getEntity());
				editor.focus();
				openDBuilderButton.setEnabled(true);
			
			} else {
				createNewModel = true;
				toggleEditorFields(false);
				setFieldsDataSource(null);
				UI.getCurrent().getNavigator()
						.navigateTo(MainView.NAME + "/" + 
							currsession.getParentcase().getTitle() + "/" + 
								currsession.getTitle() + "-id=" + currsession.getId());
			}
		} catch (NullPointerException e) {
			e.printStackTrace();
			createNewModel = true;
			toggleEditorFields(false);
			setFieldsDataSource(null);
		}

	
	
	
//		Notification not = new Notification("Click 'Save' to store cloned model!", Type.WARNING_MESSAGE);
//		not.setPosition(Position.MIDDLE_RIGHT);
//		not.setStyleName("success");
//		not.show(Page.getCurrent());
//		saveButton.setEnabled(true);
//		saveButton.focus();
	}

	public void setDBuilderTab(ModelDBuilderTab diagramtab) {
		this.diagramtab = diagramtab;
		
	}

}

package com.aaron.mbpet.views.tabs.parameterstab;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import org.vaadin.aceeditor.AceEditor;
import org.vaadin.aceeditor.AceMode;
import com.aaron.mbpet.MbpetUI;
import com.aaron.mbpet.domain.Parameters;
import com.aaron.mbpet.domain.TestSession;
import com.aaron.mbpet.services.AceUtils;
import com.aaron.mbpet.services.FileSystemUtils;
import com.aaron.mbpet.services.ParametersUtils;
import com.aaron.mbpet.views.MainView;
import com.aaron.mbpet.views.parameters.ParametersEditor;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.util.BeanItem;
import com.vaadin.event.FieldEvents.TextChangeEvent;
import com.vaadin.event.FieldEvents.TextChangeListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.combobox.FilteringMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Table;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;

public class ParametersAceEditorLayout extends VerticalLayout implements Button.ClickListener{

	AceEditor editor;// = new AceEditor();
	ComboBox themeBox;
	ComboBox modeBox;
	Button saveButton;
	Button loadButton;
    
    String fileFormat = "python";
    List<String> modeList;
    String[] modes = {"python", "dot", "gv"};
    List<String> themeList;
    String[] themes = {"ambiance", "chrome", "clouds", "cobalt", "dreamweaver", "eclipse", "github", "terminal", "twilight", "xcode"};

//    String testDir = "C:/dev/git/alternate/mbpet/MBPeT/WebContent/META-INF/output/settings.py";
//    String basepath = "C:/dev/git/alternate/mbpet/MBPeT/WebContent";	//VaadinService.getCurrent().getBaseDirectory().getAbsolutePath();
    String basepath = ((MbpetUI) UI.getCurrent()).getWebContent();
    String defaultsettingsfile = basepath + "/WEB-INF/tmp/settings.py";
    String prevModelsFolder;
    String prevReportsFolder;
    
    TestSession currsession;
    Parameters currentparams;
    JPAContainer<Parameters> parameters = ((MbpetUI) UI.getCurrent()).getParameterscontainer();
    BeanItem<Parameters> beanItem;
    ParametersFormAceView formAceView;
    

	public ParametersAceEditorLayout(AceEditor editor, String fileFormat, BeanItem<Parameters> beanItem,
			TestSession currsession, ParametersFormAceView formAceView) {	// TestSession currsession
		setSizeFull();
		setMargin(false);	//(new MarginInfo(false, true, false, true));
//		setMargin(true);
//		setSpacing(true);
				
		this.editor = editor; //= new AceEditor()
		this.fileFormat = fileFormat;
		this.currsession = currsession;
		this.currentparams = currsession.getParameters();//parameters.getItem(currsession.getParameters().getId()).getEntity(); //currsession.getParameters();
		this.beanItem = beanItem;
		this.formAceView = formAceView; 
		this.prevModelsFolder = currentparams.getModels_folder();
		this.prevReportsFolder = currentparams.getTest_report_folder();
//		Notification.show("prevModelsFolder is:" + prevModelsFolder);

//        addComponent(new Label("<h3>Give Test Parameters in settings.py file</h3>", ContentMode.HTML));
		
		addComponent(buildButtons());	
		addComponent(buildAceEditor());

//		toggleEditorFields(false);

	}

	private Component buildButtons() {
        // Horizontal Layout
        HorizontalLayout h = new HorizontalLayout(); 
        h.setWidth("100%");
        h.setSpacing(true);    

        themeList = Arrays.asList(themes);
        themeBox = new ComboBox("editor theme:", themeList);
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
        
        modeList = Arrays.asList(modes);
        modeBox = new ComboBox("code style:", modeList);
//        modeBox.setContainerDataSource(modeList);
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
	    
		
		h.addComponents(themeBox, saveButton);	// modeBox
		h.setComponentAlignment(themeBox, Alignment.BOTTOM_LEFT);
		h.setComponentAlignment(saveButton, Alignment.BOTTOM_RIGHT);
		h.setExpandRatio(saveButton, 1);

		return h;
		
	}
	
	

	private AceEditor buildAceEditor() {
		//System.out.println("SETTINGS FILE : " + defaultsettingsfile);
		// Ace Editor
		try {
			if (currentparams.getSettings_file() == null) {
				loadExampleSettings();
//				editor.setValue("Fill in parameters for Test Session '" + currsession.getTitle() + "'");	//("Hello world!\nif:\n\tthen \ndo that\n...");			
			} else {
				editor.setValue(currentparams.getSettings_file());
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
		editor.setHeight("800px");
		editor.setReadOnly(false); 
//		setEditorMode(fileFormat);
		editor.setMode(AceMode.python);
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
//			String s = editor.getValue();
//			saveToFile(s, testDir);	//+aceOutFileField.getValue());
			
			// commit settings file from Ace
			new ParametersEditor(currentparams, currsession, editor.getValue());
			currentparams = parameters.getItem(currentparams.getId()).getEntity();
			
			// commit individual field, parsed from ace
			ParametersUtils.commitAceParamData(currentparams, editor.getValue());
			
			// update Form view
			formAceView.bindFormtoBean(currentparams);
//			for (Object pid : beanItem.getItemPropertyIds()) {beanItem.getItemProperty(pid).setValue(currParameters)}	        
			formAceView.wireupTRTTable();		
			saveButton.setEnabled(false);

			// edit models directory name
      	  	FileSystemUtils fileUtils = new FileSystemUtils();
			//System.out.println("prevModelsFolder->" + prevModelsFolder + " and current folder->" +currentparams.getModels_folder());
			if (!prevModelsFolder.equals(currentparams.getModels_folder())) {
				fileUtils.renameModelsDir(	//username, sut, session, prevModelsDir, newModelsDir)
						currsession.getParentcase().getOwner().getUsername(),
						currsession.getParentcase().getTitle(), 
						currsession.getTitle(), 
						prevModelsFolder,
						currentparams.getModels_folder());
				
//				Notification.show("Previous->new folder: " + prevModelsFolder +"->"+currentparams.getModels_folder());
				prevModelsFolder = currentparams.getModels_folder();
			}
			
			// edit reports directory name
			//System.out.println("prevReportsFolder->" + prevReportsFolder + " and current folder->" +currentparams.getTest_report_folder());
			if (!prevReportsFolder.equals(currentparams.getTest_report_folder())) {
				fileUtils.renameModelsDir(	//username, sut, session, prevModelsDir, newModelsDir)
						currsession.getParentcase().getOwner().getUsername(),
						currsession.getParentcase().getTitle(), 
						currsession.getTitle(), 
						prevReportsFolder,
						currentparams.getTest_report_folder());
				
//				Notification.show("Previous->new folder: " + prevReportsFolder +"->"+currentparams.getTest_report_folder());
				prevReportsFolder = currentparams.getTest_report_folder();
			}
			
			// write settings file to disk
			fileUtils.writeSettingsToDisk(	//username, sut, session, settings_file)
					currsession.getParentcase().getOwner().getUsername(),
					currsession.getParentcase().getTitle(), 
					currsession.getTitle(), 
					currentparams.getSettings_file());
			
//			Notification.show(s, Type.WARNING_MESSAGE);
			
			
//			UI.getCurrent().getNavigator()
//			.navigateTo(MainView.NAME + "/" + 
//				currsession.getParentcase().getTitle() + "/" + 
//					currsession.getTitle() + "-id=" + currsession.getId());
        }

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
	
//	private void setAceTheme(String theme) {	
//		
//		switch (theme) {
//		case "ambiance":
//			editor.setTheme(AceTheme.ambiance);
//            break;
//        case "chrome":
//			editor.setTheme(AceTheme.chrome);
//            break;
//        case "clouds":
//			editor.setTheme(AceTheme.clouds);
//            break;
//        case "cobalt":
//			editor.setTheme(AceTheme.cobalt);
//			break;
//        case "dreamweaver":
//			editor.setTheme(AceTheme.dreamweaver);
//			break;
//        case "eclipse":
//			editor.setTheme(AceTheme.eclipse);
//			break;
//        case "github":
//			editor.setTheme(AceTheme.github);
//			break;
//        case "terminal":
//			editor.setTheme(AceTheme.terminal);
//			break;
//        case "twilight":
//			editor.setTheme(AceTheme.twilight);
//			break;
//        case "xcode":
//			editor.setTheme(AceTheme.xcode);
//			break;
//		
//        default:
//			editor.setTheme(AceTheme.clouds);
//		}
//    	
//	}

	
	
	public void toggleEditorFields(boolean b) {
		themeBox.setEnabled(b);
		modeBox.setEnabled(b);
		saveButton.setEnabled(b);
//		editor.setEnabled(b);	

		editor.focus();
	}
	
	public void setEditorValue(String settings) {
		editor.setValue(settings);
	}
	public String getEditorValue() {
		return editor.getValue();
	}	
	
	public void setPrevModelsFolder(String modelsFolder){
		prevModelsFolder = modelsFolder;
	}
	public void setPrevReportsFolder(String reportsFolder) {
		prevReportsFolder = reportsFolder;
		
	}
	
//	public void setFieldsDataSource(Parameters currparams) {
//		if (currmodel==null) {
//			binder.clear();
////			titleField.setValue("");
//			editor.setValue("");
//		} else {
//			this.currParameters = currparams;
//			
//			binder = new FieldGroup();
//			modelBeanItem = new BeanItem<Model>(currmodel);		// takes item as argument
//			binder.setItemDataSource(modelBeanItem);
//			binder.bind(editor, "dotschema");
//			
//			
//	//		titleField.setValue(currmodel.getTitle());
//	//		editor.setValue(currmodel.getDotschema());
//			
//			saveButton.setEnabled(false);
//	//		titleField.setPropertyDataSource(this.currmodel.getTitle());
//	//		editor.setPropertyDataSource(this.currmodel.getDotschema());
//		}
//	}


	
    private void loadExampleSettings() {
		//System.out.println("SETTINGS FILE : " + defaultsettingsfile);

		StringBuilder builder = new StringBuilder();
		Scanner scan = null;
		try {
			scan = new Scanner(new FileReader(defaultsettingsfile));
			while (scan.hasNextLine()) {		
				builder.append(scan.nextLine()).append(System.getProperty("line.separator"));
			}	
			//System.out.println(builder.toString());
			editor.setValue(builder.toString());
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		
	}




}

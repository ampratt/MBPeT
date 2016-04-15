package com.aaron.mbpet.views.tabs.adapterstabs;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import org.vaadin.aceeditor.AceEditor;
import org.vaadin.aceeditor.AceMode;

import com.aaron.mbpet.MbpetUI;
import com.aaron.mbpet.domain.AdapterXML;
import com.aaron.mbpet.domain.TestSession;
import com.aaron.mbpet.services.AceUtils;
import com.aaron.mbpet.views.adapters.AdapterXMLEditor;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.util.BeanItem;
import com.vaadin.event.FieldEvents.TextChangeEvent;
import com.vaadin.event.FieldEvents.TextChangeListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.combobox.FilteringMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;

public class AdapterXMLTab extends Panel implements Button.ClickListener {

	AceEditor editor;
	private TestSession currsession;
	private ComboBox themeBox;
	private Button saveButton;

	List<String> themeList;
	String[] themes = {"ambiance", "chrome", "clouds", "cobalt", "dreamweaver", "eclipse", "github", "terminal", "twilight", "xcode"};
//    String basepath = "C:/dev/git/alternate/mbpet/MBPeT/WebContent";	//VaadinService.getCurrent().getBaseDirectory().getAbsolutePath();
    String webContent = ((MbpetUI) UI.getCurrent()).getWebContent();
    String defaultAdapterXMLFile = webContent + "/WEB-INF/tmp/adapter_default.xml";
    
	JPAContainer<AdapterXML> adaptersxmlcontainer = ((MbpetUI) UI.getCurrent()).getAdaptersXMLcontainer();
	AdapterXML currentAdapterXML;
	BeanItem<AdapterXML> beanItem;

	public AdapterXMLTab(TestSession currsession) {
		
		editor = new AceEditor();
		this.currsession = currsession;
		this.currentAdapterXML = adaptersxmlcontainer.getItem(currsession.getAdapterXML().getId()).getEntity();
//		this.beanItem = new BeanItem<AdapterXML>(bean)

		setHeight("100%");
		setWidth("100%");
		addStyleName("borderless");
			
		VerticalLayout content = new VerticalLayout();
		content.setMargin(new MarginInfo(false, false, true, true));
//		content.setWidth("100%");
		content.addComponent(buildButtons());	
		content.addComponent(buildAceEditor());

		setContent(content);
		
	}
	
	
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
        saveButton.setDescription("save adapter.xml");
        saveButton.setEnabled(false);
	    
		
		h.addComponents(themeBox, saveButton);	//themeBox
		h.setComponentAlignment(themeBox, Alignment.BOTTOM_LEFT);
		h.setComponentAlignment(saveButton, Alignment.BOTTOM_LEFT);	//BOTTOM_RIGHT);
		h.setExpandRatio(saveButton, 1);

		return h;
		
	}
	
	
	private AceEditor buildAceEditor() {
//		System.out.println("ADAPTERXML FILE : " + defaultsettingsfile);
		// Ace Editor
		try {
			loadExampleSettings();
//			if (currentAdapterXML.getAdapterXML_file() == null) {
//				loadExampleSettings();
////				editor.setValue("Fill in parameters for Test Session '" + currsession.getTitle() + "'");	//("Hello world!\nif:\n\tthen \ndo that\n...");			
//			} else {
//				editor.setValue(currentAdapterXML.getAdapterXML_file());
//			}
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
		editor.setMode(AceMode.xml);
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

			// commit settings file from Ace
//        	System.out.println("editor mode:" +modeBox.getValue());
			new AdapterXMLEditor(currentAdapterXML, currsession, editor.getValue(), "adapter.xml");
			currentAdapterXML = adaptersxmlcontainer.getItem(currentAdapterXML.getId()).getEntity();
			
			saveButton.setEnabled(false);
        }

    }
    
    
    private void loadExampleSettings() {
		//System.out.println("SETTINGS FILE : " + defaultAdapterXMLFile);

		StringBuilder builder = new StringBuilder();
		Scanner scan = null;
		try {
			scan = new Scanner(new FileReader(defaultAdapterXMLFile));
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

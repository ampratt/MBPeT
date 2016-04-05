package com.aaron.mbpet.ui;

import java.util.Arrays;
import java.util.List;

import org.vaadin.aceeditor.AceEditor;
import org.vaadin.aceeditor.AceMode;
import org.vaadin.aceeditor.AceTheme;

import com.aaron.mbpet.MbpetUI;
import com.aaron.mbpet.domain.Adapter;
import com.aaron.mbpet.domain.TestSession;
import com.aaron.mbpet.services.AceUtils;
import com.google.gwt.user.client.ui.Panel;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.TextFileProperty;
import com.vaadin.event.FieldEvents.TextChangeEvent;
import com.vaadin.event.FieldEvents.TextChangeListener;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.VaadinService;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.combobox.FilteringMode;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

public class MasterTerminalWindow extends Window {
	
	AceEditor editor;
	private ComboBox themeBox;
	VerticalLayout layout;
	
	List<String> themeList;
	String[] themes = {"twilight", "terminal", "ambiance", "chrome", "clouds", "cobalt", "dreamweaver", "eclipse", "github", "xcode"};

	
	public MasterTerminalWindow() {
//      super("Create new Instance of this Test Case"); // Set window caption
	    setCaption("MBPeT Master Output");
//	    center();
	//      addStyleName("reportwindow");
	    setResizable(true);
	    setClosable(true);
	    setCloseShortcut(KeyCode.ESCAPE, null);
	    setWidth(25, Unit.EM);
	    setHeight(300, Unit.PIXELS);	//(40.0f, Unit.PERCENTAGE);


		//	    Window parentWindow = ((Window) getWindow().getParent());
	    int posY = (int) (UI.getCurrent().getPage().getBrowserWindowHeight() - this.getHeight());
//	    Float posY = posY - this.getHeight();
//	    System.out.println("Browser height:sub-window height" + UI.getCurrent().getPage().getBrowserWindowHeight() + " : " + this.getHeight());
	    setPosition(1, posY);	//1);
	//      setContent(buildWindowContent(tree, "New Instance"));
      
  	
        layout = new VerticalLayout();
        layout.setMargin(false);
        layout.setHeight("100%");
//      content.setSizeFull();
      
//	  	Label html = new Label("", ContentMode.HTML);
//	  	html.setPropertyDataSource(new TextFileProperty(file));
//	  	content.addComponent(html);
//	  	content.setExpandRatio(html, 1);

        buildContent();
  	
	  	setContent(layout);
	}

	public MasterTerminalWindow(String caption) {
		super(caption);
		// TODO Auto-generated constructor stub
	}

	public MasterTerminalWindow(String caption, Component content) {
		super(caption, content);
		// TODO Auto-generated constructor stub
	}


	public void buildContent() {		//TestSession currsession

		// theme button
		CssLayout css = new CssLayout();
		themeList = Arrays.asList(themes);
        themeBox = new ComboBox("", themeList);
//        themeBox.setContainerDataSource(themeList);
//        modeBox.setWidth(100.0f, Unit.PERCENTAGE);
        themeBox.setWidth(9, Unit.EM);
        themeBox.addStyleName("tiny");
//        themeBox.setInputPrompt("No style selected");
        themeBox.setFilteringMode(FilteringMode.CONTAINS);
        themeBox.setImmediate(true);
        themeBox.setNullSelectionAllowed(false);
        themeBox.setValue(themeList.get(0));        
        themeBox.addValueChangeListener(new ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
            	AceUtils.setAceTheme(editor, event.getProperty().getValue().toString());
            }
        });
        css.addComponent(themeBox);
		layout.addComponent(css);	//themeBox
		layout.setComponentAlignment(css, Alignment.TOP_LEFT);
		
		
		// Ace Editor
		editor = new AceEditor();		
		// use static hosted files for theme, mode, worker
//					editor.setThemePath("/static/ace");
//					editor.setModePath("/static/ace");
//					editor.setWorkerPath("/static/ace");
		editor.setWidth("100%");
		editor.setHeight("100%");	//("425px");
//		editor.setReadOnly(true); 
		editor.setMode(AceMode.python);
		editor.setTheme(AceTheme.twilight);	
		editor.setWordWrap(true);
//				setEditorMode(fileFormat);
//				editor.setUseWorker(true);
//				editor.setWordWrap(false);
//				editor.setShowInvisibles(false);
//				System.out.println(editor.getValue());
		layout.addComponent(editor);
		layout.setExpandRatio(editor, 1);
		
//		sb.append("\n\n\n\n\n\n\n\n\n\n");
//		editor.setValue(sb.toString());
//		editor.setCursorRowCol(0, 0);
//		editor.setCursorPosition(sb.length()-10);

		// Use worker (if available for the current mode)
		//editor.setUseWorker(true);
//		editor.addTextChangeListener(new TextChangeListener() {
//		    @Override
//		    public void textChange(TextChangeEvent event) {
////				        Notification.show("Text: " + event.getText());
//		        saveButton.setEnabled(true);
//		    }
//		});
//		try {
//		if (currentAdapter.getAdapter_file() == null) {
//			// LOAD EDITOR CONTENT HERE
////					loadExampleSettings();
////					editor.setValue("Fill in parameters for Test Session '" + currsession.getTitle() + "'");	//("Hello world!\nif:\n\tthen \ndo that\n...");			
//		} else {
//			editor.setValue(currentAdapter.getAdapter_file());
//		}
//	} catch (NullPointerException e1) {
//		e1.printStackTrace();
//		// LOAD EDITOR CONTENT HERE
////			editor.setValue("Fill in parameters for Test Session '" + currsession.getTitle() + "'");	//("Hello world!\nif:\n\tthen \ndo that\n...");			
//	} 
			
			
////		setSizeFull();
////		setMargin(true);
////		setSpacing(true);
//	
//		editor = new AceEditor();
////		this.currsession = currsession;
////		this.currentAdapter = adapterscontainer.getItem(currsession.getAdapter().getId()).getEntity();
//////		this.beanItem = new BeanItem<Adapter>(bean)
//		
////		setHeight("100%");
////		setWidth("100%");
////		addStyleName("borderless");
//			
////		VerticalLayout content = new VerticalLayout();
////		content.setMargin(new MarginInfo(false, false, true, true));
////		content.setWidth("100%");
//		layout.addComponent(buildButtons());	
//		layout.addComponent(buildAceEditor());
////		Panel.setContent(content);

	}


    StringBuilder sb = new StringBuilder();
	public void insertDataToEditor(StringBuilder message) {	//String
//		vert.addComponent(new Label(message));

//		System.out.println("## ACE EDITOR CURSOR POSITION BEFORE UPDATE ## > " + editor.getCursorPosition());
		if (editor.getCursorPosition() > 7000){
			// erase old data and write message at line 1
			System.out.println("Ace Editor resetting at position > " + editor.getCursorPosition());
			sb = new StringBuilder();
			editor.setValue(sb.toString());
		} 
		// add new content
		sb.append("\n");
		sb.append(message.toString());
		editor.setValue(sb.toString());
		
		//navigate to bottom of terminal
//		editor.setCursorPosition(sb.length()-10);
		editor.scrollToPosition(editor.getCursorPosition());
		
//		editor.setValue(newFieldValue) editor.getCursorPosition()
//		editor.getSelection();
	}
}

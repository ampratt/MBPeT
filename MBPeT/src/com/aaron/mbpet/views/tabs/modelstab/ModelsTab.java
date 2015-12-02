package com.aaron.mbpet.views.tabs.modelstab;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.IOException;
import java.net.URL;

import com.aaron.mbpet.components.diagrambuilder.DiagramAceLayout;
//import com.fasterxml.jackson.annotation.JsonInclude;
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.databind.SerializationFeature;
import com.vaadin.annotations.JavaScript;
import com.vaadin.annotations.PreserveOnRefresh;
import com.vaadin.annotations.StyleSheet;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;

import org.apache.tools.ant.taskdefs.Length;
import org.vaadin.aceeditor.AceEditor;
import org.vaadin.diagrambuilder.DiagramBuilder;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.Table;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;


public class ModelsTab extends VerticalLayout {

	
	Panel panel;
	public TabSheet modelsTabs;
	public ModelTableAceTab acetab;	// = new ModelTableAceView();
	public ModelDBuilderTab diagramtab;	// = new ModelDBuilderNOTWINDOW();
	public ModelAceEditorLayout editorLayout;
	AceEditor editor;
    public Table modelsTable;
    public DiagramBuilder diagramBuilder;
	
	
	public ModelsTab() {		
		//setHeight(100.0f, Unit.PERCENTAGE);
		setHeight("100%");
//	    setSizeFull();
//		setMargin(true);
//		setSpacing(true);
		
		editor = new AceEditor();
		
//		panel = new Panel();
		setHeight("100%");
//		setWidth("100%");
//		addStyleName("borderless");
		
//		VerticalLayout content = new VerticalLayout();
//		content.setMargin(new MarginInfo(false, false, true, true));
////		content.setWidth("100%");
//		content.addComponent(buildEditingTabs());

//		setContent(content);
		addComponent(buildEditingTabs());
		
	//    addComponent(vert);
	//    setExpandRatio(vert, 1);
	//   
	//    vert.addComponent(new Label("<h2>Here below will be whatever graphs are desired...</h2>", ContentMode.HTML));
	
	}

	private TabSheet buildEditingTabs(){
		modelsTabs = new TabSheet();
		modelsTabs.setSizeFull();
		modelsTabs.addStyleName(ValoTheme.TABSHEET_COMPACT_TABBAR);	//TABSHEET_EQUAL_WIDTH_TABS);
		modelsTabs.addStyleName("right-aligned-tabs");
		
	//	confTabs.addTab(graph, "User Profiles / Models");
		//graphTab.addComponent(MbpetDemoUI.graph);
	    modelsTable = new Table();
		editorLayout = new ModelAceEditorLayout(editor, "dot", modelsTabs, modelsTable);
		diagramBuilder = new DiagramBuilder();
		
		diagramtab = new ModelDBuilderTab(editor, modelsTabs, editorLayout, modelsTable, diagramBuilder);
		acetab = new ModelTableAceTab(editor, modelsTabs, editorLayout, modelsTable, diagramtab);
		editorLayout.setDBuilderTab(diagramtab);
		
		modelsTabs.addTab(acetab);
		modelsTabs.addTab(diagramtab);
		
		return modelsTabs;
	   		
	}

//	public ModelsTab() {		
//		setSizeFull();
//		setMargin(true);
//		setSpacing(true);
//
//
//
//		addComponent(new ModelTableAceView());
//
//
//		
//	}

}

package com.aaron.mbpet.components.tabs;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.IOException;
import java.net.URL;

import com.aaron.mbpet.components.diagrambuilder.DiagramAceLayout;
import com.aaron.mbpet.views.models.ModelDBuilderNOTWINDOW;
import com.aaron.mbpet.views.models.ModelTableAceView;
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
import com.vaadin.shared.ui.label.ContentMode;

import org.apache.tools.ant.taskdefs.Length;
import org.vaadin.aceeditor.AceEditor;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;


public class ModelsTab extends VerticalLayout {

	public static TabSheet modelsTabs;
	public static ModelTableAceView acetab;	// = new ModelTableAceView();
	public static ModelDBuilderNOTWINDOW diagramtab;	// = new ModelDBuilderNOTWINDOW();
	AceEditor editor;
	
	
	public ModelsTab() {		
		//setHeight(100.0f, Unit.PERCENTAGE);
	    setSizeFull();
		setMargin(true);
		setSpacing(true);
		
		editor = new AceEditor();
		
		addComponent(buildConfigTabs());
		
	//    addComponent(vert);
	//    setExpandRatio(vert, 1);
	//   
	//    vert.addComponent(new Label("<h2>Here below will be whatever graphs are desired...</h2>", ContentMode.HTML));
	
	}

	private TabSheet buildConfigTabs(){
		modelsTabs = new TabSheet();
		modelsTabs.setSizeFull();
		modelsTabs.addStyleName(ValoTheme.TABSHEET_COMPACT_TABBAR);	//TABSHEET_EQUAL_WIDTH_TABS);
		modelsTabs.addStyleName("right-aligned-tabs");
		
	//	confTabs.addTab(graph, "User Profiles / Models");
		//graphTab.addComponent(MbpetDemoUI.graph);
		acetab = new ModelTableAceView(editor);
		diagramtab = new ModelDBuilderNOTWINDOW(editor);
		
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

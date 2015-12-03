//package com.aaron.mbpet.views.tabs;
//
//import com.aaron.mbpet.views.tabs.modelstab.ModelsTab;
//import com.vaadin.annotations.JavaScript;
//import com.vaadin.annotations.StyleSheet;
//import com.vaadin.server.Sizeable.Unit;
//import com.vaadin.shared.ui.label.ContentMode;
////import com.vaadin.tests.themes.valo.components.TestIcon;
//import com.vaadin.ui.Alignment;
//import com.vaadin.ui.Button;
//import com.vaadin.ui.Component;
//import com.vaadin.ui.HorizontalLayout;
//import com.vaadin.ui.Label;
//import com.vaadin.ui.Panel;
//import com.vaadin.ui.TabSheet;
//import com.vaadin.ui.VerticalLayout;
//import com.vaadin.ui.themes.ValoTheme;
//
//@JavaScript("http://cdn.alloyui.com/2.5.0/aui/aui-min.js")
//@StyleSheet("http://cdn.alloyui.com/2.5.0/aui-css/css/bootstrap.min.css")
//public class ConfigurationTab extends VerticalLayout {
//
//	TabSheet confTabs;
//	final ModelsTab models = new ModelsTab();
//	final RampTab ramp = new RampTab();
////	final SettingsTab settings = new SettingsTab();
//	final TestAdapterTab adapter = new TestAdapterTab();
//			
//	VerticalLayout vert = new VerticalLayout();
//	
//    public ConfigurationTab() {
//    	//setHeight(100.0f, Unit.PERCENTAGE);
//        setSizeFull();
//		setMargin(true);
//		setSpacing(true);
//		
//		addComponent(buildConfigTabs());
//		
////        addComponent(vert);
////        setExpandRatio(vert, 1);
////       
////        vert.addComponent(new Label("<h2>Here below will be whatever graphs are desired...</h2>", ContentMode.HTML));
//
//    }
//    
//	private TabSheet buildConfigTabs(){
//		confTabs = new TabSheet();
//		confTabs.setSizeFull();
//		confTabs.addStyleName(ValoTheme.TABSHEET_CENTERED_TABS);	//TABSHEET_EQUAL_WIDTH_TABS);
//
////		confTabs.addTab(graph, "User Profiles / Models");
//		//graphTab.addComponent(MbpetDemoUI.graph);
//		
//		confTabs.addTab(models, "Models");
//		confTabs.addTab(ramp, "Ramp Function");
////		confTabs.addTab(settings, "Settings");
//		confTabs.addTab(adapter, "Test Adapter");
//		
//		return confTabs;
//       		
//	}
//	
//
//}

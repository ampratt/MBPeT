package com.aaron.mbpet.components.tabs;

import com.aaron.mbpet.domain.TestSession;
import com.aaron.mbpet.views.sessions.SessionViewer;
import com.vaadin.annotations.JavaScript;
import com.vaadin.annotations.StyleSheet;
import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

@JavaScript("http://cdn.alloyui.com/2.5.0/aui/aui-min.js")
@StyleSheet("http://cdn.alloyui.com/2.5.0/aui-css/css/bootstrap.min.css")
public class TabLayout extends TabSheet {   
	
//	ConfigurationTab configTab = new ConfigurationTab();
	TabSheet confTabs;
	MonitoringTab monitoringTab = new MonitoringTab();
	final ReportsTab reportsTab = new ReportsTab();
//	final ModelsTab graph = new ModelsTab();
	ModelsTab models;// = new ModelsTab();
	ParametersTab parameters;
	RampTab ramp;// = new RampTab();
	SettingsTab settings;// = new SettingsTab();
	TestAdapterTab adapter;// = new TestAdapterTab();
	
//	TestSession currsession;
	
    public TabLayout() {	//TestSession currsession
        setSizeFull();

//        this.currsession = SessionViewer.currsession;
        
        setHeight(100.0f, Unit.PERCENTAGE);
        addStyleName(ValoTheme.TABSHEET_FRAMED);
        addStyleName(ValoTheme.TABSHEET_EQUAL_WIDTH_TABS);
//        addStyleName(ValoTheme.TABSHEET_PADDED_TABBAR);
 
        /*
        for (int i = 1; i < 4; i++) {
            final VerticalLayout layout = new VerticalLayout(new Label(
                    getLoremContent(), ContentMode.HTML));
            layout.setMargin(true);
            sample.addTab(layout, "Tab " + i);
        }
        */
        VerticalLayout configTab = new VerticalLayout();
        configTab.addComponent(buildConfigTabs());

        addTab(configTab, "Configuration");
        addTab(monitoringTab, "Monitoring");
        addTab(reportsTab, "Reports");
        
    }

	private TabSheet buildConfigTabs(){
		confTabs = new TabSheet();
		confTabs.setSizeFull();
		confTabs.addStyleName(ValoTheme.TABSHEET_CENTERED_TABS);	//TABSHEET_EQUAL_WIDTH_TABS);

//		confTabs.addTab(graph, "User Profiles / Models");
		//graphTab.addComponent(MbpetDemoUI.graph);
		models = new ModelsTab();
		ramp = new RampTab();
		parameters = new ParametersTab();		//currsession
//		settings = new SettingsTab(currsession);
		adapter = new TestAdapterTab();
		
		confTabs.addTab(models, "Models");
		confTabs.addTab(ramp, "Ramp Function");
		confTabs.addTab(parameters, "Test Parameters");
//		confTabs.addTab(settings, "Settings");
		confTabs.addTab(adapter, "Test Adapter");
		return confTabs;
       		
	}
    
}

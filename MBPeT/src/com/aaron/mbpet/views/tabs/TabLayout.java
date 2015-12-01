package com.aaron.mbpet.views.tabs;

import com.aaron.mbpet.domain.TestSession;
import com.aaron.mbpet.views.sessions.SessionViewer;
import com.aaron.mbpet.views.tabs.modelstab.ModelsTab;
import com.aaron.mbpet.views.tabs.parameterstab.ParametersTab;
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
	public MonitoringTab monitoringTab;	// = new MonitoringTab();
	ReportsTab reportsTab;	// = new ReportsTab();	//final
	ModelsTab modelTab;// = new ModelsTab();
	ParametersTab parametersTab;
//	TestAdapterTab adapter;// = new TestAdapterTab();
//	RampTab ramp;// = new RampTab();
//	SettingsTab settings;// = new SettingsTab();
	
//	TestSession currsession;
	
    public TabLayout() {	//TestSession currsession
        setSizeFull();

//        this.currsession = SessionViewer.currsession;
        
        setHeight(100.0f, Unit.PERCENTAGE);
        addStyleName(ValoTheme.TABSHEET_FRAMED);
        addStyleName(ValoTheme.TABSHEET_EQUAL_WIDTH_TABS);
//        addStyleName(ValoTheme.TABSHEET_PADDED_TABBAR);
 
        monitoringTab = new MonitoringTab();
        reportsTab = new ReportsTab();

        VerticalLayout configTab = new VerticalLayout();
        configTab.addComponent(buildConfigTabs());

        addTab(configTab, "Configuration");
        addTab(getMonitoringTab(), "Monitoring");
        addTab(reportsTab, "Reports");
        
        
    }

	private TabSheet buildConfigTabs(){
		confTabs = new TabSheet();
		confTabs.setSizeFull();
		confTabs.addStyleName(ValoTheme.TABSHEET_CENTERED_TABS);	//TABSHEET_EQUAL_WIDTH_TABS);

		modelTab = new ModelsTab();
		parametersTab = new ParametersTab();
//		adapter = new TestAdapterTab();
//		ramp = new RampTab();
//		settings = new SettingsTab(currsession);
		
		confTabs.addTab(modelTab, "Models");
		confTabs.addTab(parametersTab, "Test Parameters");
//		confTabs.addTab(adapter, "Test Adapter");
//		confTabs.addTab(ramp, "Ramp Function");
//		confTabs.addTab(settings, "Settings");
		return confTabs;
       		
	}

	public MonitoringTab getMonitoringTab() {
		return monitoringTab;
	}

	public void setMonitoringTab(MonitoringTab monitoringTab) {
		this.monitoringTab = monitoringTab;
	}
    
}

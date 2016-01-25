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
	AdapterTab adapterTab;
//	RampTab ramp;// = new RampTab();
//	SettingsTab settings;// = new SettingsTab();
	
    
    private ReportsTab currentReportsComponent; 	//Field to store current component
    
	TestSession currsession;
	
    public TabLayout() {
    }

    @SuppressWarnings("deprecation")
	public TabLayout(final TestSession currsession) {	//TestSession currsession
//        setSizeFull();

        this.currsession = currsession;
        
        setHeight(100.0f, Unit.PERCENTAGE);
        addStyleName(ValoTheme.TABSHEET_FRAMED);
        addStyleName(ValoTheme.TABSHEET_EQUAL_WIDTH_TABS);
//        addStyleName(ValoTheme.TABSHEET_PADDED_TABBAR);
 
        monitoringTab = new MonitoringTab();	//reportsTab
//        reportsTab = new ReportsTab(currsession);

        VerticalLayout configTab = new VerticalLayout();
        configTab.setHeight("100%");
        configTab.addComponent(buildConfigTabs());

        addTab(configTab, "Configuration");
        addTab(getMonitoringTab(), "Monitoring");
//        addTab(reportsTab, "Reports");

        //during initialization
        currentReportsComponent = new ReportsTab(currsession);
        addTab(currentReportsComponent, "Reports");

        addListener(new TabSheet.SelectedTabChangeListener() {
            @Override
            public void selectedTabChange(SelectedTabChangeEvent event) {
                if (event.getTabSheet().getSelectedTab() == currentReportsComponent) {
                	refreshReports();
                }
            }
        });
        
    }

	private TabSheet buildConfigTabs(){
		confTabs = new TabSheet();
//		confTabs.setSizeFull();
		confTabs.setHeight(100.0f, Unit.PERCENTAGE);
		confTabs.addStyleName(ValoTheme.TABSHEET_CENTERED_TABS);	//TABSHEET_EQUAL_WIDTH_TABS);

		modelTab = new ModelsTab(currsession);
		parametersTab = new ParametersTab(currsession);
		adapterTab = new AdapterTab(currsession);
//		ramp = new RampTab();
//		settings = new SettingsTab(currsession);
		
		confTabs.addTab(modelTab, "Models");
		confTabs.addTab(parametersTab, "Test Parameters");
		confTabs.addTab(adapterTab, "Test Adapter");
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
    
	public void refreshReports(){
    	ReportsTab newReportsComponent = new ReportsTab(currsession);
        replaceComponent(currentReportsComponent, newReportsComponent);
        currentReportsComponent = newReportsComponent;
	}
	
}

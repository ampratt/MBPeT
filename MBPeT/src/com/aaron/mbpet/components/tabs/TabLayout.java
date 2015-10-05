package com.aaron.mbpet.components.tabs;

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
	MonitoringTab monitoringTab = new MonitoringTab();
	final ModelsTab models = new ModelsTab();
	final ReportsTab reportsTab = new ReportsTab();
	final ModelsTab graph = new ModelsTab();
	final RampTab ramp = new RampTab();
	final SettingsTab settings = new SettingsTab();
	final TestAdapterTab adapter = new TestAdapterTab();
	
    public TabLayout() {
        setSizeFull();

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
		TabSheet confTabs = new TabSheet();
		confTabs.setSizeFull();
		confTabs.addStyleName(ValoTheme.TABSHEET_CENTERED_TABS);	//TABSHEET_EQUAL_WIDTH_TABS);

//		confTabs.addTab(graph, "User Profiles / Models");
		//graphTab.addComponent(MbpetDemoUI.graph);
		
		confTabs.addTab(models, "Models");
		confTabs.addTab(ramp, "Ramp Function");
		confTabs.addTab(settings, "Settings");
		confTabs.addTab(adapter, "Test Adapter");
		return confTabs;
       		
	}
    
}

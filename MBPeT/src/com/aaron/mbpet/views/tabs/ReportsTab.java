package com.aaron.mbpet.views.tabs;

import com.vaadin.server.ExternalResource;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.BrowserFrame;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;

public class ReportsTab extends VerticalLayout {
	
    public ReportsTab() {
    	//setHeight(100.0f, Unit.PERCENTAGE);
        setSizeFull();
		setMargin(true);
		setSpacing(true);
        
        initReports();
	}
    	
	private void initReports() {
		addComponent(new Label("<h3><i>This will display reports for this test case:</i></h3>", ContentMode.HTML));	//layout.

		BrowserFrame browser = new BrowserFrame("Embedded Page", new ExternalResource("http://www.flotcharts.org/"));
		browser.setSizeFull();
		//    	browser.setWidth("600px");
//    	browser.setHeight("400px");
    	addComponent(browser);
        setExpandRatio(browser, 1);
    	
	} 	

}

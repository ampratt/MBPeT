package com.aaron.mbpet.views.tabs;

import com.vaadin.annotations.JavaScript;
import com.vaadin.annotations.StyleSheet;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.shared.ui.label.ContentMode;
//import com.vaadin.tests.themes.valo.components.TestIcon;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

@JavaScript("http://cdn.alloyui.com/2.5.0/aui/aui-min.js")
@StyleSheet("http://cdn.alloyui.com/2.5.0/aui-css/css/bootstrap.min.css")
public class MonitoringTab extends VerticalLayout {

	VerticalLayout vert = new VerticalLayout();
	
    public MonitoringTab() {
    	//setHeight(100.0f, Unit.PERCENTAGE);
        setSizeFull();
		setMargin(true);
		setSpacing(true);
		
        addComponent(buildPanels());
        addComponent(vert);
        setExpandRatio(vert, 1);
       
        vert.addComponent(new Label("<h2>Here below will be whatever graphs are desired...</h2>", ContentMode.HTML));

    }
    
    
    public HorizontalLayout buildPanels() {
    	HorizontalLayout row = new HorizontalLayout();
        //row.setStyleName("monitor-panels");
        row.setMargin(true);
        //row.setSpacing(true);
    	row.setWidth("100%");
        addComponent(row);
        //TestIcon testIcon = new TestIcon(60);

        Panel panel = new Panel("Response Times");
        //panel.setIcon(testIcon.get());
        panel.setContent(panelContent());
        row.addComponent(panel);

        panel = new Panel("Response Counts");
        panel.setContent(panelContent());
        row.addComponent(panel);
        
        panel = new Panel("Bandwidth");
        panel.setContent(panelContent());
        row.addComponent(panel);
        
        panel = new Panel("Slaves");
        panel.setContent(slavePanelContent());
        row.addComponent(panel);

        this.setComponentAlignment(row, Alignment.TOP_CENTER);
        return row;
    }
    
    
    Component panelContent() {
        VerticalLayout vert = new VerticalLayout();
        //vert.setSizeFull();
        vert.setMargin(true);
        vert.setHeight("6em");
        //vert.setWidth("8em");
        
        HorizontalLayout row = new HorizontalLayout();
        row.setWidth("100%");
        //layout.setSpacing(true);
        //Label content = new Label("Suspendisse dictum feugiat nisl ut dapibus. Mauris iaculis porttitor posuere. Praesent id metus massa, ut blandit odio.");
        Label minMax = new Label("min/max");
        Label data = new Label("1 / 201 ms");
        row.addComponent(minMax);
        row.addComponent(data);
        row.setComponentAlignment(minMax, Alignment.TOP_LEFT);
        row.setComponentAlignment(data, Alignment.TOP_RIGHT);
        //row.setExpandRatio(data, 1);
        vert.addComponent(row);

        row = new HorizontalLayout();
        row.setWidth("100%");
        Label average = new Label("average");
        data = new Label("101 ms");
        row.addComponent(average);
        row.addComponent(data);
        row.setComponentAlignment(average, Alignment.TOP_LEFT);
        row.setComponentAlignment(data, Alignment.TOP_RIGHT);
        vert.addComponent(row);
        
        return vert;
    }
    
    Component slavePanelContent() {
        VerticalLayout vert = new VerticalLayout();
        //vert.setSizeFull();
        vert.setMargin(true);
        vert.setHeight("6em");
        //vert.setWidth("8em");
        
        String[] statuses = {"Connected", "Generating", "Saturated"};
        
        for (int i=0; i<statuses.length; i++) {
            HorizontalLayout row = new HorizontalLayout();
            row.setWidth("100%");
           
            Label slave = new Label("Slave " + (i+1) + " - " + statuses[i]);
            row.addComponent(slave);
            vert.addComponent(row);        	
        }    
        return vert;
    }
}

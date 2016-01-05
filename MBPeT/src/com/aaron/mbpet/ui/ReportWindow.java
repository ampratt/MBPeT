package com.aaron.mbpet.ui;

import java.io.File;

import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.FileResource;
import com.vaadin.ui.BrowserFrame;
import com.vaadin.ui.Component;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

public class ReportWindow extends Window {

	public ReportWindow(File file) {
//        super("Create new Instance of this Test Case"); // Set window caption
        setCaption(file.getName());
        center();
//        addStyleName("reportwindow");
        setResizable(true);
        setClosable(true);
        setCloseShortcut(KeyCode.ESCAPE, null);
        setHeight(90.0f, Unit.PERCENTAGE);
        setWidth(90.0f, Unit.PERCENTAGE);
//        setContent(buildWindowContent(tree, "New Instance"));
        
        VerticalLayout content = new VerticalLayout();
        content.setSizeFull();
        setContent(content);
        
        System.err.println("file path:" + file.getAbsolutePath());
        Panel detailsWrapper = new Panel(buildReportLayout(file.getAbsolutePath()));
        detailsWrapper.setSizeFull();
        detailsWrapper.addStyleName(ValoTheme.PANEL_BORDERLESS);
//        detailsWrapper.addStyleName("scroll-divider");
        content.addComponent(detailsWrapper);
        content.setExpandRatio(detailsWrapper, 1f);

//        content.addComponent(buildFooter());
	}
	
    private Component buildReportLayout(String filepath) {
    	VerticalLayout vert = new VerticalLayout();
    	vert.setSizeFull();
    	
        // file as a file resource
        FileResource resource = new FileResource(new File(filepath));

		BrowserFrame html = new BrowserFrame("", new ExternalResource(filepath));

		
//        BrowserFrame html = new BrowserFrame("", resource);
        html.setSizeFull();
		
        vert.addComponent(html);
        
        // Show the file in the application
//        File file = new File ("Image from file", resource);
                
        // Let the user view the file in browser or download it
//        Link link = new Link("Link to the image file", resource);
        return vert;
	}

	public static void open(final File file) {
        Window w = new ReportWindow(file);
        UI.getCurrent().addWindow(w);
        w.focus();
    }
    
}

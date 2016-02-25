package com.aaron.mbpet.ui;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import com.vaadin.data.util.TextFileProperty;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.FileResource;
import com.vaadin.server.VaadinService;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.BrowserFrame;
import com.vaadin.ui.Component;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.Link;
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
        content.setMargin(true);
//        content.setSizeFull();
        
        System.err.println("file path:" + file.getAbsolutePath());
        
//    	Panel panel = new Panel();
//    	panel.setSizeFull();
//    	panel.addStyleName(ValoTheme.PANEL_BORDERLESS);
    	        
//    	file = new File("C:/dev/mbpet/users/apratt/yaas/test_project/test_reports/test_project_test_report_2016-01-25_14-37-40/error_rate.png");
        

    	
//     // Find the application directory
        String basepath = VaadinService.getCurrent()
                          .getBaseDirectory().getAbsolutePath();
//        System.out.println("basepath" + basepath);
//        // Image as a file resource
//        FileResource resource = new FileResource(new File(basepath +
//                                "/WEB-INF/img/draft-report-thumb.png"));
//
//        // Show the image in the application
//        Image image = new Image("Image from file", resource);
//                
//        // Let the user view the file in browser or download it
//        Link link = new Link("Link to the image file", resource);
//        content.addComponent(link);
        
        
//        File f = new File(basepath + "/WEB-INF/test_reports/" + 
//        "test_project_test_report_2016-01-25_14-37-40/master_test_report_2016-01-25_14-37-40.html");
    	Label html = new Label("", ContentMode.HTML);
    	html.setPropertyDataSource(new TextFileProperty(file));
    	content.addComponent(html);
    	content.setExpandRatio(html, 1);
  

//		BrowserFrame browser = new BrowserFrame("Embedded Page", 
//				new ExternalResource("C://dev//mbpet//users//apratt//yaas//test_project///test_reports//test_project_test_report_2016-01-25_14-37-40//master_test_report_2016-01-25_14-37-40.html"));
//		content.addComponent(browser);
    	
        setContent(content);

//        content.setExpandRatio(html, 1);
        
//        Panel detailsWrapper = new Panel(buildReportLayout(file));
//        detailsWrapper.setSizeFull();
//        detailsWrapper.addStyleName(ValoTheme.PANEL_BORDERLESS);
////        detailsWrapper.addStyleName("scroll-divider");
//        content.addComponent(detailsWrapper);
//        content.setExpandRatio(detailsWrapper, 1f);
//        content.addComponent(buildFooter());
	}
	
    private Component buildReportLayout(File file) {
    	VerticalLayout vert = new VerticalLayout();
    	vert.setSizeFull();
    	
        // file as a file resource
//        FileResource resource = new FileResource(new File(file));	//getAbsolutePath()
//		BrowserFrame html = new BrowserFrame("", new ExternalResource(filepath));
    	
    	Label html = new Label("", ContentMode.HTML);
    	html.setPropertyDataSource(new TextFileProperty(file));
		
//        BrowserFrame html = new BrowserFrame("", resource);
//        html.setSizeFull();
		
//        vert.addComponent(html);
        
        // Show the file in the application
//        File file = new File ("Image from file", resource);
                
        // Let the user view the file in browser or download it
//        Link link = new Link("Link to the image file", resource);
        return html;
	}

	public static void open(final File file) {
//		File f = new File("file:///" + file);
//		File f = new File("mbpet/users/apratt/yaas/test_project/test_reports/test_project_test_report_2016-01-25_14-37-40/master_test_report_2016-01-25_14-37-40.html");
		System.out.println("file is: " + file);
        Window w = new ReportWindow(file);
        UI.getCurrent().addWindow(w);
        w.focus();
    }
    
}

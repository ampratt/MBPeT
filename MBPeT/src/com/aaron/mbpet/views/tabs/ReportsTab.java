package com.aaron.mbpet.views.tabs;

import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.FilenameUtils;

import com.aaron.mbpet.MbpetUI;
import com.aaron.mbpet.domain.TestSession;
import com.aaron.mbpet.services.FileSystemUtils;
import com.aaron.mbpet.ui.ReportWindow;
import com.vaadin.event.LayoutEvents.LayoutClickEvent;
import com.vaadin.event.LayoutEvents.LayoutClickListener;
import com.vaadin.server.BrowserWindowOpener;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.FileResource;
import com.vaadin.server.StreamResource;
import com.vaadin.server.StreamResource.StreamSource;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.MouseEventDetails.MouseButton;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.BrowserFrame;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.Link;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;

public class ReportsTab extends Panel {
	
	VerticalLayout vert;
	TestSession currsession;
	CssLayout catalog;
	String usersBasepath = ((MbpetUI) UI.getCurrent()).getUsersBasepath();	//"C:\\dev\\mbpet\\users\\";	"C:/dev/mbpet/users/";
	String mbpetBasepath = ((MbpetUI) UI.getCurrent()).getMbpetBasepath();
	String reportsFolder;
	
    public ReportsTab(TestSession currsession) {
    	//setHeight(100.0f, Unit.PERCENTAGE);
        setSizeFull();
        this.currsession = currsession;
        
        vert = new VerticalLayout();
        vert.setMargin(true);
        vert.setSpacing(true);
		vert.addStyleName("reports");
//        setSizeFull();

		setContent(vert);
		
		reportsFolder = usersBasepath + 
						"/" + currsession.getParentcase().getOwner().getUsername() +
						"/" + currsession.getParentcase().getTitle() + 
						"/" + currsession.getTitle() +
						"/" + currsession.getParameters().getTest_report_folder();	// "/"
        
		vert.addComponent(buildReportsCatalog());

		//		initReports();
	}
    
    public void refreshReportsInLayout(){
    	vert.removeComponent(vert.getComponent(0));
		vert.addComponent(buildReportsCatalog());    	
    }
    
    	
	
	
    private Component buildReportsCatalog() {
        catalog = new CssLayout();
//        catalog.setCaption("Catalog");
        catalog.addStyleName("catalog");
        catalog.setWidth("100%");
        
        getHtmlReports();
        
        return catalog;
    }
    

	public String formatReportTitle(String reportname) {
//		String formatted;

		int pos = reportname.replaceFirst("^(\\D+).*$", "$1").length();

	     SimpleDateFormat inputFT = new SimpleDateFormat ("yyyy-MM-dd_hh-mm-ss"); 
	     SimpleDateFormat outputFT = new SimpleDateFormat ("dd.MM.yyyy, hh:mm a"); 
	
	      String input = reportname.substring(pos, reportname.length()-5); 
	      System.out.print(input + " Parses as "); 
	      Date t = null; 
	
	      try { 
	          t = inputFT.parse(input); 
	          System.out.println(t); 
	          System.out.println("my format is: " + outputFT.format(t));
	      } catch (ParseException e) { 
	          System.out.println("Unparseable using " + inputFT); 
	      }
	      
		String formatted = "Test Report<br><span>" + outputFT.format(t) + "</span>"; //5=.html

	      
	      
//		int pos = reportname.replaceFirst("^(\\D+).*$", "$1").length();
//		formatted = "Test Report<br><span>" + reportname.substring(pos, reportname.length()-5) + "</span>"; //5=.html
//		System.out.println("first int is at pos:" + pos);
//		System.out.println("formatted:" + formatted);
		
		return formatted;
	}

	public void getHtmlReports(){
		File[] directories = 
				new File(reportsFolder).listFiles(new FileFilter() {
            @Override
            public boolean accept(File file) {
                return file.isDirectory() && (!file.getName().equals("pdf"));
            }
        });
		if (!(directories==null) && directories.length>0) {

	        System.out.println("directories with reports:" + directories.length);
	        File[] singlereport = null;
	//        File[] reports = null;
	        List<File> reportlist = new ArrayList<File>();
	        Arrays.sort(directories);
	        
	        // retrieve individual html reports and add them to List
	        for (int i=directories.length-1; i>=0; i--) {			//(int i=0; i<directories.length; i++) {
	            System.out.println("finding html report - round:" + (i+1));
	        	String currentpath = directories[i].getAbsolutePath();
	           	File dir = new File(currentpath);
	
	           	singlereport = dir.listFiles(new FilenameFilter() {
	           		public boolean accept(File dir, String name){
	           			return name.endsWith(".html");
	           	  	}
	           	});
	           	try {
	           		System.out.println("singlereport:" + singlereport[0]);
	           		reportlist.add(singlereport[0]);           		
	           	} catch (IndexOutOfBoundsException e) {
	           		System.err.println(e + 
	           				"\nThere is no html report in directory: " + dir.getAbsolutePath());
	           	}
	        }
	        
	        //display thumbnail for all files in List
	        try {
	        	System.out.println("reports files:" + reportlist.size());
	        	boolean newest = true;
				for (final File html : reportlist){			//(final Movie movie : DashboardUI.getDataProvider().getMovies()) {
					VerticalLayout frame = new VerticalLayout();
				    frame.addStyleName("report-thumb");	//.addStyleName("frame");
				    frame.setMargin(true);
				    frame.setWidth(225.0f, Unit.PIXELS);
	//			    frame.setWidthUndefined();
	
				    Image thumb;
				    if (newest) {
					    thumb = new Image(null, new ThemeResource("img/draft-report-thumb-newest.png"));
			        	newest=false;
				    } else {
				    	thumb = new Image(null, new ThemeResource("img/draft-report-thumb.png"));			    	
				    }
				    thumb.setWidth(130.0f, Unit.PIXELS);	//(140.0f, Unit.PIXELS);
				    thumb.setHeight(162.0f, Unit.PIXELS);	//(175.0f, Unit.PIXELS);
				    frame.addComponent(thumb);
				    frame.setComponentAlignment(thumb, Alignment.TOP_CENTER);
	
				    Label titleLabel = new Label(formatReportTitle(html.getName()), ContentMode.HTML);
	//			    titleLabel.setWidth(140.0f, Unit.PIXELS);
				    frame.addComponent(titleLabel);
				    frame.setComponentAlignment(titleLabel, Alignment.MIDDLE_LEFT);
	                
				    
//	            	// Link w/ text and tooltip
//				    FileResource fileresource = new FileResource(f);
////			        Embedded embedded = new Embedded(null, fileresource);
////			        embedded.setType(Embedded.TYPE_BROWSER);
////			        embedded.setWidth("100px");
////			        embedded.setHeight("100px");
//			        Link link = new Link(formatReportTitle(f.getName()), 
//			        		new ThemeResource("test_reports_tmp/master_test_report_2016-03-08_15-54-17.html"));
////			        		new ExternalResource(
////	                        "file:///" + f.getAbsolutePath()));
//	                link.setDescription(f.getName());
////	                frame.addComponent(link);
////				    frame.setComponentAlignment(link, Alignment.MIDDLE_LEFT);
				    
				    
				    // FOR TESTING
//	                File pdffile = new File("C:/dev/mbpet/users/apratt/yaas/y1/test_reports/y1_test_report_2016-03-08_15-55-44/TestReport.pdf");
	                
				    frame.addLayoutClickListener(new LayoutClickListener() {
				        @Override
				        public void layoutClick(final LayoutClickEvent event) {
				            if (event.getButton() == MouseButton.LEFT) {
				            	// open to view report
//				            	ReportWindow.open(f);	//return html report
				            	
				            	// open window with pdf report
								ReportWindow.open(reportsFolder, html);
//				            	try {
//									ReportWindow.open(getPdfReport(f));
//								} catch (FileNotFoundException e) {
////									e.printStackTrace();
//									//if no file exists, then create the pdf
////									File pdf = FileSystemUtils.generatePdfReport(reportsFolder, f);
//								}
				            }
				        }
				    });
				    catalog.addComponent(frame);
				}
			} catch (NullPointerException e) {
				e.printStackTrace();
			}
		}
	}
	
//	public File getPdfReport(File html) {	//throws FileNotFoundException {
//		File pdf = new File(reportsFolder + "/pdf/" + 
//								FilenameUtils.removeExtension(html.getName()) + ".pdf" );
//		if (pdf.exists()) {
//			System.out.println("pdf report already existed");
//			return pdf;			
//		} else {
//			System.out.println("no pdf. trying to create one...");
//			//if no file exists, then create the pdf
//			FileSystemUtils fileUtils = new FileSystemUtils();
//			pdf = fileUtils.generatePdfReport(reportsFolder, html);
//		}
//		return pdf;
//	}
	

	
//	// Create the PDF source and pass the data model to it
//    Source source =
//         new javax.xml.transform.stream.StreamSource(pdffile);
//
//    // Create the stream resource and give it a file name
//    String filename = "pdf_test_report.pdf";
//    StreamResource resource =
//            new StreamResource(source, filename);
//
//    // These settings are not usually necessary. MIME type
//    // is detected automatically from the file name, but
//    // setting it explicitly may be necessary if the file
//    // suffix is not ".pdf".
//    resource.setMIMEType("application/pdf");
//    resource.getStream().setParameter(
//            "Content-Disposition",
//            "attachment; filename="+filename);
//
//    // Extend the print button with an opener
//    // for the PDF resource
//    BrowserWindowOpener opener =
//            new BrowserWindowOpener(resource);
//    opener.extend(frame);
	
	
	
//	private void initReports() {
//		vert.addComponent(new Label("<h3><i>This will display reports for this test case:</i></h3>", ContentMode.HTML));	//layout.
//
//		
//		BrowserFrame browser = new BrowserFrame("Embedded Page", new ExternalResource("http://www.flotcharts.org/"));
//		browser.setSizeFull();
//		//    	browser.setWidth("600px");
//    	browser.setHeight("400px");
//    	vert.addComponent(browser);
//        vert.setExpandRatio(browser, 1);
//    	
//	} 
	
	
}

package com.aaron.mbpet.views.sessions;

import com.aaron.mbpet.MbpetUI;
import com.aaron.mbpet.domain.TestCase;
import com.aaron.mbpet.domain.TestSession;
import com.aaron.mbpet.services.FileSystemUtils;
import com.aaron.mbpet.services.GenerateComboBoxContainer;
import com.aaron.mbpet.services.KillMBPeTProcesses;
import com.aaron.mbpet.services.MasterUtils;
import com.aaron.mbpet.services.ProgressBarThread;
import com.aaron.mbpet.services.SlaveUtils;
import com.aaron.mbpet.services.UDPThreadWorker;
import com.aaron.mbpet.views.tabs.MonitoringTab;
import com.aaron.mbpet.views.tabs.TabLayout;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.shared.ui.combobox.FilteringMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.ProgressBar;
import com.vaadin.ui.UI;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Tree;
import com.vaadin.ui.VerticalLayout;

public class SessionViewer extends VerticalLayout implements Button.ClickListener {	//implements View 
	
	Panel equalPanel = new Panel("equal panel"); 
	public Label pageTitle = new Label("");
	Tree tree;
    JPAContainer<TestCase> testcases;
    JPAContainer<TestSession> sessions;
    public TestSession currsession;

    UDPThreadWorker udpWorker;
    
    private ComboBox slaveSelect;
//  private Button newSessionButton;
//	private Button saveButton;
    public ProgressBarThread progressThread;
    public ProgressBar spinner;
    public ProgressBar progressbar;
    public Label progressstatus;
	private Label spinLabel;
	private Button startButton;
	private Button stopButton;
	
	public TabLayout tabs;
	
	public SessionViewer(String title, Tree tree) {
		setSizeFull();
		this.addStyleName("content");
		
        testcases = ((MbpetUI) UI.getCurrent()).getTestcases();
        sessions = ((MbpetUI) UI.getCurrent()).getTestsessions();
        
		this.tree = tree;
		setPageTitle(removeID(title));		//setPageTitle(title);
		
		currsession = getTestSessionByTitleID(title); //getTestSessionByTitle();
	
		addComponent(buildTopBar());

//		Component contentLayout = buildContentLayout();
//    	addComponent(contentLayout);
//    	setExpandRatio(contentLayout, 1);
    	
//		VerticalLayout tabs = new VerticalLayout();
		tabs = new TabLayout(currsession);
		addComponent(tabs);
    	setExpandRatio(tabs, 1);
	}

	public HorizontalLayout buildTopBar() {
		HorizontalLayout topBar = new HorizontalLayout();
		topBar.setStyleName("topBar-layout-padding");
		topBar.setWidth("100%");
		topBar.setSpacing(true);
		
		pageTitle.addStyleName("test-case-title");
		pageTitle.addStyleName("h2");
		
		// spinner    		
			spinner = new ProgressBar();
	        spinner.setIndeterminate(true);
	        spinner.setVisible(false);
	        spinner.addStyleName("small");
//	        spinner.setCaption("initiating slaves..");
	        
	        spinLabel = new Label("initializing test..");
	        spinLabel.addStyleName("tiny");
	        spinLabel.setVisible(false);
	        
	        VerticalLayout vert = new VerticalLayout();
	        vert.setMargin(false); vert.setSpacing(false);
	        vert.addComponents(spinner, spinLabel);
	        vert.setComponentAlignment(spinner, Alignment.MIDDLE_CENTER);
	        
        // Create the indicator, disabled until progress is started
        progressbar = new ProgressBar(new Float(0.0));
//        progressbar.setEnabled(false);
        progressbar.setVisible(false);
        progressbar.setWidth("75px");
        progressbar.addStyleName("small");
        
        progressstatus = new Label("0% done");
        progressstatus.setVisible(false); 
        progressstatus.addStyleName("tiny");

//        VerticalLayout statusvl = new VerticalLayout();
//        statusvl.setMargin(false);
//        statusvl.setSpacing(false);
//		statusvl.addComponents(progressbar, progressstatus);
//		statusvl.setComponentAlignment(progressbar, Alignment.TOP_CENTER);
//		statusvl.setComponentAlignment(progressstatus, Alignment.TOP_CENTER);
		
//		newSessionButton = new Button("", this);
//		newSessionButton.addStyleName("tiny");
//		newSessionButton.setIcon(FontAwesome.PLUS);
//		newSessionButton.setDescription("Create a new Test Session");

//		saveButton = new Button("", this);
//		saveButton.addStyleName("tiny");
//		saveButton.setIcon(FontAwesome.SAVE);
//		saveButton.setDescription("Save Settings and Parameters");
		
//        modeList = Arrays.asList(modes);
        slaveSelect = new ComboBox();	//, modeList);
        slaveSelect.setContainerDataSource(new GenerateComboBoxContainer().generateContainer(100));
//        modeBox.setContainerDataSource(modes);
//        modeBox.setWidth(100.0f, Unit.PERCENTAGE);
        slaveSelect.setWidth(5, Unit.EM);
        slaveSelect.addStyleName("tiny");
        slaveSelect.setFilteringMode(FilteringMode.CONTAINS);
        slaveSelect.setImmediate(true);
        slaveSelect.setNullSelectionAllowed(false);
        slaveSelect.select(slaveSelect.getItemIds().iterator().next());
//        slavesSelect.setValue(modeList.get(0));        
        slaveSelect.addValueChangeListener(new ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                Notification.show(slaveSelect.getValue().toString() + " slaves selected");
                //event.getProperty().getValue().toString()

            }
        });
        
        Label slaveLabel = new Label("No. Slaves");
        slaveLabel.addStyleName("tiny");
        
        	VerticalLayout sl = new VerticalLayout();
        	sl.addStyleName("slave-select-padding");
        	sl.addComponents(slaveSelect, slaveLabel);
        	sl.setDefaultComponentAlignment(Alignment.BOTTOM_CENTER);
        
		startButton = new Button("", this);
		startButton.addStyleName("tiny");
//		startButton.addStyleName("friendly");
		startButton.addStyleName("blue-start");
//		saveButton.addStyleName("friendly");
		startButton.setIcon(FontAwesome.PLAY);
		startButton.setDescription("Run Test Session");
		
		stopButton = new Button("", this);
		stopButton.addStyleName("tiny");
		stopButton.setIcon(FontAwesome.STOP);
		stopButton.setDescription("Stop Test Session");
		stopButton.setEnabled(false);

		HorizontalLayout buttons = new HorizontalLayout();
		buttons.setSpacing(true);
		buttons.addComponents(vert, progressbar, progressstatus, sl, startButton, stopButton);	//saveButton newSessionButton 
		buttons.setComponentAlignment(progressbar, Alignment.MIDDLE_RIGHT);
		buttons.setComponentAlignment(progressstatus, Alignment.MIDDLE_RIGHT);
		buttons.setComponentAlignment(sl, Alignment.MIDDLE_RIGHT);
		buttons.setComponentAlignment(startButton, Alignment.MIDDLE_RIGHT);
		buttons.setComponentAlignment(stopButton, Alignment.MIDDLE_RIGHT);
		
		topBar.addComponents(pageTitle, buttons); //(pageTitle);
//		topBar.addComponent(newUseCaseButton);
//		topBar.addComponent(saveButton);
//		topBar.addComponent(startButton);
		
		topBar.setComponentAlignment(pageTitle, Alignment.MIDDLE_LEFT);
		topBar.setComponentAlignment(buttons, Alignment.MIDDLE_RIGHT);	//(newUseCaseButton, Alignment.MIDDLE_RIGHT);
//		topBar.setComponentAlignment(saveButton, Alignment.MIDDLE_RIGHT);
//		topBar.setComponentAlignment(startButton, Alignment.MIDDLE_RIGHT);
		
		topBar.setExpandRatio(pageTitle, 2);
		topBar.setExpandRatio(buttons, 2);	//(newUseCaseButton, 2);
//		topBar.setExpandRatio(saveButton, 0);	
//		topBar.setExpandRatio(startButton, 0);
//		topBar.setExpandRatio(stopButton, 0);	    
	    
		return topBar;
	}
	
	
    public void buttonClick(ClickEvent event) {
//        if (event.getButton() == newSessionButton) {
//	        // open window to create item
//	        UI.getCurrent().addWindow(new TestSessionEditor(tree, currsession.getParentcase(), false));	//getTestCaseByTitle()	testcases.getItem(parent).getEntity()
//
//        } else if (event.getButton() == saveButton) {
//			//testing purposes
//			Notification.show("Your settings will be saved", Type.WARNING_MESSAGE);
//
//        } 
        if (event.getButton() == startButton) {
        	tabs.setSelectedTab(1);

			Notification not = new Notification("test session is starting now", Type.TRAY_NOTIFICATION);
			not.setStyleName("dark small");
			not.setDelayMsec(1000);
			not.show(Page.getCurrent());
			
			startButton.setEnabled(false);
			stopButton.setEnabled(true);
			stopButton.addStyleName("danger");

			// set display values to 0
			tabs.getMonitoringTab().updateFields(
					new String[]{"0 ms","0 ms","0","0","0","0 KB","0 KB","0"});	//,""});
				//TabLayout
			
			tabs.getMonitoringTab().generateSlaveMonitoringInfo(
					(int) slaveSelect.getValue(), "Connecting");
			
			// start round spinner till messages arrive
			displaySpinner(true);
			
//			// delete old model files
//			FileSystemUtils fileUtils = new FileSystemUtils();
//			fileUtils.cleanModelsDirectory(					
//					currsession.getParentcase().getOwner().getUsername(),
//					currsession.getParentcase().getTitle(),
//					currsession.getTitle(),
//					currsession.getParameters().getModels_folder());
//			
//			// write models to disk
//			fileUtils.writeModelsToDisk(	//username, sut, session, models_folder, mlist);
//					currsession.getParentcase().getOwner().getUsername(),
//					currsession.getParentcase().getTitle(),
//					currsession.getTitle(),
//					currsession.getParameters().getModels_folder(),
//					currsession.getModels());
			
//			// copy master files to User directory for unique execution
//			FileSystemUtils fileUtils = new FileSystemUtils();
//			fileUtils.copyMasterToUserDir(currsession.getParentcase().getOwner().getUsername());
////			"C:\\dev\\mbpet\\users\\"
			
			
			// start receiving UDP messages
			udpWorker = new UDPThreadWorker(this);
			udpWorker.fetchAndUpdateDataWith((MbpetUI) UI.getCurrent(), (int) slaveSelect.getValue());
//			int udpPort = udpWorker.getUDPPort();

			// start mbpet MASTER
//			String mastercommand = "mbpet_cli.exe " +
//					"test_project " +
//					slaveSelect.getValue().toString() + 
//					" -p " + 
//					" -b localhost:" + udpPort + 
//					" -s";
			int udpPort=0;
//			do {
//				udpPort = udpWorker.getUDPPort();
//				System.out.println("checking udp port. currently: " + udpWorker.getUDPPort());
//			} while (udpWorker.getUDPPort() == 0);
			while (!(udpWorker.getUDPPort() > 0)) {
				System.out.print("waiting udp port selection...");
			} 
			udpPort = udpWorker.getUDPPort();
			System.out.println("\nudp port selected..." + udpPort);

			
			System.out.println("udp port --being sent to master-- is: " + udpPort);		//.getUDPPort());
			MasterUtils masterUtils = new MasterUtils();		//mastercommand);
			Notification.show("Starting Master", masterUtils.getCommand(), Type.TRAY_NOTIFICATION); //mastercommand,
//			int masterport = masterUtils.getAvailablePort();
			masterUtils.startMasterStreamGobbler(slaveSelect.getValue().toString(), udpPort, currsession.getParentcase().getOwner().getUsername(), currsession);	//(mastercommand);	//(mastercommand);		//startMaster2(mastercommand);
//			masterUtils.startMaster(slaveSelect.getValue().toString(), udpPort, currsession.getParentcase().getOwner().getUsername(), currsession);	//(mastercommand);

//	        Thread t = new Thread(masterUtils);
//	        t.start();

			// start mbpet SLAVE
//			String slavecommand = "./mbpet_slave " + "127.0.0.1 -p " + masterUtils.getMasterPort();
			int masterPort=0;
			while (!(masterUtils.getMasterPort() > 0)) {
				System.out.print("waiting master port selection...");
			}masterPort = masterUtils.getMasterPort();
			System.out.println("\nmaster port selected..." + masterPort);
			
			System.out.println("master port --being sent to slave-- is: " + masterPort);
			SlaveUtils slaveUtils = new SlaveUtils();
			slaveUtils.startSlave(masterPort);		//(slavecommand);
			Notification.show("Starting Slave", slaveUtils.getCommand(), Type.TRAY_NOTIFICATION);	//slavecommand,


//	        // start progress indicator - DO THIS IN UDP CLIENT UPON RECEPTION OF FIRST MESSAGE
//	        progressThread = new ProgressBarThread(40);
//	        progressThread.start();	//fetchAndUpdateDataWith((MbpetUI) UI.getCurrent());
        	
//			MbpetUI mbpetui = new MbpetUI();
//			MbpetUI.PushThread push = mbpetui.new PushThread();
//			push.start();
			
//			new UDPServer();
//			new MasterUtils();

        } else if (event.getButton() == stopButton) {
			//testing purposes
			Notification not = new Notification("test session was interrupted", Type.TRAY_NOTIFICATION);
			not.setStyleName("dark small");
			not.setDelayMsec(1000);
			not.show(Page.getCurrent());
			
			displaySpinner(false);
			
//			// stop progressbar
//			progressThread.endThread();

			// stop UDP
			udpWorker.endThread();
			udpWorker.navToReports(false);
			
			// stop mbpet MASTER and SLAVE
			new KillMBPeTProcesses();
			
			
			// stop SLAVE(s)
			
			resetStartStopButton();
        }
    }
   

    private void displaySpinner(boolean b) {
    	spinner.setVisible(b);
    	spinLabel.setVisible(b);
    }
	
	public void displayProgressBar(boolean b) {
		// reveal progressbar
    	progressbar.setVisible(b);	//setVisible(true);
    	progressstatus.setVisible(b);	
    	
    	if (b) {
    		spinner.setVisible(false);
        	spinLabel.setVisible(false);

   	        // start progress indicator
   	        progressThread = new ProgressBarThread(this, currsession.getParameters().getTest_duration()); 		//40);
   	        progressThread.start();	//fetchAndUpdateDataWith((MbpetUI) UI.getCurrent());
    	}
	}


	public void setPageTitle(String t){
		pageTitle.setValue(t);

	}
	public String getPageTitle() {
		return pageTitle.getValue();
	}

	private String removeID(String input) {
//		String title = pageTitle.getValue();
		String title = "";
		if (input.contains("/") && input.contains("id=")) {
			title = input.substring(0, (input.indexOf("=")-2)); 
		}
//		if (input.contains("#")) {
//			title = input.substring((input.indexOf("#")+1), input.length()); 
//		}
		System.out.println("the parsed page title is: " + title);
		
		return title;
	}
	
	private TestSession getTestSessionByTitleID(String input) {
		String parsed = "";
		if (input.contains("id=")) {
			parsed = input.substring((input.indexOf("=")+1), input.length()); 
		}
		System.out.println("the parsed test session ID is: " + parsed);
		
		int id = Integer.parseInt(parsed);
//		currsession = sessions.getItem(id).getEntity();
//		
//        System.out.println("retrieved SESSION fro db is :  - " + currsession.getTitle());
		
		return sessions.getItem(id).getEntity();
	}
	
	public void resetStartStopButton() {
		startButton.setEnabled(true);
		stopButton.setEnabled(false);
		stopButton.removeStyleName("danger");
	}

//	public TabLayout getTabLayout() {
//		return tabs;
//	}
//
//	public void setTabLayout(TabLayout tabLayout) {
//		this.tabs = tabLayout;
//	}
	
}

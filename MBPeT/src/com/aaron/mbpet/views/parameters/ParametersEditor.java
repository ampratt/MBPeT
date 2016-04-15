package com.aaron.mbpet.views.parameters;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import javax.persistence.EntityManager;
import javax.persistence.NonUniqueResultException;
import javax.persistence.Persistence;
import javax.persistence.Query;

import com.aaron.mbpet.MbpetUI;
import com.aaron.mbpet.domain.DbUtils;
import com.aaron.mbpet.domain.Parameters;
import com.aaron.mbpet.domain.TestCase;
import com.aaron.mbpet.domain.TestSession;
import com.aaron.mbpet.domain.User;
import com.aaron.mbpet.services.FileSystemUtils;
import com.aaron.mbpet.services.ParametersUtils;
import com.aaron.mbpet.views.LoginView;
import com.aaron.mbpet.views.MainView;
import com.aaron.mbpet.views.cases.TestCaseForm;
import com.aaron.mbpet.views.sessions.TestSessionForm;
import com.aaron.mbpet.views.users.UserForm;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.jpacontainer.JPAContainerFactory;
import com.vaadin.data.Item;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.validator.BeanValidator;
import com.vaadin.data.validator.NullValidator;
import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.server.Page;
import com.vaadin.server.Resource;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.server.VaadinService;
import com.vaadin.shared.Position;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.AbstractTextField;
import com.vaadin.ui.Component;
import com.vaadin.ui.Field;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Tree;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

public class ParametersEditor {
	
	private JPAContainer<Parameters> parameters = ((MbpetUI) UI.getCurrent()).getParameterscontainer();
	private JPAContainer<TestSession> sessions = ((MbpetUI) UI.getCurrent()).getTestsessions();
	BeanItem<Parameters> beanItem;
	Parameters currentParams;// = new Parameters();
	
	TestSession parentsession;
//	Parameters clone;
	
	private FieldGroup binder;
	private boolean editmode = false;
	private boolean clonemode = false;
	private boolean formEdit = false;
	private boolean createmode = false;
	String prevModelsFolder;
	private boolean uploadmode = false;
	
	/*
	 * Create new Parameters
	 */
	public ParametersEditor(TestSession parentsession) {		//JPAContainer<TestCase> container
		createmode = true;
		this.parentsession = parentsession;

		this.currentParams = new Parameters(); 
		this.currentParams.setOwnersession(this.parentsession);
		this.currentParams.setSettings_file(getDefaultSettings());
			this.currentParams.setDstat_mode("None");
			this.currentParams.setUser_types("user_types.gv");
			this.currentParams.setModels_folder("models/");
			this.currentParams.setTest_report_folder("test_reports/");
			this.currentParams.setTest_duration(30);
			this.currentParams.setMonitoring_interval(3);
			this.currentParams.setMean_user_think_time(3);
			this.currentParams.setStandard_deviation(1.0);
		
//		this.currentParams.setSettings_file("Fill in parameters for Test Session '" + parentsession.getTitle() + "'");
		this.beanItem = new BeanItem<Parameters>(currentParams);

        saveParameters();
	}
	/*
	 * create uploaded parameters
	 */
	public ParametersEditor(TestSession parentsession, File uploadSessionFile) {		//JPAContainer<TestCase> container
		createmode = true;
		uploadmode  = true;
		this.parentsession = parentsession;

		this.currentParams = new Parameters(); 
		this.currentParams.setOwnersession(this.parentsession);
		this.currentParams.setSettings_file(getUploadedSettings(uploadSessionFile));
		
//		// commit individual field, parsed from ace
//		ParametersUtils.commitAceParamData(currentParams, currentParams.getSettings_file());

		this.beanItem = new BeanItem<Parameters>(currentParams);

        saveParameters();
	}
	/*
	 * Edit Mode
	 */
	public ParametersEditor(Parameters currentParams, TestSession parentsession, String settings) {		//JPAContainer<TestCase> container      
		editmode = true;
		this.parentsession = parentsession;
		
        this.currentParams = currentParams; //parameters.getItem(currentParams.getId()).getEntity();
        this.currentParams.setSettings_file(settings);
        this.beanItem = new BeanItem<Parameters>(this.currentParams);
//        binder = new FieldGroup();
        this.prevModelsFolder = currentParams.getModels_folder();

        saveParameters();
	}
	public ParametersEditor(Parameters currentParams, BeanItem<Parameters> beanItem, TestSession parentsession, FieldGroup binder) {		//JPAContainer<TestCase> container      
		formEdit = true;
		this.parentsession = parentsession;
		
	    this.currentParams = currentParams; //parameters.getItem(currentParams.getId()).getEntity();
	    this.beanItem = beanItem;	//new BeanItem<Parameters>(this.currentParams);
	    this.binder = binder;
        this.prevModelsFolder = currentParams.getModels_folder();

	    saveParameters();
	}
	//	public ParametersEditor(Parameters currentParams, BeanItem<Parameters> beanItem, TestSession parentsession, FieldGroup binder) {		//JPAContainer<TestCase> container      
//		formEdit = true;
//		this.parentsession = parentsession;
//		
//        this.currentParams = currentParams; //parameters.getItem(currentParams.getId()).getEntity();
//        this.beanItem = beanItem;	//new BeanItem<Parameters>(this.currentParams);
//
//        this.binder = binder;
//        
//        saveParameters();
//	}

	
	/*
	 * Clone existing parameters to new one
	 */
	public ParametersEditor(TestSession parentsession, String settings) {		//JPAContainer<TestCase> container
		this.clonemode = true;
		this.parentsession = parentsession;
		
		this.currentParams = new Parameters();
		this.currentParams.setOwnersession(parentsession);
		this.currentParams.setSettings_file(settings);
        this.beanItem = new BeanItem<Parameters>(this.currentParams);

        saveParameters();
	}

	public void saveParameters() {

    	Parameters queriedParams = null;
//			try {
				Object id = null;
				
				// add NEW bean object to db through jpa container
				if ((createmode==true) || (editmode==false && formEdit==false)) {
					try {
						binder.commit();
					} catch (CommitException | NullPointerException e) {
						e.printStackTrace();
					}
					
					// 1. add to container
					parameters.addEntity(beanItem.getBean());	//jpa container	

					
	                // 2. retrieving db generated id
	                EntityManager em = Persistence.createEntityManagerFactory("mbpet")
	    											.createEntityManager();	
		            Query query = em.createQuery(
		        		    "SELECT OBJECT(p) FROM Parameters p WHERE p.ownersession = :ownersession"
		        		);
		            queriedParams = (Parameters) query.setParameter("ownersession", currentParams.getOwnersession()).getSingleResult();
		            Parameters p = parameters.getItem(queriedParams.getId()).getEntity();
		            //System.out.println("the generated id is: " + p.getId());
		            id = queriedParams.getId();
              	  	
		            // Option 2. serialize blob to db
//		            DbUtils.commitUpdateToDb(currentParams.getSettings_file(), queriedParams, "settings_file");


		            
		            // 3. update parent Session to link Parameters
              	  	parentsession.setParameters(p);	//parameters.getItem(queriedParams.getId()).getEntity()
              	  	sessions.addEntity(parentsession);
              	  	
              	  	// save settings.py file to directory
              	  	FileSystemUtils fileUtils = new FileSystemUtils();
              	  	
              	  	fileUtils.writeSettingsToDisk(
	              	  		parentsession.getParentcase().getOwner().getUsername(),
							parentsession.getParentcase().getTitle(), 
							parentsession.getTitle(), 
							currentParams.getSettings_file());	
              	  	
              	  	// create models directory
              	  	fileUtils.createModelsDir(	//username, sut, session, models_dir);
							parentsession.getParentcase().getOwner().getUsername(),
							parentsession.getParentcase().getTitle(), 
							parentsession.getTitle(), 
							currentParams.getModels_folder());

              	  	// create reports directory
					fileUtils.createReportsDir(	//username, sut, session, reports_dir)
							parentsession.getParentcase().getOwner().getUsername(),
							parentsession.getParentcase().getTitle(), 
							parentsession.getTitle(), 
							currentParams.getTest_report_folder());
					
					
//	            	if(uploadmode){
//	            		// commit individual field, parsed from ace
//	            		ParametersUtils.commitAceParamData(currentParams, currentParams.getSettings_file());
//	            	}
				} else if (editmode == true && formEdit==false){

					// 1 UPDATE container
					parameters.addEntity(beanItem.getBean());
//					System.out.println("Parameters are now: " + parameters.getItem(beanItem.getBean().getId()).getEntity().getId() 
//							+ " " + parameters.getItem(beanItem.getBean().getId()).getEntity().getSettings_file());

					// 2 UPDATE parentcase reference
					parentsession.setParameters(parameters.getItem(currentParams.getId()).getEntity());
					//System.out.println("Session's Params are now: " + parentsession.getParameters().getId() + " " + parentsession.getParameters().getSettings_file());

					// edit models directory name
					//System.out.println("prevModelsFolder->" + prevModelsFolder + " and current folder->" +currentParams.getModels_folder());
					if (!prevModelsFolder.equals(currentParams.getModels_folder())) {
						new FileSystemUtils().renameModelsDir(	//username, sut, session, prevModelsDir, newModelsDir)
								parentsession.getParentcase().getOwner().getUsername(),
								parentsession.getParentcase().getTitle(), 
								parentsession.getTitle(), 
								prevModelsFolder,
								currentParams.getModels_folder());
					}
					
					// Option 2. serialize blob to db
//					DbUtils.commitUpdateToDb(currentParams.getSettings_file(), currentParams, "settings_file");

				} else if (formEdit==true) {
			        try {
						binder.commit();
					} catch (CommitException e) {
						e.printStackTrace();
					}
			        
					// 1 UPDATE container
					parameters.addEntity(beanItem.getBean());
//					System.out.println("Parameters are now: " + parameters.getItem(beanItem.getBean().getId()).getEntity().getId() 
//							+ " " + parameters.getItem(beanItem.getBean().getId()).getEntity().getSettings_file());

					// 2 UPDATE parentcase reference
					parentsession.setParameters(parameters.getItem(currentParams.getId()).getEntity());
					//System.out.println("Session's Params are now: " + parentsession.getParameters().getId() + " " + parentsession.getParameters().getSettings_file());

					// edit models directory name
					//System.out.println("prevModelsFolder->" + prevModelsFolder + " and current folder->" +currentParams.getModels_folder());
					if (!prevModelsFolder.equals(currentParams.getModels_folder())) {
						new FileSystemUtils().renameModelsDir(	//username, sut, session, prevModelsDir, newModelsDir)
								parentsession.getParentcase().getOwner().getUsername(),
								parentsession.getParentcase().getTitle(), 
								parentsession.getTitle(), 
								prevModelsFolder,
								currentParams.getModels_folder());
					}
				}
				else if (clonemode==true) {

					// 1. add to container
					parameters.addEntity(beanItem.getBean());
					
	                // 2. retrieving db generated id
	                EntityManager em = Persistence.createEntityManagerFactory("mbpet")
	    											.createEntityManager();	
		            Query query = em.createQuery(
		        		    "SELECT OBJECT(p) FROM Parameters p WHERE p.ownersession = :ownersession"
		        		);
		            queriedParams = (Parameters) query.setParameter("ownersession", currentParams.getOwnersession()).getSingleResult();
		            Parameters p = parameters.getItem(queriedParams.getId()).getEntity();
		            //System.out.println("the generated id is: " + p.getId() + " with parent session: " + p.getOwnersession().getId());
		            id = queriedParams.getId();
              	  	
		            // Option 2. serialize blob to db
//		            DbUtils.commitUpdateToDb(currentParams.getSettings_file(), queriedParams, "settings_file");

              	  	// 3. update parent Session to link Parameters !AND RECOMMIT SESSION TO CONTAINER!
              	  	parentsession.setParameters(p);	//parameters.getItem(queriedParams.getId()).getEntity()
              	  	sessions.addEntity(parentsession);
				}

//				System.out.println("\n\nALL TEST SESSIONS AND THEIR PARAMS");
//				for (Object o : sessions.getItemIds()) {
//					TestSession s = sessions.getItem(o).getEntity();
//					if (s.getParameters() != null) {
//						System.out.println(s.getId() + " " + s.getParameters().getId());						
//					}
//				}
//				System.out.println("\n\nALL PARAMETERS IN CONTAINER");
//				for (Object o : parameters.getItemIds()) {
//					Parameters p = parameters.getItem(o).getEntity();
//					System.out.println("parameter: " + p.getId() + " -> owner: " + p.getOwnersession());						
//				}
            	
//		            	if (clonemode == true) {
//		            		confirmNotification(sessions.getItem(id).getEntity().getTitle(), "was created");
//		            		close();
////		            		getUI().getNavigator()
////	            				.navigateTo(MainView.NAME + "/" + 
////	            						parentCase.getTitle() + "/" + currentParams.getTitle());
//		            	} else 
 
        		if (editmode==true) {
	        		confirmNotification(String.valueOf(currentParams.getId()), "Parameters edited");	//String.valueOf(currentParams.getId())
            	} else {
	        		confirmNotification(String.valueOf(currentParams.getId()), "Parameters created");	//String.valueOf(currentParams.getId())
            	} 
            
//			} catch (CommitException e) {
//				Notification.show("'Title' cannot be Empty. Please try again.", Type.WARNING_MESSAGE);
//				new ParametersEditor(parentsession);
//			} catch (NonUniqueResultException e) {
//				Notification.show("'Title' must be a unique name.\n'" +
//									queriedParams.getTitle() + 
//									"' already exists.\n\nPlease try again.", Type.WARNING_MESSAGE);
//				UI.getCurrent().addWindow(new ParametersEditor(tree, parentsession));
//			}


    }
	

	private String getDefaultSettings(){
//		InputStream inStream = this.getClass().getClassLoader().getResourceAsStream("META-INF/output/parameters_default.py");
		String BASEDIR;
	    if (VaadinService.getCurrent() != null) {
	        BASEDIR = VaadinService.getCurrent().getBaseDirectory().getAbsolutePath();
	    } else {
	        BASEDIR = "WebContent";
	    }
	    //System.out.println("BASEDIR ->" + BASEDIR);
	    BASEDIR+="/META-INF/output/parameters_default.py";	
	    String lineSeparator = System.getProperty("line.separator");

		Scanner scan = null;
		try {
			scan = new Scanner(new File(BASEDIR));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		StringBuilder sb = new StringBuilder();

		while (scan.hasNextLine()){
		   sb.append(scan.nextLine()).append(lineSeparator);
		}
		
		return sb.toString();
		
		
//	    File file = new File(inStream);
//	    StringBuilder fileContents = new StringBuilder((int)file.length());
//	    Scanner scanner = new Scanner(file);
//	    String lineSeparator = System.getProperty("line.separator");
//
//	    try {
//	        while(scanner.hasNextLine()) {        
//	            fileContents.append(scanner.nextLine() + lineSeparator);
//	        }
//	        return fileContents.toString();
//	    } finally {
//	        scanner.close();
//	    }


	}
	
	private String getUploadedSettings(File uploadSessionFile){
	    String lineSeparator = System.getProperty("line.separator");

		Scanner scan = null;
		try {
			scan = new Scanner(new File(uploadSessionFile + "/settings.py"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		StringBuilder sb = new StringBuilder();

		while (scan.hasNextLine()){
		   sb.append(scan.nextLine()).append(lineSeparator);
		}
		
		return sb.toString();
	}
	
	private void confirmNotification(String deletedItem, String message) {
        // welcome notification
        Notification notification = new Notification("",Type.TRAY_NOTIFICATION);
        notification
                .setDescription(message);
        notification.setHtmlContentAllowed(true);
        notification.setStyleName("dark small");	//tray  closable login-help
        notification.setPosition(Position.BOTTOM_RIGHT);
        notification.setDelayMsec(500);
        notification.show(Page.getCurrent());
	}

	    

}
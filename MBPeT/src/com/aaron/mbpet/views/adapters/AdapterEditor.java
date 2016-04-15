package com.aaron.mbpet.views.adapters;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.persistence.Query;

import com.aaron.mbpet.MbpetUI;
import com.aaron.mbpet.domain.Adapter;
import com.aaron.mbpet.domain.Adapter;
import com.aaron.mbpet.domain.Parameters;
import com.aaron.mbpet.domain.TestSession;
import com.aaron.mbpet.services.FileSystemUtils;
import com.aaron.mbpet.services.ParametersUtils;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.data.util.BeanItem;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinService;
import com.vaadin.shared.Position;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import com.vaadin.ui.Notification.Type;

public class AdapterEditor {
	
	private JPAContainer<Adapter> adapterscontainer = ((MbpetUI) UI.getCurrent()).getAdapterscontainer();
	private JPAContainer<TestSession> sessions = ((MbpetUI) UI.getCurrent()).getTestsessions();
	BeanItem<Adapter> beanItem;
	Adapter currentAdapter;// = new Adapter();
	
	TestSession ownersession;
//	Adapter clone;
	
	private FieldGroup binder;
	private boolean editmode = false;
	private boolean clonemode = false;
	private boolean formEdit = false;
	private boolean createmode = false;
  	FileSystemUtils fileUtils = new FileSystemUtils();
  	private String fileType = ".py";
	/*
	 * Create new Adapter
	 */
	public AdapterEditor(TestSession ownersession) {		//JPAContainer<TestCase> container
		createmode = true;
		this.ownersession = ownersession;

		this.currentAdapter = new Adapter(); 
		this.currentAdapter.setOwnersession(this.ownersession);
		this.currentAdapter.setAdapter_file(getDefaultAdapter());
//		this.currentParams.setSettings_file("Fill in Adapter for Test Session '" + parentsession.getTitle() + "'");
		this.beanItem = new BeanItem<Adapter>(currentAdapter);

        saveAdapter();
	}
	/*
	 * Create uploaded Adapter
	 */
	public AdapterEditor(TestSession ownersession, File uploadSessionFile) {		//JPAContainer<TestCase> container
		createmode = true;
		this.ownersession = ownersession;

		this.currentAdapter = new Adapter(); 
		this.currentAdapter.setOwnersession(this.ownersession);
		this.currentAdapter.setAdapter_file(getUploadedAdapterPY(uploadSessionFile));		
		
		this.beanItem = new BeanItem<Adapter>(currentAdapter);

        saveAdapter();
	}
	/*
	 * Edit Mode
	 */
	public AdapterEditor(Adapter currentAdapter, TestSession ownersession, 
			String adapter, String fileType) {		//JPAContainer<TestCase> container      
		editmode = true;
		this.ownersession = ownersession;
		this.fileType = fileType;
		
        this.currentAdapter = currentAdapter; //Adapter.getItem(currentParams.getId()).getEntity();
        this.currentAdapter.setAdapter_file(adapter);
        this.beanItem = new BeanItem<Adapter>(this.currentAdapter);
//        binder = new FieldGroup();

        saveAdapter();
	}
//	public AdapterEditor(Adapter currentAdapter, BeanItem<Adapter> beanItem, TestSession ownersession, FieldGroup binder) {		//JPAContainer<TestCase> container      
//		formEdit = true;
//		this.ownersession = ownersession;
//		
//	    this.currentAdapter = currentAdapter; //Adapter.getItem(currentParams.getId()).getEntity();
//	    this.beanItem = beanItem;	//new BeanItem<Adapter>(this.currentParams);
//	    this.binder = binder;
//
//	    saveAdapter();
//	}
	
	
	//	public AdapterEditor(Adapter currentParams, BeanItem<Adapter> beanItem, TestSession parentsession, FieldGroup binder) {		//JPAContainer<TestCase> container      
//		formEdit = true;
//		this.parentsession = parentsession;
//		
//        this.currentParams = currentParams; //Adapter.getItem(currentParams.getId()).getEntity();
//        this.beanItem = beanItem;	//new BeanItem<Adapter>(this.currentParams);
//
//        this.binder = binder;
//        
//        saveAdapter();
//	}

	
	/*
	 * Clone existing Adapter to new one
	 */
	public AdapterEditor(TestSession ownersession, String adapter_file) {		//JPAContainer<TestCase> container
		this.clonemode = true;
		this.ownersession = ownersession;
		
		this.currentAdapter = new Adapter();
		this.currentAdapter.setOwnersession(ownersession);
		this.currentAdapter.setAdapter_file(adapter_file);
        this.beanItem = new BeanItem<Adapter>(this.currentAdapter);

        saveAdapter();
	}

	public void saveAdapter() {

    	Adapter queriedAdapter = null;
//			try {
				Object id = null;
				
				// add NEW bean object to db through jpa container
				if ((createmode==true) || (editmode==false && formEdit==false)) {
					try {
//						String adap = currentAdapter.getAdapter_file();
//						if(adap == null || adap.equals("")){
//							System.out.println("adapter was null");
//							currentAdapter.setAdapter_file("not empty anymore");
//							beanItem.getItemProperty("adapter_file").setValue("not empty anymore");
//						}
						binder.commit();
					} catch (CommitException | NullPointerException e) {
						e.printStackTrace();
					}
					
					// 1. add to container
					adapterscontainer.addEntity(beanItem.getBean());	//jpa container	

					
	                // 2. retrieving db generated id
	                EntityManager em = Persistence.createEntityManagerFactory("mbpet")
	    											.createEntityManager();	
		            Query query = em.createQuery(
		        		    "SELECT OBJECT(a) FROM Adapter a WHERE a.ownersession = :ownersession"
		        		);
		            queriedAdapter = (Adapter) query.setParameter("ownersession", currentAdapter.getOwnersession()).getSingleResult();
		            Adapter a = adapterscontainer.getItem(queriedAdapter.getId()).getEntity();
		            //System.out.println("the generated id is: " + a.getId());
		            id = queriedAdapter.getId();
              	  	
		            // Option 2. serialize blob to db
//		            DbUtils.commitUpdateToDb(currentParams.getSettings_file(), queriedParams, "settings_file");

		            // 3. update parent Session to link Adapter
              	  	ownersession.setAdapter(a);	//Adapter.getItem(queriedParams.getId()).getEntity()
              	  	sessions.addEntity(ownersession);
              	  	
              	  	// save adapter.py file to directory              	  	
              	  	fileUtils.writeAdapterToDisk(
	              	  		ownersession.getParentcase().getOwner().getUsername(),
							ownersession.getParentcase().getTitle(), 
							ownersession.getTitle(), 
							currentAdapter.getAdapter_file(),
							"adapter.py");	
              	  	
					
				} else if (editmode == true && formEdit==false){

					// 1 UPDATE container
					adapterscontainer.addEntity(beanItem.getBean());
//					System.out.println("Adapter is now: " + adapterscontainer.getItem(beanItem.getBean().getId()).getEntity().getId() 
//							+ " " + adapterscontainer.getItem(beanItem.getBean().getId()).getEntity().getAdapter_file());

					// 2 UPDATE parentcase reference
					ownersession.setAdapter(adapterscontainer.getItem(currentAdapter.getId()).getEntity());
					//System.out.println("Session's Adapter is now: " + ownersession.getAdapter().getId() + " " + ownersession.getAdapter().getAdapter_file());

					// write adapter file to disk
					fileUtils.writeAdapterToDisk(	//username, sut, session, settings_file)
							ownersession.getParentcase().getOwner().getUsername(),
							ownersession.getParentcase().getTitle(), 
							ownersession.getTitle(), 
							currentAdapter.getAdapter_file(),
							fileType);
					
				} else if (formEdit==true) {
			        try {
						binder.commit();
					} catch (CommitException e) {
						e.printStackTrace();
					}
			        
					// 1 UPDATE container
					adapterscontainer.addEntity(beanItem.getBean());
//					System.out.println("Adapter is now: " + adapterscontainer.getItem(beanItem.getBean().getId()).getEntity().getId() 
//							+ " " + adapterscontainer.getItem(beanItem.getBean().getId()).getEntity().getAdapter_file());

					// 2 UPDATE parentcase reference
					ownersession.setAdapter(adapterscontainer.getItem(currentAdapter.getId()).getEntity());
//					System.out.println("Session's Adapter is now: " + ownersession.getAdapter().getId() + " " + ownersession.getAdapter().getAdapter_file());

				}
				else if (clonemode==true) {

					// 1. add to container
					adapterscontainer.addEntity(beanItem.getBean());
					
	                // 2. retrieving db generated id
	                EntityManager em = Persistence.createEntityManagerFactory("mbpet")
	    											.createEntityManager();	
		            Query query = em.createQuery(
		        		    "SELECT OBJECT(a) FROM Adapter a WHERE a.ownersession = :ownersession"
		        		);
		            queriedAdapter = (Adapter) query.setParameter("ownersession", currentAdapter.getOwnersession()).getSingleResult();
		            Adapter a = adapterscontainer.getItem(queriedAdapter.getId()).getEntity();
		            //System.out.println("the generated id is: " + a.getId() + " with parent session: " + a.getOwnersession().getId());
		            id = queriedAdapter.getId();
              	  	
		            // Option 2. serialize blob to db
//		            DbUtils.commitUpdateToDb(currentParams.getSettings_file(), queriedParams, "settings_file");

              	  	// 3. update parent Session to link Adapter !AND RECOMMIT SESSION TO CONTAINER!
              	  	ownersession.setAdapter(a);	//Adapter.getItem(queriedParams.getId()).getEntity()
              	  	sessions.addEntity(ownersession);
				}

				// TESTING???
//				System.out.println("\n\nALL TEST SESSIONS AND THEIR Adapters");
//				for (Object o : sessions.getItemIds()) {
//					TestSession s = sessions.getItem(o).getEntity();
//					if (s.getAdapter() != null) {
//						System.out.println(s.getId() + " " + s.getAdapter().getId());						
//					}
//				}
//				System.out.println("\n\nALL Adapters IN CONTAINER");
//				for (Object o : adapterscontainer.getItemIds()) {
//					Adapter a = adapterscontainer.getItem(o).getEntity();
//					System.out.println("adapter: " + a.getId() + " -> owner: " + a.getOwnersession());						
//				}
            	
//		            	if (clonemode == true) {
//		            		confirmNotification(sessions.getItem(id).getEntity().getTitle(), "was created");
//		            		close();
////		            		getUI().getNavigator()
////	            				.navigateTo(MainView.NAME + "/" + 
////	            						parentCase.getTitle() + "/" + currentParams.getTitle());
//		            	} else 
 
        		if (editmode==true) {
	        		confirmNotification(String.valueOf(currentAdapter.getId()), "Adapter edited");	//String.valueOf(currentParams.getId())
            	} else {
	        		confirmNotification(String.valueOf(currentAdapter.getId()), "Adapter created");	//String.valueOf(currentParams.getId())
            	} 
            
//			} catch (CommitException e) {
//				Notification.show("'Title' cannot be Empty. Please try again.", Type.WARNING_MESSAGE);
//				new AdapterEditor(parentsession);
//			} catch (NonUniqueResultException e) {
//				Notification.show("'Title' must be a unique name.\n'" +
//									queriedParams.getTitle() + 
//									"' already exists.\n\nPlease try again.", Type.WARNING_MESSAGE);
//				UI.getCurrent().addWindow(new AdapterEditor(tree, parentsession));
//			}


    }
	

	private String getDefaultAdapter(){
//		InputStream inStream = this.getClass().getClassLoader().getResourceAsStream("META-INF/output/Adapter_default.py");
		String BASEDIR;
	    if (VaadinService.getCurrent() != null) {
	        BASEDIR = VaadinService.getCurrent().getBaseDirectory().getAbsolutePath();
	    } else {
	        BASEDIR = "WebContent";
	    }
	    //System.out.println("BASEDIR ->" + BASEDIR);
	    BASEDIR+="/META-INF/output/adapter_default.py";	
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
	
	private String getUploadedAdapterPY(File uploadSessionFile){
	    String lineSeparator = System.getProperty("line.separator");

		Scanner scan = null;
		try {
			scan = new Scanner(new File(uploadSessionFile + "/adapter.py"));
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

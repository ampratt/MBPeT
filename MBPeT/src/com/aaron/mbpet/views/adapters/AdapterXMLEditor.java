package com.aaron.mbpet.views.adapters;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.persistence.Query;

import com.aaron.mbpet.MbpetUI;
import com.aaron.mbpet.domain.AdapterXML;
import com.aaron.mbpet.domain.TestSession;
import com.aaron.mbpet.services.FileSystemUtils;
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

public class AdapterXMLEditor {
	
	private JPAContainer<AdapterXML> adaptersxmlcontainer = ((MbpetUI) UI.getCurrent()).getAdaptersXMLcontainer();
	private JPAContainer<TestSession> sessions = ((MbpetUI) UI.getCurrent()).getTestsessions();
	BeanItem<AdapterXML> beanItem;
	AdapterXML currentAdapterXML;// = new Adapter();
	
	TestSession ownersession;
//	AdapterXML clone;
	
	private FieldGroup binder;
	private boolean editmode = false;
	private boolean clonemode = false;
	private boolean formEdit = false;
	private boolean createmode = false;
  	FileSystemUtils fileUtils = new FileSystemUtils();
  	private String fileType = ".xml";
  	
  	
	/*
	 * Create new AdapterXML
	 */
	public AdapterXMLEditor(TestSession ownersession) {		//JPAContainer<TestCase> container
		createmode = true;
		this.ownersession = ownersession;

		this.currentAdapterXML = new AdapterXML(); 
		this.currentAdapterXML.setOwnersession(this.ownersession);
		this.currentAdapterXML.setAdapterXML_file(getDefaultSettings());
//		this.currentParams.setSettings_file("Fill in AdapterXML for Test Session '" + parentsession.getTitle() + "'");
		this.beanItem = new BeanItem<AdapterXML>(currentAdapterXML);

        saveAdapterXML();
	}

	/*
	 * Edit Mode
	 */
	public AdapterXMLEditor(AdapterXML currentAdapterXML, TestSession ownersession, 
			String adapterXML, String fileType) {		//JPAContainer<TestCase> container      
		editmode = true;
		this.ownersession = ownersession;
		this.fileType = fileType;
		
        this.currentAdapterXML = currentAdapterXML; //Adapter.getItem(currentParams.getId()).getEntity();
        this.currentAdapterXML.setAdapterXML_file(adapterXML);
        this.beanItem = new BeanItem<AdapterXML>(this.currentAdapterXML);
//        binder = new FieldGroup();

        saveAdapterXML();
	}

	
	/*
	 * Clone existing AdapterXML to new one
	 */
	public AdapterXMLEditor(TestSession ownersession, String adapterXML_file) {		//JPAContainer<TestCase> container
		this.clonemode = true;
		this.ownersession = ownersession;
		
		this.currentAdapterXML = new AdapterXML();
		this.currentAdapterXML.setOwnersession(ownersession);
		this.currentAdapterXML.setAdapterXML_file(adapterXML_file);
        this.beanItem = new BeanItem<AdapterXML>(this.currentAdapterXML);

        saveAdapterXML();
	}

	public void saveAdapterXML() {

    	AdapterXML queriedAdapterXML = null;
//			try {
				Object id = null;
				
				// add NEW bean object to db through jpa container
				if ((createmode==true) || (editmode==false && formEdit==false)) {
					try {
//						String adap = currentAdapterXML.getAdapterXML_file();
//						if(adap == null || adap.equals("")){
//							System.out.println("AdapterXML was null");
//							currentAdapterXML.setAdapterXML_file("not empty anymore");
//							beanItem.getItemProperty("adapter_file").setValue("not empty anymore");
//						}
						binder.commit();
					} catch (CommitException | NullPointerException e) {
						e.printStackTrace();
					}
					
					// 1. add to container
					adaptersxmlcontainer.addEntity(beanItem.getBean());	//jpa container	

					
	                // 2. retrieving db generated id
	                EntityManager em = Persistence.createEntityManagerFactory("mbpet")
	    											.createEntityManager();	
		            Query query = em.createQuery(
		        		    "SELECT OBJECT(a) FROM AdapterXML a WHERE a.ownersession = :ownersession"
		        		);
		            queriedAdapterXML = (AdapterXML) query.setParameter("ownersession", currentAdapterXML.getOwnersession()).getSingleResult();
		            AdapterXML a = adaptersxmlcontainer.getItem(queriedAdapterXML.getId()).getEntity();
		            System.out.println("the generated id is: " + a.getId());
		            id = queriedAdapterXML.getId();
              	  	
		            // Option 2. serialize blob to db
//		            DbUtils.commitUpdateToDb(currentParams.getSettings_file(), queriedParams, "settings_file");

		            // 3. update parent Session to link AdapterXML
              	  	ownersession.setAdapterXML(a);	//AdapterXML.getItem(queriedParams.getId()).getEntity()
              	  	sessions.addEntity(ownersession);
              	  	
              	  	// save adapter.xml file to directory              	  	
              	  	fileUtils.writeAdapterToDisk(
	              	  		ownersession.getParentcase().getOwner().getUsername(),
							ownersession.getParentcase().getTitle(), 
							ownersession.getTitle(), 
							currentAdapterXML.getAdapterXML_file(),
							"adapter.xml");	
              	  	
					
				} else if (editmode == true && formEdit==false){

					// 1 UPDATE container
					adaptersxmlcontainer.addEntity(beanItem.getBean());
					System.out.println("AdapterXML is now: " + adaptersxmlcontainer.getItem(beanItem.getBean().getId()).getEntity().getId() 
							+ " " + adaptersxmlcontainer.getItem(beanItem.getBean().getId()).getEntity().getAdapterXML_file());

					// 2 UPDATE parentcase reference
					ownersession.setAdapterXML(adaptersxmlcontainer.getItem(currentAdapterXML.getId()).getEntity());
					System.out.println("Session's AdapterXML is now: " + ownersession.getAdapterXML().getId() + " " + ownersession.getAdapterXML().getAdapterXML_file());

					// write AdapterXML file to disk
					fileUtils.writeAdapterToDisk(	//username, sut, session, settings_file)
							ownersession.getParentcase().getOwner().getUsername(),
							ownersession.getParentcase().getTitle(), 
							ownersession.getTitle(), 
							currentAdapterXML.getAdapterXML_file(),
							fileType);
					
				} else if (formEdit==true) {
			        try {
						binder.commit();
					} catch (CommitException e) {
						e.printStackTrace();
					}
			        
					// 1 UPDATE container
			        adaptersxmlcontainer.addEntity(beanItem.getBean());
					System.out.println("AdapterXML is now: " + adaptersxmlcontainer.getItem(beanItem.getBean().getId()).getEntity().getId() 
							+ " " + adaptersxmlcontainer.getItem(beanItem.getBean().getId()).getEntity().getAdapterXML_file());

					// 2 UPDATE parentcase reference
					ownersession.setAdapterXML(adaptersxmlcontainer.getItem(currentAdapterXML.getId()).getEntity());
					System.out.println("Session's AdapterXML is now: " + ownersession.getAdapterXML().getId() + " " + ownersession.getAdapterXML().getAdapterXML_file());

				}
				else if (clonemode==true) {

					// 1. add to container
					adaptersxmlcontainer.addEntity(beanItem.getBean());
					
	                // 2. retrieving db generated id
	                EntityManager em = Persistence.createEntityManagerFactory("mbpet")
	    											.createEntityManager();	
		            Query query = em.createQuery(
		        		    "SELECT OBJECT(a) FROM AdapterXML a WHERE a.ownersession = :ownersession"
		        		);
		            queriedAdapterXML = (AdapterXML) query.setParameter("ownersession", currentAdapterXML.getOwnersession()).getSingleResult();
		            AdapterXML a = adaptersxmlcontainer.getItem(queriedAdapterXML.getId()).getEntity();
		            System.out.println("the generated id is: " + a.getId() + " with parent session: " + a.getOwnersession().getId());
		            id = queriedAdapterXML.getId();
              	  	
		            // Option 2. serialize blob to db
//		            DbUtils.commitUpdateToDb(currentParams.getSettings_file(), queriedParams, "settings_file");

              	  	// 3. update parent Session to link AdapterXML !AND RECOMMIT SESSION TO CONTAINER!
              	  	ownersession.setAdapterXML(a);	//Adapter.getItem(queriedParams.getId()).getEntity()
              	  	sessions.addEntity(ownersession);
				}

				System.out.println("\n\nALL TEST SESSIONS AND THEIR AdapterXMLs");
				for (Object o : sessions.getItemIds()) {
					TestSession s = sessions.getItem(o).getEntity();
					if (s.getAdapterXML() != null) {
						System.out.println(s.getId() + " " + s.getAdapterXML().getId());						
					}
				}
				System.out.println("\n\nALL AdapterXMLs IN CONTAINER");
				for (Object o : adaptersxmlcontainer.getItemIds()) {
					AdapterXML a = adaptersxmlcontainer.getItem(o).getEntity();
					System.out.println("AdapterXML: " + a.getId() + " -> owner: " + a.getOwnersession());						

				}
            	
//		            	if (clonemode == true) {
//		            		confirmNotification(sessions.getItem(id).getEntity().getTitle(), "was created");
//		            		close();
////		            		getUI().getNavigator()
////	            				.navigateTo(MainView.NAME + "/" + 
////	            						parentCase.getTitle() + "/" + currentParams.getTitle());
//		            	} else 
 
        		if (editmode==true) {
	        		confirmNotification(String.valueOf(currentAdapterXML.getId()), "Adapter.xml edited");	//String.valueOf(currentParams.getId())
            	} else {
	        		confirmNotification(String.valueOf(currentAdapterXML.getId()), "Adapter.xml created");	//String.valueOf(currentParams.getId())
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
	

	private String getDefaultSettings(){
//		InputStream inStream = this.getClass().getClassLoader().getResourceAsStream("META-INF/output/Adapter_default.py");
		String BASEDIR;
	    if (VaadinService.getCurrent() != null) {
	        BASEDIR = VaadinService.getCurrent().getBaseDirectory().getAbsolutePath();
	    } else {
	        BASEDIR = "WebContent";
	    }
	    System.out.println("BASEDIR ->" + BASEDIR);
	    BASEDIR+="/WEB-INF/tmp/adapter_default.xml";	
//	    BASEDIR+="/META-INF/output/adapter_default.py";	
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

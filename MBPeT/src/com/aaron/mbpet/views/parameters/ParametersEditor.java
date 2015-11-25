package com.aaron.mbpet.views.parameters;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
	
	private JPAContainer<Parameters> parameters = MainView.parameterscontainer;
	BeanItem<Parameters> beanItem;
	Parameters currentParams;// = new Parameters();
	
	TestSession parentsession;
//	Parameters clone;
	
	private FieldGroup binder;
	private boolean editmode = false;
	private boolean clonemode = false;
	private boolean formEdit = false;
	

	/*
	 * Create new Parameters
	 */
	public ParametersEditor(TestSession parentsession) {		//JPAContainer<TestCase> container
		this.parentsession = parentsession;

		this.currentParams = new Parameters(); 
		this.currentParams.setOwnersession(this.parentsession);
		this.currentParams.setSettings_file("Fill in parameters for Test Session '" + parentsession.getTitle() + "'");
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

        saveParameters();
	}
	public ParametersEditor(Parameters currentParams, BeanItem<Parameters> beanItem, TestSession parentsession, FieldGroup binder) {		//JPAContainer<TestCase> container      
		formEdit = true;
		this.parentsession = parentsession;
		
	    this.currentParams = currentParams; //parameters.getItem(currentParams.getId()).getEntity();
	    this.beanItem = beanItem;	//new BeanItem<Parameters>(this.currentParams);
	
	    this.binder = binder;
	    
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
				if (editmode == false && formEdit==false) {
					try {
						binder.commit();
					} catch (CommitException e) {
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
		            System.out.println("the generated id is: " + p.getId());
		            id = queriedParams.getId();
              	  	
		            // Option 2. serialize blob to db
//		            DbUtils.commitUpdateToDb(currentParams.getSettings_file(), queriedParams, "settings_file");

		            // 3. update parent Session to link Parameters
              	  	parentsession.setParameters(p);	//parameters.getItem(queriedParams.getId()).getEntity()
              	  	MainView.sessions.addEntity(parentsession);

				} else if (editmode == true && formEdit==false){

					// 1 UPDATE container
					parameters.addEntity(beanItem.getBean());
					System.out.println("Parameters are now: " + parameters.getItem(beanItem.getBean().getId()).getEntity().getId() 
							+ " " + parameters.getItem(beanItem.getBean().getId()).getEntity().getSettings_file());

					// 2 UPDATE parentcase reference
					parentsession.setParameters(parameters.getItem(currentParams.getId()).getEntity());
					System.out.println("Session's Params are now: " + parentsession.getParameters().getId() + " " + parentsession.getParameters().getSettings_file());

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
					System.out.println("Parameters are now: " + parameters.getItem(beanItem.getBean().getId()).getEntity().getId() 
							+ " " + parameters.getItem(beanItem.getBean().getId()).getEntity().getSettings_file());

					// 2 UPDATE parentcase reference
					parentsession.setParameters(parameters.getItem(currentParams.getId()).getEntity());
					System.out.println("Session's Params are now: " + parentsession.getParameters().getId() + " " + parentsession.getParameters().getSettings_file());

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
		            System.out.println("the generated id is: " + p.getId() + " with parent session: " + p.getOwnersession().getId());
		            id = queriedParams.getId();
              	  	
		            // Option 2. serialize blob to db
//		            DbUtils.commitUpdateToDb(currentParams.getSettings_file(), queriedParams, "settings_file");

              	  	// 3. update parent Session to link Parameters !AND RECOMMIT SESSION TO CONTAINER!
              	  	parentsession.setParameters(p);	//parameters.getItem(queriedParams.getId()).getEntity()
              	  	MainView.sessions.addEntity(parentsession);
				}

				System.out.println("\n\nALL TEST SESSIONS AND THEIR PARAMS");
				for (Object o : MainView.sessions.getItemIds()) {
					TestSession s = MainView.sessions.getItem(o).getEntity();
					if (s.getParameters() != null) {
						System.out.println(s.getId() + " " + s.getParameters().getId());						
					}
				}
				System.out.println("\n\nALL PARAMETERS IN CONTAINER");
				for (Object o : parameters.getItemIds()) {
					Parameters p = parameters.getItem(o).getEntity();
					System.out.println("parameter: " + p.getId() + " -> owner: " + p.getOwnersession());						

				}
            	
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
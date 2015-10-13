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
import com.aaron.mbpet.views.MBPeTMenu;
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

// Define a sub-window by inheritance
public class ParametersEditor {

	
	private JPAContainer<Parameters> parameters;
	BeanItem<Parameters> beanItem;
	Parameters currentParams;// = new Parameters();
	
	TestSession parentsession;
	Parameters clone;
	
	boolean editmode = false;
	boolean clonemode = false;
	


	/*
	 * Create new Parameters
	 */
	public ParametersEditor(TestSession parentsession) {		//JPAContainer<TestCase> container
		this.parentsession = parentsession;
		parameters = MBPeTMenu.parameters;	//JPAContainerFactory.make(TestSession.class, MbpetUI.PERSISTENCE_UNIT);	//container;

		currentParams = new Parameters(); 
		currentParams.setOwnersession(this.parentsession);
		this.beanItem = new BeanItem<Parameters>(currentParams);

        saveParameters();
	}

	/*
	 * Edit Mode
	 */
	public ParametersEditor(Parameters currentParams, TestSession parentsession, String settings) {		//JPAContainer<TestCase> container      
		editmode = true;
		this.parentsession = parentsession;
		
		parameters = MBPeTMenu.parameters;	//JPAContainerFactory.make(TestSession.class, MbpetUI.PERSISTENCE_UNIT);	//container;
        this.currentParams = parameters.getItem(currentParams.getId()).getEntity();
        this.currentParams.setSettings_file(settings);
        this.beanItem = new BeanItem<Parameters>(this.currentParams);


        saveParameters();
	}
	
	
	/*
	 * Clone existing test Session to new one
	 */
	public ParametersEditor(TestSession parentsession, String settings) {		//JPAContainer<TestCase> container
		this.clonemode = true;
		this.parentsession = parentsession;
		
		parameters = MBPeTMenu.parameters;	
		
		this.clone = new Parameters();
		clone.setOwnersession(parentsession);
		clone.setSettings_file(settings);

		this.beanItem = new BeanItem<Parameters>(clone);

        saveParameters();
	}

	public void saveParameters() {

    	
    	Parameters queriedParams = null;
    	
//			try {

				Object id = null;
				
				// add NEW bean object to db through jpa container
				if (editmode == false && clonemode==false) {
					
					// 1. add to container
					parameters.addEntity(beanItem.getBean());	//jpa container	


	                // 2. retrieving db generated id
	                EntityManager em = Persistence.createEntityManagerFactory("mbpet")
	    											.createEntityManager();	
		            Query query = em.createQuery(
		        		    "SELECT OBJECT(p) FROM Parameters p WHERE p.ownersession = :ownersession"
		        		);
		            queriedParams = (Parameters) query.setParameter("ownersession", currentParams.getOwnersession()).getSingleResult();
		            System.out.println("the generated id is: " + queriedParams.getId());
		            id = queriedParams.getId();
              	  	
		            // Option 2. serialize blob to db
//		            DbUtils.commitUpdateToDb(currentParams.getSettings_file(), queriedParams, "settings_file");

		            // 3. update parent Session to link Parameters
              	  	parentsession.setParameters(queriedParams);
	            	
				} else if (editmode == true){
					
//              	  	//1 UPDATE parentcase reference
//					parentsession.updateSessionData(parameters.getItem(currentParams.getId()).getEntity());
//					System.out.println("Parameters are now: " + currentParams);

					// 1 UPDATE container
					parameters.addEntity(beanItem.getBean());
//					System.out.println("Entity is now: " + parameters.getItem(currentParams.getId()).getEntity().getSettings_file());

					// Option 2. serialize blob to db
					DbUtils.commitUpdateToDb(currentParams.getSettings_file(), currentParams, "settings_file");

				} else if (clonemode==true) {

					// 1. add to container
					parameters.addEntity(beanItem.getBean());	//jpa container	
					
	                // 2. retrieving db generated id
	                EntityManager em = Persistence.createEntityManagerFactory("mbpet")
	    											.createEntityManager();	
		            Query query = em.createQuery(
		        		    "SELECT OBJECT(p) FROM Parameters p WHERE p.ownersession = :ownersession"
		        		);
		            queriedParams = (Parameters) query.setParameter("ownersession", clone.getOwnersession()).getSingleResult();
		            System.out.println("the generated id is: " + queriedParams.getId());
		            id = queriedParams.getId();
              	  	
		            // Option 2. serialize blob to db
		            DbUtils.commitUpdateToDb(clone.getSettings_file(), queriedParams, "settings_file");

              	  	// 3. update parent Session to link Parameters
              	  	parentsession.setParameters(queriedParams);
	            	
				}

            	
//		            	if (clonemode == true) {
//		            		confirmNotification(sessions.getItem(id).getEntity().getTitle(), "was created");
//		            		close();
////		            		getUI().getNavigator()
////	            				.navigateTo(MainView.NAME + "/" + 
////	            						parentCase.getTitle() + "/" + clone.getTitle());
//		            	} else 
 
        		if (editmode==true ) {
	        		confirmNotification(String.valueOf(currentParams.getId()), "was edited");
            		
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
        Notification notification = new Notification(deletedItem, Type.TRAY_NOTIFICATION);
        notification
                .setDescription(message);
        notification.setHtmlContentAllowed(true);
        notification.setStyleName("dark small");	//tray  closable login-help
        notification.setPosition(Position.BOTTOM_RIGHT);
        notification.setDelayMsec(5000);
        notification.show(Page.getCurrent());
	}

	    

}
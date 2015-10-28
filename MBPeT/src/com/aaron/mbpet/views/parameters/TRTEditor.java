package com.aaron.mbpet.views.parameters;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NonUniqueResultException;
import javax.persistence.Persistence;
import javax.persistence.Query;

import com.aaron.mbpet.MbpetUI;
import com.aaron.mbpet.domain.Model;
import com.aaron.mbpet.domain.Parameters;
import com.aaron.mbpet.domain.TRT;
import com.aaron.mbpet.domain.TestCase;
import com.aaron.mbpet.domain.TestSession;
import com.aaron.mbpet.domain.User;
import com.aaron.mbpet.views.LoginView;
import com.aaron.mbpet.views.MBPeTMenu;
import com.aaron.mbpet.views.MainView;
import com.aaron.mbpet.views.cases.ModelEditorTable;
import com.aaron.mbpet.views.cases.SessionEditorTable;
import com.aaron.mbpet.views.cases.TestCaseEditor;
import com.aaron.mbpet.views.cases.TestCaseForm;
import com.aaron.mbpet.views.parameters.ParametersEditor;
import com.aaron.mbpet.views.users.UserForm;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.jpacontainer.JPAContainerFactory;
import com.vaadin.data.Item;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.filter.Compare.Equal;
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
import com.vaadin.ui.Grid;
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
public class TRTEditor extends Window implements Button.ClickListener {


	private Grid grid;
	private Button createButton;
	private Button cancelButton;
	
	private JPAContainer<Parameters> parameters = MBPeTMenu.parameters;
	private JPAContainer<TRT> trtcontainer;
	BeanItem<TRT> beanItem;
	TRT currTrt;
	Parameters parentparams;
	
	FieldGroup binder;
	private TRTForm TRTForm;

	boolean editmode = false;
	private String prevTitle="";

	
	/*
	 * Create new Test Session
	 */
	public TRTEditor(Parameters parentparameters, Grid grid) {
		
		currTrt = new TRT(); 
		this.beanItem = new BeanItem<TRT>(currTrt);
		trtcontainer = MBPeTMenu.trtcontainer;	

		init(parentparameters, grid);
	}


	/*
	 * Edit Mode
	 */
	public TRTEditor(Object trtid, Parameters parentparameters, Grid grid) {		//JPAContainer<TestCase> container
		this.editmode = true;
        
        trtcontainer = MBPeTMenu.trtcontainer;	//JPAContainerFactory.make(TestSession.class, MbpetUI.PERSISTENCE_UNIT);	//container;
        this.currTrt = trtcontainer.getItem(trtid).getEntity();
        this.beanItem = new BeanItem<TRT>(currTrt);

		prevTitle = currTrt.getAction();

        init(parentparameters, grid);
	}
	
	
	private void init(Parameters parentparameters, Grid grid) {
		center();
		setResizable(false);
		setClosable(true);
		setModal(false);

		this.parentparams = parentparameters;
		this.grid = grid;
		
        setSizeUndefined();
        setContent(AutoGeneratedLayoutDesign()); //ManualLayoutDesign
        setCaption(buildCaption());
	}
	
	
    /**
     * @return the caption of the editor window
     */
    private String buildCaption() {
    	if (editmode) {
    		return String.format("Edit Target Response Time: %s", 
    				currTrt.getAction());
    	} else {
    		return "Create Target Response Time";
    	}
//    	else if (!(parentparams == null) ) {		//.getItemProperty("firstname").getValue()
//    		return String.format("Create Target Response Time: %s", 
//    				parentparams.getTitle());		//testcases.getItem(parentCase.getId()).getItemProperty("title")
//    	} 
    }
    
	
    
	private Component AutoGeneratedLayoutDesign() {
		final VerticalLayout layout = new VerticalLayout();
		layout.setSpacing(true);
		layout.setMargin(true);
	
		// set parent Test Case manually without a field
		if (editmode == false) {
			currTrt.setParentparameter(parentparams);			
		}
		
		TRTForm = new TRTForm();
		layout.addComponent(TRTForm);

		binder = new FieldGroup();
//		BeanItem<Person> item = new BeanItem<Person>(person);		// takes item as argument
//		item.addNestedProperty("address.street");	// Address info is not person but address to which person is linked
		
		binder.setItemDataSource(beanItem); 	// link to data model to binder
		binder.bindMemberFields(TRTForm);	// link the layout to binder	
		
		for (Field<?> field : binder.getFields()) {
			if (field.getCaption().equals("Action"))
				field.focus();
		}
		
		// button layout
		HorizontalLayout buttons = new HorizontalLayout();
		buttons.setWidth("100%");
		buttons.addStyleName("buttons-margin-top");
		layout.addComponent(buttons);
		
		createButton = new Button("Create", this);
		if (editmode) createButton.setCaption("Save");
		createButton.addStyleName(ValoTheme.BUTTON_PRIMARY);
		createButton.setClickShortcut(KeyCode.ENTER);
		
		cancelButton = new Button("Cancel", this);
		
		buttons.addComponents(createButton, cancelButton);
		buttons.setComponentAlignment(createButton, Alignment.MIDDLE_LEFT);
		buttons.setComponentAlignment(cancelButton, Alignment.MIDDLE_RIGHT);
		
		return layout;
	}
	
	
	
	   public void buttonClick(ClickEvent event) {
	        if (event.getButton() == createButton) {
//	            editorForm.commit();
//	            fireEvent(new EditorSavedEvent(this, personItem));
	        	
	        	TRT queriedTRT = null;
	        	String wrongTitle = "";
	        	
					try {
						TRTForm.toggleValidationMessages(true);
						//					title.setValidationVisible(true);

						// commit the fieldgroup
						binder.commit();
						
						// check SESSION title doesnt exist for THIS SESSION
						int id =0;
						boolean titleOK = true;
						id = beanItem.getBean().getId();	//testsession.getId();
						System.out.println("parentparams.getTarget_response_times() : " + parentparams.getTarget_response_times());
						for (TRT t : parentparams.getTarget_response_times()) {	//sessions.getItemIds()
							System.out.println("Existing action -> new action : " + t.getAction() + "->" + currTrt.getAction());
							System.out.println("Existing id -> new id : " + t.getId() + "->" + id);
							if (t.getAction().equals(currTrt.getAction()) && !(t.getId()==id) ) {	
								currTrt.setAction(prevTitle);
								if (editmode == true) {
									trtcontainer.addEntity(currTrt);
								}
								wrongTitle = t.getAction();

								titleOK = false;
								break;
							}
						}
						
						
						if (titleOK == true) {
							System.out.println("TITLE WAS FINE. EDITING");
							
							// add NEW bean object to db through jpa container
							if (editmode == false) {
									
								// 1. add to container
								trtcontainer.addEntity(beanItem.getBean()); //jpa container	
								
								// 2. retrieving db generated id
								EntityManager em = Persistence.createEntityManagerFactory("mbpet")
										.createEntityManager();
								Query query = em.createQuery("SELECT OBJECT(t) FROM TRT t WHERE t.action = :action AND t.parentparameter = :parentparameter");
								//		            query.setParameter("title", newsession.getTitle());
								query.setParameter("action", currTrt.getAction());
								query.setParameter("parentparameter", currTrt.getParentparameter()); //MainView.sessionUser
								queriedTRT = (TRT) query.getSingleResult();

								System.out.println("the generated id is: " + queriedTRT.getId());
								id = queriedTRT.getId(); // here is the id we need for tree
								

								// 3. update parent Parameters to add TRT to List<TRT> trt
								parentparams.addTRT(queriedTRT);
								parameters.addEntity(parentparams);
								
								
								// 4. UPDATE grid
			              	  	
								
								System.out.println("WHAT IS NEW LIST OF SESSIONS: "
												+ parentparams.getTarget_response_times()); // testing purposes
								for (TRT s : parentparams.getTarget_response_times()) {
									System.out.println(s.getId() + " - "
											+ s.getAction()); // testing purposes	            		
								}
									
				            	
							} else if (editmode == true){
								// EDIT existing object
								
								
			              	  	//1 UPDATE parentparameters reference
//								parentparams.updateSessionData(trtcontainer.getItem(currTrt.getId()).getEntity());
								
								System.out.println("Test session is now: " + currTrt.getAction());
	
								// 2 UPDATE container
								trtcontainer.addEntity(beanItem.getBean());
								System.out.println("Entity is now: " + trtcontainer.getItem(currTrt.getId()).getEntity().getAction());
	
								// 3 UPDATE tree title
//			              	  	tree.setItemCaption(currTrt.getId(), trtcontainer.getItem(currTrt.getId()).getEntity().getAction());
			              	  	
			              	  		              	  		
			              	  	id = currTrt.getId();
	
							}
		              	  	
//			            	System.out.println("WHAT IS NEW LIST OF SESSIONS: " + parentCase.getSessions()); // testing purposes
//			            	for (TestSession s : parentCase.getSessions()) {
//				            	System.out.println(s.getId() + " - " + s.getTitle()); // testing purposes	            		
//			            	}
			            	
			            	
			            	if ( editmode==false ) {
			            		// 4 UPDATE table title
//			            		table.select(beanItem.getBean().getId());	//(testsession.getId());
			            		confirmNotification(currTrt.getAction(), "was created");
			            		close();
	
	//		            		getUI().getNavigator()
	//		            			.navigateTo(MainView.NAME + "/" + 
	//		            				parentCase.getTitle());		//sessions.getItem(id).getEntity()		            		
			            	
			            	} else if ( editmode==true ) {
			            		confirmNotification(currTrt.getAction(), "was edited");
//			            		getUI().getNavigator()
//		            			.navigateTo(MainView.NAME + "/" + 
//		            				parentparams.getTitle() + "/" + beanItem.getBean().getTitle() + "id=" + beanItem.getBean().getId());		//testsession.getTitle() sessions.getItem(id).getEntity()		            		
			            	} 
			            	
		            	
			            // title already existed	
						} else {
							System.out.println("title was NOT fine.");
//							testsession = sessions.getItem(id).getEntity();
//							System.out.println("db session is: " + testsession.getId() + " " + testsession.getTitle());

							if (editmode==false) {
								binder.discard();
								Notification.show("The Action '" + wrongTitle + "' already exists. Please rename.", Type.ERROR_MESSAGE);	//testsession.getTitle()
								UI.getCurrent().addWindow(new TRTEditor(parentparams, grid));								
							} else if (editmode==true){
								binder.discard();
								Notification.show("The Action '" + wrongTitle + "' already exists. Please rename.", Type.ERROR_MESSAGE);
								UI.getCurrent().addWindow(new TRTEditor(id, parentparams, grid));																										
							} 
								
						}
		            
					} catch (CommitException e) {
						binder.discard();
						Notification.show("All fields are recquired! Please try again.", Type.WARNING_MESSAGE);
						UI.getCurrent().addWindow(new TRTEditor(parentparams, grid));
					} catch (NonUniqueResultException e) {
						e.printStackTrace();
						binder.discard();
						Notification.show("The Action '" + currTrt.getAction() + "' already exists. Please rename.", Type.ERROR_MESSAGE);
						UI.getCurrent().addWindow(new TRTEditor(parentparams, grid));
					}
//					catch (NonUniqueResultException e) {
//						binder.discard();
//						Notification.show("'Title' must be a unique name.\n'" +
//											queriedSession.getTitle() + 
//											"' already exists.\n\nPlease try again.", Type.WARNING_MESSAGE);
//						UI.getCurrent().addWindow(new TestSessionEditor(tree, parentCase));
//					}
	            
	        } else if (event.getButton() == cancelButton) {
	        	binder.discard();
	        }
//	        binder.clear();
//	        form.disableValidationMessages();
//	        setTreeItemsExpanded();

			close(); // Close the sub-window
	    }

	   
	   
	private void filterContainerBySUT(TestCase sut) {
    	trtcontainer.removeAllContainerFilters();
//    	Equal ownerfilter = new Equal("parentcase", getTestCaseByTitle());//  ("parentcase", getTestCaseByTitle(), true, false);
    	Equal paramsfilter = new Equal("parentparameter", parentparams);//  ("parentcase", getTestCaseByTitle(), true, false);
    	
    	trtcontainer.addContainerFilter(paramsfilter);
	}

	
	private void confirmNotification(String deletedItem, String message) {
        // welcome notification
        Notification notification = new Notification(deletedItem, Type.TRAY_NOTIFICATION);
        notification
                .setDescription(message);
        notification.setHtmlContentAllowed(true);
        notification.setStyleName("dark small");	//tray  closable login-help
        notification.setPosition(Position.BOTTOM_RIGHT);
        notification.setDelayMsec(500);
        notification.show(Page.getCurrent());
	}
	
	

}
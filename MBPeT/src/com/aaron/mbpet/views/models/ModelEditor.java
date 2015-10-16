/**
 * Window for Create/Edit Model title ... called from SUT page panel->table
 */
package com.aaron.mbpet.views.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NonUniqueResultException;
import javax.persistence.Persistence;
import javax.persistence.Query;

import com.aaron.mbpet.MbpetUI;
import com.aaron.mbpet.domain.Model;
import com.aaron.mbpet.domain.TestCase;
import com.aaron.mbpet.domain.TestSession;
import com.aaron.mbpet.domain.User;
import com.aaron.mbpet.views.LoginView;
import com.aaron.mbpet.views.MBPeTMenu;
import com.aaron.mbpet.views.MainView;
import com.aaron.mbpet.views.cases.TestCaseForm;
import com.aaron.mbpet.views.sessions.TestSessionEditor;
import com.aaron.mbpet.views.users.UserForm;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.jpacontainer.JPAContainerFactory;
import com.vaadin.data.Item;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.data.util.filter.SimpleStringFilter;
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
import com.vaadin.shared.ui.combobox.FilteringMode;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.AbstractTextField;
import com.vaadin.ui.ComboBox;
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
public class ModelEditor extends Window implements Button.ClickListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5370960944210111329L;

	private Table table;
	private Button createButton;
	private Button cancelButton;
	Field titlefield;
	private TextField title; 
	private ComboBox sessionCombobox;
	
//	private JPAContainer<TestCase> testcases;
	private JPAContainer<Model> models;
	private JPAContainer<TestSession> sessions;
	BeanItem<Model> modelBeanItem;
	Model currmodel;
	
	private ModelForm form;
	FieldGroup binder;
	TestCase parentcase;
	TestSession parentsession;
	Model clone;
	
	boolean editmode = false;
	boolean navToCasePage = false;
	boolean clonemode = false;

	private IndexedContainer ic;


	/*
	 * Create new Model
	 */
	public ModelEditor(TestCase parentcase, boolean navToCasePage) {		//JPAContainer<TestCase> container
//      super("Create a new Test Case"); // Set window caption
		this.models = MBPeTMenu.models;
		currmodel = new Model(); 
		this.modelBeanItem = new BeanItem<Model>(currmodel);
		this.parentsession = new TestSession();
		
		this.navToCasePage = navToCasePage;
		
		init(parentsession, parentcase);
	}
	public ModelEditor(TestSession parentsession, TestCase parentcase) {		//JPAContainer<TestCase> container
//      super("Create a new Test Case"); // Set window caption
		this.models = MBPeTMenu.models;
		currmodel = new Model(); 
		this.modelBeanItem = new BeanItem<Model>(currmodel);
		
		init(parentsession, parentcase);
	}
	
	/*
	 * Edit Mode
	 */
	public ModelEditor(Object modelid, TestSession parentsession, TestCase parentcase) {		//JPAContainer<TestCase> container
		this.editmode = true;
		this.navToCasePage = false;
        
		this.models = MBPeTMenu.models;
        this.currmodel = models.getItem(modelid).getEntity();
        this.modelBeanItem = new BeanItem<Model>(currmodel);

        init(parentsession, parentcase);
	}
	
	/*
	 * Edit from TestCase Home Page
	 */
	public ModelEditor(Object modelid, TestSession parentsession, TestCase parentcase, Table table) {		//JPAContainer<TestCase> container
		this.editmode = true;
		this.navToCasePage = true;
		this.table = table;
		
		this.models = MBPeTMenu.models;
		this.currmodel = models.getItem(modelid).getEntity();
		this.modelBeanItem = new BeanItem<Model>(currmodel);
		
		init(parentsession, parentcase);
	}
	
	/*
	 * Clone existing Model to new one
	 */
	public ModelEditor(Object modelId, TestSession parentsession, TestCase parentcase,
			Table table, boolean clonemode) {		//JPAContainer<TestCase> container
		this.clonemode = clonemode;
		this.table = table;
		
		this.models = MBPeTMenu.models;
		this.currmodel = models.getItem(modelId).getEntity();
		
		this.clone = new Model();
		clone.setTitle("(clone) " + currmodel.getTitle());
		clone.setDotschema(currmodel.getDotschema());
		clone.setParentsession(currmodel.getParentsession());
		clone.setParentsut(currmodel.getParentsut());

		this.modelBeanItem = new BeanItem<Model>(clone);
		
//		testsession = new TestSession(); 
//		this.newSessionItem = new BeanItem<TestSession>(testsession);
//		sessions = MBPeTMenu.sessions;	//JPAContainerFactory.make(TestSession.class, MbpetUI.PERSISTENCE_UNIT);	//container;
		
		init(parentsession, parentcase);
	}
	
	private void init(TestSession parentsession, TestCase parentcase) {
		center();
		setResizable(false);
		setClosable(true);
		setModal(true);

		this.parentcase = parentcase;
		this.parentsession = parentsession;
//		this.models = MBPeTMenu.models;	//JPAContainerFactory.make(TestSession.class, MbpetUI.PERSISTENCE_UNIT);	//container;
		this.sessions = MBPeTMenu.sessions;
		
        setSizeUndefined();
        setContent(AutoGeneratedLayoutDesign()); //ManualLayoutDesign
        setCaption(buildCaption());
	}
	
	
    /**
     * @return the caption of the editor window
     */
    private String buildCaption() {
    	if (clonemode==true) {
    		return String.format("Clone Model: %s", 
    				currmodel.getTitle());
    	} else if (editmode==true) {
    		return String.format("Edit Model: %s", 
    				currmodel.getTitle());
    	} 
//    	else if (!(parentsession == null) ) {		//.getItemProperty("firstname").getValue()
//    		return String.format("Add Model to: %s", 
//    				parentsession.getTitle());		//testcases.getItem(parentCase.getId()).getItemProperty("title")
//    	} 
    	else {
    		return "Create a new Model";
    	}
    }
    
	
    
	private Component AutoGeneratedLayoutDesign() {
		final VerticalLayout layout = new VerticalLayout();
		layout.setSpacing(true);
		layout.setMargin(true);
		
		
//		layout.addComponent(new Label("<h4>Add Test Session</h4>", ContentMode.HTML));
	
		// set parent Test Case and Session manually without a field
		if (editmode == false && (clonemode==false)) {
//			model.setParentsession(parentsession);	
			currmodel.setParentsut(parentcase);
		}
		
		// create fields manually
//		form = new TestSessionForm();
//		layout.addComponent(form);
		
		binder = new FieldGroup();
//		BeanItem<Person> item = new BeanItem<Person>(person);		// takes item as argument
//		item.addNestedProperty("address.street");	// Address info is not person but address to which person is linked
//		item.addNestedProperty("address.zip");
//		item.addNestedProperty("address.city");
		
		binder.setItemDataSource(modelBeanItem); 	// link to data model to binder
		
//		binder.bindMemberFields(form);	// link to layout
		
		// using bind() to determine what type of field is created yourself...
		title = new TextField();
		binder.bind(title, "title");
		title.setWidth(22, Unit.EM);
		title.setCaption("Title");
		title.focus();
		title.setImmediate(true);
		title.addValidator(new BeanValidator(Model.class, "title"));
//		title.setValidationVisible(false);
		title.setNullRepresentation("");
		
		sessionCombobox = new ComboBox("Select parent Session");
//		binder.bind(sessionCombobox, "parentsession");
		sessionCombobox.setWidth(22, Unit.EM);
		sessionCombobox.setNullSelectionAllowed(false);
//		sessionCombobox.setFilteringMode(FilteringMode.CONTAINS);
		sessionCombobox.setImmediate(true);
//		sessionCombobox.addValidator(new BeanValidator(Model.class, "parentsession"));
//		title.setValidationVisible(false);
//		sessionCombobox.setNullRepresentation("");
//		sessionCombobox.setContainerDataSource(sessions);
		updateFilters();

		ic = new IndexedContainer();
		ic.addContainerProperty("id", Integer.class, "");
		ic.addContainerProperty("title", String.class, "");
		
		// set null option
//		String nullitem = "-- none --";
//		sessionCombobox.addItem(nullitem);
//		sessionCombobox.setNullSelectionItemId(nullitem);
		
		for (Object id : sessions.getItemIds()) {
			System.out.println("Session-Parent: " + sessions.getItem(id).getEntity().getTitle() +
					sessions.getItem(id).getEntity().getParentcase());
			
			Object itemId = ic.addItem();
			Item item = ic.getItem(itemId);
			item.getItemProperty("id").setValue(sessions.getItem(id).getEntity().getId());
			item.getItemProperty("title").setValue(sessions.getItem(id).getEntity().getTitle());
			
		}
		sessionCombobox.setContainerDataSource(ic);
		sessionCombobox.setItemCaptionPropertyId("title");
		
		if (editmode==true || clonemode==true) {
			for ( Object itemId : ic.getItemIds()) {
//				System.out.println("\nIC ITEM: " + ic.getItem(itemId).getItemProperty("id").getValue().toString() +" "+ ic.getItem(itemId).getItemProperty("title").getValue().toString());
//				System.out.println("\nPrentsession ITEM: " + parentsession.getTitle());
				if ( ic.getItem(itemId).getItemProperty("title").getValue().toString().equals(parentsession.getTitle()) ) {
					System.out.println("\n\nSELECTED PARENTSESSION IC ITEM: " + ic.getItem(itemId).getItemProperty("title").getValue().toString());
					sessionCombobox.select(itemId);// getIdByIndex(ic.size()-1));
//					Object capid = sessionCombobox.getValue();// ic.getItem(binder.getField("parentsession").getValue());
					Item item = ic.getItem(itemId);
					currmodel.setParentsession(sessions.getItem(item.getItemProperty("id").getValue()).getEntity());			
					
				}
			}

		} else {
			sessionCombobox.select(ic.getIdByIndex(ic.size()-1));
			Object capid = sessionCombobox.getValue();// ic.getItem(binder.getField("parentsession").getValue());
			Item item = ic.getItem(capid);
			currmodel.setParentsession(sessions.getItem(item.getItemProperty("id").getValue()).getEntity());			
		}
		
		sessionCombobox.addValueChangeListener(new ValueChangeListener() {
			@Override
			public void valueChange(ValueChangeEvent event) {
				// TODO Auto-generated method stub
//				Notification.show("selected: " + event.getProperty().getValue().toString());
//				System.out.println("binder field value: " + binder.getField("parentsession").getValue().toString());
				System.out.println("combobox value: " + sessionCombobox.getValue().toString());

//				Item item = ic.getItem(binder.getField("parentsession").getValue());
				Object capid = sessionCombobox.getValue();// ic.getItem(binder.getField("parentsession").getValue());
				Item item = ic.getItem(capid);
				System.out.println("sessionCombobox prop id(session): " + item.getItemProperty("id").getValue().toString());
				parentsession = sessions.getItem(item.getItemProperty("id").getValue()).getEntity();
				System.out.println(parentsession.getTitle());	// setValue(parentsession);
				currmodel.setParentsession(parentsession);	
			}
		});
		
		
//		for (Object id : sessions.getItemIds()) {
//			System.out.println("Session-Parent: " + sessions.getItem(id).getEntity().getTitle() +
//					sessions.getItem(id).getEntity().getParentcase());
//			
//			Object itemid = sessionCombobox.addItem();
//			sessionCombobox.setItemCaption(itemid, sessions.getItem(id).getEntity().getTitle());
//		}
		
//		sessionCombobox.setItemCaptionPropertyId("title");
//		sessionCombobox.select(parentsession);
		
		layout.addComponent(title);
		layout.addComponent(sessionCombobox);
		
		binder.setBuffered(true);
		
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
	        	
	        	Model queriedModel = null;
	        	
					try {
//						form.enableValidationMessages();
	//					title.setValidationVisible(true);
						
						// add NEW bean object to db through jpa container
						if (editmode == false && clonemode == false) {
							// commit the fieldgroup
							binder.commit();
							
							// add to container
							models.addEntity(modelBeanItem.getBean());	//jpa container	
							
			                // add created item to tree (after retrieving db generated id)
			                EntityManager em = Persistence.createEntityManagerFactory("mbpet")
			    											.createEntityManager();	
				            Query query = em.createQuery(
				        		    "SELECT OBJECT(t) FROM Model t WHERE t.title = :title"
				        		);
		//		            query.setParameter("title", newsession.getTitle());
				            queriedModel = (Model) query.setParameter("title", currmodel.getTitle()).getSingleResult();
				            System.out.println("the generated id is: " + queriedModel.getId());
				            Object id = queriedModel.getId();	// here is the id we need for tree
				            		        			
//				            // add to tree in right order
//				            if ( tree.hasChildren(parentsession.getId()) ) {
//				            	sortAddToTree(id);				            	
//				            } else {
//				            	tree.addItem(id);
//				            	tree.setParent(id, parentsession.getId());
//				            	tree.setChildrenAllowed(id, false);
//				            	tree.setItemCaption(id, models.getItem(id).getEntity().getTitle());		//newsession.getTitle()
//				            	tree.expandItem(parentsession.getId());
//				            	tree.select(id);				            	
//				            }
		        			
		              	  			              	  	
		              	  	// update parent Case to add Session to testCase List<Session> sessions
		              	  	parentsession.addModel(queriedModel);
		              	  	parentcase.addModel(queriedModel);
		//              	  	List<TestSession> listofsessions = parentCase.getSessions();
		//              	  	listofsessions.add(queriedSession);		//sessions.getItem(id).getEntity()
		//              	  	parentCase.setSessions(listofsessions);
		              	  	
			            	System.out.println("WHAT IS NEW LIST OF MODELS: " + parentsession.getModels()); // testing purposes
			            	for (Model m : parentsession.getModels()) {
				            	System.out.println(m.getId() + " - " + m.getTitle()); // testing purposes	            		
			            	}
			            	// nav to created test case
//			    			getUI().getNavigator()
//			         			.navigateTo(MainView.NAME + "/" + 
//		     							parentCase.getTitle() + "/" + queriedSession.getTitle());
			            	
						} else if (editmode == true){
							// EDIT existing object
							
							// commit the fieldgroup
							binder.commit();
							
		              	  	//1 UPDATE parent Case and Session reference
//							parentCase.removeSession(sessions.getItem(testsession.getId()).getEntity());
//							parentCase.addSession(sessions.getItem(testsession.getId()).getEntity());
							parentsession.updateModelData(models.getItem(currmodel.getId()).getEntity());
							parentcase.updateModelData(models.getItem(currmodel.getId()).getEntity());
							
							System.out.println("Test session is now: " + currmodel.getTitle());
//							System.out.println("Entity is now: " + sessions.getItem(testsession.getId()).getEntity().getTitle());

							// 2 UPDATE container
							models.addEntity(modelBeanItem.getBean());
							System.out.println("Entity is now: " + models.getItem(currmodel.getId()).getEntity().getTitle());

							// 3 UPDATE tree title
//		              	  	tree.setItemCaption(model.getId(), models.getItem(model.getId()).getEntity().getTitle());

						} else if (clonemode == true){
							// CLONE 
				           System.out.println("\n\nWE ARE IN CLONE MODE!!!!!!!!!\n\n");

							// 1 commit the fieldgroup
							binder.commit();
							
							// 2 add to container
							models.addEntity(modelBeanItem.getBean());	//jpa container	
							
			                // 3 retrieve generated id from db
			                EntityManager em = Persistence.createEntityManagerFactory("mbpet")
			    											.createEntityManager();	
				            Query query = em.createQuery(
				        		    "SELECT OBJECT(t) FROM Model t WHERE t.title = :title");
				            queriedModel = (Model) query.setParameter("title", clone.getTitle()).getSingleResult();
				            System.out.println("the generated id is: " + queriedModel.getId());
				            Object id = queriedModel.getId();	// here is the id we need for tree				            
		        			
				            // 4 update parent Case to add Session to testCase List<Session> sessions
		              	  	parentsession.addModel(queriedModel);
		              	  	sessions.addEntity(parentsession);
		              	  	parentcase.addModel(queriedModel);
		              	  	
			            	System.out.println("WHAT IS NEW LIST OF SESSIONS: " + parentsession.getModels()); // testing purposes
			            	for (Model m : parentsession.getModels()) {
				            	System.out.println(m.getId() + " - " + m.getTitle()); // testing purposes	            		
			            	}
						}
		                

	              	  	
		            	System.out.println("WHAT IS NEW LIST OF SESSIONS: " + parentsession.getModels()); // testing purposes
		            	for (Model m : parentsession.getModels()) {
			            	System.out.println(m.getId() + " - " + m.getTitle()); // testing purposes	            		
		            	}
		            	
	            		if ( clonemode==true ) {
		            		confirmNotification(queriedModel.getTitle(), "was created");
		            		close();	            			
	            			
	            		} else if ( navToCasePage==true && editmode==false ) {
		            		// UPDATE table title
		            		table.select(currmodel.getId());
		            		confirmNotification(currmodel.getTitle(), "was created");
		            		close();
		            	
		            	} else if ( navToCasePage==true && editmode==true ) {
		            		confirmNotification(currmodel.getTitle(), "was edited");
		            		close();
		            		
		            	} else {
		            		getUI().getNavigator()
		            			.navigateTo(MainView.NAME + "/" + 
		            				parentsession.getTitle() + "/" + currmodel.getTitle());		//sessions.getItem(id).getEntity()		            		
		            	}
		            
					} catch (CommitException e) {
						binder.discard();
						Notification not = new Notification("'Title' cannot be empty.", Type.ERROR_MESSAGE);
						not.setStyleName("failure small");
						not.show(Page.getCurrent());
						UI.getCurrent().addWindow(new ModelEditor(parentsession, parentcase));
					} catch (NonUniqueResultException e) {
						binder.discard();
						Notification not = new Notification("'Title' must be a unique name.\n'",
											modelBeanItem.getBean().getTitle() +	//queriedModel.getTitle() + 
											"' already exists.\n\nPlease try again.", Type.WARNING_MESSAGE);
						not.setStyleName("failure small");
						not.show(Page.getCurrent());
						UI.getCurrent().addWindow(new ModelEditor(parentsession, parentcase));
					}
					catch (NullPointerException e) {
						binder.discard();
//						Notification.show("'Parent Session' cannot be empty " + e.getMessage().toString(), Type.ERROR_MESSAGE);
//						UI.getCurrent().addWindow(new ModelEditor(parentsession, parentcase));
					}
	            
	        } else if (event.getButton() == cancelButton) {
	        	binder.discard();
	        }
//	        binder.clear();
//	        form.disableValidationMessages();
//	        setTreeItemsExpanded();

			close(); // Close the sub-window
	    }
		
		
	    private void updateFilters() {
	    	sessions.removeAllContainerFilters();
	    	
	    	Equal casefilter = new Equal("parentcase", parentcase);//  ("parentcase", getTestCaseByTitle(), true, false);	    	
//	    	SimpleStringFilter filter = new SimpleStringFilter("title", modelsFilter, true, false);
	    	sessions.addContainerFilter(casefilter);
//	    	sessions.addContainerFilter(filter);
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
		
//	    public void setTreeItemsExpanded() {
//	        // Expand whole tree
//	    	System.out.println(tree.getItemIds());
//	    	System.out.println(tree.rootItemIds());
//		    for (final Object id : tree.rootItemIds()) {
//		    	tree.expandItemsRecursively(id);
//		    	tree.setChildrenAllowed(id, true);
//	        } 
//	    }
	    

}
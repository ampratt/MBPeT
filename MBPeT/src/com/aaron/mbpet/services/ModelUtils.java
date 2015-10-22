package com.aaron.mbpet.services;

import javax.persistence.EntityManager;
import javax.persistence.NonUniqueResultException;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.validation.ConstraintViolationException;

import com.aaron.mbpet.domain.Model;
import com.aaron.mbpet.domain.TestCase;
import com.aaron.mbpet.domain.TestSession;
import com.aaron.mbpet.views.MBPeTMenu;
import com.aaron.mbpet.views.MainView;
import com.aaron.mbpet.views.models.ModelForm;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.data.Item;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.data.util.filter.Compare.Equal;
import com.vaadin.data.validator.BeanValidator;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.server.Page;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.shared.Position;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.Field;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.themes.ValoTheme;

public class ModelUtils {

//	private JPAContainer<TestCase> testcases;
	private static JPAContainer<Model> models;
	private static JPAContainer<TestSession> sessions;
	static BeanItem<Model> modelBeanItem;
	static Model currmodel;
	
	private ModelForm form;
	static FieldGroup binder;
	static TestCase parentcase;
	static TestSession parentsession;
	Model clone;
	
	static boolean editmode = false;
	boolean navToCasePage = false;
	boolean clonemode = false;

	private IndexedContainer ic;


	/*
	 * Create new Model
	 */
	public ModelUtils(TestCase parentcase) {
		this.models = MBPeTMenu.models;
		currmodel = new Model(); 
		this.modelBeanItem = new BeanItem<Model>(currmodel);
		this.parentsession = new TestSession();
		
		this.navToCasePage = navToCasePage;
		
		init(parentsession, parentcase);
	}
	
	public ModelUtils(TestSession parentsession, TestCase parentcase) {		//JPAContainer<TestCase> container
//      super("Create a new Test Case"); // Set window caption
		this.models = MBPeTMenu.models;
		currmodel = new Model(); 
		this.modelBeanItem = new BeanItem<Model>(currmodel);
		
		init(parentsession, parentcase);
	}
	
	/*
	 * Edit Mode
	 */
	public ModelUtils(Object modelid, TestSession parentsession, TestCase parentcase) {		//JPAContainer<TestCase> container
		this.editmode = true;
		this.navToCasePage = false;
        
		this.models = MBPeTMenu.models;
        this.currmodel = models.getItem(modelid).getEntity();
        this.modelBeanItem = new BeanItem<Model>(currmodel);

        init(parentsession, parentcase);
	}
	
	/*
	 * Clone existing Model to new one
	 */
	public ModelUtils(Object modelId, TestSession parentsession, TestCase parentcase,
			Table table, boolean clonemode) {		//JPAContainer<TestCase> container
		this.clonemode = clonemode;
		
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
		this.parentcase = parentcase;
		this.parentsession = parentsession;
//		this.models = MBPeTMenu.models;	//JPAContainerFactory.make(TestSession.class, MbpetUI.PERSISTENCE_UNIT);	//container;
		this.sessions = MBPeTMenu.sessions;
	}
    
	
	
	public static Model createNewModel(Model model, TestSession currsession, FieldGroup fieldbinder) {
		models = MBPeTMenu.models;
		currmodel = model; 
		modelBeanItem = new BeanItem<Model>(currmodel);
		
//        sessions = MBPeTMenu.sessions;
		parentsession = currsession;
		parentcase = parentsession.getParentcase();
		
		currmodel.setParentsession(parentsession);
		currmodel.setParentsut(parentcase);

		binder = fieldbinder;
		
    	Model queriedModel = null;
    	
			try {
				// CREATE new Model
				
				// 1. commit the fieldgroup
				binder.commit();
				
				// 2. ADD to container
				models.addEntity(modelBeanItem.getBean());	//jpa container	

          	  	// 3. update parent Case to add Session to testCase List<Session> sessions
                EntityManager em = Persistence.createEntityManagerFactory("mbpet")
    											.createEntityManager();	
	            Query query = em.createQuery(
	        		    "SELECT OBJECT(t) FROM Model t WHERE t.title = :title"
	        		);
//		            query.setParameter("title", newsession.getTitle());
	            queriedModel = (Model) query.setParameter("title", currmodel.getTitle()).getSingleResult();
	            System.out.println("the generated id is: " + queriedModel.getId());
	              	  	
          	  	// 3. update parent Case to add Session to testCase List<Session> sessions
          	  	parentsession.addModel(queriedModel);
          	  	parentcase.addModel(queriedModel);
       	
//          	  	// 2. UPDATE parent Case and Session reference
//				parentsession.updateModelData(models.getItem(currmodel.getId()).getEntity());
//				parentcase.updateModelData(models.getItem(currmodel.getId()).getEntity());
////				System.out.println("Entity is now: " + sessions.getItem(testsession.getId()).getEntity().getTitle());


            	System.out.println("WHAT IS NEW LIST OF MODELS: " + parentsession.getModels()); // testing purposes
            	for (Model m : parentsession.getModels()) {
	            	System.out.println(m.getId() + " - " + m.getTitle()); // testing purposes	            		
            	}
							
            	confirmNotification("Model '" + queriedModel.getTitle() + "'", "saved");
        	
			} catch (ConstraintViolationException e) {
				binder.discard();
				Notification.show("'Title' cannot be empty and must be between 1 and 40 characters long.", Type.WARNING_MESSAGE);
				System.out.println(e.getMessage().toString());
			} 
			catch (CommitException e) {
				binder.discard();
				Notification.show("'Title' cannot be empty " + e.getMessage().toString() , Type.WARNING_MESSAGE);
			} catch (NonUniqueResultException e) {
				binder.discard();
				Notification.show("'Title' must be a unique name.\n'" +
									queriedModel.getTitle() + 
									"' already exists.\n\nPlease try again.", Type.WARNING_MESSAGE);
			}
			catch (NullPointerException e) {
				binder.discard();
//						Notification.show("'Parent Session' cannot be empty " + e.getMessage().toString(), Type.ERROR_MESSAGE);
//						UI.getCurrent().addWindow(new ModelEditor(parentsession, parentcase));
			}
			
//	        binder.clear();
			
			return queriedModel;
	}
	
	public static Model editModel(Model model, TestSession currsession, FieldGroup fieldbinder) {
        
		models = MBPeTMenu.models;
        currmodel = models.getItem(model.getId()).getEntity();
        modelBeanItem = new BeanItem<Model>(currmodel);
        
//        sessions = MBPeTMenu.sessions;
        parentsession = currsession;
		parentcase = parentsession.getParentcase();

		binder = fieldbinder;
    	
		Model editedmodel = null;
			try {
				// EDIT existing Model
				
				// 1. commit the fieldgroup
				binder.commit();
				
				editedmodel = models.getItem(currmodel.getId()).getEntity();
				
          	  	// 2. UPDATE parent Case and Session reference
				parentsession.updateModelData(editedmodel);	//(models.getItem(currmodel.getId()).getEntity());
				parentcase.updateModelData(editedmodel); //(models.getItem(currmodel.getId()).getEntity());
//				System.out.println("Entity is now: " + sessions.getItem(testsession.getId()).getEntity().getTitle());

				// 3. UPDATE container
				models.addEntity(modelBeanItem.getBean());
				System.out.println("Entity is now: " + editedmodel.getTitle());

            	confirmNotification("Model '" + editedmodel.getTitle() + "'", "saved");

			} catch (ConstraintViolationException e) {
				binder.discard();
				Notification.show("'Title' cannot be empty and must be between 1 and 40 characters long.", Type.WARNING_MESSAGE);
			} catch (CommitException e) {
				binder.discard();
				Notification.show("'Title' cannot be empty ", Type.WARNING_MESSAGE);
			} catch (NonUniqueResultException e) {
				binder.discard();
				Notification.show("'Title' must be a unique name.\n'" +
								currmodel.getTitle() + 
									"' already exists.\n\nPlease try again.", Type.WARNING_MESSAGE);
			}
			catch (NullPointerException e) {
				binder.discard();
//						Notification.show("'Parent Session' cannot be empty " + e.getMessage().toString(), Type.ERROR_MESSAGE);
//						UI.getCurrent().addWindow(new ModelEditor(parentsession, parentcase));
			}
			
//	        binder.clear();
			
			return editedmodel;

	    }
		
		
	
		private static void confirmNotification(String deletedItem, String message) {
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

		public static String compareTitles(String titleFieldvalue, String editorvalue) {

//			String word = cardArray.get(card);
			String str = "";
			boolean match = false;
			try {
				String[] lines = editorvalue.split("\n"); 
				System.out.println("firstline is: " + lines);
				String newString = "";
				if (lines[0].contains(titleFieldvalue)){
				    match = true;
//				    return editorvalue;
				    str = editorvalue;

				} 
//				else {
//					// replace first line
//					String[] words = lines[0].split(" ");
//					words[1] = titleFieldvalue;
//					for (int i = 0; i < words.length; i++)
//						newString += words[i] + " ";
//					
//					System.out.println(newString);
//					
//					// replace entire file
//					lines[0] = newString;
//					newString = "";
//					for (int i=0; i<lines.length; i++) {
//						newString += lines[i] + "\n";	
//					}
////					return newString;
//					str = newString;
//					
//				}
			} catch (ArrayIndexOutOfBoundsException e) {
				Notification not = new Notification("Heads up!", "Check that your file matches proper .dot format syntax.");
				not.setStyleName("failure");
				not.setPosition(Position.MIDDLE_RIGHT);
				not.show(Page.getCurrent());
			}
			
		if (match == false) {
			Notification not = new Notification("Heads up!", "the 'Title field' and title in the '.dot file' do not match");
			not.setStyleName("failure");
			not.setPosition(Position.MIDDLE_RIGHT);
			not.show(Page.getCurrent());
		}
			return str;
			
//			for (String word : editorvalue.split("\n")) {	// " "  \\s+
//				System.out.println("this line is: " + word);
//			    if (word.contains(titleFieldvalue)){
//			        match = true;
//			        return;
//			    }
//			}
			
//			if (match == false) {
//				Notification not = new Notification("Heads up!", "the 'Title field' and title in the '.dot file' do not match");
//				not.setStyleName("failure");
//				not.setPosition(Position.MIDDLE_RIGHT);
//				not.show(Page.getCurrent());
//			}

		}
	    

}
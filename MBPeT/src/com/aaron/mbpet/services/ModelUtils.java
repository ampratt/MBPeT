package com.aaron.mbpet.services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NonUniqueResultException;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.validation.ConstraintViolationException;

import com.aaron.mbpet.MbpetUI;
import com.aaron.mbpet.domain.Model;
import com.aaron.mbpet.domain.TestCase;
import com.aaron.mbpet.domain.TestSession;
import com.aaron.mbpet.views.MainView;
import com.aaron.mbpet.views.models.ModelEditor;
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

	private JPAContainer<Model> models;
	private JPAContainer<TestSession> sessions;
	private JPAContainer<TestCase> testcases = ((MbpetUI) UI.getCurrent()).getTestcases();
	BeanItem<Model> modelBeanItem;
	Model currmodel;
	TestCase parentcase;
	TestSession parentsession;
	
	FieldGroup binder;	
	private IndexedContainer ic;

	boolean editmode = false;
	private String prevTitle = "";
	private String wrongTitle = "";
	boolean navToCasePage = false;
	boolean clonemode = false;

	FileSystemUtils fileUtils = new FileSystemUtils();
 
		
	public Model createNewModel(Model model, TestSession currsession, FieldGroup fieldbinder) {	//Model model, 
		models = ((MbpetUI) UI.getCurrent()).getModels();
		currmodel = model; //new Model(); 
		modelBeanItem = new BeanItem<Model>(currmodel);
		
//        sessions = MBPeTMenu.sessions;
		parentsession = currsession;
		parentcase = testcases.getItem(parentsession.getParentcase().getId()).getEntity(); 	//parentsession.getParentcase();

		currmodel.setParentsession(parentsession);
		currmodel.setParentsut(parentcase);

		binder = fieldbinder;
		
    	Model queriedModel = null;
    	
			try {
				// CREATE new Model
				
				// 1. commit the fieldgroup
				binder.commit();

		    	System.out.println("WHAT IS NEW LIST OF SESSIONS: " + currmodel.getParentsession().getModels()); // testing purposes
		    	for (Model m : currmodel.getParentsession().getModels()) {
		        	System.out.println(m.getId() + " - " + m.getTitle()); // testing purposes	            		
		    	}
		    	
				// check Model title doesnt exist for THIS SESSION
				int id =0;
				boolean titleOK = true;
				id = modelBeanItem.getBean().getId();	//testsession.getId();
				System.out.println("desired session and its Models : " + currmodel.getParentsession() + " - " + currmodel.getParentsession().getModels());	//sessions.getItem(currmodel.getParentsession().getId()).getEntity().getModels());
				

				// compare title against those in desired target session
				for (Model m : parentsession.getModels()) {	//currmodel.getParentsession()  sessions.getItemIds()
					
					System.out.println("Existing title -> new title : " + m.getTitle() + "->" + currmodel.getTitle());
					System.out.println("Existing id -> new id : " + m.getId() + "->" + id);
					System.out.println("Existing p.session -> attempted new p.session : " + m.getParentsession().getTitle() + "->" + currmodel.getParentsession().getTitle());
					
					if ( m.getTitle().equals(currmodel.getTitle().trim()) 
							&& !(m.getId()==id)) {					//&& !(m.getId()==id)
						currmodel.setTitle("");
						currmodel.setDotschema("");
						wrongTitle = m.getTitle();
						titleOK = false;
						break;
					}
					
				}

				
            	if (titleOK == true) {
					System.out.println("title was OK.");

					checkTitles();
					
//          	  	// 2. UPDATE parent Case and Session reference
//				parentsession.updateModelData(models.getItem(currmodel.getId()).getEntity());
//				parentcase.updateModelData(models.getItem(currmodel.getId()).getEntity());
////				System.out.println("Entity is now: " + sessions.getItem(testsession.getId()).getEntity().getTitle());

            		// 2. ADD to container
					models.addEntity(modelBeanItem.getBean()); //jpa container	
					
					// 3. update parent Case to add Session to testCase List<Session> sessions
					EntityManager em = Persistence.createEntityManagerFactory("mbpet").createEntityManager();
					Query query = em.createQuery(
							"SELECT OBJECT(m) FROM Model m WHERE m.title = :title AND m.parentsession = :parentsession");
//	            	queriedModel = (Model) query.setParameter("title", currmodel.getTitle()).getSingleResult();
					query.setParameter("title", currmodel.getTitle());
					query.setParameter("parentsession",currmodel.getParentsession());
					queriedModel = (Model) query.getSingleResult();
					System.out.println("the generated id is: "+ queriedModel.getId());
					
					// 3. update parent Case to add Session to testCase List<Session> sessions
					parentsession.addModel(queriedModel);
					parentcase.addModel(queriedModel);
					System.out.println("WHAT IS NEW LIST OF MODELS: "+ parentsession.getModels()); // testing purposes
					
	            	System.out.println("WHAT IS NEW LIST OF MODELS: " + parentsession.getModels()); // testing purposes
	            	for (Model m : parentsession.getModels()) {
		            	System.out.println(m.getId() + " - " + m.getTitle()); // testing purposes	            		
	            	}
	            	
					// 4. write model file to disk
					fileUtils.writeModelToDisk(	//username, sut, session, settings_file)
							parentcase.getOwner().getUsername(),
							parentcase.getTitle(), 
							parentsession.getTitle(),
							parentsession.getParameters().getModels_folder(),
							queriedModel);
					
					confirmNotification("Model '" + queriedModel.getTitle()+ "'", "saved");
					
				} else {
					System.out.println("title was NOT fine.");
					binder.discard();
					Notification.show("The title '" + wrongTitle + "' already exists for this Session. Please rename this model.", Type.ERROR_MESSAGE);	//testsession.getTitle()
//					UI.getCurrent().addWindow(new ModelEditor(parentsession, parentcase));
//					createNewModel(currmodel, parentsession, binder);
				}
        	
			} 
//			catch (ConstraintViolationException e) {
//				binder.discard();
//				Notification.show("'Title' cannot be empty and must be between 1 and 40 characters long.", Type.WARNING_MESSAGE);
//				System.out.println(e.getMessage().toString());
//			} 
			catch (CommitException e) {
				binder.discard();
				Notification.show("'Title' cannot be empty " + e.getMessage().toString() , Type.WARNING_MESSAGE);
			} catch (NonUniqueResultException e) {
				binder.discard();
				Notification.show("'Title' must be a unique name.\n'" +
									currmodel.getTitle() +	//queriedModel.getTitle() + 
									"' already exists.\n\nPlease try again.", Type.WARNING_MESSAGE);
			} catch (NullPointerException e) {
				binder.discard();
				if (currmodel.getTitle() == null)
						Notification.show("'Title' field cannot be empty.", Type.ERROR_MESSAGE);
//						UI.getCurrent().addWindow(new ModelEditor(parentsession, parentcase));
			}
			
//	        binder.clear();
			
			return queriedModel;
	}
	
	
	
	
	
	
	public Model editModel(Model model, TestSession currsession, FieldGroup fieldbinder) {
        
		models = ((MbpetUI) UI.getCurrent()).getModels();
        currmodel = models.getItem(model.getId()).getEntity();
        modelBeanItem = new BeanItem<Model>(currmodel);
        
//        sessions = MBPeTMenu.sessions;
        parentsession = currsession;				//sessions.getItem(parentsession.getId()).getEntity();
		parentcase = testcases.getItem(currmodel.getParentsut().getId()).getEntity(); 	//parentsession.getParentcase();

		prevTitle = currmodel.getTitle();
		System.out.println("prevTitle: " + prevTitle);
	
		binder = fieldbinder;
    	
		Model editedmodel = null;
			try {
				// EDIT existing Model
				
				// 1. commit the fieldgroup
				binder.commit();

		    	System.out.println("WHAT IS NEW LIST OF SESSIONS: " + currmodel.getParentsession().getModels()); // testing purposes
		    	for (Model m : currmodel.getParentsession().getModels()) {
		        	System.out.println(m.getId() + " - " + m.getTitle()); // testing purposes	            		
		    	}
		    	
				// check Model title doesnt exist for THIS SESSION
				int id =0;
				boolean titleOK = true;
				id = modelBeanItem.getBean().getId();	//testsession.getId();
				System.out.println("desired session and its Models : " + currmodel.getParentsession() + " - " + currmodel.getParentsession().getModels());	//sessions.getItem(currmodel.getParentsession().getId()).getEntity().getModels());
				
				// compare title against those in desired target session
				for (Model m : parentsession.getModels()) {	//sessions.getItemIds()
					System.out.println("Existing title -> new title : " + m.getTitle() + "->" + currmodel.getTitle());
					System.out.println("Existing id -> new id : " + m.getId() + "->" + id);
					System.out.println("Existing p.session -> attempted new p.session : " + m.getParentsession().getTitle() + "->" + currmodel.getParentsession().getTitle());
					
					if (m.getTitle().equals(currmodel.getTitle().trim()) && m.getId()!=id ) {	
						System.out.println("NOT ALLOWED...resetting model" );
						currmodel.setTitle(prevTitle);
						models.addEntity(currmodel);
						
						wrongTitle = m.getTitle();
						titleOK = false;
						break;
					}
				} 
	
				
				if (titleOK == true) {
					// check title
					checkTitles();
					
					// 3 UPDATE container
					models.addEntity(modelBeanItem.getBean());
					System.out.println("Entity is now: " + models.getItem(currmodel.getId()).getEntity().getTitle());

              	  	// 1.1 UPDATE parent Session reference
					parentsession.updateModelData(models.getItem(currmodel.getId()).getEntity());
//              	  	sessions.addEntity(parentsession);

					// 1.2 UPDATE parent Case reference
					parentcase.updateModelData(models.getItem(currmodel.getId()).getEntity());

					
					editedmodel = models.getItem(currmodel.getId()).getEntity();
					
//					// 2. UPDATE parent Case and Session reference
//					parentsession.updateModelData(editedmodel);	//(models.getItem(currmodel.getId()).getEntity());
//					parentcase.updateModelData(editedmodel); //(models.getItem(currmodel.getId()).getEntity());
////				System.out.println("Entity is now: " + sessions.getItem(testsession.getId()).getEntity().getTitle());
//					
//					// 3. UPDATE container
//					models.addEntity(modelBeanItem.getBean());
//					System.out.println("Entity is now: " + editedmodel.getTitle());
					
	            	System.out.println("WHAT IS NEW LIST OF SESSIONS: " + currmodel.getParentsession().getModels()); // testing purposes
	            	for (Model m : currmodel.getParentsession().getModels()) {
		            	System.out.println(m.getId() + " - " + m.getTitle()); // testing purposes	            		
	            	}
	            	
					// 4. write model file to disk
	            		// 4.1 first delete old file is it doesn't match the file name
	            		if (!prevTitle.equals(currmodel.getTitle())){
	            			fileUtils.deleteModelFromDisk(
	            					parentcase.getOwner().getUsername(),
	    							parentcase.getTitle(), 
	    							parentsession.getTitle(),
	    							parentsession.getParameters().getModels_folder(),
	    							prevTitle);
	            		}
	            		// 4.2 then write new file to disk
						fileUtils.writeModelToDisk(	//username, sut, session, settings_file)
							parentcase.getOwner().getUsername(),
							parentcase.getTitle(), 
							parentsession.getTitle(),
							parentsession.getParameters().getModels_folder(),
							editedmodel);
	            	
					confirmNotification("Model '" + editedmodel.getTitle() + "'", "saved");					
				
				} else {
					System.out.println("title was NOT fine.");
					binder.discard();
					Notification.show("The title '" + wrongTitle + "' already exists for this Session. Please rename this model.", Type.ERROR_MESSAGE);	//testsession.getTitle()
//					UI.getCurrent().addWindow(new ModelEditor(parentsession, parentcase));
//					createNewModel(currmodel, parentsession, binder);
				}

			} catch (ConstraintViolationException e) {
				binder.discard();
				Notification.show("'Title' cannot be empty and must be between 1 and 40 characters long.", Type.WARNING_MESSAGE);
				e.printStackTrace();
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
				System.out.println(e.getMessage().toString());
//						Notification.show("'Parent Session' cannot be empty " + e.getMessage().toString(), Type.ERROR_MESSAGE);
//						UI.getCurrent().addWindow(new ModelEditor(parentsession, parentcase));
			}
			
//	        binder.clear();
			
			return editedmodel;

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



		public String renameAceTitle(String titleFieldvalue, String editorvalue) {

			String newString = "";
			String firstline = "";
	        StringBuilder builder = new StringBuilder();
	        String newline = System.getProperty("line.separator");
	        boolean match = false;
			try {
				// get first line
				String[] lines = editorvalue.split("\n"); 
				firstline = lines[0];
				System.out.println("firstline is: " + lines[0]);
				
//				// replace title in first line
				String[] words = lines[0].split(" ");
				firstline = words[0];
				if (!firstline.contains("graph"))
					firstline = "digraph";
				firstline += " " + titleFieldvalue + " {";

				// restructure lines
				newString = firstline + "\n";
				builder.append(firstline).append(newline);
				for (int i = 1; i < lines.length; i++) {
					newString += lines[i] + "\n";
					builder.append(lines[i]);	//.append(newline);
				}
				
//				System.out.println(newString);
//				System.out.println(builder.toString());

			} catch (ArrayIndexOutOfBoundsException e) {
				Notification not = new Notification("Heads up!", "Check that your file matches proper .dot format syntax.");
				not.setStyleName("failure");
				not.setPosition(Position.MIDDLE_RIGHT);
				not.show(Page.getCurrent());
			} catch (NullPointerException e) {
				String s = "digraph " + titleFieldvalue + " {\n" +
							"\t//STATES\n"  +
							"\t1\n\n" +
							"\t//TRANSITIONS\n"  +
							"}";
				return s;
			}
			
			return newString;	//builder.toString();	
		}
		
		
		
		private void checkTitles() {
			List<Object> bfields = new ArrayList<Object>(binder.getBoundPropertyIds());
//			for (Object id : bfields) {
//				System.out.println(id.toString() + " " + binder.getField(id).getValue());
//				binder.getField(id).getValue();
//			}
			String ace = null;
			if (binder.getField(bfields.get(1)).getValue() != null)
				ace = binder.getField(bfields.get(1)).getValue().toString();
			String corrected = renameAceTitle(
					binder.getField(bfields.get(0)).getValue().toString(),
					ace);		//(selected.getDotschema());
			
			currmodel.setDotschema(corrected);
//			binder.getField(bfields.get(1)).setValue(corrected);
//			editor.setValue(corrected);
		
		}
		
		
//		public static String compareTitles(String titleFieldvalue, String editorvalue) {
//
////			String word = cardArray.get(card);
//			String str = "";
//			boolean match = false;
//			try {
//				String[] lines = editorvalue.split("\n"); 
//				System.out.println("firstline is: " + lines);
//				String newString = "";
//				if (lines[0].contains(titleFieldvalue)){
//				    match = true;
////				    return editorvalue;
//				    str = editorvalue;
//
//				} 
//
//			} catch (ArrayIndexOutOfBoundsException e) {
//				Notification not = new Notification("Heads up!", "Check that your file matches proper .dot format syntax.");
//				not.setStyleName("failure");
//				not.setPosition(Position.MIDDLE_RIGHT);
//				not.show(Page.getCurrent());
//			}
//			
//		if (match == false) {
//			Notification not = new Notification("Heads up!", "the 'Title field' and title in the '.dot file' do not match");
//			not.setStyleName("failure");
//			not.setPosition(Position.MIDDLE_RIGHT);
//			not.show(Page.getCurrent());
//		}
//			return str;
//
//		}
		

		
		
		
		
//		/*
//		 * Create new Model
//		 */
////		public ModelUtils(TestCase parentcase) {
////			this.models = MBPeTMenu.models;
////			currmodel = new Model(); 
////			this.modelBeanItem = new BeanItem<Model>(currmodel);
////			this.parentsession = new TestSession();
////			
////			this.navToCasePage = navToCasePage;
////			
////			init(parentsession, parentcase);
////		}
	//	
//		public ModelUtils(TestSession parentsession, TestCase parentcase) {		//JPAContainer<TestCase> container
////	      super("Create a new Test Case"); // Set window caption
//			this.models = MBPeTMenu.models;
//			this.currmodel = new Model(); 
//			this.modelBeanItem = new BeanItem<Model>(currmodel);
//			
//			init(parentsession, parentcase);
//		}
	//	
//		/*
//		 * Edit Mode
//		 */
//		public ModelUtils(Object modelid, TestSession parentsession, TestCase parentcase) {		//JPAContainer<TestCase> container
//			this.editmode = true;
//			this.navToCasePage = false;
//	        
//			this.models = MBPeTMenu.models;
//	        this.currmodel = models.getItem(modelid).getEntity();
//	        this.modelBeanItem = new BeanItem<Model>(currmodel);
	//
//	        init(parentsession, parentcase);
//		}
	//	
//		/*
//		 * Clone existing Model to new one
//		 */
//		public ModelUtils(Object modelId, TestSession parentsession, TestCase parentcase,
//				Table table, boolean clonemode) {		//JPAContainer<TestCase> container
//			this.clonemode = clonemode;
//			
//			this.models = MBPeTMenu.models;
//			this.currmodel = models.getItem(modelId).getEntity();
//			
//			this.clone = new Model();
//			clone.setTitle("(clone) " + currmodel.getTitle());
//			clone.setDotschema(currmodel.getDotschema());
//			clone.setParentsession(currmodel.getParentsession());
//			clone.setParentsut(currmodel.getParentsut());
	//
//			this.modelBeanItem = new BeanItem<Model>(clone);
//			
////			testsession = new TestSession(); 
////			this.newSessionItem = new BeanItem<TestSession>(testsession);
////			sessions = MBPeTMenu.sessions;	//JPAContainerFactory.make(TestSession.class, MbpetUI.PERSISTENCE_UNIT);	//container;
//			
//			init(parentsession, parentcase);
//		}
	//	
//		private void init(TestSession parentsession, TestCase parentcase) {
//			this.parentcase = parentcase;
//			this.parentsession = parentsession;		//sessions.getItem(parentsession.getId()).getEntity()
////			this.models = MBPeTMenu.models;	
//			this.sessions = MBPeTMenu.sessions;
//		}
}
package com.aaron.mbpet.ui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.aaron.mbpet.domain.Model;
import com.aaron.mbpet.domain.Parameters;
import com.aaron.mbpet.domain.TestCase;
import com.aaron.mbpet.domain.TestSession;
import com.aaron.mbpet.views.MBPeTMenu;
import com.aaron.mbpet.views.MainView;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.data.Item;
import com.vaadin.data.util.filter.Compare.Equal;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.server.Page;
import com.vaadin.shared.Position;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Tree;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

// Define a sub-window by inheritance
public class ConfirmDeleteMenuItemWindow extends Window implements Button.ClickListener {
	private static final long serialVersionUID = 5408254248079275265L;

	Tree menutree;
	Button delete;
	Button cancel;
	
	JPAContainer<TestCase> testcases;
	JPAContainer<TestSession> sessions;
	JPAContainer<Model> models;
	JPAContainer<Parameters> parameters;
	TestCase parentcase;
	
	private Object target;
	private String message;

	
	public ConfirmDeleteMenuItemWindow(Tree tree, Object targetId, String message) {
        super("Heads Up!");
        center();
        setResizable(false);
        setClosable(false);
        setModal(true);

        this.menutree = tree;
        this.testcases = MBPeTMenu.getTestcases();
        this.sessions = MBPeTMenu.getTestsessions();
        this.models = MBPeTMenu.models;
        this.parameters = MBPeTMenu.parameters;
        
        this.target = targetId; 
        this.message = message;
        
        setContent(buildWindowContent(tree));
	}
	
	
        private Component buildWindowContent(final Tree tree) {	//, final Object target, String message
        	// Some basic content for the window
            VerticalLayout vc = new VerticalLayout();
            vc.addComponent(new Label(message, ContentMode.HTML));
            vc.setMargin(true);
            vc.setSpacing(true);
            
            
            // confirm deletion from menu
            delete = new Button("Delete", this);
            delete.addStyleName("danger");
//            submit.setClickShortcut(KeyCode.ENTER);
            
            
            // cancel changes and close window
            cancel = new Button("Cancel");
//            addStyleName("danger");


            HorizontalLayout buttons = new HorizontalLayout();
            buttons.setWidth("100%");
            buttons.addComponent(cancel);
            buttons.addComponent(delete);
            buttons.setComponentAlignment(cancel, Alignment.MIDDLE_LEFT);
            buttons.setComponentAlignment(delete, Alignment.MIDDLE_RIGHT);
            
            vc.addComponent(buttons);     
            
            return vc;
        }     
        
        
        public void buttonClick(ClickEvent event) {
        	if (event.getButton() == delete) {
        		
	        	Object parentid = null;

	        	// delete session. session items are never root
	        	if (!menutree.isRoot(target)) {		       	        
	        		parentid = menutree.getParent(target);
	        		parentcase = testcases.getItem(parentid).getEntity();
	        		System.out.println("\nparent OBJECT is: " + parentid);
	        		System.out.println("\nparent TestCase is: " + parentcase);
	        		
	        		// for notification
	        		String deleteditem = sessions.getItem(target).getEntity().getTitle();
	        		
	        		// DELETE
	        		deleteSessionAndDescendants(sessions.getItem(target).getEntity());
	        		
//	                // 1. remove item from tree
//	                menutree.removeItem(target);
//
//	                // pre 1. delete child models and parameters
//	                TestSession currsession = sessions.getItem(target).getEntity();
//	                for (Model m : currsession.getModels()) {
//	                	models.removeItem(m.getId());
//	                }
//	                parameters.removeItem(currsession.getParameters().getId());
//	                                            
//	                // 2. remove child from Case's list of Sessions
//	                parentcase.removeSession(sessions.getItem(target).getEntity());
//	                	                
//	                // 3. delete session from container
//	                sessions.removeItem(target);
	                
	                menutree.select(parentid);
	                
	                //navigate to parent
	                getUI()
	    	            .getNavigator()
	    	            	.navigateTo(MainView.NAME + "/" + 
	    	            			parentcase.getTitle() + "-sut=" + parentcase.getId());
	                
	                confirmNotification(deleteditem);
	        	
	        	} else {
	        		TestCase testcase = testcases.getItem(target).getEntity();
	        		// 1) delete any child sessions first 
	        		if (menutree.hasChildren(target)) {
	        			System.out.println(testcase.getSessions().toArray().toString());
	        			int numsessions = testcase.getSessions().size();
	        			
	        			// Delete Session and it's models and parameters
	        			deleteSUTWithDecendants(testcase);

//	        			List<Object> children = new ArrayList<>(menutree.getChildren(target));
////            		Collection children = tree.getChildren(target);
//	        			for (int i=0; i<children.size(); i++) {	//for (Object child : children) {
//	        				menutree.removeItem(children.get(i));
		
	        		}
	        		// for notification
	        		String deleteditem = testcases.getItem(target).getEntity().getTitle();
	        		
	        		// 2) delete test case itself
	        		
	                // 2.1 remove item from tree
	                menutree.removeItem(target);
	                                            
	                // 2.2 remove test case from user's list of cases?
	                MainView.sessionUser.removeCase(testcases.getItem(target).getEntity());
	                
	                // 2.3 delete TestCase from container
	                testcases.removeItem(target);
        		
	        		// navigate to landing page
                	getUI()
    	            	.getNavigator()
    	            		.navigateTo(MainView.NAME + "/" + "landingPage");
	                confirmNotification(deleteditem);
	        	}		        	
	        	close();
	        	
        	} else if (event.getButton() == delete) {
                close(); // Close the sub-window
        	}
        }
        



		private void deleteSUTWithDecendants(TestCase testcase) {
			//copy list of sessions to iterate over
			List<TestSession> slist = testcase.getSessions();
			for (int i=0; i<slist.size(); i++) { 		//testcase.getSessions().size()  for (TestSession s : testcase.getSessions()) {
            
				System.out.println("SUT size list of sessions : " + testcase.getSessions().size());
				
				// get session to remove
				deleteSessionAndDescendants(testcase.getSessions().get(i));
			}
			//finally set empty list of sessions
			// remove child from Case's list of Sessions
            testcase.setSessions(null);

            
//				// get iterator for sessions
////			Iterator<TestSession> ite = testcase.getSessions().iterator();
////			while (ite.hasNext()) {
////				TestSession ses = ite.next();
//
////			for (TestSession ses : testcase.getSessions()) {
//				System.out.println("SUT size list of sessions : " + testcase.getSessions().size());
//				// get session to remove
//				TestSession ses = testcase.getSessions().get(i);	//sessions.getItem(testcase.getSessions().get(i).getId()).getEntity();
//				
//                System.out.println("removing from SUT, session : " + ses.getTitle());
//
//                // remove item from tree
//                menutree.removeItem(ses.getId());
//                                            
//                // pre 1. delete child models and parameters
////                TestSession currsession = sessions.getItem(target).getEntity();
////                for (Model m : ses.getModels()) {
//                Iterator<Model> it = ses.getModels().iterator();
//    			while (it.hasNext()) {
//    				Model m = it.next();
//    				ses.removeModel(m);
//                	models.removeItem(m.getId());
//                }
//                try {
//                	// remove for session's list
//                	ses.setParameters(null);
//
//                	// remove from db
//					parameters.removeItem(ses.getParameters().getId());
//				} catch (NullPointerException e) {
//					e.printStackTrace();
//				}
//
////                // remove child from Case's list of Sessions
////                testcase.removeSession(ses);	//(sessions.getItem(ses.getId()).getEntity());	//s
//                               
//                // delete session from container
//                sessions.removeItem(ses.getId());
//			} 
			
		}


		private void deleteSessionAndDescendants(TestSession ses) {

            System.out.println("removing from SUT, session : " + ses.getTitle());

            // 1 remove item from tree
            menutree.removeItem(ses.getId());
                                        
            // 2 delete child models
//            TestSession currsession = sessions.getItem(target).getEntity();
//            for (Model m : ses.getModels()) {
//			List<Model> mlist = ses.getModels();
            filterModelsBySession(ses);
			Collection<Object> mlist = models.getItemIds();
//			for (int i=0; i<mlist.size(); i++) { 		//testcase.getSessions().size()  for (TestSession s : testcase.getSessions()) {
			for (Object id : mlist) {
				System.out.println("SESSION size list of models : " + ses.getModels().size());
				// get session to remove
				Model m = models.getItem(id).getEntity();	//ses.getModels().get(i);
				ses.removeModel(m);
				// remove from container
				models.removeItem(m.getId());
			}
			models.removeAllContainerFilters();
			// update session's list of models
			ses.setModels(null);
			
			// 3 delete parameters
            try {
            	// remove for session's list
            	Parameters p = ses.getParameters();
            	ses.setParameters(null);
            	
            	// remove from db
            	parameters.removeItem(p.getId());

			} catch (NullPointerException e) {
				e.printStackTrace();
			}
            
            // delete session from container
            sessions.removeItem(ses.getId());
				
				
//            Iterator<Model> it = ses.getModels().iterator();
//			while (it.hasNext()) {
//				Model m = it.next();
//				ses.removeModel(m);
//            	models.removeItem(m.getId());
//            }
//            try {
//            	// remove for session's list
//            	ses.setParameters(null);
//
//            	// remove from db
//				parameters.removeItem(ses.getParameters().getId());
//			} catch (NullPointerException e) {
//				e.printStackTrace();
//			}
//
////            // remove child from Case's list of Sessions
////            testcase.removeSession(ses);	//(sessions.getItem(ses.getId()).getEntity());	//s
//                           
//            // delete session from container
//            sessions.removeItem(ses.getId());
			
		}


		public void filterModelsBySession(TestSession ses){
	    	models.removeAllContainerFilters();
//	    	Equal ownerfilter = new Equal("parentcase", getTestCaseByTitle());//  ("parentcase", getTestCaseByTitle(), true, false);
	    	Equal casefilter = new Equal("parentsession", ses);//  ("parentcase", getTestCaseByTitle(), true, false);
	    	
	    	models.addContainerFilter(casefilter);
		}
		
		
		private void confirmNotification(String deletedItem) {
            // welcome notification
            Notification notification = new Notification(deletedItem);
            notification
                    .setDescription("<span>was deleted.</span>");
            notification.setHtmlContentAllowed(true);
            notification.setStyleName("tray dark small closable login-help");
            notification.setPosition(Position.BOTTOM_RIGHT);
            notification.setDelayMsec(500);
            notification.show(Page.getCurrent());
    	}

}

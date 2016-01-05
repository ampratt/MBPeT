package com.aaron.mbpet.ui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.aaron.mbpet.MbpetUI;
import com.aaron.mbpet.domain.Model;
import com.aaron.mbpet.domain.Parameters;
import com.aaron.mbpet.domain.TRT;
import com.aaron.mbpet.domain.TestCase;
import com.aaron.mbpet.domain.TestSession;
import com.aaron.mbpet.domain.User;
import com.aaron.mbpet.services.FileSystemUtils;
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

	Tree menutree;
	Button delete;
	Button cancel;
	
	JPAContainer<TestCase> testcases;
	JPAContainer<TestSession> sessions;
	JPAContainer<Model> models;
	JPAContainer<Parameters> parameters;
	JPAContainer<TRT> trtcontainer;
	private User sessionuser = ((MbpetUI) UI.getCurrent()).getSessionUser();
	TestCase parentcase;
	FileSystemUtils fileUtils = new FileSystemUtils();

	private Object targetId;
	private String message;

	
	public ConfirmDeleteMenuItemWindow(Tree tree, Object targetId, String message) {
        super("Heads Up!");
        center();
        setResizable(false);
        setClosable(true);
        setModal(true);

        this.menutree = tree;
        this.testcases = ((MbpetUI) UI.getCurrent()).getTestcases();
        this.sessions = ((MbpetUI) UI.getCurrent()).getTestsessions();
        this.models = ((MbpetUI) UI.getCurrent()).getModels();
        this.parameters = ((MbpetUI) UI.getCurrent()).getParameterscontainer();
        this.trtcontainer = ((MbpetUI) UI.getCurrent()).getTrtcontainer();
        
        this.targetId = targetId; 
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
            cancel = new Button("Cancel", this);
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
	        	if (!menutree.isRoot(targetId)) {		       	        
	        		parentid = menutree.getParent(targetId);
	        		parentcase = testcases.getItem(parentid).getEntity();
	        		System.out.println("\nparent OBJECT is: " + parentid);
	        		System.out.println("\nparent TestCase is: " + parentcase);
	        		
	        		TestSession deletedSession = sessions.getItem(targetId).getEntity();
	        		
	        		// for notification
	        		String deleteditem = sessions.getItem(targetId).getEntity().getTitle();
	        		
	        		// delete session directory on disk
	        		fileUtils.deleteSessionDirectory(
	        				deletedSession.getParentcase().getOwner().getUsername(), 
	        				deletedSession.getParentcase().getTitle(), 
	        				deletedSession.getTitle());

	        		// DELETE
	        		deleteSessionAndDescendants(sessions.getItem(targetId).getEntity(), parentcase);

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
	        		TestCase testcase = testcases.getItem(targetId).getEntity();
	        		// 1) delete any child sessions first 
	        		if (menutree.hasChildren(targetId)) {
	        			System.out.println(testcase.getSessions().toArray().toString());
//	        			int numsessions = testcase.getSessions().size();
	        			
		        		// delete sut directory on disk
		        		fileUtils.deleteSUTDirectory(
		        				testcase.getOwner().getUsername(), 
		        				testcase.getTitle());
		        		
	        			// Delete Session and it's models and parameters
	        			deleteSUTWithDecendants(testcase);

//	        			List<Object> children = new ArrayList<>(menutree.getChildren(target));
////            		Collection children = tree.getChildren(target);
//	        			for (int i=0; i<children.size(); i++) {	//for (Object child : children) {
//	        				menutree.removeItem(children.get(i));
		
	        		}
	        		// for notification
	        		String deleteditem = testcases.getItem(targetId).getEntity().getTitle();
	        		
	        		// 2) delete test case itself
	        		
	                // 2.1 remove item from tree
	                menutree.removeItem(targetId);
	                                            
	                // 2.2 remove test case from user's list of cases?
	                sessionuser.removeCase(testcases.getItem(targetId).getEntity());
	                
	                // 2.3 delete TestCase from container
	                testcases.removeItem(targetId);
        		
	        		// navigate to landing page
                	getUI()
    	            	.getNavigator()
    	            		.navigateTo(MainView.NAME + "/" + "landingPage");
                	
	                confirmNotification(deleteditem);
	        	}		        	
	        	close();
	        	
        	} else if (event.getButton() == cancel) {
                close(); // Close the sub-window
        	}
        }
        



		private void deleteSUTWithDecendants(TestCase testcase) {
			System.out.println("### DELETING SUT ###");

			//copy list of sessions to iterate over
//			List<TestSession> slist = testcase.getSessions();
//			for (int i=0; i<slist.size()+1; i++) { 		//testcase.getSessions().size()  for (TestSession s : testcase.getSessions()) {
            filterSessionsBySUT(testcase);
			Collection<Object> slist = sessions.getItemIds();
//			for (int i=0; i<mlist.size(); i++) { 		//testcase.getSessions().size()  for (TestSession s : testcase.getSessions()) {
			int count = 1;
			for (Object id : slist) {
				// get session to remove
//				Model m = sessions.getItem(id).getEntity();
				
				System.out.println("SUT size list of sessions : " + testcase.getSessions().size());
				
				// get session to remove
				deleteSessionAndDescendants(sessions.getItem(id).getEntity(), testcase);
				System.out.println("### SESSION " + count + " DELETED ###");
				count ++;
			}
			sessions.removeAllContainerFilters();
//			// remove children from Case's list of Sessions
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



		private void deleteSessionAndDescendants(TestSession ses, TestCase testcase) {
			System.out.println("### DELETING SESSION ###  :  " + ses.getTitle());

            // 1 remove item from tree
            menutree.removeItem(ses.getId());
                                        
            // 2 delete child models
				//            TestSession currsession = sessions.getItem(target).getEntity();
				//            for (Model m : ses.getModels()) {
				//			List<Model> mlist = ses.getModels();
			System.out.println("### DELETING MODELS ###");

            filterModelsBySession(ses);
			Collection<Object> mlist = models.getItemIds();
//			for (int i=0; i<mlist.size(); i++) { 		//testcase.getSessions().size()  for (TestSession s : testcase.getSessions()) {
			for (Object id : mlist) {
				System.out.println("SESSION size list of models : " + ses.getModels().size());
				// get model to remove
				Model m = models.getItem(id).getEntity();	//ses.getModels().get(i);
				ses.removeModel(m);
				// remove from container
				models.removeItem(m.getId());
			}
			models.removeAllContainerFilters();
			// update session's list of models
			ses.setModels(null);
			
			
			// 3 delete parameters
			System.out.println("### DELETING PARAMETERS ###");
			System.out.println("### DELETING TRT's ###");
			
        	Parameters p = ses.getParameters();

			//copy list of TRT's to iterate over
//			List<TRT> trtlist = p.getTarget_response_times();
			filterTRTsByParameter(p);
			Collection<Object> trtlist = trtcontainer.getItemIds();
			for (Object id : trtlist){		//for (int i=0; i<trtlist.size(); i++) { 
				System.out.println("Parameter size list of trt's : " + p.getTarget_response_times().size());
				
				// get trt to remove
				TRT trt = trtcontainer.getItem(id).getEntity();	
				p.removeTRT(trt);
				
				// remove from container
				trtcontainer.removeItem(trt.getId());
			}
			trtcontainer.removeAllContainerFilters();
			
			// update session's list of models
			p.setTarget_response_times(null);
			
            try {
    			System.out.println("### DELETING Actual PARAMETERS ###");

            	// remove for session's list
//            	Parameters p = ses.getParameters();
            	ses.setParameters(null);
            	
            	// remove from db
            	parameters.removeItem(p.getId());

			} catch (NullPointerException e) {
				e.printStackTrace();
			}
            
            // remove from Case's list of Sessions
            testcase.removeSession(ses);
//            testcase.setSessions(null);

            // delete session from container
            sessions.removeItem(ses.getId());
				
				
            try {
				System.out.println("SUT list of sessions :" + testcase.getSessions());
				for (TestSession s : testcase.getSessions()) {
					System.out.println(s.getId() + " " + s.getTitle() + " - parent -> " + s.getParentcase().getTitle());
				}
			} catch (NullPointerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
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

		private void filterSessionsBySUT(TestCase testcase) {
	    	sessions.removeAllContainerFilters();
//	    	Equal ownerfilter = new Equal("parentcase", getTestCaseByTitle());//  ("parentcase", getTestCaseByTitle(), true, false);
	    	Equal casefilter = new Equal("parentcase", testcase);//  ("parentcase", getTestCaseByTitle(), true, false);
	    	
	    	sessions.addContainerFilter(casefilter);			
		}

		public void filterModelsBySession(TestSession ses){
	    	models.removeAllContainerFilters();
//	    	Equal ownerfilter = new Equal("parentcase", getTestCaseByTitle());//  ("parentcase", getTestCaseByTitle(), true, false);
	    	Equal casefilter = new Equal("parentsession", ses);//  ("parentcase", getTestCaseByTitle(), true, false);
	    	
	    	models.addContainerFilter(casefilter);
		}

		public void filterTRTsByParameter(Parameters p){
			trtcontainer.removeAllContainerFilters();
//	    	Equal ownerfilter = new Equal("parentcase", getTestCaseByTitle());//  ("parentcase", getTestCaseByTitle(), true, false);
	    	Equal trtfilter = new Equal("parentparameter", p);//  ("parentcase", getTestCaseByTitle(), true, false);
	    	
	    	trtcontainer.addContainerFilter(trtfilter);
		}
		
		private void confirmNotification(String deletedItem) {
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

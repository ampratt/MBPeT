package com.aaron.mbpet.ui;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NonUniqueResultException;
import javax.persistence.Persistence;
import javax.persistence.Query;

import com.aaron.mbpet.MbpetUI;
import com.aaron.mbpet.domain.Adapter;
import com.aaron.mbpet.domain.AdapterXML;
import com.aaron.mbpet.domain.Model;
import com.aaron.mbpet.domain.TestCase;
import com.aaron.mbpet.domain.TestSession;
import com.aaron.mbpet.domain.User;
import com.aaron.mbpet.services.FileSystemUtils;
import com.aaron.mbpet.services.ModelUtils;
import com.aaron.mbpet.services.ParametersUtils;
import com.aaron.mbpet.views.MainView;
import com.aaron.mbpet.views.adapters.AdapterEditor;
import com.aaron.mbpet.views.adapters.AdapterXMLEditor;
import com.aaron.mbpet.views.parameters.ParametersEditor;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.filter.Compare.Equal;
import com.vaadin.data.validator.BeanValidator;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.server.Page;
import com.vaadin.shared.Position;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.ui.Component;
import com.vaadin.ui.Field;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Tree;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

// Define a sub-window by inheritance
public class ZippedSessionCreator  {

	private JPAContainer<TestSession> sessions = ((MbpetUI) UI.getCurrent()).getTestsessions();;
	private JPAContainer<TestCase> testcases = ((MbpetUI) UI.getCurrent()).getTestcases();
	BeanItem<TestSession> newSessionItem;
	TestSession newsession;
	TestCase parentsut;
	private User owner;
	private Tree tree; 

	/*
	 * Create new Test Session
	 */
	public ZippedSessionCreator(File projectDir, User curruser, TestCase parentSut, Tree tree) {		//JPAContainer<TestCase> container		
		
		// create new session / ASSIGN title and parent SUT
		newsession = new TestSession();	//(projectDir.getName(), parentSut); 
		this.newSessionItem = new BeanItem<TestSession>(newsession);
		
		this.owner = curruser;
		this.parentsut = parentSut;
		this.tree = tree;
		
		createSession(projectDir);
	}
	
	public void createSession(File projectDir){
		
	// 1. set TITLE.		check SESSION title doesn't exist for THIS SESSION
		// get title from dir
		String dirtitle = projectDir.getName();	//getSessionTitleFromDir(projectDir);
		System.out.println("Session title from dir name: " + dirtitle);
		
		//compare to existing sessions
		boolean titleOK = true;
			System.out.println("WHAT IS current LIST OF SESSIONS: "
					+ parentsut.getSessions()); // testing purposes
			for (TestSession s : parentsut.getSessions()) {
				System.out.println(s.getId() + " - " + s.getTitle()); // testing purposes	            		
			}
		System.out.println("parentsut.getSessions() : " + parentsut.getSessions());
		for (TestSession s : parentsut.getSessions()) {
			System.out.println("Existing title -> new title : " + s.getTitle() + "->" + dirtitle);
			if ( s.getTitle().equals(dirtitle) ) {	
				titleOK = false;
				break;
			}
		}
		
		if(titleOK){
			newsession.setTitle(dirtitle);
		} else if (!titleOK){
			//find next number iteration available
			int i=2;
			boolean titleexists;	// = false;
			while(!titleOK){
//				dirtitle+=i;
				titleexists = false;
				for (TestSession s : parentsut.getSessions()) {
					System.out.println("!titleOK Existing title -> new title : " + s.getTitle() + "->" + (dirtitle+i));
					if ( s.getTitle().equals((dirtitle+i)) ) {	
						titleexists = true;
						break;
					}
				}
				if (!titleexists){
					newsession.setTitle((dirtitle+=i));
					titleOK = false;
					break;
				} else
					i++;
			}
		}
		
		// 2. set PARENT SUT
		newsession.setParentcase(parentsut);
		
		// 3. add to container
		sessions.addEntity(newSessionItem.getBean()); //jpa container	
		
		// add created item to tree (after retrieving db generated id)
		EntityManager em = Persistence.createEntityManagerFactory("mbpet")
				.createEntityManager();
		Query query = em.createQuery("SELECT OBJECT(t) FROM TestSession t WHERE t.title = :title AND t.parentcase = :parentcase");
		//		            query.setParameter("title", newsession.getTitle());
		query.setParameter("title",newsession.getTitle());
		query.setParameter("parentcase",newsession.getParentcase()); //MainView.sessionUser
		TestSession queriedSession = (TestSession) query.getSingleResult();
		//				            queriedSession = (TestSession) query.setParameter("title", testsession.getTitle()).getSingleResult();
		System.out.println("the generated id is: " + queriedSession.getId());
		int id = queriedSession.getId();
		
		// 4. create session directory for test and reports
		new FileSystemUtils().createSessionTestDir(
				newsession.getParentcase().getOwner().getUsername(),
				newsession.getParentcase().getTitle(), 
				newsession.getTitle());
		
		
		
		// 5. set PARAMETERS
		new ParametersEditor(queriedSession, projectDir);
    		// commit individual field, parsed from ace
    		ParametersUtils.commitAceParamData(queriedSession.getParameters(), queriedSession.getParameters().getSettings_file());
    	
		// 6. set ADAPTER.PY object
		new AdapterEditor(queriedSession, projectDir);
		
		// 7. set ADAPTER.XML object
		new AdapterXMLEditor(queriedSession);
		
		// 8. set MODELS
		ModelUtils modelUtils = new ModelUtils();
		File[] modelFiles = 
				new File(projectDir+"/models/").listFiles(new FileFilter() {
            @Override
            public boolean accept(File file) {
                return file.isFile() && (file.getName().endsWith(".gv"));
            }
        });
		if (!(modelFiles==null) && modelFiles.length>0) {
	        System.out.println("number of uploaded models:" + modelFiles.length);
	        // retrieve individual html reports and add them to List
	        for (File f : modelFiles) {			//(int i=0; i<directories.length; i++) {
//	            System.out.println("processing model - " + f.getName());

	        	// CREATE the models from the files
				modelUtils.createUploadedModel(f, queriedSession); //currmodel.getParentsession(),

	        }
		}
		
		
		// 9. ADD to tree in right order
		if (tree.hasChildren(parentsut.getId())) {
			sortAddToTree(id);
		} else {
			tree.addItem(id);
			tree.setParent(id, parentsut.getId());
			tree.setChildrenAllowed(id, false);
			tree.setItemCaption(id, sessions.getItem(id).getEntity().getTitle()); //newsession.getTitle()
			tree.expandItem(parentsut.getId());
			tree.select(id);
		}
		
		// 10. update parent Case to add Session to testCase List<Session> sessions
		parentsut.addSession(queriedSession);
		testcases.addEntity(parentsut);
		//              	  	List<TestSession> listofsessions = parentCase.getSessions();
		//              	  	listofsessions.add(queriedSession);		//sessions.getItem(id).getEntity()
		//              	  	parentCase.setSessions(listofsessions);
		
		System.out.println("WHAT IS NEW LIST OF SESSIONS: "
						+ parentsut.getSessions()); // testing purposes
		for (TestSession s : parentsut.getSessions()) {
			System.out.println(s.getId() + " - " + s.getTitle()); // testing purposes	            		
		}

		
	}
	
	

	int getTrailingInteger(String str) {
	    int positionOfLastDigit = getPositionOfLastDigit(str);
	    if (positionOfLastDigit == str.length()) {
	        // string does not end in digits
	        return -1;
	    }
	    return Integer.parseInt(str.substring(positionOfLastDigit));
	}

	int getPositionOfLastDigit(String str) {
	    int pos;
	    for (pos=str.length()-1; pos>=0; --pos){
	        char c = str.charAt(pos);
	        if (!Character.isDigit(c)) break;
	    }
	    return pos + 1;
	}
	
	
	

	private void sortAddToTree(Object sid) {
//        // sort session elements				            
//		List<TestSession> caseSessions = parentCase.getSessions();
//		List<Integer> sortedids = new ArrayList<Integer>(); 
//		for (TestSession s : caseSessions){
//			sortedids.add(s.getId());
//			// remove tree items
//			tree.removeItem(s.getId());
//		}
//		Collections.sort(sortedids);
//		Collections.reverse(sortedids);
//		System.out.println("SORTED ID's: " + sortedids);
		
		List<Object> sortedids = new ArrayList<Object>(); 
		for ( Object child : tree.getChildren(parentsut.getId())) {
			sortedids.add(child);
		}
		
		for( Object s : sortedids ) {
			// remove tree items
			tree.removeItem(s);			
		}
		
		// add new item and then re-add old items
        tree.addItem(sid);
        tree.setParent(sid, parentsut.getId());
        tree.setChildrenAllowed(sid, false);
  	  	tree.setItemCaption(sid, sessions.getItem(sid).getEntity().getTitle());		//newsession.getTitle()
    	tree.expandItem(parentsut.getId());
  	  	tree.select(sid);
  	  	
		for (Object id : sortedids) {	//testcase.getSessions()	matchingsessions
			Object sessionid = sessions.getItem(id).getEntity().getId();
			tree.addItem(sessionid);
			tree.setItemCaption(sessionid, sessions.getItem(id).getEntity().getTitle());
			tree.setParent(sessionid, parentsut.getId());
			tree.setChildrenAllowed(sessionid, false);
		}
	
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
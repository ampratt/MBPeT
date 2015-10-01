package com.aaron.mbpet.views.sessions;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

import com.aaron.mbpet.MbpetUI;
import com.aaron.mbpet.components.tabs.TabLayout;
import com.aaron.mbpet.domain.TestCase;
import com.aaron.mbpet.domain.TestSession;
import com.aaron.mbpet.ui.NewUseCaseInstanceWindow;
import com.aaron.mbpet.views.MBPeTMenu;
import com.aaron.mbpet.views.users.UserEditor.EditorSavedEvent;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.jpacontainer.JPAContainerFactory;
import com.vaadin.data.Container;
import com.vaadin.data.util.HierarchicalContainer;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.event.ListenerMethod.MethodException;
import com.vaadin.event.SelectionEvent;
import com.vaadin.event.SelectionEvent.SelectionListener;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Resource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Tree;
import com.vaadin.ui.VerticalLayout;

public class SessionViewer extends VerticalLayout implements Button.ClickListener {	//implements View 
	private static final long serialVersionUID = -5878465079008311569L;
	
	Panel equalPanel = new Panel("equal panel"); 
	public static Label pageTitle = new Label("");
	Tree tree;
    JPAContainer<TestCase> testcases;

	private Button saveButton;
	private Button stopButton;
	private Button startButton;
	private Button newSessionButton;
	
	public SessionViewer(String title, Tree tree) {
		setSizeFull();
		this.addStyleName("content");
		
        testcases = MBPeTMenu.getTestcases();
		
		this.tree = tree;
		setPageTitle(title);
		
		addComponent(buildTopBar());

//		Component contentLayout = buildContentLayout();
//    	addComponent(contentLayout);
//    	setExpandRatio(contentLayout, 1);
    	
//		VerticalLayout tabs = new VerticalLayout();
		TabLayout tabs = new TabLayout();
		addComponent(tabs);
    	setExpandRatio(tabs, 1);
	}

	public HorizontalLayout buildTopBar() {
		pageTitle.addStyleName("test-case-title");
		pageTitle.addStyleName("h2");
		
		newSessionButton = new Button("", this);
		saveButton = new Button("", this);
		startButton = new Button("", this);
		stopButton = new Button("", this);
		
		newSessionButton.addStyleName("tiny");
		saveButton.addStyleName("tiny");
//		saveButton.addStyleName("friendly");
		startButton.addStyleName("tiny");
//		startButton.addStyleName("primary");
		stopButton.addStyleName("tiny");
		
		newSessionButton.setIcon(FontAwesome.PLUS);
		saveButton.setIcon(FontAwesome.SAVE);
		startButton.setIcon(FontAwesome.PLAY);
		stopButton.setIcon(FontAwesome.STOP);
		
		newSessionButton.setDescription("Create a new Test Session");
		saveButton.setDescription("Save Settings and Parameters");
		startButton.setDescription("Run Test Session");
		stopButton.setDescription("Stop Test Session");
		
		HorizontalLayout topBar = new HorizontalLayout();
		topBar.setStyleName("topBar-layout-padding");
		topBar.setWidth("100%");
		topBar.setSpacing(true);

		HorizontalLayout buttons = new HorizontalLayout();
		buttons.setSpacing(true);
		buttons.addComponents(newSessionButton, saveButton, startButton, stopButton); 
		
		topBar.addComponents(pageTitle, buttons); //(pageTitle);
//		topBar.addComponent(newUseCaseButton);
//		topBar.addComponent(saveButton);
//		topBar.addComponent(startButton);
		
		topBar.setComponentAlignment(pageTitle, Alignment.MIDDLE_LEFT);
		topBar.setComponentAlignment(buttons, Alignment.MIDDLE_RIGHT);	//(newUseCaseButton, Alignment.MIDDLE_RIGHT);
//		topBar.setComponentAlignment(saveButton, Alignment.MIDDLE_RIGHT);
//		topBar.setComponentAlignment(startButton, Alignment.MIDDLE_RIGHT);
		
		topBar.setExpandRatio(pageTitle, 2);
		topBar.setExpandRatio(buttons, 2);	//(newUseCaseButton, 2);
//		topBar.setExpandRatio(saveButton, 0);	
//		topBar.setExpandRatio(startButton, 0);
//		topBar.setExpandRatio(stopButton, 0);	    
	    
		return topBar;
	}
	
	
    public void buttonClick(ClickEvent event) {
        if (event.getButton() == newSessionButton) {
	        // open window to create item
	        UI.getCurrent().addWindow(new TestSessionEditor(tree, getTestCaseByTitle() ));	//testcases.getItem(parent).getEntity()

        } else if (event.getButton() == saveButton) {
			//testing purposes
			Notification.show("Your settings will be saved", Type.WARNING_MESSAGE);

        } else if (event.getButton() == startButton) {
			//testing purposes
			Notification.show("This will launch the test session to the master", Type.WARNING_MESSAGE);

        } else if (event.getButton() == stopButton) {
			//testing purposes
			Notification.show("This will stop the test", Type.WARNING_MESSAGE);

        }
    }
   

	private TestCase getTestCaseByTitle() {
		String title = pageTitle.getValue();
		if (title.contains("/")) {
			title = title.substring(0, title.indexOf("/")); 
		}
		System.out.println("the parses test case title is: " + title);
		
		// add created item to tree (after retrieving db generated id)
        EntityManager em = Persistence.createEntityManagerFactory("mbpet")
										.createEntityManager();	
        Query query = em.createQuery(
    		    "SELECT OBJECT(t) FROM TestCase t WHERE t.title = :title"
    		);
//        query.setParameter("title", newsession.getTitle());
        TestCase queriedCase = 
        		(TestCase) query.setParameter("title", title).getSingleResult();
        System.out.println("retrieved TC fro db is : " 
        				+  queriedCase.getId() + " - " + queriedCase.getTitle());
			            
		return testcases.getItem(queriedCase.getId()).getEntity();
		
	}

	
	public static void setPageTitle(String t){
		pageTitle.setValue(t);

	}
	public String getPageTitle() {
		return pageTitle.getValue();
	}

//    @Override
//    public void enter(ViewChangeEvent event) {
//        if (event.getParameters() == null
//            || event.getParameters().isEmpty()) {
//        	setPageTitle("didn't get anything?");	//title.setValue
////            equalPanel.setContent(
////                new Label("Nothing to see here, " +
////                          "just pass along."));
//            return;
//        } else {
//        	Notification.show(event.getParameters(), Type.WARNING_MESSAGE);
//        	System.out.println(event.getParameters());
//            setPageTitle(event.getParameters());	//title.setValue
////            equalPanel.setContent(new AnimalViewer(
////                event.getParameters()));
//        }
//    }


    /*
     * Handle actions
     */
//    @Override
//    public void handleAction(final Action action, final Object sender,
//            final Object target) {
//        if (action == ACTION_ADD) {
//            // Allow children for the target item, and expand it
//            sample.setChildrenAllowed(target, true);
//            sample.expandItem(target);
// 
//            // Create new item, set parent, disallow children (= leaf node)
//            final Object itemId = sample.addItem();
//            sample.setParent(itemId, target);
//            sample.setChildrenAllowed(itemId, false);
// 
//            // Set the name for this item (we use it as item caption)
//            final Item item = sample.getItem(itemId);
//            final Property name = item
//                    .getItemProperty(ExampleUtil.hw_PROPERTY_NAME);
//            name.setValue("New Item");
// 
//        } else if (action == ACTION_DELETE) {
//            final Object parent = sample.getParent(target);
//            sample.removeItem(target);
//            // If the deleted object's parent has no more children, set it's
//            // childrenallowed property to false (= leaf node)
//            if (parent != null && sample.getChildren(parent) == null) {
//                sample.setChildrenAllowed(parent, false);
//            }
//        }
//    }
	
//	public Component buildContentLayout(){
//	HorizontalLayout contentLayout = new HorizontalLayout();
//	contentLayout.setWidth("100%");
//	contentLayout.addStyleName("content");
//	
////    equalPanel.setWidth("100%");
//    equalPanel.addStyleName("equal-panel");
//    contentLayout.addComponent(equalPanel);
////    contentLayout.setExpandRatio(equalPanel, 1);
//    
//    contentLayout.addComponent(buildTreeMenu2());
////    contentLayout.addComponent(BuildTreeMenu());
//    
//    // Create a grid
//    Grid grid = new Grid();
//    // Define some columns
//    grid.addColumn("name", String.class);
//    grid.addColumn("born", Integer.class);
//    // Add some data rows
//    grid.addRow("Nicolaus Copernicus", 1543);
//    grid.addRow("Galileo Galilei", 1564);
//    grid.addRow("Johannes Kepler", 1571);
//
////    contentLayout.addComponent(grid);
//    
//    grid.addSelectionListener(new SelectionListener() {
//        @Override
//        public void select(SelectionEvent event) {                   
//        	getUI()
//        		.getNavigator()
//        			.navigateTo(MainView.NAME + "/" + 
//    							event.getSelected().toString());
//        	
//			// update title
//        	setPageTitle(event.getSelected().toString());
////        	title.setValue(event.getSelected().toString());
//        }
//    }); 
//
//    return contentLayout;
//}
	
}

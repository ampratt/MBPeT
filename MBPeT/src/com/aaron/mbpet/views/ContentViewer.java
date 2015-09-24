package com.aaron.mbpet.views;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.persistence.Query;

import com.aaron.mbpet.MbpetUI;
import com.aaron.mbpet.domain.TestCase;
import com.aaron.mbpet.domain.TestSession;
import com.aaron.mbpet.tabs.TabLayout;
import com.aaron.mbpet.ui.AnimalViewer;
import com.aaron.mbpet.ui.NewUseCaseInstanceWindow;
import com.aaron.mbpet.ui.TestSessionEditor;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.jpacontainer.JPAContainerFactory;
import com.vaadin.data.Container;
import com.vaadin.data.util.HierarchicalContainer;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.event.SelectionEvent;
import com.vaadin.event.SelectionEvent.SelectionListener;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Tree;
import com.vaadin.ui.VerticalLayout;

public class ContentViewer extends VerticalLayout {	//implements View 
	private static final long serialVersionUID = -5878465079008311569L;
	
	Panel equalPanel = new Panel("equal panel"); 
	public static Label pageTitle = new Label("");
	Tree tree;
    JPAContainer<TestCase> testcases;
	
	public ContentViewer(String title, Tree tree) {
		setSizeFull();
		this.addStyleName("content");
		
        testcases = JPAContainerFactory.make(TestCase.class,
        		MbpetUI.PERSISTENCE_UNIT);
		
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
		
		Button newUseCaseButton = new Button("New Use Case");
		Button saveButton = new Button("Save settings");
		Button startButton = new Button("Start Test");
		newUseCaseButton.addStyleName("tiny");
		saveButton.addStyleName("tiny");
		startButton.addStyleName("tiny");
	
		HorizontalLayout topBar = new HorizontalLayout();
		topBar.setStyleName("topBar-layout-padding");
		topBar.setWidth("100%");
		topBar.setSpacing(true);
		
		topBar.addComponent(pageTitle);
		topBar.addComponent(newUseCaseButton);
		topBar.addComponent(saveButton);
		topBar.addComponent(startButton);
		
		topBar.setComponentAlignment(pageTitle, Alignment.MIDDLE_LEFT);
		topBar.setComponentAlignment(newUseCaseButton, Alignment.MIDDLE_RIGHT);
		topBar.setComponentAlignment(saveButton, Alignment.MIDDLE_RIGHT);
		topBar.setComponentAlignment(startButton, Alignment.MIDDLE_RIGHT);
		
		topBar.setExpandRatio(pageTitle, 2);
		topBar.setExpandRatio(newUseCaseButton, 2);
		topBar.setExpandRatio(saveButton, 0);	
		topBar.setExpandRatio(startButton, 0);
		
	    saveButton.addClickListener(new Button.ClickListener() {
			public void buttonClick(ClickEvent event) {
				//testing purposes
				Notification.show("Your settings will be saved", Type.WARNING_MESSAGE);
			}
		});
	    startButton.addClickListener(new Button.ClickListener() {
			public void buttonClick(ClickEvent event) {
				//testing purposes
				Notification.show("This will launch the test case to the master", Type.WARNING_MESSAGE);
			}
		});		
	    newUseCaseButton.addClickListener(new Button.ClickListener() {
			public void buttonClick(ClickEvent event) {
				//get currently selected TestCase
				// add created item to tree (after retrieving db generated id)
//                EntityManager em = Persistence
//    					.createEntityManagerFactory("mbpet")
//    					.createEntityManager();	
//	            Query queryByTestSessionName = em.createQuery(
//	        		    "SELECT OBJECT(t) FROM TestCase t WHERE t.title = :title"
//	        		);
//	            queryByTestSessionName.setParameter("title", testcases.getI.getTitle());
//	            TestSession queriedSession = (TestSession) queryByTestSessionName.getSingleResult();
//	            System.out.println("the generated id is: " + queriedSession.getId());
//	            Object id = queriedSession.getId();	// here is the id we need for tree
	            
	            
		        // open window to create item
		        UI.getCurrent().addWindow(new TestSessionEditor(tree, 
		        				));	//testcases.getItem(parent).getEntity()

//				NewUseCaseInstanceWindow sub = new NewUseCaseInstanceWindow(tree, pageTitle.getValue());
//		        // Add it to the root component
//		        UI.getCurrent().addWindow(sub);
			}
		});	
	    
	    
		return topBar;
	}
	
//	public Component buildContentLayout(){
//    	HorizontalLayout contentLayout = new HorizontalLayout();
//    	contentLayout.setWidth("100%");
//    	contentLayout.addStyleName("content");
//    	
////        equalPanel.setWidth("100%");
//        equalPanel.addStyleName("equal-panel");
//        contentLayout.addComponent(equalPanel);
////        contentLayout.setExpandRatio(equalPanel, 1);
//        
//        contentLayout.addComponent(buildTreeMenu2());
////        contentLayout.addComponent(BuildTreeMenu());
//        
//        // Create a grid
//        Grid grid = new Grid();
//        // Define some columns
//        grid.addColumn("name", String.class);
//        grid.addColumn("born", Integer.class);
//        // Add some data rows
//        grid.addRow("Nicolaus Copernicus", 1543);
//        grid.addRow("Galileo Galilei", 1564);
//        grid.addRow("Johannes Kepler", 1571);
//
////        contentLayout.addComponent(grid);
//        
//        grid.addSelectionListener(new SelectionListener() {
//            @Override
//            public void select(SelectionEvent event) {                   
//            	getUI()
//            		.getNavigator()
//            			.navigateTo(MainView.NAME + "/" + 
//        							event.getSelected().toString());
//            	
//				// update title
//            	setPageTitle(event.getSelected().toString());
////            	title.setValue(event.getSelected().toString());
//            }
//        }); 
//
//        return contentLayout;
//    }
	

	
	private Component buildTreeMenu2() {   
	    	final Tree tree = new Tree("HierarchicalContainer:");
	    	tree.setContainerDataSource(createTreeContent());
//	    	tree.addStyleName("treemenu");
   	
	        // Add actions (context menu)
//	        sample.addActionHandler(this);
	 
	        // Cause valueChange immediately when the user selects
	        tree.setImmediate(true);
	 
	        // Set tree to show the 'name' property as caption for items
//	        sample.setItemCaptionPropertyId(ExampleUtil.hw_PROPERTY_NAME);
//	        tree.setItemCaptionMode(ItemCaptionMode.PROPERTY);
	 
	        // Expand whole tree
//	        System.out.println("tree.rootItemIds(): " + tree.rootItemIds());
//	        for (final Object id : tree.rootItemIds()) {
//	            tree.expandItemsRecursively(id);
//	        }
	 
	        tree.addItemClickListener(new ItemClickListener() {
				@Override
				public void itemClick(ItemClickEvent event) {
					// TODO Auto-generated method stub
	            	String selected = (String) event.getItemId();
	            	String path = selected;
	            	System.out.println("this is the current ITEM selection: " + selected.toString());
					if (!tree.isRoot(event.getItemId())) {
//						Object parent = tree.getParent(event.getItemId());
						String parent = (String) tree.getParent(event.getItemId());
		            	System.out.println("Parent is: " + parent);
		            	path = parent+ "/" + selected;
					}
					
					getUI()
		         		.getNavigator()
		         			.navigateTo(MainView.NAME + "/" + 
     							path);

					// update title
					setPageTitle(path);
//					title.setValue(path);
				}
			});
	        
//	        tree.addValueChangeListener(new ValueChangeListener(){
//	            public void valueChange(ValueChangeEvent event){
//	            	// get parent and child
//	            	Object current = event.getProperty();
//	            	System.out.println("this is the current PROPERTY	 selection: " + event.getProperty().toString());
//	            	if(!tree.isRoot(event.getProperty())){
//	            		Object parent = tree.getParent(event.getProperty());
////		            	System.out.println("Parent is: " + tree.getParent(event.getProperty()).toString());
//
//	            	}
//	            }
//         }
//	        );
	    	
			return tree;
		}

	  
    
    @SuppressWarnings("unchecked")
	private Container createTreeContent() {
		   final Object[][] testCases = new Object[][]{
	    	        new Object[]{"Portal", "01.02.15", "03.04.15", "04.05.15", "06.07.15"},
	    	        new Object[]{"demo", "01.02.15", "03.04.15", "04.05.15", "06.07.15"},
	    	        new Object[]{"random", "01.02.15", "03.04.15", "04.05.15", "06.07.15"}
		        };
	    	        
		   HierarchicalContainer hc = new HierarchicalContainer();
		   hc.addContainerProperty("name", String.class, "");
		   
//		   for (int i=0; i<testCases.length; i++) {
//	            hc.addItem(testCases[i]);
//	       }
		   for (int i=0; i<testCases.length; i++) {
				String testCase = (String) (testCases[i][0]);
				hc.addItem(testCase);
				hc.setChildrenAllowed(testCase, true);	//false
				
//				System.out.println("\ntestCases[i].length: " + testCases[i].length);
//		        System.out.println("parent is: " + testCase);

				if (testCases[i].length == 1) {
				    // The test case has no instances so make it a leaf.
				} else {
				    // Add children (instances) under the test cases.
					for (int j=1; j<testCases[i].length; j++) {
					
						// Add the item as a regular item.
				        String instance = (String) (testCases[i][j]);
				        hc.addItem(instance);
//				        System.out.println("instance is: " +instance);					    
					    
//				        hc.getContainerProperty(instance, "name").setValue(instance);

					    // Set it to be a child.
				        hc.setParent(instance, testCase);
					    
					    // Make the instance look like leaves.
				        hc.setChildrenAllowed(instance, false);
				        
//				        // Get the item object
//				        Item item = hc.getItem(instance);
//				        // Access a property in the item
//				        Property<String> nameProperty = item.getItemProperty("name");
//				        System.out.println("hc.getItem is: " + nameProperty);
					}
				}
			}
		   
	        for (final Object id : hc.rootItemIds()) {
	        	System.out.println("HC parent: " + hc.rootItemIds());
	            System.out.println("HC children: " +hc.getChildren(id));
	        }
	        
	        return hc;
	}


//	private Component BuildTreeMenu() {
//    	final Object[][] planets = new Object[][]{
//    	        new Object[]{"Mercury"}, 
//    	        new Object[]{"Venus"},
//    	        new Object[]{"Earth", "The Moon"},    
//    	        new Object[]{"Mars", "Phobos", "Deimos"},
//    	        new Object[]{"Jupiter", "Io", "Europa", "Ganymedes",
//    	                                "Callisto"}
//	        };
//    	        
//    	Tree tree = new Tree("The Planets and Major Moons");
//
//    	/* Add planets as root items in the tree. */
//    	for (int i=0; i<planets.length; i++) {
//    	    String planet = (String) (planets[i][0]);
//    	    tree.addItem(planet);
//    	    
//    	    if (planets[i].length == 1) {
//    	        // The planet has no moons so make it a leaf.
//    	        tree.setChildrenAllowed(planet, false);
//    	    } else {
//    	        // Add children (moons) under the planets.
//    	        for (int j=1; j<planets[i].length; j++) {
//    	            String moon = (String) planets[i][j];
//    	            
//    	            // Add the item as a regular item.
//    	            tree.addItem(moon);
//    	            
//    	            // Set it to be a child.
//    	            tree.setParent(moon, planet);
//    	            
//    	            // Make the moons look like leaves.
//    	            tree.setChildrenAllowed(moon, false);
//    	        }
//
//    	        // Expand the subtree.
//    	        tree.expandItemsRecursively(planet);
//    	    }
//    	}
////    	main.addComponent(tree);
//    	
//		return tree;
//	}

	
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
	
}

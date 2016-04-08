package com.aaron.mbpet.views;


import com.aaron.mbpet.MbpetUI;
import com.aaron.mbpet.domain.Model;
import com.aaron.mbpet.domain.TestCase;
import com.aaron.mbpet.domain.TestSession;
import com.aaron.mbpet.domain.User;
import com.aaron.mbpet.views.cases.CreateTestCaseWindow;
import com.aaron.mbpet.views.cases.TestCaseEditor;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.data.Item;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Table;
import com.vaadin.ui.Tree;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Notification.Type;

public class LandingPage extends VerticalLayout {	//implements View 

    public static final String NAME = "landingPage"; 
	Tree tree;
	User currUser;
    JPAContainer<TestCase> testcases;
    private Table sessionsTable;
    
	public LandingPage(Tree tree, User currUser) {
		this.tree = tree;
		this.currUser = currUser;
		setSizeFull();
		this.addStyleName("content");
		
        testcases = ((MbpetUI) UI.getCurrent()).getTestcases();
	
        Label title = new Label("Welcome to the MBPeT Dashboard for web-application performance testing");
        title.addStyleName("landing-page-title");
        title.addStyleName("h2");
        addComponent(title);
        setComponentAlignment(title, Alignment.TOP_CENTER);
        
        SUTEditorTable sutTable = new SUTEditorTable(tree, currUser);
//        addComponent(buildActionButtons());
//        Component content = buildSUTPanel();
        addComponent(sutTable);
        setExpandRatio(sutTable, 1);
        setComponentAlignment(sutTable, Alignment.MIDDLE_CENTER);
	 }

	
	public HorizontalLayout buildActionButtons() {
		HorizontalLayout hc = new HorizontalLayout();
		hc.setWidth("100%");

		Button createTestCase = new Button("create new SUT");
		hc.addComponent(createTestCase);
		
        // button listener
		createTestCase.addClickListener(new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
		        // open window to create item
//				CreateTestCaseWindow sub = new CreateTestCaseWindow(tree);
		        UI.getCurrent().addWindow(new TestCaseEditor(tree));

		        // Add it to the root component
//		        UI.getCurrent().addWindow(sub);
			}
		});
		hc.setComponentAlignment(createTestCase, Alignment.MIDDLE_CENTER);

		return hc;
	}

//    @Override
//    public void enter(ViewChangeEvent event) {
//        if (event.getParameters() == null
//            || event.getParameters().isEmpty()) {
////        	title.setValue("didn't get anything?");
////            equalPanel.setContent(
////                new Label("Nothing to see here, " +
////                          "just pass along."));
//            return;
//        } else {
////        	String str = event.getParameters();
//////            setPageTitle(str);
////            title.setValue(str);
////            title.setCaption(str);
////            
////        	System.out.println("EVENTS PARAMS WERE: " + event.getParameters().toString());
////            equalPanel.setContent(new AnimalViewer(
////                event.getParameters()));
//        }
//    }
	 
}

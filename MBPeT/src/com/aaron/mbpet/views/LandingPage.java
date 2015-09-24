package com.aaron.mbpet.views;


import com.aaron.mbpet.ui.CreateTestCaseWindow;
import com.aaron.mbpet.ui.TestCaseEditor;
import com.vaadin.data.Item;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Tree;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Notification.Type;

public class LandingPage extends VerticalLayout {	//implements View 
	private static final long serialVersionUID = -5311837216590841111L;

    public static final String NAME = "landingPage"; 
	Tree tree;
	
	public LandingPage(Tree tree) {
		this.tree = tree;
		setSizeFull();
		this.addStyleName("content");
	
        Label title = new Label("Welcome to the MBPet tool for web application performance testing.");
        title.addStyleName("landing-page-title");
        title.addStyleName("h2");
        
        addComponent(title);
        addComponent(buildActionButtons());
        
//	        setComponentAlignment(getComponent(1), Alignment.MIDDLE_CENTER);
	 }
	
	public HorizontalLayout buildActionButtons() {
		HorizontalLayout hc = new HorizontalLayout();
		hc.setWidth("100%");

		Button createTestCase = new Button("create new Test Case");
		hc.addComponent(createTestCase);
		
        // button listener
		createTestCase.addClickListener(new ClickListener() {
			/**
			 * 
			 */
			private static final long serialVersionUID = -3952503576887345886L;

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

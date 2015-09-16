package com.aaron.mbpet.ui;

import com.aaron.mbpet.tabs.TabLayout;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Resource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Notification.Type;

public class PageTemplate extends VerticalLayout implements View {

	Label title = new Label("default");
	
	public PageTemplate() {
		setSizeFull();
		
		addComponent(buildTopBar());
		
//		TabLayout tabs = new TabLayout();
		VerticalLayout tabs = new VerticalLayout();
		addComponent(tabs);
		setExpandRatio(tabs, 1);
//		addComponent(buildTabs());
	}
	
	private HorizontalLayout buildTopBar() {
		//h1.setValue(pageTitle);
		title.addStyleName("h1");
		
		Button newUseCaseButton = new Button("New Use Case");
		Button saveButton = new Button("Save settings");
		Button startButton = new Button("Start Test");
		
		HorizontalLayout header = new HorizontalLayout();
		//header.setStyleName("header-padding");
		header.setWidth("100%");
		header.setSpacing(true);
		header.addComponent(title);
		header.addComponent(saveButton);
		header.addComponent(startButton);
		
		header.setComponentAlignment(title, Alignment.MIDDLE_LEFT);
		header.setComponentAlignment(newUseCaseButton, Alignment.MIDDLE_RIGHT);
		header.setComponentAlignment(saveButton, Alignment.MIDDLE_RIGHT);
		header.setComponentAlignment(startButton, Alignment.MIDDLE_RIGHT);
		header.setExpandRatio(title, 1);
		header.setExpandRatio(newUseCaseButton, 2);
		header.setExpandRatio(saveButton, 0);	
		header.setExpandRatio(startButton, 0);
		
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
				//testing purposes
				Notification.show("This will launch the test case to the master", Type.WARNING_MESSAGE);
			}
		});	
		
		return header;
	}

	public void setPageTitle(String t){
		title.setValue(t);

	}
	public String getPageTitle() {
		return title.getValue();
	}

    @Override
    public void enter(ViewChangeEvent event) {
        if (event.getParameters() == null
            || event.getParameters().isEmpty()) {
        	setPageTitle("Nothing to see here, just pass along.");
//            equalPanel.setContent(
//                new Label("Nothing to see here, " +
//                          "just pass along."));
            return;
        } else {
        	title.setValue(event.getParameters());
//        	AnimalViewer str = new AnimalViewer(event.getParameters());
//            setPageTitle(str.toString());
        }
    }

	
}

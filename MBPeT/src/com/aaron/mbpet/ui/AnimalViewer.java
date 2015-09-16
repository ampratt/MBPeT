package com.aaron.mbpet.ui;

import com.vaadin.annotations.DesignRoot;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.VerticalLayout;

@DesignRoot
public class AnimalViewer extends VerticalLayout {
    private static final long serialVersionUID = 572784347380517093L;

    Label watching = new Label();
    Embedded pic = new Embedded("this will have a pic");
    Label picLabel = new Label("this replaces a picture");
    Label back = new Label();
    
    public AnimalViewer(String animal) {
//        Design.read(this);
        
        watching.setValue("You are currently watching a " +
                          animal);
//        pic.setSource(new ThemeResource("img/" + animal + "-128px.png"));
        picLabel.setValue("here is your: " + animal);
        back.setValue("and " + animal +
            " is watching you back");
        
        addComponent(watching);
        addComponent(picLabel);
        addComponent(back);
    }
}


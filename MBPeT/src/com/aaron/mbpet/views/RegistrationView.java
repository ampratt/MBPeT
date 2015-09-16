package com.aaron.mbpet.views;

import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;

/** A start view for navigating to the main view */
public class RegistrationView extends VerticalLayout implements View {
    private static final long serialVersionUID = -3398565663865641952L;

    public static final String NAME = "registration";

    
    public RegistrationView() {		//final Navigator navigator
        setSizeFull();
        
//        LoginForm login = new LoginForm();
//        addComponent(login);
//        setComponentAlignment(login, Alignment.MIDDLE_CENTER);

        Button button = new Button("Create Account",
                new Button.ClickListener() {
            private static final long serialVersionUID = -1809072471885383781L;
            @Override
            public void buttonClick(ClickEvent event) {
                UI.getCurrent().getNavigator().navigateTo(LoginView.NAME);
            }
        });
        addComponent(button);
//        setComponentAlignment(button, Alignment.MIDDLE_CENTER);
    }        
    
    @Override
    public void enter(ViewChangeEvent event) {
        Notification.show("You need to create a user account", 
        		Notification.Type.ASSISTIVE_NOTIFICATION);
    }
}


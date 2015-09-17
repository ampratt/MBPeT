package com.aaron.mbpet.views;

import com.aaron.mbpet.domain.User;
import com.aaron.mbpet.ui.UserEditor;
import com.aaron.mbpet.ui.UserEditor.EditorSavedEvent;
import com.aaron.mbpet.ui.UserEditor.EditorSavedListener;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.data.util.BeanItem;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;

/** A start view for navigating to the main view */
public class RegistrationView extends HorizontalLayout implements View {
    private static final long serialVersionUID = -3398565663865641952L;

    public static final String NAME = "registration";
    private JPAContainer<User> persons;

    
    public RegistrationView() {		//final Navigator navigator
        setSizeFull();
        
//        LoginForm login = new LoginForm();
//        addComponent(login);
//        setComponentAlignment(login, Alignment.MIDDLE_CENTER);

        
        // using JPAContainer and custom editor form
        final BeanItem<User> newPersonItem = new BeanItem<User>(
                new User());
        UserEditor userEditor = new UserEditor(newPersonItem);
        userEditor.addListener(new EditorSavedListener() {
            @Override
            public void editorSaved(EditorSavedEvent event) {
                persons.addEntity(newPersonItem.getBean());
	            UI.getCurrent().getNavigator()
            		.navigateTo(LoginView.NAME +
            			 "/" + newPersonItem.getBean());
            }
        });
        addComponent(userEditor);
        

        // using FieldGroup to bind components to UserForm
		UserFormView userform = new UserFormView(persons);
		addComponent(userform);
		this.setComponentAlignment(userform, Alignment.MIDDLE_CENTER);
        
		
//        Button button = new Button("Create Account",
//                new Button.ClickListener() {
//            private static final long serialVersionUID = -1809072471885383781L;
//            @Override
//            public void buttonClick(ClickEvent event) {
//                UI.getCurrent().getNavigator().navigateTo(LoginView.NAME);
//            }
//        });
//        addComponent(button);
//        setComponentAlignment(button, Alignment.MIDDLE_CENTER);
    }        
    
    @Override
    public void enter(ViewChangeEvent event) {
        Notification.show("You need to create a user account", 
        		Notification.Type.ASSISTIVE_NOTIFICATION);
    }
}


package com.aaron.mbpet.views;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import javax.persistence.Query;

import com.aaron.mbpet.MbpetUI;
import com.aaron.mbpet.domain.User;
import com.aaron.mbpet.ui.PersonEditor;
import com.aaron.mbpet.ui.PersonEditor.EditorSavedEvent;
import com.aaron.mbpet.ui.PersonEditor.EditorSavedListener;
import com.aaron.mbpet.utils.PasswordValidator;
import com.aaron.mbpet.utils.UsernameValidator;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.jpacontainer.JPAContainerFactory;
import com.vaadin.data.Item;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.validator.AbstractValidator;
import com.vaadin.data.validator.EmailValidator;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.Position;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.themes.ValoTheme;
	
/** A start view for navigating to the main view */
public class LoginView extends VerticalLayout  implements View, Button.ClickListener {	//
    private static final long serialVersionUID = -3398565663865641952L;

    public static final String NAME = ""; 
    
    VerticalLayout layoutPanel;
    TextField username;
    PasswordField password;
    Button loginButton;
    private JPAContainer<User> persons;
    
	@Override
    public void enter(ViewChangeEvent event) {
		// TODO get user by userId passed from parameters and fetch from db
//		String createdUser = event.getParameters();
//		username.setValue(createdUser);

//        Notification.show("Welcome to the MBPeT design demo", 
//        			Notification.Type.TRAY_NOTIFICATION);
    }
	
    public LoginView() {
    	setSpacing(true);
    	setSizeFull();
    	this.addStyleName("login-background-grey");

        persons = JPAContainerFactory.make(User.class,
        		MbpetUI.PERSISTENCE_UNIT);
//        Component loginForm = buildLoginForm();
//        addComponent(loginForm);
//        setComponentAlignment(loginForm, Alignment.MIDDLE_CENTER);
        
final VerticalLayout loginPanel = new VerticalLayout();
        loginPanel.setSizeUndefined();
        loginPanel.setSpacing(true);
        loginPanel.addStyleName("login-panel");

        loginPanel.addComponent(buildLabels());
        loginPanel.addComponent(buildFields());
        loginPanel.addComponent(buildRegistrationFields());
        
        addComponent(loginPanel);
        setComponentAlignment(loginPanel, Alignment.MIDDLE_CENTER);
        welcomeNotification();

    }        

	private Component buildLabels() {
        HorizontalLayout labels = new HorizontalLayout();	//CssLayout
        labels.addStyleName("labels");
        labels.setWidth("100%");

        Label welcome = new Label("Welcome");
        welcome.setSizeUndefined();
        welcome.addStyleName(ValoTheme.LABEL_H4);
        welcome.addStyleName(ValoTheme.LABEL_COLORED);
        labels.addComponent(welcome);
        labels.setComponentAlignment(welcome, Alignment.TOP_LEFT);

        Label title = new Label("MBPeT Dashboard");
        title.setSizeUndefined();
        title.addStyleName(ValoTheme.LABEL_H3);
        title.addStyleName(ValoTheme.LABEL_LIGHT);
        labels.addComponent(title);
        labels.setComponentAlignment(title, Alignment.TOP_RIGHT);
        
        return labels;
    }
    

	public Component buildFields() {
        HorizontalLayout fields = new HorizontalLayout();
        fields.setSpacing(true);
        fields.addStyleName("fields");

        username = new TextField("Username");
        username.setIcon(FontAwesome.USER);
        username.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);
        username.addValidator(new UsernameValidator());
        username.setRequired(true);
//        username.setInputPrompt("Your username (eg. test@test.com)");
        username.setValue("jim.halpert");
//        username.addValidator(new EmailValidator(
//                "Username must be an email address"));
//        username.setInvalidAllowed(false);
        username.focus();
        
        password = new PasswordField("Password");
        password.setIcon(FontAwesome.LOCK);
        password.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);
        password.addValidator(new PasswordValidator());
        password.setRequired(true);
        password.setValue("passw0rd");
        password.setNullRepresentation("");
        
        
        loginButton = new Button("Sign In", this);	//, this
        loginButton.addStyleName(ValoTheme.BUTTON_PRIMARY);
        loginButton.setClickShortcut(KeyCode.ENTER);
//        loginButton.focus();

        fields.addComponents(username, password, loginButton);
        fields.setComponentAlignment(loginButton, Alignment.BOTTOM_LEFT);

//        loginButton.addClickListener(new Button.ClickListener() {
//            @Override
//            public void buttonClick(final ClickEvent event) {
//            	UI.getCurrent()
//    				.getNavigator()
//        				.navigateTo(MainView.NAME);// + "/" + "landingPage");
//            }
//        });
        		
        return fields;
    }


    private Component buildRegistrationFields() {
        HorizontalLayout fields = new HorizontalLayout();
        fields.setSpacing(true);
        fields.addStyleName("fields");
        fields.setWidth("100%");
        
        Button registerButton = new Button("Create account",
        		new Button.ClickListener() {
					@Override
					public void buttonClick(ClickEvent event) {
						//launch window to create user
		                final BeanItem<User> newPersonItem = new BeanItem<User>(
		                        new User());
		                PersonEditor personEditor = new PersonEditor(newPersonItem, "Create New User Account", false);
		                personEditor.addListener(new EditorSavedListener() {
		                    @Override
		                    public void editorSaved(EditorSavedEvent event) {
		                        persons.addEntity(newPersonItem.getBean());
		                    }
		                });
		                personEditor.setModal(true);
		                UI.getCurrent().addWindow(personEditor);
		                personEditor.center();
			            
			            //method 2 - separate registration page with fieldgroup
//						UI.getCurrent().getNavigator()
//		            			.navigateTo(RegistrationView.NAME);
					}
				});
        registerButton.addStyleName("link");
        fields.addComponent(registerButton);
        fields.setComponentAlignment(registerButton, Alignment.MIDDLE_RIGHT);
 		
 		return fields;
	}
    
    
    
    @Override
    public void buttonClick(ClickEvent event) {
        // Validate the fields using the navigator. By using validators for the
        // fields we reduce the amount of queries we have to use to the database
        // for wrongly entered passwords
        if (!username.isValid() || !password.isValid()) {
            return;
        }

        String usernameStr = username.getValue();
        String passwordStr = this.password.getValue();

        // Credentials were valid.
        // proceed to: Validate username and password with database here. 
        // username and password must match against db
        boolean isValid = false;
        EntityManager em = Persistence
					.createEntityManagerFactory("mbpet")
					.createEntityManager();	
        
        Query queryByUsername = em.createQuery(
    		    "SELECT OBJECT(u) FROM User u WHERE u.username = :username"
    		);
        queryByUsername.setParameter("username", username.getValue());
        User personById = new User();
        try {
        	personById = (User) queryByUsername.getSingleResult();
        	if (personById.getUsername().equals(usernameStr) && 
        			personById.getPassword().equals(passwordStr)) {
        		isValid = true;
//        		System.out.println("user associated with username is : " +
//        				personById.getFirstname() + personById.getLastname() );
        	} else {
        		Notification.show("Login Failure", "Password was incorrect. \nPlease try again.", Type.WARNING_MESSAGE);
        		return;
        	}
        } catch (NoResultException e) {
        	Notification.show("Login Failure", "No such user exists. \nPlease try your username/password again.", Type.WARNING_MESSAGE);
        }
//        boolean isValid = true;//usernameStr.equals("jim.halpert")
//                && password.equals("passw0rd");	//passw0rd
       
        if (isValid) {

        	// store current user in session attribute        	
        	getSession().setAttribute("sessionUser", personById);
        	
        	// Item object to store session user
        	BeanItem<User> personItemById = new BeanItem<User>((User) queryByUsername.getSingleResult());	
        	getSession().setAttribute("sessionUserItem", personItemById);
        	
        	System.out.println("the query gave this: " +
        			personById + "\n" + personById.getUsername() +
    				"\n" + personById.getFirstname() +
    				"\n" + personById.getLastname());
        	
        	System.out.println("session user object: " + getSession().getAttribute("sessionUser").toString());
       	
            // Store the current username in the service session
            getSession().setAttribute("user", usernameStr);

            // Navigate to main view
            UI.getCurrent().getNavigator().navigateTo(MainView.NAME + "/" + "landingPage");	//SimpleLoginMainView.NAME

        } else {
            // some error happened
//            this.password.setValue(null);
            username.focus();	//this.password

        }
    }
    
    
    
	private void welcomeNotification() {
        // welcome notification
        Notification notification = new Notification(
                "Welcome to Dashboard Demo");
        notification
                .setDescription("<span>This application is not real, it only demonstrates an application built with the <a href=\"https://vaadin.com\">Vaadin framework</a>.</span> <span>No username or password is required, just click the <b>Sign In</b> button to continue.</span>");
        notification.setHtmlContentAllowed(true);
        notification.setStyleName("tray dark small closable login-help");
        notification.setPosition(Position.BOTTOM_CENTER);
        notification.setDelayMsec(20000);
//        notification.show(Page.getCurrent());
	}

	
	
//  // Validator for validating the passwords
//  private static final class PasswordValidator extends
//          AbstractValidator<String> {
//
//      public PasswordValidator() {
//          super("The password provided is not valid");
//      }
//
//      @Override
//      protected boolean isValidValue(String value) {
//          // Password must be at least 8 characters long and contain at least one number
//          if (value != null
//                  && (value.length() < 8 || !value.matches(".*\\d.*"))) {
//              return false;
//          }
//          return true;
//      }
//
//      @Override
//      public Class<String> getType() {
//          return String.class;
//      }
//  }
    
}

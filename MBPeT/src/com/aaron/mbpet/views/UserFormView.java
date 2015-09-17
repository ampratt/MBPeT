package com.aaron.mbpet.views;

import com.aaron.mbpet.domain.User;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.data.util.BeanItem;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public class UserFormView extends HorizontalLayout {

	public UserFormView(){
		setMargin(true);
		setWidth("100%");

		addComponent(ManualLayoutDesign());
		addComponent(AutoGeneratedLayoutDesign());
	}
	
	/**
	 * If you know the data structure behind, define things manually for more control
	 * @return
	 */
	private Component ManualLayoutDesign() {
		final VerticalLayout layout = new VerticalLayout();
//		layout.setMargin(true);
//		setContent(layout);
		
		layout.addComponent(new Label("<h2>Create new user account.</h2>", 
				ContentMode.HTML));
		
		final User user = new User();
		user.setFirstName("Jim");
		user.setLastName("Halpert");
		user.setUsername("jim.halpert");
		user.setPassword("passw0rd");
		user.setOrganization("AA");
//		person.setEmail("john.smith@eample.com");
		
//		Address address = new Address();
//		address.setStreet("Main street");
//		address.setCity("Turku");
//		address.setZip("123456");
//		person.setAddress(address);
		
		// empty bean for testing
//		person = new Person();
//		person.setAddress(new Address());
		
		// CREATE FIELDS MANUALLY
		final UserForm form = new UserForm();
		layout.addComponent(form);
		
		final FieldGroup binder = new FieldGroup();
		BeanItem<User> item = new BeanItem<User>(user);		// takes item as argument
//		item.addNestedProperty("address.street");	// Address info is not person but address to which person is linked
//		item.addNestedProperty("address.zip");
//		item.addNestedProperty("address.city");
		
		binder.setItemDataSource(item); 	// link to data model to binder
		
		binder.bindMemberFields(form);	// link to layout		
		
		// button layout
		HorizontalLayout buttons = new HorizontalLayout();
//		buttons.setWidth("100%");
		layout.addComponent(buttons);
		
		Button submitButton = new Button("Submit");
		submitButton.addStyleName(ValoTheme.BUTTON_PRIMARY);
//		submitButton.setClickShortcut(KeyCode.ENTER);
		submitButton.addClickListener(new Button.ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				try {
					form.enableValidationMessages();
					binder.commit();


				
	                UI.getCurrent().getNavigator()
	                	.navigateTo(LoginView.NAME +
	                			 "/" + user.getId());
	                Notification.show("User successfully created: " +
	                		"\nusername: " + user.GetUsername() +
	                		"\npassword: " + user.getPassword() +
	                		"\nuser object id" + user.getId(),
	                		Type.TRAY_NOTIFICATION);
				} catch (CommitException e) {
					e.printStackTrace();
				}				
			}
		});
		
		Button cancelButton = new Button("Cancel");
		cancelButton.addClickListener(new Button.ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
                UI.getCurrent().getNavigator().navigateTo(LoginView.NAME);			
			}
		});
		
		buttons.addComponents(submitButton, cancelButton);
		buttons.setComponentAlignment(submitButton, Alignment.MIDDLE_LEFT);
		buttons.setComponentAlignment(cancelButton, Alignment.MIDDLE_RIGHT);
		
//		layout.addComponent(new Button("save", new Button.ClickListener() {
//			@Override
//			public void buttonClick(ClickEvent event) {
//				try {
//					form.enableValidationMessages();
//					binder.commit();
//				} catch (CommitException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				
//			}
//		}));
		
		return layout;
	}
	
	
	
	private Component AutoGeneratedLayoutDesign() {
		final VerticalLayout layout = new VerticalLayout();
		layout.setMargin(true);
//		setContent(layout);
		
		layout.addComponent(new Label("<h2>Auto-Generated field creation</h2>", ContentMode.HTML));

		final User user = new User();
		user.setFirstName("Pam");
		user.setLastName("Halpert");
		user.setUsername("pam.halpert");
		user.setPassword("passw0rd");
		user.setOrganization("AA");
//		user.setEmail("john.smith@eample.com");
		
//		Address address = new Address();
//		address.setStreet("Main street");
//		address.setCity("Turku");
//		address.setZip("123456");
//		user.setAddress(address);
		
		final FieldGroup binder = new FieldGroup();
		BeanItem<User> item = new BeanItem<User>(user);		// takes item as argument
//		item.addNestedProperty("address.street");	// Address info is not person but address to which person is linked
//		item.addNestedProperty("address.zip");
//		item.addNestedProperty("address.city");
		
		binder.setItemDataSource(item); 	// link to data model to binder
		
//		binder.bindMemberFields(form);	// link to layout

		// GENERATE FIELDS
		
//		for (Object propertyId : item.getItemPropertyIds()) {
////			if(!"address".equals(propertyId)) {
//			if("password".equals(propertyId)) {
//				PasswordField field = new PasswordField();
//				binder.bind(field, propertyId);
//				layout.addComponent(field);					
//			} else {
//				Field field = binder.buildAndBind(propertyId);
//				layout.addComponent(field);							
//			}
//		}
		
		// using buildAndBind()
//		Field field = binder.buildAndBind("firstname");
//		layout.addComponent(field);
//		
//		// using bind() to determine what type of field is created yourself...
//		TextField unamefield = new TextField();
//		binder.bind(unamefield, "username");
//		layout.addComponent(unamefield);
		
		PasswordField pfield = new PasswordField();
		binder.bind(pfield, "password"	);
		layout.addComponent(pfield);	
		
		layout.addComponent(new Button("save", new Button.ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				try {
					binder.commit();
				} catch (CommitException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		}));
		
		return layout;
	}
}

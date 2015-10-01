package com.aaron.mbpet.views.users;

import com.aaron.mbpet.domain.User;
import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.data.validator.BeanValidator;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;

public class UserForm extends GridLayout {

	@PropertyId("firstname")
	private TextField firstname = new TextField("First Name");
	
	@PropertyId("lastname")
	private TextField lastname = new TextField("Last Name");

	@PropertyId("username")
	private TextField username = new TextField("Username");

	@PropertyId("password")
	private PasswordField password = new PasswordField("Password");

	@PropertyId("organization")
	private TextField organization = new TextField("optional");

//	@PropertyId("email")
//	private TextField email = new TextField("Email address");
	
//	@PropertyId("address.street")
//	private TextField street = new TextField("Street");
//
//	@PropertyId("address.zip")
//	private TextField zip = new TextField("Zip");
//	
//	@PropertyId("address.city")
//	private TextField city = new TextField("City");
	
	public UserForm(){
		super(2,4);
		
		firstname.addValidator(new BeanValidator(User.class, "firstname"));
		lastname.addValidator(new BeanValidator(User.class, "lastname"));
		username.addValidator(new BeanValidator(User.class, "username"));
		password.addValidator(new BeanValidator(User.class, "password"));
//		email.addValidator(new BeanValidator(User.class, "email"));
		
		firstname.focus();
		username.setWidth("100%");
		password.setWidth("100%");
		organization.setWidth("100%");
//		organization.setCaption("optional");
		
		disableValidationMessages();

		firstname.setNullRepresentation("");
		lastname.setNullRepresentation("");
		username.setNullRepresentation("");
		password.setNullRepresentation("");
		organization.setNullRepresentation("");
		
		addComponent(firstname);
		addComponent(lastname);
		addComponent(username, 0,1,1,1);
		addComponent(password, 0,2,1,2);
		addComponent(organization, 0,3,1,3);
	}

	public void disableValidationMessages() {
		firstname.setValidationVisible(false);
		lastname.setValidationVisible(false);
		username.setValidationVisible(false);
		password.setValidationVisible(false);		
	}
	
	public void enableValidationMessages() {
		firstname.setValidationVisible(true);
		lastname.setValidationVisible(true);
		username.setValidationVisible(true);		
		password.setValidationVisible(true);		
	}
	
	
}

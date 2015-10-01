package com.aaron.mbpet.components;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import javax.persistence.Query;

import com.aaron.mbpet.domain.User;
import com.vaadin.data.validator.AbstractValidator;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;

// Validator for validating the passwords
public final class UsernameValidator extends
        AbstractValidator<String> {

	static String message = "Username must be more than 4 characters";
	
    public UsernameValidator() {
        super("Username must be more bewteen 4 and 30 characters");
    }

    @Override
    protected boolean isValidValue(String value) {
        // Password must be at least 6 characters long and contain at least one number
    	
        if (value != null  && (value.length() < 4 || value.length() > 30)) {
            return false;
        } else if (value == null) return false;
//        
//        EntityManager em = Persistence
//				.createEntityManagerFactory("mbpet")
//				.createEntityManager();	
//        
//	    Query queryByUsername = em.createQuery(
//			    "SELECT OBJECT(u) FROM User u WHERE u.username = :username"
//			);
//	    queryByUsername.setParameter("username", value);
//	    User personById = new User();
//	    try {
//	    	personById = (User) queryByUsername.getSingleResult();
//	    	if (personById.getUsername().equals(value) ) {
////	    		isValid = true;
//	    		return true;
//	//    		System.out.println("user associated with username is : " +
//	//    				personById.getFirstname() + personById.getLastname() );
//	    	} else {
//	    		message = "Password was incorrect. Please try again.";
////	    		Notification.show("Login Failure", "Password was incorrect. \nPlease try again.", Type.WARNING_MESSAGE);
//	    		return false;
//	    	}
//	    } catch (NoResultException e) {
//	    	message = "No such user exists. Please try your username/password again.";
////	    	Notification.show("Login Failure", "No such user exists. \nPlease try your username/password again.", Type.WARNING_MESSAGE);
//	    }
		return true;
	    
    }

    @Override
    public Class<String> getType() {
        return String.class;
    }
}

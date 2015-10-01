package com.aaron.mbpet.components;

import com.vaadin.data.validator.AbstractValidator;

// Validator for validating the passwords
public final class PasswordValidator extends
        AbstractValidator<String> {

    public PasswordValidator() {
        super("Password must contain 6 characters and at least 1 number");
    }

    @Override
    protected boolean isValidValue(String value) {
        // Password must be at least 6 characters long and contain at least one number
        if (value != null
                && (value.length() < 6 || !value.matches(".*\\d.*"))) {
            return false;
        }
        return true;
    }

    @Override
    public Class<String> getType() {
        return String.class;
    }
}
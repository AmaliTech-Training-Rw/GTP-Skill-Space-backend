package com.skillspace.user.util.customValidation;


import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ContactValidator implements ConstraintValidator<ValidContact, String> {
    @Override
    public boolean isValid(String contact, ConstraintValidatorContext context) {
        // Define the regex pattern for a valid contact number
        return contact != null && contact.matches("^\\+?[0-9. ()-]{7,}$");
    }
}

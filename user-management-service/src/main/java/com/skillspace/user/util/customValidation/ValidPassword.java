package com.skillspace.user.util.customValidation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PasswordValidator.class)
public @interface ValidPassword {
    String message() default "Invalid password. Must be at least 8 characters long and include a mix of uppercase, lowercase, and special characters.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}


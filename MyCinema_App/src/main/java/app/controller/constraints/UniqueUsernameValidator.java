package app.controller.constraints;

import app.database.exception.NullParameterPassed;
import app.database.service.IRepository;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UniqueUsernameValidator implements ConstraintValidator<UniqueUsername, String> {

    @Autowired
    IRepository service;

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        boolean isUsernameInUse = false;
        try {
            return value != null && !service.isUsernameAlreadyInUse(value);
        } catch (NullParameterPassed nullParameterPassed) {
            nullParameterPassed.printStackTrace();
        }

        return value != null && isUsernameInUse;
    }

}
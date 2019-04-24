package app.controller.constraints;

import app.DatabasePop;
import app.database.exception.NullParameterPassed;
import app.database.service.IRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
          logger.warn("Null parameter passed");
        }

        return value != null && isUsernameInUse;
    }

    private static final Logger logger = LoggerFactory.getLogger(DatabasePop.class);

}
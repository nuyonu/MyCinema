package app.controller.constraints;

import app.database.infrastructure.IRepositoryUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UniqueUsernameValidator implements ConstraintValidator<UniqueUsername, String> {

    @Autowired
    IRepositoryUser repositoryUser;

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value != null && repositoryUser.findByUsername(value) != null;
    }

    private static final Logger logger = LoggerFactory.getLogger(UniqueUsernameValidator.class);

}
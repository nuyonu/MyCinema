package app.controller.constraints;

import app.database.infrastructure.IRepositoryUser;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UniqueEmailValidator implements ConstraintValidator<UniqueEmail, String> {
    @Autowired
    IRepositoryUser repositoryUser;

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value != null && repositoryUser.findByEmail(value) == null;
    }
}
package App.Controller.Constraints;

import javax.validation.*;

import App.Database.Exception.NullParameterPassed;
import App.Database.Service.IRepository;
import org.springframework.beans.factory.annotation.Autowired;

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
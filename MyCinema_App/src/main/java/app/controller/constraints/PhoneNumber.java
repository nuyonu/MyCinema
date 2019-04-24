package app.controller.constraints;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;

@Constraint(validatedBy = PhoneNumberValidator.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({TYPE, FIELD, ANNOTATION_TYPE, PARAMETER})
public @interface PhoneNumber {

    String message() default "This is not valid phone number!";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
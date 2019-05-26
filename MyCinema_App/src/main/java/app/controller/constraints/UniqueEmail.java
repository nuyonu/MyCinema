package app.controller.constraints;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;

@Constraint(validatedBy = UniqueEmailValidator.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({ TYPE, FIELD, ANNOTATION_TYPE })
public @interface UniqueEmail {
    String message() default "There is already user with this email!";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
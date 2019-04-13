package App.Controller.Constraints;

import java.lang.annotation.*;
import javax.validation.*;

import static java.lang.annotation.ElementType.*;

@Constraint(validatedBy = UniqueUsernameValidator.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({ TYPE, FIELD, ANNOTATION_TYPE })
public @interface UniqueUsername {

    public String message() default "There is already user with this username!";

    public Class<?>[] groups() default {};

    public Class<? extends Payload>[] payload() default{};

}
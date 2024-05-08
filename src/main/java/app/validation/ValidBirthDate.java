package app.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = BirthDateValidator.class)
public @interface ValidBirthDate {
    String message() default "From date must be less than To date";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    String fromBirthDate();
    String toBirthDate();
}

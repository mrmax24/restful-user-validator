package app.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.lang.reflect.Field;
import java.time.LocalDate;

public class BirthDateValidator implements ConstraintValidator<ValidBirthDate, Object> {
    private String fromBirthDate;
    private String toBirthDate;

    @Override
    public void initialize(ValidBirthDate constraintAnnotation) {
        fromBirthDate = constraintAnnotation.fromBirthDate();
        toBirthDate = constraintAnnotation.toBirthDate();
    }

    @Override
    public boolean isValid(Object date, ConstraintValidatorContext context) {
        try {
            Field fromField = date.getClass().getDeclaredField(fromBirthDate);
            fromField.setAccessible(true);
            LocalDate fromValue = (LocalDate) fromField.get(date);

            Field toField = date.getClass().getDeclaredField(toBirthDate);
            toField.setAccessible(true);
            LocalDate toValue = (LocalDate) toField.get(date);

            return fromValue == null || toValue == null || fromValue.isBefore(toValue);
        } catch (NoSuchFieldException e) {
            throw new IllegalArgumentException("Field not found: " + e.getMessage());
        } catch (IllegalAccessException e) {
            throw new IllegalArgumentException("Field access error: " + e.getMessage());
        }
    }
}

package app.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import org.springframework.beans.factory.annotation.Value;

public class MinAgeValidator implements ConstraintValidator<MinAge, LocalDate> {
    @Value("${app.user.minAge}")
    private int minAge;

    @Override
    public boolean isValid(LocalDate birthDateValue, ConstraintValidatorContext context) {
        if (birthDateValue == null) {
            return true;
        }
        LocalDate today = LocalDate.now();
        LocalDate minBirthDate = today.minusYears(minAge);
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(
                        "You must be at least " + minAge + " years old to register on this website")
                .addConstraintViolation();
        return !birthDateValue.isAfter(minBirthDate);
    }
}

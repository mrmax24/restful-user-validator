package app.dto;

import app.validation.ValidBirthDate;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@ValidBirthDate(fromBirthDate = "fromBirthDate", toBirthDate = "toBirthDate")
public class DateRangeDto {
    private LocalDate fromBirthDate;
    private LocalDate toBirthDate;
}

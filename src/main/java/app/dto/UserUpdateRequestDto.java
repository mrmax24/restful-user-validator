package app.dto;

import app.validation.MinAge;
import jakarta.validation.constraints.Email;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserUpdateRequestDto {
    @Email
    private String email;
    private String firstName;
    private String lastName;
    @MinAge
    private LocalDate birthDate;
    private String address;
    private String phoneNumber;

    public UserUpdateRequestDto(String email) {
        this.email = email;
    }
}

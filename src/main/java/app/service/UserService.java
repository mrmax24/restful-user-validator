package app.service;

import app.dto.DateRangeDto;
import app.dto.UserRequestDto;
import app.dto.UserResponseDto;
import app.dto.UserUpdateRequestDto;
import java.util.List;

public interface UserService {
    UserResponseDto createUser(UserRequestDto requestDto);

    UserResponseDto updateUser(Long id, UserRequestDto requestDto);

    UserResponseDto updateUserFields(Long id, UserUpdateRequestDto requestDto);

    List<UserResponseDto> searchByBirthDateRange(DateRangeDto dateRangeDto);

    void deleteUser(Long id);
}

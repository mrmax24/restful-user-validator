package app.controller;

import app.dto.DataWrapper;
import app.dto.DateRangeDto;
import app.dto.UserRequestDto;
import app.dto.UserResponseDto;
import app.dto.UserUpdateRequestDto;
import app.service.UserService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PutMapping("/{id}")
    public DataWrapper<UserResponseDto> updateUser(@PathVariable Long id,
                                      @RequestBody @Valid UserRequestDto userRequestDto) {
        UserResponseDto updatedUser = userService.updateUser(id, userRequestDto);
        DataWrapper<UserResponseDto> response = new DataWrapper<>(updatedUser);
        response.addMetadata("message", "User is updated successfully");
        return response;
    }

    @PatchMapping("/{id}")
    public DataWrapper<UserResponseDto> updateUserFields(
            @PathVariable Long id, @RequestBody @Valid UserUpdateRequestDto userRequestDto) {
        UserResponseDto updatedUser = userService.updateUserFields(id, userRequestDto);
        DataWrapper<UserResponseDto> response = new DataWrapper<>(updatedUser);
        response.addMetadata("message", "User is updated successfully");
        return response;
    }

    @DeleteMapping("/{id}")
    public DataWrapper<String> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        DataWrapper<String> response = DataWrapper.empty();
        response.addMetadata("message", "User is deleted successfully");
        return response;
    }

    @GetMapping
    public DataWrapper<List<UserResponseDto>> searchByBirthDateRange(
            @ModelAttribute @Valid DateRangeDto dateRangeDto) {
        List<UserResponseDto> userResponseDtos = userService.searchByBirthDateRange(dateRangeDto);

        List<UserResponseDto> transformedList = userResponseDtos.stream()
                .map(userDto -> new UserResponseDto(userDto.getId(), userDto.getEmail(),
                        userDto.getFirstName(), userDto.getLastName(), userDto.getBirthDate(),
                        userDto.getAddress(), userDto.getPhoneNumber()))
                .collect(Collectors.toList());

        DataWrapper<List<UserResponseDto>> listDataWrapper = new DataWrapper<>(transformedList);
        listDataWrapper.addMetadata("message", "Users born between"
                + dateRangeDto.getFromBirthDate() + " and " + dateRangeDto.getToBirthDate());
        return listDataWrapper;
    }
}

package app.controller;

import app.dto.DataWrapper;
import app.dto.UserRequestDto;
import app.dto.UserResponseDto;
import app.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final UserService userService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public DataWrapper<UserResponseDto> createUser(@RequestBody @Valid
                                                       UserRequestDto userRequestDto) {
        UserResponseDto userResponseDto = userService.createUser(userRequestDto);
        DataWrapper<UserResponseDto> response = new DataWrapper<>(userResponseDto);
        response.addMetadata("message", "User is created successfully");
        return response;
    }
}

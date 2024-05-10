package app.service.impl;

import app.dto.DateRangeDto;
import app.dto.UserRequestDto;
import app.dto.UserResponseDto;
import app.dto.UserUpdateRequestDto;
import app.exception.RegistrationException;
import app.mapper.UserMapper;
import app.mapper.UserUpdateMapper;
import app.model.User;
import app.service.UserService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final Map<Long, User> userMap = new HashMap<>();
    private final UserMapper userMapper;
    private final UserUpdateMapper userUpdateMapper;
    private final AtomicLong userIdCounter = new AtomicLong(1);

    @Override
    public UserResponseDto createUser(UserRequestDto requestDto) {
        checkIfUserRegistered(requestDto.getEmail());
        User user = userMapper.toModel(requestDto);
        Long increment = getIncrement();
        userMap.put(increment, user);
        UserResponseDto userDto = userMapper.toDto(user);
        userDto.setId(increment);
        return userDto;
    }

    @Override
    public UserResponseDto updateUser(Long id, UserRequestDto requestDto) {
        User user = userMapper.toModel(requestDto);
        userMap.put(id, user);
        User userFromStorage = userMap.get(id);
        UserResponseDto userDto = userMapper.toDto(userFromStorage);
        userDto.setId(id);
        return userDto;
    }

    @Override
    public UserResponseDto updateUserFields(Long id, UserUpdateRequestDto requestDto) {
        User user = userMap.get(id);
        userUpdateMapper.updateUserFromDto(requestDto, user);
        userMap.put(id, user);
        UserResponseDto userDto = userMapper.toDto(user);
        userDto.setId(id);
        return userDto;
    }

    @Override
    public List<UserResponseDto> searchByBirthDateRange(DateRangeDto dateRangeDto) {
        List<UserResponseDto> userDtos = new ArrayList<>();
        userMap.forEach((id, user) -> {
            if (dateRangeDto.getFromBirthDate() == null || dateRangeDto.getToBirthDate() == null) {
                saveUser(id, user, userDtos);
                return;
            }
            if (user.getBirthDate().plusDays(1).isAfter(dateRangeDto.getFromBirthDate())
                    && user.getBirthDate().minusDays(1)
                    .isBefore(dateRangeDto.getToBirthDate())) {
                saveUser(id, user, userDtos);
            }
        });
        return userDtos;
    }

    @Override
    public void deleteUser(Long id) {
        userMap.remove(id);
    }

    private Long getIncrement() {
        return userIdCounter.getAndIncrement();
    }

    private void saveUser(Long userId, User user, List<UserResponseDto> userDtos) {
        UserResponseDto userDto = userMapper.toDto(user);
        userDto.setId(userId);
        userDtos.add(userDto);
    }

    private void checkIfUserRegistered(String email) {
        userMap.entrySet().stream()
                .filter(entry -> entry.getValue().getEmail().equals(email))
                .forEach(entry -> {
                    throw new RegistrationException("Can't register user "
                            + "with email " + email + ", this user already exists.");
                });
    }
}

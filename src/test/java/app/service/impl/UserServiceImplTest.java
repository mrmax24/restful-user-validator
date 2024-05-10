package app.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

import app.dto.DateRangeDto;
import app.dto.UserRequestDto;
import app.dto.UserResponseDto;
import app.dto.UserUpdateRequestDto;
import app.mapper.UserMapper;
import app.mapper.UserUpdateMapper;
import app.model.User;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

public class UserServiceImplTest {

    @Mock
    private UserMapper userMapper;

    @Mock
    private UserUpdateMapper userUpdateMapper;

    @InjectMocks
    private UserServiceImpl userService;
    private Long userId;
    private UserRequestDto requestDto;
    private Map<Long, User> userMap;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        this.userMap = Mockito.mock(Map.class);
        ReflectionTestUtils.setField(userService, "userMap", userMap);

        userId = 1L;
        requestDto = new UserRequestDto(
                "johndoe@example.com",
                "John",
                "Doe",
                LocalDate.of(1990, 1, 1),
                "123 Street",
                "+1234567890");
    }

    @Test
    public void testCreateUser() {
        User mockedUser = new User();
        mockedUser.setEmail(requestDto.getEmail());
        mockedUser.setFirstName(requestDto.getFirstName());
        mockedUser.setLastName(requestDto.getLastName());
        mockedUser.setBirthDate(requestDto.getBirthDate());
        mockedUser.setAddress(requestDto.getAddress());
        mockedUser.setPhoneNumber(requestDto.getPhoneNumber());

        UserResponseDto mockUserResponseDto = new UserResponseDto(1L,
                requestDto.getEmail(),
                requestDto.getFirstName(),
                requestDto.getLastName(),
                requestDto.getBirthDate(),
                requestDto.getAddress(),
                requestDto.getPhoneNumber());

        when(userMapper.toModel(any(UserRequestDto.class))).thenReturn(mockedUser);
        when(userMapper.toDto(any(User.class))).thenReturn(mockUserResponseDto);

        UserResponseDto responseDto = userService.createUser(requestDto);

        assertNotNull(responseDto);
        assertEquals(requestDto.getEmail(), responseDto.getEmail());
        assertEquals(requestDto.getFirstName(), responseDto.getFirstName());
        assertEquals(requestDto.getLastName(), responseDto.getLastName());
        assertEquals(requestDto.getBirthDate(), responseDto.getBirthDate());
        assertEquals(requestDto.getAddress(), responseDto.getAddress());
        assertEquals(requestDto.getPhoneNumber(), responseDto.getPhoneNumber());
    }

    @Test
    public void testUpdateUser() {
        User mockedUser = new User();
        mockedUser.setEmail(requestDto.getEmail());
        mockedUser.setFirstName(requestDto.getFirstName());
        mockedUser.setLastName(requestDto.getLastName());
        mockedUser.setBirthDate(requestDto.getBirthDate());
        mockedUser.setAddress(requestDto.getAddress());
        mockedUser.setPhoneNumber(requestDto.getPhoneNumber());
        userMap.put(1L, mockedUser);

        when(userMapper.toModel(any(UserRequestDto.class))).thenReturn(mockedUser);
        Mockito.when(userMap.get(userId)).thenReturn(mockedUser);
        when(userMapper.toDto(any(User.class))).thenAnswer(invocation -> {
            User arg = invocation.getArgument(0);
            return new UserResponseDto(userId, arg.getEmail(), arg.getFirstName(),
                    arg.getLastName(), arg.getBirthDate(), arg.getAddress(), arg.getPhoneNumber());
        });

        UserResponseDto updatedUser = userService.updateUser(userId, requestDto);

        assertNotNull(updatedUser);
        assertEquals(userId, updatedUser.getId());
        assertEquals(requestDto.getEmail(), updatedUser.getEmail());
        assertEquals(requestDto.getFirstName(), updatedUser.getFirstName());
        assertEquals(requestDto.getLastName(), updatedUser.getLastName());
        assertEquals(requestDto.getBirthDate(), updatedUser.getBirthDate());
        assertEquals(requestDto.getAddress(), updatedUser.getAddress());
        assertEquals(requestDto.getPhoneNumber(), updatedUser.getPhoneNumber());
    }

    @Test
    public void testUpdateUserFields() {
        User userInMap = new User();
        userInMap.setEmail("oldemail@example.com");
        userInMap.setFirstName("Old First Name");
        userInMap.setLastName("Old Last Name");
        userInMap.setBirthDate(LocalDate.of(1985, 12, 31));
        userInMap.setAddress("Old Address");
        userInMap.setPhoneNumber("+9876543210");
        userMap.put(userId, userInMap);

        UserResponseDto responseDto = new UserResponseDto(userId, requestDto.getEmail(),
                userInMap.getFirstName(), userInMap.getLastName(), userInMap.getBirthDate(),
                userInMap.getAddress(), userInMap.getPhoneNumber());

        UserUpdateRequestDto requestDto = new UserUpdateRequestDto("new_email@gmail.com");

        Mockito.when(userMap.get(userId)).thenReturn(userInMap);
        Mockito.doNothing().when(userUpdateMapper).updateUserFromDto(requestDto, userInMap);
        Mockito.when(userMapper.toDto(userInMap)).thenReturn(responseDto);

        UserResponseDto actualResponseDto = userService.updateUserFields(userId, requestDto);

        assertNotNull(actualResponseDto);
        assertEquals(responseDto, actualResponseDto);
    }

    @Test
    public void testSearchByBirthDateRange() {
        User user1 = new User();
        user1.setEmail("johndoe@example.com");
        user1.setFirstName("John");
        user1.setLastName("Doe");
        user1.setBirthDate(LocalDate.of(1990, 1, 1));
        user1.setAddress("123 Street");
        user1.setPhoneNumber("+1234567890");

        User user2 = new User();
        user2.setEmail("janedoe@example.com");
        user2.setFirstName("Alice");
        user2.setLastName("Doe");
        user2.setBirthDate(LocalDate.of(1985, 5, 15));
        user2.setAddress("456 Avenue");
        user2.setPhoneNumber("+9876543210");

        userMap.put(1L, user1);
        userMap.put(2L, user2);

        Mockito.when(userMap.get(1L)).thenReturn(user1);
        Mockito.when(userMap.get(2L)).thenReturn(user2);

        DateRangeDto dateRangeDto = new DateRangeDto(LocalDate.of(1996, 1, 1),
                LocalDate.of(1997, 12, 31));
        List<UserResponseDto> actualUsers = userService.searchByBirthDateRange(dateRangeDto);

        assertEquals(0, actualUsers.size());
    }

    @Test
    public void testDeleteUser() {
        User user = new User();
        userMap.put(userId, user);
        userService.deleteUser(userId);
        assertFalse(userMap.containsKey(userId));
    }
}

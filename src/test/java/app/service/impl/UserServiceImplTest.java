package app.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

import app.dto.UserRequestDto;
import app.dto.UserResponseDto;
import app.mapper.UserMapper;
import app.mapper.UserUpdateMapper;
import app.model.User;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class UserServiceImplTest {

    @Mock
    private UserMapper userMapper;

    @Mock
    private UserUpdateMapper userUpdateMapper;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateUser() {
        UserRequestDto requestDto = new UserRequestDto(
                "johndoe@example.com",
                "John",
                "Doe",
                LocalDate.of(1990, 1, 1),
                "123 Street",
                "+1234567890");

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
}

package app.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import app.dto.UserRequestDto;
import app.dto.UserResponseDto;
import app.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@WebMvcTest(AuthenticationController.class)
public class AuthenticationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @Test
    public void whenValidUserDto_thenCreateUserAndReturn201() throws Exception {
        UserRequestDto requestDto = new UserRequestDto(
                "johndoe@example.com",
                "John",
                "Doe",
                LocalDate.of(1990, 1, 1),
                "123 Street",
                "+1234567890"
        );
        UserResponseDto mockUserResponseDto = new UserResponseDto(1L,
                requestDto.getEmail(),
                requestDto.getFirstName(),
                requestDto.getLastName(),
                requestDto.getBirthDate(),
                requestDto.getAddress(),
                requestDto.getPhoneNumber());

        doReturn(mockUserResponseDto).when(userService).createUser(any(UserRequestDto.class));

        ResultActions resultActions = mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)));

        resultActions.andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.email").value(requestDto.getEmail()))
                .andExpect(jsonPath("$.data.firstName").value(requestDto.getFirstName()))
                .andExpect(jsonPath("$.data.lastName").value(requestDto.getLastName()))
                .andExpect(jsonPath("$.data.birthDate")
                        .value(requestDto.getBirthDate().toString()))
                .andExpect(jsonPath("$.data.address").value(requestDto.getAddress()))
                .andExpect(jsonPath("$.data.phoneNumber").value(requestDto.getPhoneNumber()));
    }

    @Test
    public void whenRequiredFieldIsEmpty_thenValidationErrors() throws Exception {
        UserRequestDto invalidUserRequestDto = new UserRequestDto(
                "johndoe@example.com",
                "",
                "Doe",
                LocalDate.of(1997, 1, 1),
                "123 Street",
                "+1234567890");
        getResultActions(invalidUserRequestDto)
                .andExpect(status().isBadRequest());
    }

    @Test
    public void whenNotEmptyOptionalFieldIsEmpty_thenValidationErrors() throws Exception {
        UserRequestDto invalidUserRequestDto = new UserRequestDto(
                "johndoe@example.com",
                "John",
                "Doe",
                LocalDate.of(1997, 1, 1),
                "",
                "");
        getResultActions(invalidUserRequestDto)
                .andExpect(status().isCreated());
    }

    @Test
    public void whenEmailIsInvalid_thenValidationErrors() throws Exception {
        UserRequestDto invalidUserRequestDto = new UserRequestDto(
                "invalid-email",
                "John",
                "Doe",
                LocalDate.of(1997, 1, 1),
                "123 Street",
                "+1234567890");
        getResultActions(invalidUserRequestDto)
                .andExpect(status().isBadRequest());
    }

    @Test
    public void whenBirthDateIsInvalid_thenValidationErrors() throws Exception {
        UserRequestDto invalidUserRequestDto = new UserRequestDto(
                "johndoe@example.com",
                "John",
                "Doe",
                LocalDate.of(2008, 1, 1),
                "123 Street",
                "+1234567890");
        getResultActions(invalidUserRequestDto)
                .andExpect(status().isBadRequest());
    }

    private ResultActions getResultActions(UserRequestDto requestDto) throws Exception {
        return mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)));
    }
}

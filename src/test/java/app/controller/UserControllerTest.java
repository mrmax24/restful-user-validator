package app.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import app.dto.DateRangeDto;
import app.dto.UserRequestDto;
import app.dto.UserResponseDto;
import app.dto.UserUpdateRequestDto;
import app.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @Test
    public void whenValidUserDto_thenUpdateUserAndReturn200() throws Exception {
        Long userId = 1L;
        UserRequestDto requestDto = new UserRequestDto(
                "johndoe@example.com",
                "John",
                "Doe",
                LocalDate.of(1997, 1, 1),
                "123 Street",
                "1234567890");

        UserResponseDto mockUserResponseDto = new UserResponseDto(userId,
                requestDto.getEmail(), requestDto.getFirstName(),
                requestDto.getLastName(), requestDto.getBirthDate(),
                requestDto.getAddress(), requestDto.getPhoneNumber());

        doReturn(mockUserResponseDto).when(userService).updateUser(any(Long.class),
                any(UserRequestDto.class));

        ResultActions resultActions = mockMvc.perform(put("/users/{id}", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)));

        resultActions.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data.id").value(userId))
                .andExpect(jsonPath("$.data.email").value(requestDto.getEmail()))
                .andExpect(jsonPath("$.data.firstName").value(requestDto.getFirstName()))
                .andExpect(jsonPath("$.data.lastName").value(requestDto.getLastName()))
                .andExpect(jsonPath("$.data.birthDate").value(requestDto.getBirthDate().toString()))
                .andExpect(jsonPath("$.data.address").value(requestDto.getAddress()))
                .andExpect(jsonPath("$.data.phoneNumber").value(requestDto.getPhoneNumber()))
                .andExpect(jsonPath("$.metadata.message")
                        .value("User is updated successfully"));
    }

    @Test
    public void whenSendingOneField_thenUpdateUserPartiallyAndReturn200() throws Exception {
        Long userId = 1L;
        UserUpdateRequestDto requestDto = new UserUpdateRequestDto(
                "johndoe@example.com");

        UserResponseDto mockUserResponseDto = new UserResponseDto(userId,
                requestDto.getEmail(), requestDto.getFirstName(),
                requestDto.getLastName(), requestDto.getBirthDate(),
                requestDto.getAddress(), requestDto.getPhoneNumber());

        doReturn(mockUserResponseDto).when(userService).updateUserFields(any(Long.class),
                any(UserUpdateRequestDto.class));

        ResultActions resultActions = mockMvc.perform(patch("/users/{id}", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)));

        resultActions.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data.id").value(userId))
                .andExpect(jsonPath("$.data.email").value(requestDto.getEmail()))
                .andExpect(jsonPath("$.data.firstName").value(requestDto.getFirstName()))
                .andExpect(jsonPath("$.data.lastName").value(requestDto.getLastName()))
                .andExpect(jsonPath("$.data.birthDate").value(requestDto.getBirthDate()))
                .andExpect(jsonPath("$.data.address").value(requestDto.getAddress()))
                .andExpect(jsonPath("$.data.phoneNumber").value(requestDto.getPhoneNumber()))
                .andExpect(jsonPath("$.metadata.message")
                        .value("User is updated successfully"));
    }

    @Test
    public void whenValidDateRangeDto_thenSearchByBirthDateRangeAndReturnUserList()
            throws Exception {
        DateRangeDto dateRangeDto = new DateRangeDto(
                LocalDate.of(1990, 1, 1),
                LocalDate.of(2000, 12, 31));

        List<UserResponseDto> mockUserList = List.of(
                new UserResponseDto(1L, "johndoe@example.com", "John", "Doe",
                        LocalDate.of(1995, 5, 15), "123 Street", "1234567890"));

        doReturn(mockUserList).when(userService).searchByBirthDateRange(any(DateRangeDto.class));

        ResultActions resultActions = mockMvc.perform(get("/users")
                .param("fromBirthDate", dateRangeDto.getFromBirthDate().toString())
                .param("toBirthDate", dateRangeDto.getToBirthDate().toString())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dateRangeDto)));

        resultActions.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data", hasSize(1)))
                .andExpect(jsonPath("$.data[0].id").value(1L))
                .andExpect(jsonPath("$.data[0].email").value("johndoe@example.com"))
                .andExpect(jsonPath("$.data[0].firstName").value("John"))
                .andExpect(jsonPath("$.data[0].lastName").value("Doe"))
                .andExpect(jsonPath("$.data[0].birthDate").value("1995-05-15"))
                .andExpect(jsonPath("$.data[0].address").value("123 Street"))
                .andExpect(jsonPath("$.data[0].phoneNumber").value("1234567890"))
                .andExpect(jsonPath("$.metadata.message")
                        .value("Users born between " + dateRangeDto.getFromBirthDate()
                                + " and " + dateRangeDto.getToBirthDate()));
    }

    @Test
    public void whenBirthDateIsNotDefined_thenReturnAllUsers() throws Exception {
        DateRangeDto dateRangeDto = new DateRangeDto(null, null);

        List<UserResponseDto> mockUserList = Arrays.asList(
                new UserResponseDto(1L, "johndoe@example.com", "John", "Doe",
                        LocalDate.of(1995, 5, 15), "123 Street", "1234567890"),
                new UserResponseDto(2L, "janedoe@example.com", "Jane", "Doe",
                        LocalDate.of(1992, 10, 20), "456 Avenue", "0987654321")
        );

        doReturn(mockUserList).when(userService).searchByBirthDateRange(any(DateRangeDto.class));

        ResultActions resultActions = mockMvc.perform(get("/users")
                .param("fromBirthDate", (String) null)
                .param("toBirthDate", (String) null)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dateRangeDto)));

        resultActions.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data", hasSize(2)))
                .andExpect(jsonPath("$.data[0].id").value(1L))
                .andExpect(jsonPath("$.data[0].email").value("johndoe@example.com"))
                .andExpect(jsonPath("$.data[0].firstName").value("John"))
                .andExpect(jsonPath("$.data[0].lastName").value("Doe"))
                .andExpect(jsonPath("$.data[0].birthDate").value("1995-05-15"))
                .andExpect(jsonPath("$.data[0].address").value("123 Street"))
                .andExpect(jsonPath("$.data[0].phoneNumber").value("1234567890"))
                .andExpect(jsonPath("$.data[1].id").value(2L))
                .andExpect(jsonPath("$.data[1].email").value("janedoe@example.com"))
                .andExpect(jsonPath("$.data[1].firstName").value("Jane"))
                .andExpect(jsonPath("$.data[1].lastName").value("Doe"))
                .andExpect(jsonPath("$.data[1].birthDate").value("1992-10-20"))
                .andExpect(jsonPath("$.data[1].address").value("456 Avenue"))
                .andExpect(jsonPath("$.data[1].phoneNumber").value("0987654321"))
                .andExpect(jsonPath("$.metadata.message")
                        .value("Users born between " + dateRangeDto.getFromBirthDate() + " and "
                                + dateRangeDto.getToBirthDate()));
    }

    @Test
    public void whenBirthDateIsOutOfRange_thenReturnEmptyList() throws Exception {
        DateRangeDto dateRangeDto = new DateRangeDto(
                LocalDate.of(1996, 1, 1),
                LocalDate.of(2000, 12, 31));

        List<UserResponseDto> mockUserList = List.of(
                new UserResponseDto(1L, "johndoe@example.com", "John", "Doe",
                        LocalDate.of(1995, 5, 15), "123 Street", "1234567890")
        );

        doReturn(mockUserList).when(userService).searchByBirthDateRange(any(DateRangeDto.class));

        ResultActions resultActions = mockMvc.perform(get("/users")
                .param("fromBirthDate", dateRangeDto.getFromBirthDate().toString())
                .param("toBirthDate", dateRangeDto.getToBirthDate().toString())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dateRangeDto)));

        resultActions.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data", hasSize(1)))
                .andExpect(jsonPath("$.metadata.message")
                        .value("Users born between " + dateRangeDto.getFromBirthDate() + " and "
                                + dateRangeDto.getToBirthDate()));
    }

    @Test
    public void whenInvalidDateRangeDto_thenValidationErrors() throws Exception {
        DateRangeDto dateRangeDto = new DateRangeDto(
                LocalDate.of(2000, 1, 1),
                LocalDate.of(1996, 12, 31));

        List<UserResponseDto> mockUserList = List.of(
                new UserResponseDto(1L, "johndoe@example.com", "John", "Doe",
                        LocalDate.of(1995, 5, 15), "123 Street", "1234567890")
        );

        doReturn(mockUserList).when(userService).searchByBirthDateRange(any(DateRangeDto.class));

        mockMvc.perform(get("/users")
                .param("fromBirthDate", dateRangeDto.getFromBirthDate().toString())
                .param("toBirthDate", dateRangeDto.getToBirthDate().toString())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dateRangeDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void whenValidUserId_thenDeleteUserAndReturnOk() throws Exception {
        Long userId = 1L;
        mockMvc.perform(delete("/users/{id}", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.metadata.message")
                        .value("User is deleted successfully"));
    }
}

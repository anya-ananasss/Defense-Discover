package backend.academy.userservice.controller;

import backend.academy.userservice.dto.UserDto;
import backend.academy.userservice.model.User;
import backend.academy.userservice.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    private UserDto testUserDto;
    private User testUser;
    private List<User> testUsers;

    @BeforeEach
    void setUp() {
        testUserDto = UserDto.builder()
            .id(1L)
            .username("testUser")
            .email("test@example.com")
            .password("password123")
            .build();

        testUser = User.builder()
            .id(1L)
            .username("testUser")
            .email("test@example.com")
            .password("password123")
            .build();

        testUsers = Arrays.asList(
            testUser,
            User.builder()
                .id(2L)
                .username("testUser2")
                .email("test2@example.com")
                .password("password456")
                .build()
        );
    }

    @Test
    void getAllUsersShouldReturnListOfUsers() throws Exception {
        when(userService.getAllUsers()).thenReturn(testUsers);

        mockMvc.perform(get("/users"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$[0].id").value(1))
            .andExpect(jsonPath("$[0].username").value("testUser"))
            .andExpect(jsonPath("$[1].id").value(2))
            .andExpect(jsonPath("$[1].username").value("testUser2"));
    }

    @Test
    void getUserByIdShouldReturnUser() throws Exception {
        when(userService.getUserById(1L)).thenReturn(testUserDto);

        mockMvc.perform(get("/users/1"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.username").value("testUser"))
            .andExpect(jsonPath("$.email").value("test@example.com"));
    }

    @Test
    void getUserByUsernameShouldReturnUser() throws Exception {
        when(userService.getUserByUsername("testUser")).thenReturn(testUserDto);

        mockMvc.perform(get("/users/by-username/testUser"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.username").value("testUser"))
            .andExpect(jsonPath("$.email").value("test@example.com"));
    }

    @Test
    void getUserByEmailShouldReturnUser() throws Exception {
        when(userService.getUserByEmail("test@example.com")).thenReturn(testUserDto);

        mockMvc.perform(get("/users/by-email")
                .param("email", "test@example.com"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.username").value("testUser"))
            .andExpect(jsonPath("$.email").value("test@example.com"));
    }

    @Test
    void repairPasswordByEmailShouldCallService() throws Exception {
        mockMvc.perform(get("/users/repairPassword/by-email")
                .param("email", "test@example.com")
                .param("newPassword", "newPassword123"))
            .andExpect(status().isOk());

        verify(userService).repairPassword("test@example.com", "newPassword123");
    }

    @Test
    void createUserShouldReturnCreatedUser() throws Exception {
        when(userService.createUser(any(UserDto.class))).thenReturn(testUserDto);

        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testUserDto)))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.username").value("testUser"))
            .andExpect(jsonPath("$.email").value("test@example.com"));
    }

    @Test
    void updateUserShouldReturnUpdatedUser() throws Exception {
        when(userService.updateUser(eq(1L), any(UserDto.class))).thenReturn(testUserDto);

        mockMvc.perform(put("/users/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testUserDto)))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.username").value("testUser"))
            .andExpect(jsonPath("$.email").value("test@example.com"));
    }

    @Test
    void deleteUserShouldCallService() throws Exception {
        mockMvc.perform(delete("/users/1"))
            .andExpect(status().isOk());

        verify(userService).deleteUser(1L);
    }
} 
package backend.academy.userservice.controller;


import backend.academy.userservice.dto.RoleDto;
import backend.academy.userservice.model.Role;
import backend.academy.userservice.service.RoleService;
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
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RoleController.class)
class RoleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private RoleService roleService;

    private Role testRole;
    private RoleDto testRoleDto;
    private List<Role> testRoles;

    @BeforeEach
    void setUp() {
        testRole = Role.builder()
            .id(1L)
            .name("TEST_ROLE")
            .build();

        testRoleDto = RoleDto.builder()
            .id(1L)
            .name("TEST_ROLE")
            .build();

        testRoles = Arrays.asList(
            testRole,
            Role.builder().id(2L).name("ADMIN").build()
        );
    }

    @Test
    void getAllRolesShouldReturnListOfRoles() throws Exception {
        when(roleService.getAllRoles()).thenReturn(testRoles);

        mockMvc.perform(get("/roles"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$[0].id").value(testRole.getId()))
            .andExpect(jsonPath("$[0].name").value(testRole.getName()))
            .andExpect(jsonPath("$[1].id").value(2L))
            .andExpect(jsonPath("$[1].name").value("ADMIN"));
    }

    @Test
    void getRoleByIdShouldReturnRole() throws Exception {
        when(roleService.getRoleById(1L)).thenReturn(testRole);

        mockMvc.perform(get("/roles/1"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(testRole.getId()))
            .andExpect(jsonPath("$.name").value(testRole.getName()));
    }

    @Test
    void getRoleByNameShouldReturnRole() throws Exception {
        when(roleService.getRoleByName("TEST_ROLE")).thenReturn(testRole);

        mockMvc.perform(get("/roles/by-name/TEST_ROLE"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(testRole.getId()))
            .andExpect(jsonPath("$.name").value(testRole.getName()));
    }

    @Test
    void createRoleShouldReturnCreatedRole() throws Exception {
        when(roleService.createRole(any(RoleDto.class))).thenReturn(testRole);

        mockMvc.perform(post("/roles")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testRoleDto)))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(testRole.getId()))
            .andExpect(jsonPath("$.name").value(testRole.getName()));
    }

    @Test
    void updateRoleShouldReturnUpdatedRole() throws Exception {
        when(roleService.updateRole(eq(1L), any(RoleDto.class))).thenReturn(testRole);

        mockMvc.perform(put("/roles/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testRoleDto)))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(testRole.getId()))
            .andExpect(jsonPath("$.name").value(testRole.getName()));
    }

    @Test
    void deleteRoleShouldReturnNoContent() throws Exception {
        doNothing().when(roleService).deleteRole(1L);

        mockMvc.perform(delete("/roles/1"))
            .andExpect(status().isOk());
    }
}
package backend.academy.userservice.service;


import backend.academy.userservice.dto.RoleDto;
import backend.academy.userservice.model.Role;
import backend.academy.userservice.repository.RoleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RoleServiceTest {

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
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
    void getAllRolesShouldReturnListOfRoles() {
        when(roleRepository.findAll()).thenReturn(testRoles);

        List<Role> result = roleService.getAllRoles();

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getName()).isEqualTo("TEST_ROLE");
        assertThat(result.get(1).getName()).isEqualTo("ADMIN");
        verify(roleRepository).findAll();
    }

    @Test
    void getRoleByIdShouldReturnRoleWhenExists() {
        when(roleRepository.findById(1L)).thenReturn(Optional.of(testRole));

        Role result = roleService.getRoleById(1L);

        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("TEST_ROLE");
        verify(roleRepository).findById(1L);
    }

    @Test
    void getRoleByIdShouldThrowExceptionWhenNotFound() {
        when(roleRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
            () -> roleService.getRoleById(999L));
        verify(roleRepository).findById(999L);
    }

    @Test
    void getRoleByNameShouldReturnRoleWhenExists() {
        when(roleRepository.findByName("TEST_ROLE")).thenReturn(Optional.of(testRole));

        Role result = roleService.getRoleByName("TEST_ROLE");

        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("TEST_ROLE");
        verify(roleRepository).findByName("TEST_ROLE");
    }

    @Test
    void getRoleByNameShouldThrowExceptionWhenNotFound() {
        when(roleRepository.findByName("NON_EXISTENT")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
            () -> roleService.getRoleByName("NON_EXISTENT"));
        verify(roleRepository).findByName("NON_EXISTENT");
    }

    @Test
    void createRoleShouldSaveAndReturnNewRole() {
        when(roleRepository.save(any(Role.class))).thenReturn(testRole);

        Role result = roleService.createRole(testRoleDto);

        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("TEST_ROLE");
        verify(roleRepository).save(any(Role.class));
    }

    @Test
    void updateRoleShouldUpdateExistingRole() {
        when(roleRepository.findById(1L)).thenReturn(Optional.of(testRole));
        when(roleRepository.save(any(Role.class))).thenReturn(testRole);

        Role result = roleService.updateRole(1L, testRoleDto);

        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("TEST_ROLE");
        verify(roleRepository).findById(1L);
        verify(roleRepository).save(any(Role.class));
    }

    @Test
    void deleteRoleShouldCallRepository() {
        doNothing().when(roleRepository).deleteById(1L);

        roleService.deleteRole(1L);

        verify(roleRepository).deleteById(1L);
    }
}
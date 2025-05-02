package backend.academy.userservice.service;

import backend.academy.userservice.dto.RoleDto;
import backend.academy.userservice.model.Role;
import backend.academy.userservice.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;

    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    public Role getRoleById(Long id) {
        return roleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Role not found"));
    }

    public Role getRoleByName(String name) {
        return roleRepository.findByName(name)
                .orElseThrow(() -> new RuntimeException("Role not found"));
    }

    public Role createRole(RoleDto roleDto) {
        Role role = new Role();
        role.setName(roleDto.getName());
        return roleRepository.save(role);
    }

    public Role updateRole(Long id, RoleDto roleDto) {
        Role role = getRoleById(id);
        role.setName(roleDto.getName());
        return roleRepository.save(role);
    }

    public void deleteRole(Long id) {
        roleRepository.deleteById(id);
    }
}
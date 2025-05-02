package backend.academy.apigateway.client;

import backend.academy.apigateway.dto.security.RoleDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "role-client", url = "${role.client.url}")
public interface RoleClient {

    @GetMapping("/roles")
    List<RoleDto> getAllRoles();

    @GetMapping("/roles/{id}")
    RoleDto getRoleById(@PathVariable("id") Long id);

    @PostMapping("/roles")
    RoleDto createRole(@RequestBody RoleDto roleDto);

    @PutMapping("/roles/{id}")
    RoleDto updateRole(@PathVariable("id") Long id, @RequestBody RoleDto roleDto);

    @DeleteMapping("/roles/{id}")
    void deleteRole(@PathVariable("id") Long id);

    @GetMapping("/roles/by-name/{name}")
    RoleDto getRoleByName(@PathVariable("name") String name);
}

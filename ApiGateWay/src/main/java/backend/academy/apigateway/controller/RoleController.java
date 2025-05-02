package backend.academy.apigateway.controller;



import backend.academy.apigateway.dto.security.RoleDto;
import backend.academy.apigateway.dto.security.UserDto;
import backend.academy.apigateway.exception.RoleDoesntExist;
import backend.academy.apigateway.service.RoleService;
import backend.academy.apigateway.utils.ApiPaths;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(ApiPaths.ADMIN_API)
public class RoleController {

    private final RoleService roleService;

    @PostMapping("/getUsersByRole")
    public ResponseEntity<List<UserDto>> getUsersByRole(@RequestBody RoleDto role) {
        try {
            return ResponseEntity.ok(roleService.findUsersByRole(role));
        }catch (RoleDoesntExist e){
            return ResponseEntity.badRequest().body(null);
        }catch (Exception e){
            return ResponseEntity.unprocessableEntity().body(null);
        }
    }


    @GetMapping("/getAllRoles")
    public ResponseEntity<List<RoleDto>> getRoles() {
        try {
            return ResponseEntity.ok(roleService.getAllRoles());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
}

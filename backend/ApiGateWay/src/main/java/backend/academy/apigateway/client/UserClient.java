package backend.academy.apigateway.client;

import backend.academy.apigateway.dto.security.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "user-client", url = "${user.client.url}")
public interface UserClient {

     @GetMapping("/users")
     List<UserDto> getAllUsers();

     @GetMapping("/users/{id}")
     UserDto getUserById(@PathVariable("id") Long id);

     @PostMapping("/users")
     UserDto createUser(@RequestBody UserDto userDto);

     @PutMapping("/users/{id}")
     UserDto updateUser(@PathVariable("id") Long id, @RequestBody UserDto userDto);

     @DeleteMapping("/users/{id}")
     void deleteUser(@PathVariable("id") Long id);

     @GetMapping("/users/by-username/{username}")
     UserDto getUserByUsername(@PathVariable("username") String username);

     @GetMapping("/users/by-email")
     UserDto getUserByEmail(@RequestParam("email") String email);

     @GetMapping("/users/repairPassword/by-email")
     UserDto repairPasswordByEmail(@RequestParam("email") String email,
                                   @RequestParam("newPassword") String newPassword);
}


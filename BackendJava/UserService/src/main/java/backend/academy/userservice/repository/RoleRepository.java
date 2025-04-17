package backend.academy.userservice.repository;


import backend.academy.userservice.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByRolename(String role_name);
}

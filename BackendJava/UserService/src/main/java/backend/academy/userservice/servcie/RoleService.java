package backend.academy.userservice.servcie;

import backend.academy.userservice.model.Role;
import backend.academy.userservice.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;


    public RoleDto create(Role role){

    }

}

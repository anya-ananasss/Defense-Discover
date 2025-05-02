package backend.academy.apigateway.service.impl;



import backend.academy.apigateway.dto.security.UserDto;
import backend.academy.apigateway.dto.security.UserPrincipal;
import backend.academy.apigateway.client.UserClient;
import backend.academy.apigateway.service.MyUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements MyUserDetailsService {


    private final UserClient userRepo;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDto user = userRepo.getUserByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("user not found");
        }

        return new UserPrincipal(user);
    }
}

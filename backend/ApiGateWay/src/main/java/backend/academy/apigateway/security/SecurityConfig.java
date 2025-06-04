package backend.academy.apigateway.security;

import backend.academy.apigateway.service.EndPointRouteService;
import backend.academy.apigateway.utils.ApiPaths;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtFilter jwtFilter;

    private final UserDetailsService userDetailsService;

    private final EndPointRouteService endPointRouteService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {



        return http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(request -> {
                    var corsConfig = new CorsConfiguration();
                    corsConfig.setAllowedOrigins(List.of("*"));
                    corsConfig.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
                    corsConfig.setAllowedHeaders(List.of("*"));
                    corsConfig.setAllowCredentials(false);
                    corsConfig.setMaxAge(3600L);
                    return corsConfig;
                }))
                .authorizeHttpRequests(request ->

                        {
                            request
                                    .requestMatchers(
                                            "/swagger-ui/**",
                                            "/swagger-ui.html",
                                            "/v3/api-docs",
                                            "/v3/api-docs/**",
                                            "/v3/api-docs/*"
                                    ).permitAll()
                                    .requestMatchers(HttpMethod.GET, ApiPaths.BASE_API + "/*").permitAll()
                                    .requestMatchers(HttpMethod.POST, ApiPaths.BASE_API + "/*").permitAll()
                                    .requestMatchers(HttpMethod.POST, ApiPaths.USER_API + "/*").hasAnyAuthority("USER", "ADMIN")
                                    .requestMatchers(HttpMethod.POST, ApiPaths.USER_API + "/**").hasAnyAuthority("USER", "ADMIN")
                                    .requestMatchers(HttpMethod.GET, ApiPaths.ADMIN_API + "/*").hasAuthority("ADMIN")
                                    .requestMatchers(HttpMethod.POST, ApiPaths.ADMIN_API + "/*").hasAuthority("ADMIN")
                                    .requestMatchers(HttpMethod.GET, ApiPaths.ADMIN_API + "/**").hasAuthority("ADMIN")
                                    .requestMatchers(HttpMethod.POST, ApiPaths.ADMIN_API + "/**").hasAuthority("ADMIN");
                        }
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .build();


    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(new BCryptPasswordEncoder(12));
        provider.setUserDetailsService(userDetailsService);


        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();

    }


}
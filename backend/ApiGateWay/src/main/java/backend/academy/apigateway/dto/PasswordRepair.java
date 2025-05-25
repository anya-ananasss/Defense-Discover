package backend.academy.apigateway.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class PasswordRepair {
    private String password;
    private String email;
}

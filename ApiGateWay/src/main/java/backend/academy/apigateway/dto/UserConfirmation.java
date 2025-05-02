package backend.academy.apigateway.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class UserConfirmation {
    private String username;
    private String email;
    private String code;
}

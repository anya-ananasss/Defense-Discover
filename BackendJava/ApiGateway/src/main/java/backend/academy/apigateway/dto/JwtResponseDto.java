package backend.academy.apigateway.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JwtResponseDto {

    /**
     * Dto ответа в виде JWT токена
     */

    private String token;


}
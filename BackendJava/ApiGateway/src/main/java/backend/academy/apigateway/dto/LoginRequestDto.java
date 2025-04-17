package backend.academy.apigateway.dto;

import lombok.Data;

@Data
public class LoginRequestDto {

    /**
     * Запрос на login
     */


    private String username;
    private String password;


}

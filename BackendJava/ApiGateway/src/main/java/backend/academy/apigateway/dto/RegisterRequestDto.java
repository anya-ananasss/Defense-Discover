package backend.academy.apigateway.dto;

import lombok.Data;

@Data
public class RegisterRequestDto {

    /**
     * Запрос на регистрацию
     */

    private String username;
    private String password;
    private String email;    //todo валидация email
}
package backend.academy.userservice.exception;

import lombok.Getter;

public class UserNotFoundException extends Exception {
    @Getter
    private final String responseMessage;
    public UserNotFoundException(String user) {
        super(user + " not found");
        responseMessage = user + "not found";
    }
}

package backend.academy.apigateway.exception;

public class RoleDoesntExist extends RuntimeException {
    public RoleDoesntExist(String message) {
        super(message);
    }
}

package backend.academy.apigateway.exception;

public class WrongConfirmationCode extends Exception{
    public WrongConfirmationCode(String message) {
        super(message);
    }
}

package backend.academy.modelservice.exception;

public class QuestionDoesntExists extends RuntimeException {
    public QuestionDoesntExists(String message) {
        super(message);
    }
}

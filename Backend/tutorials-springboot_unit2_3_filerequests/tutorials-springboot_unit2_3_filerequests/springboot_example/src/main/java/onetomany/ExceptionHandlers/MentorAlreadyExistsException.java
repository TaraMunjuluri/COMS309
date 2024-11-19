package onetomany.ExceptionHandlers;

public class MentorAlreadyExistsException extends RuntimeException {
    public MentorAlreadyExistsException(String message) {
        super(message);
    }
}

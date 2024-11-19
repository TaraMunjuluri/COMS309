package onetomany.ExceptionHandlers;

public class MenteeAlreadyExistsException extends RuntimeException {
    public MenteeAlreadyExistsException(String message) {
        super(message);
    }
}
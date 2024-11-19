package onetomany.ExceptionHandlers;

public class MentorNotFoundException extends RuntimeException {
    public MentorNotFoundException(String message) {
        super(message);
    }
}

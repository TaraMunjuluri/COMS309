package onetomany.ExceptionHandlers;

public class InterestNotFoundException extends RuntimeException {
    public InterestNotFoundException(String message) {
        super(message);  // Passes the message to the parent RuntimeException class.
    }
}

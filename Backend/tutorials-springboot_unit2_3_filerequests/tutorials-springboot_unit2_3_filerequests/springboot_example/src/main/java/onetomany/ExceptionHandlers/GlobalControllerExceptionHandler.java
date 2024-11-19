package onetomany.ExceptionHandlers;

import org.springframework.core.convert.ConversionFailedException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Global exception handler for handling application-wide exceptions.
 */
@RestControllerAdvice
public class GlobalControllerExceptionHandler {

    @ExceptionHandler(ConversionFailedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleConversion(RuntimeException ex) {
        return ex.getMessage();  // Returns a 400 Bad Request with the exception message.
    }

    @ExceptionHandler(InterestNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleInterestNotFound(RuntimeException ex) {
        return ex.getMessage();  // Returns a 404 Not Found with the exception message.
    }
    @ExceptionHandler(UnauthorizedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED) // 401 Unauthorized
    public String handleUnauthorizedException(UnauthorizedException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler(MenteeAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT) // 409 Conflict
    public String handleMenteeAlreadyExistsException(MenteeAlreadyExistsException ex) {
        return ex.getMessage();
    }
    @ExceptionHandler(NoteNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleNoteNotFoundException(NoteNotFoundException ex) {
        return ex.getMessage();
    }
    @ExceptionHandler(MentorAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public String handleMentorAlreadyExistsException(MentorAlreadyExistsException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler(MentorNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleMentorNotFoundException(MentorNotFoundException ex) {
        return ex.getMessage();
    }
    @ExceptionHandler(ImageNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleImageNotFoundException(ImageNotFoundException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleGenericRuntimeException(RuntimeException ex) {
        return ex.getMessage();
    }

}

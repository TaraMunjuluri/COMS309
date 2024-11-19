package onetomany.ExceptionHandlers;

public class ImageNotFoundException extends Throwable {
        public ImageNotFoundException(String message) {
            super(message);
        }
}

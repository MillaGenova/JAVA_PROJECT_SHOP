package exceptions;

public class ExpiredDateException extends Exception {

    public ExpiredDateException(final String message) {
        super(message);
    }

    public ExpiredDateException(final String message,
                                final Throwable throwable) {
        super(message, throwable);
    }

}
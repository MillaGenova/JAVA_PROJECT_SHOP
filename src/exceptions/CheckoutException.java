package exceptions;

public class CheckoutException extends Exception {

    public CheckoutException(final String message) {
        super(message);
    }

    public CheckoutException(final String message,
                             final Throwable throwable) {
        super(message, throwable);
    }

}
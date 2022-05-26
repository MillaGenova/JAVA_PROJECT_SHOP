package exceptions;

public class ProductsDatabaseException extends Exception {

    public ProductsDatabaseException(final String message) {
        super(message);
    }

    public ProductsDatabaseException(final String message,
                                     final Throwable throwable) {
        super(message, throwable);
    }

}
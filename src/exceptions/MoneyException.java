package exceptions;

public class MoneyException extends Exception {

    public MoneyException(final String message) {
        super(message);
    }

    public MoneyException(final String message,
                          final Throwable throwable) {
        super(message, throwable);
    }

}
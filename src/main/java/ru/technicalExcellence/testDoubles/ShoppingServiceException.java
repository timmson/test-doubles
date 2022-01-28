package ru.technicalExcellence.testDoubles;

public class ShoppingServiceException extends Exception {

    public ShoppingServiceException(String message) {
        super(message);
    }

    public ShoppingServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}

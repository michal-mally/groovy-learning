package pl.softwaremind.ckjava.recommendation.groovy.training;

public class OrderAlreadyClosedException extends RuntimeException {

    public OrderAlreadyClosedException(String message) {
        super(message);
    }

    public OrderAlreadyClosedException(String message, Throwable cause) {
        super(message, cause);
    }

}

package pl.softwaremind.ckjava.recommendation.groovy.training.domain;

public class OrderException extends RuntimeException {

    public OrderException(String message) {
        super(message);
    }

    public OrderException(String message, Throwable cause) {
        super(message, cause);
    }

}

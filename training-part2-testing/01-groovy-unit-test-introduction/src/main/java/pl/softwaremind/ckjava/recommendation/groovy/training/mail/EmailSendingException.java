package pl.softwaremind.ckjava.recommendation.groovy.training.mail;

public class EmailSendingException extends Exception {

    public EmailSendingException(String message) {
        super(message);
    }

    public EmailSendingException(String message, Throwable cause) {
        super(message, cause);
    }

}

package pl.softwaremind.ckjava.recommendation.groovy.training.mail;

public interface EmailServer {

    void sendEmail(Email email) throws EmailSendingException;

}

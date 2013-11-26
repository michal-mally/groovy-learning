package pl.softwaremind.ckjava.recommendation.groovy.training.service;

import pl.softwaremind.ckjava.recommendation.groovy.training.domain.Order;
import pl.softwaremind.ckjava.recommendation.groovy.training.domain.OrderException;
import pl.softwaremind.ckjava.recommendation.groovy.training.mail.Email;
import pl.softwaremind.ckjava.recommendation.groovy.training.mail.EmailSendingException;
import pl.softwaremind.ckjava.recommendation.groovy.training.mail.EmailServer;

import java.util.logging.Logger;

public class EmailOrderSender implements OrderSender {

    private static final Logger logger = Logger.getLogger(EmailOrderSender.class.getName());

    private static final int RETRY_COUNT = 3;

    private static final String MAIL_SUBJECT = "Order #%s is ready";

    private static final String MAIL_BODY = "Dear %s,\n\n  Please check the order #%s.\n\nBest regards,\n%s";

    private final EmailServer emailServer;

    private final String from;

    public EmailOrderSender(EmailServer emailServer, String from) {
        this.emailServer = emailServer;
        this.from = from;
    }

    @Override
    public void sendOrder(String recipient, Order order) {
        if (!order.isClosed()) {
            throw new OrderException("It is not allowed to send open orders!");
        }

        final Email email = composeEmailOfOrder(recipient, order);
        for (int i = 0; i < RETRY_COUNT; i++) {
            try {
                this.emailServer.sendEmail(email);
                return;
            } catch (EmailSendingException e) {
                logger.warning(String.format("Error sending email for %d time!", i));
            }
        }

        throw new OrderException(String.format("Unable to send email for %d. Giving up!", RETRY_COUNT));
    }

    private Email composeEmailOfOrder(String recipient, Order order) {
        return new Email(
                this.from,
                recipient,
                String.format(MAIL_SUBJECT, order.getNumber()),
                String.format(MAIL_BODY, recipient, order.getNumber(), this.from),
                order
        );
    }

}

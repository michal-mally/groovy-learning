package pl.softwaremind.ckjava.recommendation.groovy.training.service

import pl.softwaremind.ckjava.recommendation.groovy.training.domain.Order
import pl.softwaremind.ckjava.recommendation.groovy.training.domain.OrderException
import pl.softwaremind.ckjava.recommendation.groovy.training.domain.OrderItem
import pl.softwaremind.ckjava.recommendation.groovy.training.mail.Email
import pl.softwaremind.ckjava.recommendation.groovy.training.mail.EmailSendingException
import pl.softwaremind.ckjava.recommendation.groovy.training.mail.EmailServer
import spock.lang.Specification
import spock.lang.Unroll

class EmailOrderSenderTest extends Specification {

    List<Email> sentEmails = []

    int throwExceptionCount = 0

    def emailServer = [
            sendEmail: { email ->
                if (throwExceptionCount) {
                    throwExceptionCount--
                    throw new EmailSendingException("Unable to send email!")
                }
                sentEmails << email
            }
    ] as EmailServer

    def from = "no-reply@example.com"

    def orderSender = new EmailOrderSender(emailServer, from)

    def 'shall not allow to send open Order'() {
        given: 'any open order'
        def order = new Order('order number 1234')
                .addItem(OrderItem.withDefault())

        when: 'order is being sent to someone'
        orderSender.sendOrder('recipient', order)

        then: 'sending is not allowed'
        thrown OrderException
        and: 'there is no interaction with email server'
        ! sentEmails
    }

    def 'shall send closed order properly'() {
        given: 'any closed order'
        def order = new Order('order number 1234')
                .addItem(OrderItem.withDefault())
                .close()
        and: 'recipient'
        def recipient = 'recipient'

        when: 'order is being sent to recipient'
        orderSender.sendOrder(recipient, order)

        then: 'one email is sent'
        sentEmails.size() == 1
        def email = sentEmails[0]
        and: 'has order as attachment'
        email.attachments == [order]
        and: 'has correctly set properties'
        email.from == this.from
        email.to == recipient
        email.subject == "Order #$order.number is ready"
        email.body == """Dear $recipient,

  Please check the order #$order.number.

Best regards,
$from"""
    }

    @Unroll
    def 'shall succeed in delivering order even if #attemptFails tries fail'() {
        given: 'any closed order'
        def order = new Order('order number 1234')
                .addItem(OrderItem.withDefault())
                .close()
        and: 'first (and second) attempt fails'
        this.throwExceptionCount = attemptFails

        when: 'order is being sent to recipient'
        orderSender.sendOrder('recipient', order)

        then: 'email is sent'
        sentEmails.size() == 1

        where:
        attemptFails << [1, 2]
    }

    def 'shall not deliver order if 3 attempts to deliver fails'() {
        given: 'any closed order'
        def order = new Order('order number 1234')
                .addItem(OrderItem.withDefault())
                .close()
        and: 'first (and second) attempt fails'
        this.throwExceptionCount = 3

        when: 'order is being sent to recipient'
        orderSender.sendOrder('recipient', order)

        then: 'sender gives up'
        thrown OrderException
        and: 'email is not sent'
        ! sentEmails
    }

}

package pl.softwaremind.ckjava.recommendation.groovy.training.service

import pl.softwaremind.ckjava.recommendation.groovy.training.domain.Order
import pl.softwaremind.ckjava.recommendation.groovy.training.domain.OrderException
import pl.softwaremind.ckjava.recommendation.groovy.training.domain.OrderItem
import pl.softwaremind.ckjava.recommendation.groovy.training.mail.Email
import pl.softwaremind.ckjava.recommendation.groovy.training.mail.EmailSendingException
import pl.softwaremind.ckjava.recommendation.groovy.training.mail.EmailServer
import spock.lang.Specification

class EmailOrderSenderTest extends Specification {

    def emailServer = Mock(EmailServer)

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
        0 * emailServer._
    }

    def 'shall send closed order properly'() {
        Email email = null

        given: 'any closed order'
        def order = new Order('order number 1234')
                .addItem(OrderItem.withDefault())
                .close()
        and: 'recipient'
        def recipient = 'recipient'

        when: 'order is being sent to recipient'
        orderSender.sendOrder(recipient, order)

        then: 'one email is sent'
        1 * emailServer.sendEmail(_) >> { email = it[0] }
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

    def 'shall succeed in delivering order even if first 2 tries fail'() {
        Email email = null

        given: 'any closed order'
        def order = new Order('order number 1234')
                .addItem(OrderItem.withDefault())
                .close()
        and: 'first (and second) attempt fails'

        when: 'order is being sent to recipient'
        orderSender.sendOrder('recipient', order)

        then: 'maximum 3 attempts are made'
        (1..3) * emailServer.sendEmail(_) >>
                { throw new EmailSendingException("Error sending email") } >>
                { throw new EmailSendingException("Error sending email") } >>
                { email = it[0] }
        and: 'email is sent'
        email
    }

    def 'shall not deliver order if 3 attempts to deliver fails'() {
        given: 'any closed order'
        def order = new Order('order number 1234')
                .addItem(OrderItem.withDefault())
                .close()
        and: 'all attempts to send email fail'

        when: 'order is being sent to recipient'
        orderSender.sendOrder('recipient', order)

        then: 'sender gives up'
        thrown OrderException
        and: 'maximum 3 attempts are made'
        3 * emailServer.sendEmail(_) >> { throw new EmailSendingException("Error sending email!") }
    }

}

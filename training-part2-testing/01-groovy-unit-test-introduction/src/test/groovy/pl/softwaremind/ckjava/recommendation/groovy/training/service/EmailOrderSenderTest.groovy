package pl.softwaremind.ckjava.recommendation.groovy.training.service

import org.mockito.ArgumentCaptor
import pl.softwaremind.ckjava.recommendation.groovy.training.domain.Order
import pl.softwaremind.ckjava.recommendation.groovy.training.domain.OrderException
import pl.softwaremind.ckjava.recommendation.groovy.training.domain.OrderItem
import pl.softwaremind.ckjava.recommendation.groovy.training.mail.Email
import pl.softwaremind.ckjava.recommendation.groovy.training.mail.EmailSendingException
import pl.softwaremind.ckjava.recommendation.groovy.training.mail.EmailServer
import spock.lang.Specification
import spock.lang.Unroll

import static org.mockito.Matchers.any
import static org.mockito.Mockito.*

class EmailOrderSenderTest extends Specification {

    def emailServer = mock(EmailServer)

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
        verifyZeroInteractions(emailServer)
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
        def emailCaptor = new ArgumentCaptor<Email>()
        verify(emailServer, times(1)).sendEmail(emailCaptor.capture())
        def email = emailCaptor.value
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
        def stubber = doThrow(new EmailSendingException("Error sending email"))
        if (attemptFails == 2) {
            stubber = stubber.doThrow(new EmailSendingException("Error sending email"))
        }
        stubber.doNothing()
                .when(emailServer).sendEmail(any(Email))

        when: 'order is being sent to recipient'
        orderSender.sendOrder('recipient', order)

        then: 'email is sent'
        verify(emailServer, times(attemptFails + 1)).sendEmail(any(Email))

        where:
        attemptFails << [1, 2]
    }

    def 'shall not deliver order if 3 attempts to deliver fails'() {
        given: 'any closed order'
        def order = new Order('order number 1234')
                .addItem(OrderItem.withDefault())
                .close()
        and: 'all attempts to send email fail'
        doThrow(new EmailSendingException("Error sending email!"))
                .when(emailServer).sendEmail(any(Email))

        when: 'order is being sent to recipient'
        orderSender.sendOrder('recipient', order)

        then: 'sender gives up'
        thrown OrderException
        and: 'maximum 3 attempts are made'
        verify(emailServer, times(3)).sendEmail(any(Email))
    }

}

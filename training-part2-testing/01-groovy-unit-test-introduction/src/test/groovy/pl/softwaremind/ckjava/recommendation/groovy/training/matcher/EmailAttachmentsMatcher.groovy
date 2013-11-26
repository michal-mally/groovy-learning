package pl.softwaremind.ckjava.recommendation.groovy.training.matcher
import org.hamcrest.BaseMatcher
import org.hamcrest.Description
import org.hamcrest.Matcher
import pl.softwaremind.ckjava.recommendation.groovy.training.mail.Email

class EmailAttachmentsMatcher extends BaseMatcher<Email> {

    private final List<Object> expectedAttachments

    static Matcher<Email> hasAttachments(Object... attachments) {
        new EmailAttachmentsMatcher(attachments as List<Object>)
    }

    @Override
    boolean matches(Object item) {
        Email.cast(item).attachments == this.expectedAttachments
    }

    @Override
    void describeTo(Description description) {
        description.appendText("Email w/ attachments = " + this.expectedAttachments)
    }

    private EmailAttachmentsMatcher(List<Object> expectedAttachments) {
        this.expectedAttachments = expectedAttachments
    }

}

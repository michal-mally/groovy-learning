package pl.softwaremind.ckjava.recommendation.groovy.training.mail;

import java.util.Collections;
import java.util.List;

import static java.util.Arrays.asList;

public class Email {

    private final String from;

    private final String to;

    private final String subject;

    private final String body;

    private final List<Object> attachments;

    public Email(String from, String to, String subject, String body, Object... attachments) {
        this.from = from;
        this.to = to;
        this.subject = subject;
        this.body = body;
        this.attachments = asList(attachments);
    }

    public String getFrom() {
        return this.from;
    }

    public String getTo() {
        return this.to;
    }

    public String getSubject() {
        return this.subject;
    }

    public String getBody() {
        return this.body;
    }

    public List<Object> getAttachments() {
        return Collections.unmodifiableList(this.attachments);
    }

    @Override
    public String toString() {
        return "Email{" +
                "from='" + from + '\'' +
                ", to='" + to + '\'' +
                ", subject='" + subject + '\'' +
                ", body='" + body + '\'' +
                ", attachments=" + attachments +
                '}';
    }

}

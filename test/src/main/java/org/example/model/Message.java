package org.example.model;

public class Message {
    private final String subject;
    private final String body;

    /**
     * Creates a new email message
     * @param subject The email subject
     * @param body The email body
     * @throws IllegalArgumentException if subject or body is null or empty
     */
    public Message(String subject, String body) {
        if (subject == null || subject.trim().isEmpty()) {
            throw new IllegalArgumentException("Subject cannot be null or empty");
        }
        if (body == null || body.trim().isEmpty()) {
            throw new IllegalArgumentException("Body cannot be null or empty");
        }
        this.subject = subject.trim();
        this.body = body.trim();
    }

    public String getSubject() {
        return subject;
    }

    public String getBody() {
        return body;
    }

    @Override
    public String toString() {
        return "Message{subject='" + subject + "'}";
    }
}
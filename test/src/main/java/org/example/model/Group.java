package org.example.model;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class Group {
    private final String sender;
    private final List<String> recipients;
    private static final Pattern EMAIL_PATTERN = Pattern
            .compile("^[A-Za-z0-9+_.-]+@(.+)$");

    /**
     * Creates a new email group
     * @param sender The email address of the sender
     * @param recipients List of recipient email addresses
     * @throws IllegalArgumentException if sender is invalid or recipients list is invalid
     */
    public Group(String sender, List<String> recipients) {
        if (!isValidEmail(sender)) {
            throw new IllegalArgumentException("Invalid sender email address: " + sender);
        }
        if (recipients == null || recipients.isEmpty()) {
            throw new IllegalArgumentException("Recipients list cannot be null or empty");
        }
        if (recipients.size() > 4) {  // Max 4 recipients as per requirements (1 sender + 4 recipients = 5 total)
            throw new IllegalArgumentException("Too many recipients: maximum is 4");
        }
        
        for (String recipient : recipients) {
            if (!isValidEmail(recipient)) {
                throw new IllegalArgumentException("Invalid recipient email address: " + recipient);
            }
        }

        this.sender = sender;
        this.recipients = new ArrayList<>(recipients); // Create defensive copy
    }

    /**
     * Validates an email address
     * @param email The email address to validate
     * @return true if the email is valid, false otherwise
     */
    private static boolean isValidEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        return EMAIL_PATTERN.matcher(email).matches();
    }

    public String getSender() {
        return sender;
    }

    public List<String> getRecipients() {
        return new ArrayList<>(recipients); // Return defensive copy
    }

    @Override
    public String toString() {
        return "Group{sender='" + sender + "', recipients=" + recipients + "}";
    }
}
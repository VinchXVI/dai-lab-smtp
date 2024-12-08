package org.example.model;

import java.util.*;
import java.io.*;

public class PrankGenerator {
    private final List<String> allEmails;
    private final List<Message> allMessages;
    private final int numberOfGroups;
    private final Random random;

    /**
     * Creates a new prank generator
     * @param emailsFile Path to the file containing email addresses
     * @param messagesFile Path to the file containing prank messages
     * @param numberOfGroups Number of groups to generate
     * @throws IllegalArgumentException if any parameter is invalid
     */
    public PrankGenerator(String emailsFile, String messagesFile, int numberOfGroups) {
        if (numberOfGroups < 1) {
            throw new IllegalArgumentException("Le nombre de groupes doit être positif");
        }
    
        this.random = new Random();
        this.numberOfGroups = numberOfGroups;
        
        // Chargement et validation des emails
        this.allEmails = loadEmails(emailsFile);
        Map<String, String> emailErrors = InputValidator.validateEmails(this.allEmails);
        if (!emailErrors.isEmpty()) {
            System.err.println("Erreurs dans la liste d'emails :");
            emailErrors.forEach((email, error) -> System.err.println("- " + email + ": " + error));
            throw new IllegalArgumentException("La liste d'emails contient des erreurs");
        }
        
        // Chargement et validation des messages
        this.allMessages = loadMessages(messagesFile);
        Map<String, String> messageErrors = InputValidator.validateMessages(this.allMessages);
        if (!messageErrors.isEmpty()) {
            System.err.println("Erreurs dans la liste de messages :");
            messageErrors.forEach((msg, error) -> System.err.println("- " + msg + ": " + error));
            throw new IllegalArgumentException("La liste de messages contient des erreurs");
        }
        
        // Validation de la configuration des groupes
        InputValidator.validateGroupConfiguration(numberOfGroups, allEmails.size());
    }

    /**
     * Load emails from file
     */
    private List<String> loadEmails(String filename) {
        List<String> emails = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (!line.isEmpty()) {
                    emails.add(line);
                }
            }
        } catch (IOException e) {
            throw new IllegalArgumentException("Error reading emails file: " + e.getMessage());
        }
        return emails;
    }

    /**
     * Load messages from file
     */
    private List<Message> loadMessages(String filename) {
        List<Message> messages = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            StringBuilder currentMessage = new StringBuilder();
            String subject = null;
            
            String line;
            while ((line = br.readLine()) != null) {
                if (line.startsWith("subject:")) {
                    subject = line.substring(8).trim();
                } else if (line.contains(";endOfPrank;")) {
                    if (subject != null) {
                        messages.add(new Message(subject, currentMessage.toString().trim()));
                    }
                    subject = null;
                    currentMessage.setLength(0);
                } else if (subject != null) {
                    currentMessage.append(line).append("\n");
                }
            }
        } catch (IOException e) {
            throw new IllegalArgumentException("Error reading messages file: " + e.getMessage());
        }
        return messages;
    }

    /**
     * Generates groups for the prank campaign
     * @return Map of groups to their assigned messages
     */
    public Map<Group, Message> generateGroups() {
        Map<Group, Message> groupAssignments = new HashMap<>();
        List<String> availableEmails = new ArrayList<>(allEmails);
        
        for (int i = 0; i < numberOfGroups; i++) {
            // Determine random group size (2-5 people)
            int groupSize = random.nextInt(4) + 2; // +2 because we want 2-5
            groupSize = Math.min(groupSize, availableEmails.size()); // Don't exceed available emails
            
            // Select random emails for this group
            Collections.shuffle(availableEmails);
            String sender = availableEmails.remove(0);
            List<String> recipients = new ArrayList<>();
            
            for (int j = 0; j < groupSize - 1 && !availableEmails.isEmpty(); j++) {
                recipients.add(availableEmails.remove(0));
            }
            
            // Create group and assign random message
            Group group = new Group(sender, recipients);
            Message message = allMessages.get(random.nextInt(allMessages.size()));
            groupAssignments.put(group, message);
        }
        
        return groupAssignments;
    }
}
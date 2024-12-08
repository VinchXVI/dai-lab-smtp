package org.example.model;

import java.util.*;
import java.util.regex.Pattern;

public class InputValidator {
    private static final Pattern EMAIL_PATTERN = Pattern
            .compile("^[A-Za-z0-9+_.-]+@(.+)$");
            
    private static final int MIN_GROUP_SIZE = 2;

    /**
     * Valide une liste d'adresses email
     * @return Une map avec les erreurs trouvées (email -> message d'erreur)
     */
    public static Map<String, String> validateEmails(List<String> emails) {
        Map<String, String> errors = new HashMap<>();
        
        if (emails == null || emails.isEmpty()) {
            errors.put("GLOBAL", "La liste d'emails est vide");
            return errors;
        }

        for (String email : emails) {
            if (email == null || email.trim().isEmpty()) {
                errors.put(email, "L'email ne peut pas être vide");
            } else if (!EMAIL_PATTERN.matcher(email).matches()) {
                errors.put(email, "Format d'email invalide");
            }
        }

        return errors;
    }

    /**
     * Valide une liste de messages
     * @return Une map avec les erreurs trouvées (message -> message d'erreur)
     */
    public static Map<String, String> validateMessages(List<Message> messages) {
        Map<String, String> errors = new HashMap<>();
        
        if (messages == null || messages.isEmpty()) {
            errors.put("GLOBAL", "La liste de messages est vide");
            return errors;
        }

        for (Message msg : messages) {
            if (msg.getSubject() == null || msg.getSubject().trim().isEmpty()) {
                errors.put(msg.toString(), "Le sujet ne peut pas être vide");
            }
            if (msg.getBody() == null || msg.getBody().trim().isEmpty()) {
                errors.put(msg.toString(), "Le corps du message ne peut pas être vide");
            }
        }

        return errors;
    }

    /**
     * Valide le nombre de groupes par rapport au nombre d'emails disponibles
     * @throws IllegalArgumentException si la configuration est invalide
     */
    public static void validateGroupConfiguration(int numberOfGroups, int numberOfEmails) {
        if (numberOfGroups <= 0) {
            throw new IllegalArgumentException("Le nombre de groupes doit être positif");
        }

        int minEmailsNeeded = numberOfGroups * MIN_GROUP_SIZE;
        if (numberOfEmails < minEmailsNeeded) {
            throw new IllegalArgumentException(
                String.format("Il faut au moins %d emails pour créer %d groupes de %d personnes minimum. " +
                            "Seulement %d emails disponibles.", 
                            minEmailsNeeded, numberOfGroups, MIN_GROUP_SIZE, numberOfEmails)
            );
        }

        int maxGroupsPossible = numberOfEmails / MIN_GROUP_SIZE;
        if (numberOfGroups > maxGroupsPossible) {
            throw new IllegalArgumentException(
                String.format("Avec %d emails, vous pouvez créer au maximum %d groupes de %d personnes minimum",
                            numberOfEmails, maxGroupsPossible, MIN_GROUP_SIZE)
            );
        }
    }
}
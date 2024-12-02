package org.example;
import java.io.*;
import java.util.*;

public class mail {
    public static void main(String[] args) {

        String filePath = "/mail.txt";
        List<String> emails = extractEmailsFromFile(filePath);

        // Afficher les emails extraits
        System.out.println("Adresses e-mail trouvées :");
        for (String email : emails) {
            System.out.println(email);
        }
    }

    public static List<String> extractEmailsFromFile(String filePath) {
        List<String> emailList = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                // Vérifie si la ligne contient un email valide avant de l'ajouter

                    emailList.add(line); // On utilise trim() pour enlever les espaces éventuels

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return emailList;
    }
}

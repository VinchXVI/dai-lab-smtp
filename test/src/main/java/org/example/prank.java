package org.example;

import java.io.*;
import java.util.*;
import java.util.regex.*;

public class prank {
    public static void main(String[] args) {

        String filePath = "/prank.txt";
        List<String> emails = extractPrankEmails(filePath);


        System.out.println("e-mail trouvées :");
        for (String email : emails) {
            System.out.println(email + "\n");
        }
    }

    /**
     * Fonction qui extrait des adresses e-mail formatées entre "subject:" et ";endOfPrank;".
     *
     * @param filePath Chemin d'accès au fichier texte
     * @return Une liste contenant toutes les adresses e-mail extraites
     */
    public static List<String> extractPrankEmails(String filePath) {
        List<String> emailList = new ArrayList<>();
        StringBuilder content = new StringBuilder();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                content.append(line).append("\n");
            }
        } catch (IOException e) {
            System.err.println("Erreur lors de la lecture du fichier : " + e.getMessage());
        }

        String emailRegex = "subject:(.*?)\\;endOfPrank;";
        Pattern pattern = Pattern.compile(emailRegex, Pattern.DOTALL);
        Matcher matcher = pattern.matcher(content.toString());


        while (matcher.find()) {
            emailList.add(matcher.group(1).trim());
        }

        return emailList;
    }
}

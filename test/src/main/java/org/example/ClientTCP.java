package org.example;

import java.io.*;
import java.net.Socket;
import java.util.List;
import java.util.Scanner;

import static org.example.mail.extractEmailsFromFile;
import static org.example.prank.extractPrankEmails;

public class ClientTCP {
    public static void main(String[] args) {
        // Définir l'adresse et le port du serveur
        String serveurAdresse = "127.0.0.1"; // localhost
        int port = 1025;

        List<String> test = extractEmailsFromFile("/mail.txt");
        List<String> test2 = extractPrankEmails("/prank.txt");

        String MAILFROM = "MAIL FROM:";
        String RCPT = "RCPT TO:";

        Scanner sc = new Scanner(System.in);

        // Déclare le socket et les flux
        try (Socket socket = new Socket(serveurAdresse, port);
             BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter output = new PrintWriter(socket.getOutputStream(), true)) {

            System.out.println("Connexion établie avec le serveur sur le port " + port);

            String message;

            // Envoyer un message au serveur
            //output.println("Bonjour, serveur !");
            while(true) {
                // Lire une réponse du serveur

                String reponse;

                do {
                    reponse = input.readLine();
                    System.out.println("Réponse du serveur : " + reponse);
                }while(reponse.contains("-"));

                System.out.println("Saisissez une réponse : ");
                //On vide la ligne avant d'en lire une autre
                //sc.nextLine();
                String str = sc.nextLine();

                if(str.equals("EXIT")) break;

                output.println(str);
            }

        } catch (IOException e) {
            System.err.println("Erreur : " + e.getMessage());
        }
    }
}

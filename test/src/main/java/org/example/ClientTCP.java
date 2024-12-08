package org.example;

import org.example.model.Group;
import org.example.model.Message;
import org.example.model.PrankGenerator;

import java.io.*;
import java.net.Socket;
import java.util.Map;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class ClientTCP {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 1025;
    private Socket socket;
    private BufferedReader input;
    private PrintWriter output;

    public void connect() throws IOException {
        socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
        input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        output = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8), true);
        readResponse();
        sendCommand("EHLO localhost");
    }

    private void sendCommand(String command) throws IOException {
        System.out.println("CLIENT: " + command);
        output.println(command);
        output.flush(); // Assurez-vous que la commande est envoyée immédiatement
        readResponse();
    }

    private void readResponse() throws IOException {
        String response;
        do {
            response = input.readLine();
            if (response == null) {
                throw new IOException("Connection closed by server");
            }
            System.out.println("SERVER: " + response);
        } while (response.charAt(3) == '-');
    }

    public void sendEmail(Group group, Message message) throws IOException {
        // MAIL FROM
        sendCommand("MAIL FROM: <" + group.getSender() + ">");
        
        // RCPT TO
        for (String recipient : group.getRecipients()) {
            sendCommand("RCPT TO: <" + recipient + ">");
        }
        
        // DATA
        sendCommand("DATA");
        
        // Message content with proper encoding headers
        output.write("From: " + group.getSender() + "\r\n");
        output.write("To: " + String.join(", ", group.getRecipients()) + "\r\n");
        output.write("Subject: =?UTF-8?B?" + Base64.getEncoder().encodeToString(message.getSubject().getBytes(StandardCharsets.UTF_8)) + "?=\r\n");
        output.write("MIME-Version: 1.0\r\n");
        output.write("Content-Type: text/plain; charset=UTF-8\r\n");
        output.write("Content-Transfer-Encoding: base64\r\n");
        output.write("\r\n");
        
        // Encode body in Base64
        String encodedBody = Base64.getEncoder().encodeToString(message.getBody().getBytes(StandardCharsets.UTF_8));
        output.write(encodedBody + "\r\n");
        
        output.write(".\r\n");
        output.flush();
        
        readResponse();
    }
    
    public void close() throws IOException {
        if (socket != null && !socket.isClosed()) {
            try {
                sendCommand("QUIT");
            } finally {
                socket.close();
            }
        }
    }

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java ClientTCP <numberOfGroups>");
            return;
        }

        int numberOfGroups;
        try {
            numberOfGroups = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            System.out.println("Le nombre de groupes doit être un entier valide");
            return;
        }

        ClientTCP client = new ClientTCP();
        
        try {
            PrankGenerator generator = new PrankGenerator(
                "mail.txt",
                "prank.txt",
                numberOfGroups
            );
            
            Map<Group, Message> campaign = generator.generateGroups();
            
            client.connect();
            
            for (Map.Entry<Group, Message> entry : campaign.entrySet()) {
                System.out.println("\nEnvoi d'un email au groupe : " + entry.getKey());
                client.sendEmail(entry.getKey(), entry.getValue());
                // Ajout d'une petite pause entre chaque envoi
                Thread.sleep(100);
            }
            
        } catch (Exception e) {
            System.err.println("Erreur : " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                client.close();
            } catch (IOException e) {
                System.err.println("Erreur lors de la fermeture : " + e.getMessage());
            }
        }
    }
}
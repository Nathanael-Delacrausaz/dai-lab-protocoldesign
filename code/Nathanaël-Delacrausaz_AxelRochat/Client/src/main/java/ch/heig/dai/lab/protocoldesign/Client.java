package ch.heig.dai.lab.protocoldesign;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    final String SERVER_ADDRESS = "1.2.3.4";
    final int SERVER_PORT = 1234;

    public static void main(String[] args) {
        Client client = new Client();
        client.run();
    }

    private void run() {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             Scanner scanner = new Scanner(System.in)) {

            // Receive and display the welcome message from the server
            String welcomeMessage = in.readLine();
            System.out.println("Server: " + welcomeMessage);

            while (true) {
                System.out.print("Enter operation (e.g., ADD 10 20) or 'exit' to quit: ");
                String command = scanner.nextLine();

                // Exit command
                if (command.equalsIgnoreCase("exit")) {
                    System.out.println("Exiting...");
                    break;
                }

                // Send the command to the server
                out.println(command);

                // Read and display the server response
                String response = in.readLine();
                if (response != null) {
                    System.out.println("Server: " + response);
                } else {
                    System.out.println("Server closed the connection.");
                    break;
                }
            }
        } catch (IOException e) {
            System.err.println("Error connecting to server: " + e.getMessage());
        }
    }
}

package ch.heig.dai.lab.protocoldesign;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    final int SERVER_PORT = 1234;

    public static void main(String[] args) {
        // Create a new server and run it
        Server server = new Server();
        server.run();
    }

    private void run() {
        // Open a ServerSocket on port 1234
        try (ServerSocket serverSocket = new ServerSocket(SERVER_PORT)) {
            System.out.println("Server started on port " + SERVER_PORT);

            while (true) {
                // Accept a new client connection
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected: " + clientSocket.getInetAddress());
                new ClientHandler(clientSocket).start();
            }
        } catch (IOException e) {
            System.err.println("Server error: " + e.getMessage());
        }
    }

    // Define the ClientHandler as a private static inner class
    private static class ClientHandler extends Thread {
        private final Socket clientSocket;

        public ClientHandler(Socket clientSocket) {
            this.clientSocket = clientSocket;
        }

        @Override
        public void run() {
            try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                 PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {

                out.println("Welcome to your favorite calculator!");
                out.println("Please enter an operation and 2 numbers in the format: ADD 5 3");
                out.println("Valid operations are: ADD, SUB, MULT, DIV");

                String clientMessage;
                while ((clientMessage = in.readLine()) != null) {
                    System.out.println("Received: " + clientMessage);

                    // Process the message
                    String response = processClientMessage(clientMessage);
                    out.println(response);

                    System.out.println("Processed and responded to client.");
                }
            } catch (IOException e) {
                System.err.println("Client handler error: " + e.getMessage());
            } finally {
                try {
                    clientSocket.close();
                    System.out.println("Client disconnected");
                } catch (IOException e) {
                    System.err.println("Error closing client socket: " + e.getMessage());
                }
            }
        }

        // Method to process client messages and perform arithmetic operations
        private String processClientMessage(String message) {
            String[] parts = message.split(" ");
            if (parts.length != 3) {
                return "Invalid input format. Please use: OPERATION number1 number2";
            }

            String operation = parts[0].toUpperCase();
            double num1, num2;
            try {
                num1 = Double.parseDouble(parts[1]);
                num2 = Double.parseDouble(parts[2]);
            } catch (NumberFormatException e) {
                return "Invalid numbers. Please enter valid numerical values.";
            }

            double result;
            switch (operation) {
                case "ADD":
                    result = num1 + num2;
                    break;
                case "SUB":
                    result = num1 - num2;
                    break;
                case "MULT":
                    result = num1 * num2;
                    break;
                case "DIV":
                    if (num2 == 0) {
                        return "Error: Division by zero is not allowed.";
                    }
                    result = num1 / num2;
                    break;
                default:
                    return "Invalid operation. Valid operations are: ADD, SUB, MULT, DIV.";
            }

            return "Result: " + result;
        }
    }
}

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
                new Thread(new ClientHandler(clientSocket)).start();
            }
        } catch (IOException e) {
            System.err.println("Server error: " + e.getMessage());
        }
    }

    private static class ClientHandler implements Runnable {
        private final Socket clientSocket;

        public ClientHandler(Socket clientSocket) {
            this.clientSocket = clientSocket;
        }

        @Override
        public void run() {
            try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                 PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {

                // Send welcome message to the client
                out.println("Welcome. Valid operations : ADD, SUB, MULT, DIV");

                String clientMessage;
                while ((clientMessage = in.readLine()) != null) {
                    System.out.println("Received: " + clientMessage);

                    // Split the client's message into parts
                    String[] parts = clientMessage.trim().split("\\s+");
                    if (parts.length != 3) {
                        out.println("Invalid input format. Please use: OPERATION number1 number2");
                        continue;
                    }

                    String operation = parts[0].toUpperCase();
                    double num1, num2;
                    try {
                        num1 = Double.parseDouble(parts[1]);
                        num2 = Double.parseDouble(parts[2]);
                    } catch (NumberFormatException e) {
                        out.println("Invalid numbers. Please use numeric values for number1 and number2.");
                        continue;
                    }

                    // Perform the requested operation
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
                                out.println("Division by zero is not allowed.");
                                continue;
                            }
                            result = num1 / num2;
                            break;
                        default:
                            out.println("Invalid operation. Valid operations are: ADD, SUB, MULT, DIV");
                            continue;
                    }

                    // Send the result to the client
                    out.println("Result: " + result);
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
    }
}

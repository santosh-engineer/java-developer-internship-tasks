import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

/**
 * Multithreaded Chat Server - Task 3
 * This server handles multiple client connections and broadcasts messages
 * 
 * @author Santosh
 * @version 1.0
 */
public class ChatServer {
    private static final int PORT = 12345;
    private static Set<ClientHandler> clients = ConcurrentHashMap.newKeySet();
    private static ExecutorService threadPool = Executors.newCachedThreadPool();
    
    /**
     * Main method to start the server
     */
    public static void main(String[] args) {
        System.out.println("=== Chat Server Started ===");
        System.out.println("Listening on port " + PORT);
        
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            
            // Add shutdown hook for graceful shutdown
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                System.out.println("\nShutting down server...");
                threadPool.shutdown();
                try {
                    serverSocket.close();
                } catch (IOException e) {
                    System.err.println("Error closing server socket: " + e.getMessage());
                }
            }));
            
            while (true) {
                try {
                    Socket clientSocket = serverSocket.accept();
                    ClientHandler clientHandler = new ClientHandler(clientSocket);
                    clients.add(clientHandler);
                    threadPool.execute(clientHandler);
                    
                    System.out.println("New client connected. Total clients: " + clients.size());
                } catch (IOException e) {
                    System.err.println("Error accepting client connection: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.err.println("Server error: " + e.getMessage());
        }
    }
    
    /**
     * Broadcasts a message to all connected clients
     * @param message Message to broadcast
     * @param sender Client who sent the message
     */
    public static void broadcastMessage(String message, ClientHandler sender) {
        Iterator<ClientHandler> iterator = clients.iterator();
        while (iterator.hasNext()) {
            ClientHandler client = iterator.next();
            if (client != sender) {
                if (!client.sendMessage(message)) {
                    iterator.remove();
                }
            }
        }
    }
    
    /**
     * Removes a client from the server
     * @param client Client to remove
     */
    public static void removeClient(ClientHandler client) {
        clients.remove(client);
        System.out.println("Client disconnected. Total clients: " + clients.size());
    }
    
    /**
     * Gets the count of connected clients
     * @return Number of connected clients
     */
    public static int getClientCount() {
        return clients.size();
    }
}

/**
 * Handles individual client connections
 */
class ClientHandler implements Runnable {
    private Socket socket;
    private BufferedReader reader;
    private PrintWriter writer;
    private String username;
    
    public ClientHandler(Socket socket) {
        this.socket = socket;
        try {
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            System.err.println("Error setting up client handler: " + e.getMessage());
        }
    }
    
    @Override
    public void run() {
        try {
            // Get username from client
            writer.println("Enter your username:");
            username = reader.readLine();
            
            if (username == null || username.trim().isEmpty()) {
                username = "Anonymous";
            }
            
            System.out.println("User '" + username + "' joined the chat");
            
            // Send welcome message
            writer.println("Welcome to the chat, " + username + "!");
            writer.println("Type 'QUIT' to leave the chat");
            
            // Broadcast join message
            ChatServer.broadcastMessage(username + " joined the chat", this);
            
            String message;
            while ((message = reader.readLine()) != null) {
                if ("QUIT".equalsIgnoreCase(message.trim())) {
                    break;
                }
                
                // Handle special commands
                if (message.startsWith("/")) {
                    handleCommand(message);
                } else {
                    // Broadcast regular message
                    String formattedMessage = "[" + username + "]: " + message;
                    System.out.println(formattedMessage);
                    ChatServer.broadcastMessage(formattedMessage, this);
                }
            }
            
        } catch (IOException e) {
            System.err.println("Error handling client " + username + ": " + e.getMessage());
        } finally {
            disconnect();
        }
    }
    
    /**
     * Handles special commands
     * @param command Command to handle
     */
    private void handleCommand(String command) {
        String[] parts = command.split(" ", 2);
        String cmd = parts[0].toLowerCase();
        
        switch (cmd) {
            case "/help":
                writer.println("Available commands:");
                writer.println("/help - Show this help message");
                writer.println("/users - Show connected users count");
                writer.println("/time - Show current server time");
                writer.println("QUIT - Leave the chat");
                break;
            case "/users":
                writer.println("Connected users: " + ChatServer.getClientCount());
                break;
            case "/time":
                writer.println("Server time: " + new Date());
                break;
            default:
                writer.println("Unknown command. Type /help for available commands.");
        }
    }
    
    /**
     * Sends a message to this client
     * @param message Message to send
     * @return true if successful, false otherwise
     */
    public boolean sendMessage(String message) {
        try {
            writer.println(message);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Disconnects the client
     */
    private void disconnect() {
        try {
            ChatServer.removeClient(this);
            if (username != null) {
                ChatServer.broadcastMessage(username + " left the chat", this);
                System.out.println("User '" + username + "' left the chat");
            }
            
            if (reader != null) reader.close();
            if (writer != null) writer.close();
            if (socket != null) socket.close();
        } catch (IOException e) {
            System.err.println("Error during disconnect: " + e.getMessage());
        }
    }
    
    public String getUsername() {
        return username;
    }
}
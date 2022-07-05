package server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import server.storage.DataStorage;

public class Server {
    private static final String address = "127.0.0.1";
    private static final int port = 8090;
    private static final ExecutorService executor =
            Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    public void run() {
        try (ServerSocket server = new ServerSocket(port, 50, InetAddress.getByName(address))) {
            DataStorage.init();
            System.out.println("Server started!");
            while (true) {
                System.out.println("Waiting for a new client...");
                Socket socket = server.accept();
                System.out.println("Client connected.");
                System.out.println("Processing request...");
                executor.execute(new ClientHandler(socket));
            }

            /*while (true) {
                System.out.println("Waiting for a new client...");
                Socket socket = server.accept();
                System.out.println("Client connected.");
                new Thread(() -> {
                    ClientHandler clientHandler = new ClientHandler(socket);
                    System.out.println("Processing request...");
                    clientHandler.run();
                }).start();
            }*/

        } catch (IOException e) {
            throw new RuntimeException("Can't start the server with port "
                    + port + " and address " + address, e);
        }
    }
}

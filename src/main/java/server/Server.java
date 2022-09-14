package server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import server.storage.DataStorage;

public class Server implements Runnable {
    private static final String address = "127.0.0.1";
    private static final int port = 8090;
    private static final ExecutorService executor =
            Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    public void run() {
        try (ServerSocket server = new ServerSocket(port, 50, InetAddress.getByName(address))) {
            DataStorage.init();
            System.out.println("Server: Server started!");
            while (true) {
                System.out.println("Server: Waiting for a new client...");
                Socket client = server.accept();
                System.out.println("Server: Client connected.");
                System.out.println("Server: Processing request...");
                executor.execute(new ClientHandler(client));
            }
        } catch (IOException e) {
            throw new RuntimeException("Can't start the server with port "
                    + port + " and address " + address, e);
        }
    }
}

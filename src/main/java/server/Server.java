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
            while (!executor.isShutdown()) {
                System.out.println("Waiting for a new client...");
                try {
                    Socket socket = server.accept();
                    System.out.println("Processing request...");
                    executor.execute(new ClientHandler(socket));
                } catch(IOException e) {
                    throw new RuntimeException("Can't accept connection for a client");
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Can't start the server with port "
                    + port + " and address " + address, e);
        }
    }

    public static void closeServer() {
        executor.shutdown();
        System.out.println("Server was closed.");
        System.exit(0);
    }
}

package server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import server.storage.DataStorage;

public class Server {
    private static final String address = "127.0.0.1";
    private static final int port = 8090;
    private static final ExecutorService executor =
            Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    private static boolean keepConnection = true;

    public void run() {
        try (ServerSocket server = new ServerSocket(port, 50, InetAddress.getByName(address))) {
            DataStorage.init();
            System.out.println("Server started!");
            while (keepConnection) {
                ClientHandler clientHandler = new ClientHandler(server);
                executor.submit(clientHandler);
            }
            executor.shutdown();
        } catch (IOException e) {
            throw new RuntimeException("Can't start the server with port "
                    + port + " and address " + address, e);
        }
    }

    public static void closeServer() {
        keepConnection = false;
    }
}

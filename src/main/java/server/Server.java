package server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import server.storage.DataStorage;
import server.util.Params;

public class Server implements Runnable {
    private static final ExecutorService executor =
            Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    private boolean keepOn = true;

    public void run() {
        try (ServerSocket server = new ServerSocket(
                Params.serverPort,
                50,
                InetAddress.getByName(Params.serverAddress))) {
            DataStorage.init();
            System.out.println("Server: Server started!");
            while (keepOn) {
                System.out.println("Server: Waiting for a new client...");
                Socket client = server.accept();
                System.out.println("Server: Client connected.");
                System.out.println("Server: Processing request...");
                executor.execute(new ClientHandler(client));
            }
            System.out.println("Server: Server was stopped.");
        } catch (IOException e) {
            throw new RuntimeException("Can't start the server with port "
                    + Params.serverPort + " and address " + Params.serverAddress, e);
        }
    }

    public void stop() {
        this.keepOn = false;
    }
}

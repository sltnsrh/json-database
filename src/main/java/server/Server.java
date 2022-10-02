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
    private final ExecutorService executor =
            Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    private final ServerSocket serverSocket;

    {
        try {
            serverSocket = new ServerSocket(
                    Params.SERVER_PORT, 50, InetAddress.getByName(Params.SERVER_ADDRESS));
        } catch (IOException e) {
            throw new RuntimeException("Can't start the server with port: " + Params.SERVER_PORT
            + " and address: " + Params.SERVER_ADDRESS);
        }
    }

    public void run() {
        try {
            DataStorage.init();
            System.out.println("Server: Server started!");
            while (!serverSocket.isClosed()) {
                System.out.println("Server: Waiting for a new client...");
                Socket client = serverSocket.accept();
                System.out.println("Server: Client connected.");
                executor.execute(new ClientHandler(client, this));
                System.out.println("Server: Processing request...");
                try {
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            System.out.println("Server: Server was stopped.");
        } catch (IOException e) {
            throw new RuntimeException("Can't accept a client, because server is closed: " + serverSocket.isClosed());
        } finally {
            executor.shutdown();
        }
    }

    public void stop() {
        try {
            this.serverSocket.close();
        } catch (IOException e) {
            throw new RuntimeException("Can't close server socket connection.");
        }
    }
}

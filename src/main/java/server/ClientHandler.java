package server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import server.service.ProcessExecutor;

public class ClientHandler implements Runnable {
    private static final String EXIT_MARK = "exit";
    private final ServerSocket server;

    public ClientHandler(ServerSocket server) {
        this.server = server;
    }

    @Override
    public void run() {
        try (
                Socket socket = server.accept();
                DataInputStream input = new DataInputStream(socket.getInputStream());
                DataOutputStream output = new DataOutputStream(socket.getOutputStream())
        ) {
            String jsonClientRequest = input.readUTF();
            ServerResponse serverResponse = getServerResponse(jsonClientRequest);
            String jsonServerResponse = getJsonServerResponse(serverResponse);
            output.writeUTF(jsonServerResponse);
            if (jsonClientRequest.toLowerCase().contains(EXIT_MARK)) {
                Server.closeServer();
            }
        } catch (IOException e) {
            throw new RuntimeException("Can't accept client socket connection.", e);
        }
    }

    private String getJsonServerResponse(ServerResponse serverResponse) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(serverResponse);
    }

    private ServerResponse getServerResponse(String jsonClientRequest) {
        ProcessExecutor processExecutor = new ProcessExecutor();
        return processExecutor.getResponse(jsonClientRequest);
    }
}

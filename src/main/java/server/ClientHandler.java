package server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import server.service.ProcessExecutor;
import server.service.ResponseProducer;

public class ClientHandler implements Runnable {
    private final Socket client;

    public ClientHandler(Socket client) {
        this.client = client;
    }

    @Override
    public void run() {
        try (
                DataInputStream input = new DataInputStream(client.getInputStream());
                DataOutputStream output = new DataOutputStream(client.getOutputStream())
        ) {
            String jsonClientRequest = input.readUTF();
            ResponseProducer responseProducer = new ResponseProducer();
            ServerResponse serverResponse = new ProcessExecutor(responseProducer).getResponse(jsonClientRequest);
            String jsonServerResponse = getJsonServerResponse(serverResponse);
            output.writeUTF(jsonServerResponse);
        } catch (IOException e) {
            throw new RuntimeException("Can't accept client socket connection.", e);
        }
    }

    private String getJsonServerResponse(ServerResponse serverResponse) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(serverResponse);
    }
}

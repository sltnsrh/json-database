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
    private static final String EXIT_MARK = "exit";
    private final Socket client;
    private boolean stayOnline = true;

    public ClientHandler(Socket client) {
        this.client = client;
    }

    @Override
    public void run() {
        try {
            DataInputStream input = new DataInputStream(client.getInputStream());
            DataOutputStream output = new DataOutputStream(client.getOutputStream());
            while (stayOnline) {
                String jsonClientRequest = input.readUTF();
                ResponseProducer responseProducer = new ResponseProducer();
                ServerResponse serverResponse = new ProcessExecutor(responseProducer).getResponse(jsonClientRequest);
                String jsonServerResponse = getJsonServerResponse(serverResponse);
                output.writeUTF(jsonServerResponse);
                if (jsonClientRequest.toLowerCase().contains(EXIT_MARK)) {
                    stayOnline = false;
                    client.close();
                    input.close();
                    output.close();
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Can't accept client socket connection.", e);
        }
    }

    private String getJsonServerResponse(ServerResponse serverResponse) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(serverResponse);
    }
}

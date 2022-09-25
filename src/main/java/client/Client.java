package client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import server.util.Params;

public class Client {

    public void runClient(RequestToServer requestToServer) {
        try (
                Socket socket = new Socket(
                        InetAddress.getByName(Params.serverAddress), Params.serverPort);
                DataInputStream input = new DataInputStream(socket.getInputStream());
                DataOutputStream output = new DataOutputStream(socket.getOutputStream())
        ) {
            System.out.println("Client: Client started!");
            String jsonClientRequest = requestToServer.getStringRequest();
            output.writeUTF(jsonClientRequest);
            System.out.println("Client: Sent: " + jsonClientRequest);
            String serverMsg = input.readUTF();
            System.out.println("Client: Received: " + serverMsg);
            } catch (IOException ex) {
            throw new RuntimeException("Client: Can't establish socket connection.", ex);
        }
    }
}

package client;

import client.communication.Client;
import client.util.RequestToServer;
import com.beust.jcommander.JCommander;

public class Main {

    public static void main(String[] args) {
        RequestToServer requestToServer = new RequestToServer();
        JCommander.newBuilder()
                .addObject(requestToServer)
                .build()
                .parse(args);
        Client client = new Client();
        client.run(requestToServer);
    }
}

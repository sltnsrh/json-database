package server.service;

import client.Client;
import client.RequestToServer;
import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import server.Server;

class DataSetterTest {
    private static final ExecutorService executor = Executors.newFixedThreadPool(3);

    @BeforeAll
    static void setup() throws InterruptedException {
        File db = new File("src/main/resources/data/db.json");
        db.delete();
        executor.execute(new Server());
        Thread.sleep(1000);
    }

    @Test
    public void setValidDataWithOnePrimitiveKey() {
        RequestToServer setRequest = RequestToServer.builder()
                .setType("set")
                .setKey("Person")
                .setValue("Bob")
                .build();
        Client client = new Client();
        client.runClient(setRequest);
    }

    @Test
    public void setValidDataFromJsonFileWithExistingKey() {
        RequestToServer setRequest = RequestToServer.builder()
                .setFileName("test2Set.json")
                .build();
        Client client = new Client();
        client.runClient(setRequest);
    }

    @Test
    public void setValidDataFromJsonFileWithNotExistingKey() {
        RequestToServer setRequest = RequestToServer.builder()
                .setFileName("test3Set.json")
                .build();
        Client client = new Client();
        client.runClient(setRequest);
    }

}
package server.service;

import client.Client;
import client.RequestToServer;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import server.Server;
import server.util.Params;

class DataSetterTest {
    private static Client client;
    private static Server server;

    @BeforeAll
    static void setup() {
        File db = new File(Params.DB_PATH);
        if (db.exists()) {
            db.delete();
        }
        server = new Server();
        new Thread(server).start();
        client = new Client();
    }

    @Test
    public void setValidDataWithOnePrimitiveKey() {
        RequestToServer setRequest = RequestToServer.builder()
                .setType("set")
                .setKey("Person")
                .setValue("Bob")
                .build();
        client.runClient(setRequest);
        assertTrueDataExistInDb("\"Person\": \"Bob\"");
    }

    @Test
    public void setValidDataFromJsonFileWithExistingKey() {
        RequestToServer setRequest = RequestToServer.builder()
                .setFileName("test2Set.json")
                .build();
        client.runClient(setRequest);
        assertTrueDataExistInDb("launches\": \"88");
    }

    @Test
    public void setValidDataFromJsonFileWithNotExistingKey() {
        RequestToServer setRequest = RequestToServer.builder()
                .setFileName("test3Set.json")
                .build();
        client.runClient(setRequest);
        assertTrueDataExistInDb("name\": \"Tesla");
    }

    private void assertTrueDataExistInDb(String data) {
        try (Stream<String> dbStream = Files.lines(Paths.get(Params.DB_PATH))) {
            Assertions.assertTrue(
                    dbStream.anyMatch(line -> line.contains(data)));
        } catch (IOException e) {
            throw new RuntimeException("Can't read file with path: " + Params.DB_PATH);
        }
    }

    @AfterAll
    static void stop() {
        server.stop();
    }
}

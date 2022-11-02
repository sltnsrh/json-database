package server.service;

import client.communication.Client;
import client.util.RequestToServer;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import server.communication.Server;

class DataGetterTest {
    private static final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private static Client client;
    private static Server server;

    @BeforeAll
    static void setup() {
        String DB_FILE_PATH = "src/main/resources/data/db.json";
        File db = new File(DB_FILE_PATH);
        if (db.exists()) {
            db.delete();
        }
        server = new Server();
        new Thread(server).start();
        RequestToServer setRequest = RequestToServer.builder()
                .setFileName("test3Set.json")
                .build();
        client = new Client();
        client.run(setRequest);
        System.setOut(new PrintStream(outContent));
    }

    @Test
    public void getValueWithKeysArray() {
        RequestToServer setRequest = RequestToServer.builder()
                .setFileName("testGet.json")
                .build();
        client.run(setRequest);
        Assertions.assertTrue(outContent.toString().contains("\"value\": \"Tesla\""));
    }

    @Test
    public void getValueWithPrimitiveKey() {
        RequestToServer setRequest = RequestToServer.builder()
                .setType("get")
                .setKey("machine")
                .build();
        client.run(setRequest);
        Assertions.assertTrue(outContent.toString().contains("Tesla"));
    }

    @Test
    public void getValueWithNoteExistingKey() {
        RequestToServer setRequest = RequestToServer.builder()
                .setType("get")
                .setKey("notexistkey")
                .build();
        client.run(setRequest);
        Assertions.assertTrue(outContent.toString().contains("No such key"));
    }

    @Test
    public void getValueWithKeysArrayWithOneKey() {
        RequestToServer setRequest = RequestToServer.builder()
                .setFileName("test2Get.json")
                .build();
        client.run(setRequest);
        Assertions.assertTrue(outContent.toString().contains("\"value\": {\n"
                + "    \"car\": {\n"
                + "      \"name\": \"Tesla\"\n"
                + "    }\n"
                + "  }"));
    }

    @Test
    public void getValueWithKeysArrayWithOneNotExistingKey() {
        RequestToServer setRequest = RequestToServer.builder()
                .setFileName("test3Get.json")
                .build();
        client.run(setRequest);
        Assertions.assertTrue(outContent.toString().contains("No such key"));
    }

    @Test
    public void getValueWithKeysArrayAndNotExistKey() {
        RequestToServer setRequest = RequestToServer.builder()
                .setFileName("test4Get.json")
                .build();
        client.run(setRequest);
        Assertions.assertTrue(outContent.toString().contains("No such key"));
    }

    @Test
    public void getValueWithKeysArrayAndValueAsJsonObject() {
        RequestToServer setRequest = RequestToServer.builder()
                .setFileName("test5Get.json")
                .build();
        client.run(setRequest);
        Assertions.assertTrue(outContent.toString().contains("\"name\": \"Tesla\""));
    }

    @AfterAll
    static void stop() {
        server.stop();
    }
}

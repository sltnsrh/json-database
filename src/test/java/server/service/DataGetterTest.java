package server.service;

import client.Client;
import client.RequestToServer;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class DataGetterTest {
    private static final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private static final Client client = new Client();

    @BeforeAll
    static void setup() {
        String DB_FILE_PATH = "src/main/resources/data/db.json";
        File db = new File(DB_FILE_PATH);
        if (db.exists()) {
            db.delete();
        }
        RequestToServer setRequest = RequestToServer.builder()
                .setFileName("test3Set.json")
                .build();
        client.runClient(setRequest);
        System.setOut(new PrintStream(outContent));
    }

    @Test
    public void getValueWithKeysArray() {
        RequestToServer setRequest = RequestToServer.builder()
                .setFileName("testGet.json")
                .build();
        client.runClient(setRequest);
        Assertions.assertTrue(outContent.toString().contains("\"value\": \"Tesla\""));
    }

    @Test
    public void getValueWithPrimitiveKey() {
        RequestToServer setRequest = RequestToServer.builder()
                .setType("get")
                .setKey("machine")
                .build();
       client.runClient(setRequest);
        Assertions.assertTrue(outContent.toString().contains("Tesla"));
    }

    @Test
    public void getValueWithNoteExistingKey() {
        RequestToServer setRequest = RequestToServer.builder()
                .setType("get")
                .setKey("notexistkey")
                .build();
        client.runClient(setRequest);
        Assertions.assertTrue(outContent.toString().contains("No such key"));
    }
}

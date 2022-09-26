package server.service;

import client.Client;
import client.RequestToServer;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class DataSetterTest {
    private static final String DB_FILE_PATH = "src/main/resources/data/db.json";
    private static final Client client = new Client();

    @BeforeAll
    static void setup() {
        File db = new File(DB_FILE_PATH);
        if (db.exists()) {
            db.delete();
        }
    }

    @Test
    public void setValidDataWithOnePrimitiveKey() {
        RequestToServer setRequest = RequestToServer.builder()
                .setType("set")
                .setKey("Person")
                .setValue("Bob")
                .build();
        client.runClient(setRequest);
        try {
            Assertions.assertTrue(
                    Files.lines(Path.of(DB_FILE_PATH))
                            .anyMatch(line -> line.contains("\"Person\": \"Bob\"")));
        } catch (IOException e) {
            throw new RuntimeException("Can't read file with path: " + DB_FILE_PATH);
        }
    }

    @Test
    public void setValidDataFromJsonFileWithExistingKey() {
        RequestToServer setRequest = RequestToServer.builder()
                .setFileName("test2Set.json")
                .build();
        client.runClient(setRequest);
        try {
            Assertions.assertTrue(
                    Files.lines(Path.of(DB_FILE_PATH))
                            .anyMatch(line -> line.contains("launches\": \"88")));
        } catch (IOException e) {
            throw new RuntimeException("Can't read file with path: " + DB_FILE_PATH);
        }
    }

    @Test
    public void setValidDataFromJsonFileWithNotExistingKey() {
        RequestToServer setRequest = RequestToServer.builder()
                .setFileName("test3Set.json")
                .build();
        client.runClient(setRequest);
        try {
            Assertions.assertTrue(
                    Files.lines(Path.of(DB_FILE_PATH))
                            .anyMatch(line -> line.contains("name\": \"Tesla")));
        } catch (IOException e) {
            throw new RuntimeException("Can't read file with path: " + DB_FILE_PATH);
        }
    }
}

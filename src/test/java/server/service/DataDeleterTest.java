package server.service;

import client.Client;
import client.RequestToServer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.Server;
import server.storage.DataStorage;
import server.util.Params;

class DataDeleterTest {
    private static final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
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
        System.setOut(new PrintStream(outContent));
    }

    @BeforeEach
    void rewriteDb() {
        String jsonData = "{\n"
                + "    \"rocket\": {\n"
                + "      \"launches\": \"88\",\n"
                + "      \"name\": \"falcon\"\n"
                + "    }\n"
                + "}";
        JsonElement jsonContent = JsonParser.parseString(jsonData);
        DataStorage.getDataBase().add("person", jsonContent);
        DataStorage.writeDbToFile();
    }

    @Test
    void dataDeleterFromJsonFileWithKeyAsArray() {
        RequestToServer setRequest = RequestToServer.builder()
                .setFileName("testDelete.json")
                .build();
        client.runClient(setRequest);
        assertFalseDataExistInDb("\"name\": \"falcon\"");
    }

    @Test
    void dataDeleterWithPrimitiveKey() {
        RequestToServer deleteRequest = RequestToServer.builder()
                .setType("delete")
                .setKey("person")
                .build();
        client.runClient(deleteRequest);
        assertFalseDataExistInDb("person");
    }

    @Test
    void dataDeleterWithPrimitiveNotExistKey() {
        RequestToServer deleteRequest = RequestToServer.builder()
                .setType("delete")
                .setKey("noexistkey")
                .build();
        client.runClient(deleteRequest);
        Assertions.assertTrue(outContent.toString().contains("No such key"));
    }

    @Test
    void dataDeleterWithArrayNotExistKey() {
        RequestToServer deleteRequest = RequestToServer.builder()
                .setFileName("test2Delete.json")
                .build();
        client.runClient(deleteRequest);
        Assertions.assertTrue(outContent.toString().contains("No such key"));
    }

    private void assertFalseDataExistInDb(String data) {
        try (Stream<String> dbStream = Files.lines(Paths.get(Params.DB_PATH))) {
            Assertions.assertFalse(
                    dbStream.anyMatch(line -> line.contains(data)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @AfterAll
    static void stop() {
        server.stop();
    }
}
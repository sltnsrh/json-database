package server.storage;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import server.util.Params;

public final class DataStorage {
    private static JsonObject dataBase;

    private DataStorage() {
    }

    public static JsonObject getDataBase() {
        return dataBase;
    }

    public static void init() {
        try {
            dataBase = readDbFromFile(Path.of(Params.DB_PATH)).getAsJsonObject();
        } catch (IOException e) {
            throw new RuntimeException("Can't get data from the file: " + Params.DB_PATH, e);
        }
    }

    private static JsonObject readDbFromFile(Path path) throws IOException {
        try (Reader reader = Files.newBufferedReader(path)) {
            return (JsonObject) JsonParser.parseReader(reader);
        } catch (Exception e) {
            Files.writeString(path, "{}");
            try (Reader reader = Files.newBufferedReader(path)) {
                return (JsonObject) JsonParser.parseReader(reader);
            }
        }
    }

    public static void writeDbToFile() {
        File file = new File(Params.DB_PATH);
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(new GsonBuilder().setPrettyPrinting().create().toJson(dataBase));
        } catch (IOException e) {
            throw new RuntimeException("Can't write data to DB " + Params.DB_PATH, e);
        }
    }

    public static JsonElement findElement(JsonArray keys) {
        JsonElement searchElement = DataStorage.dataBase;
        for (JsonElement key: keys) {
            if (!searchElement.isJsonObject() || !searchElement.getAsJsonObject().has(key.getAsString())) {
                return null;
            }
            searchElement = searchElement.getAsJsonObject().get(key.getAsString());
        }
        return searchElement;
    }

    public static JsonElement createElement(JsonArray keys) {
        JsonElement searchElement = DataStorage.dataBase;
        for (JsonElement key: keys) {
            if (!searchElement.getAsJsonObject().has(key.getAsString())) {
                searchElement.getAsJsonObject().add(key.getAsString(), new JsonObject());
            }
            searchElement = searchElement.getAsJsonObject().get(key.getAsString());
        }
        return searchElement;
    }
}

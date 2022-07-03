package server.service;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import server.RequestFromClient;
import server.storage.DataStorage;
import server.util.ReadWriteLocker;

public class DataDeleter implements Executable {

    @Override
    public String processData(RequestFromClient requestFromClient) {
        try {
            ReadWriteLocker.writeLock.lock();
            JsonElement key = requestFromClient.getKey();
            if (key.isJsonPrimitive() && DataStorage.getDataBase().has(key.getAsString())) {
                DataStorage.getDataBase().remove(key.getAsString());
            } else if (key.isJsonArray()) {
                JsonArray keys = key.getAsJsonArray();
                String keyToDelete = keys.remove(keys.size() - 1).getAsString();
                JsonElement jsonElementToDelete = DataStorage.findElement(keys);
                if (jsonElementToDelete == null) {
                    return "ERROR";
                }
                jsonElementToDelete.getAsJsonObject().remove(keyToDelete);
            }
            DataStorage.writeDbToFile();
        } finally {
            ReadWriteLocker.writeLock.unlock();
        }
        return "OK";
    }
}

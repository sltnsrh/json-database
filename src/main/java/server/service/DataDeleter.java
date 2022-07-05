package server.service;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import server.RequestFromClient;
import server.ServerResponse;
import server.storage.DataStorage;
import server.util.ReadWriteLocker;
import server.util.ResponseProducer;

public class DataDeleter implements Executable {

    @Override
    public ServerResponse processData(RequestFromClient requestFromClient) {
        try {
            ReadWriteLocker.writeLock.lock();
            JsonElement key = requestFromClient.getKey();
            if (key.isJsonPrimitive() && DataStorage.getDataBase().has(key.getAsString())) {
                DataStorage.getDataBase().remove(key.getAsString());
                DataStorage.writeDbToFile();
                return ResponseProducer.getOk();
            }
            if (key.isJsonArray()) {
                JsonArray keys = key.getAsJsonArray();
                String keyToDelete = keys.remove(keys.size() - 1).getAsString();
                JsonElement jsonElementToDelete = DataStorage.findElement(keys);
                if (jsonElementToDelete == null || !jsonElementToDelete.getAsJsonObject().has(keyToDelete)) {
                    return ResponseProducer.getNoSuchKey();
                }
                jsonElementToDelete.getAsJsonObject().remove(keyToDelete);
                DataStorage.writeDbToFile();
                return ResponseProducer.getOk();
            }
        } finally {
            ReadWriteLocker.writeLock.unlock();
        }
        return ResponseProducer.getNoSuchKey();
    }
}

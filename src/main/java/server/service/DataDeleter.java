package server.service;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import server.communication.RequestFromClient;
import server.communication.ServerResponse;
import server.storage.DataStorage;
import server.util.ReadWriteLocker;

public class DataDeleter implements Executable {

    @Override
    public ServerResponse processData(RequestFromClient requestFromClient,
                                      ResponseProducer responseProducer) {
        try {
            ReadWriteLocker.writeLock.lock();
            JsonElement key = requestFromClient.getKey();
            if (key.isJsonPrimitive() && DataStorage.getDataBase().has(key.getAsString())) {
                return getResponseFromPrimitive(key, responseProducer);
            }
            if (key.isJsonArray()) {
                return getResponseFromJsonArray(key, responseProducer);
            }
        } finally {
            ReadWriteLocker.writeLock.unlock();
        }
        return responseProducer.getNoSuchKey();
    }

    private ServerResponse getResponseFromPrimitive(
            JsonElement key, ResponseProducer responseProducer
    ) {
        DataStorage.getDataBase().remove(key.getAsString());
        DataStorage.writeDbToFile();
        return responseProducer.getOk();
    }

    private ServerResponse getResponseFromJsonArray(
            JsonElement key, ResponseProducer responseProducer
    ) {
        JsonArray keys = key.getAsJsonArray();
        String keyToDelete = keys.remove(keys.size() - 1).getAsString();
        JsonElement jsonElementToDelete = DataStorage.findElement(keys);
        if (jsonElementToDelete == null || !jsonElementToDelete.getAsJsonObject().has(keyToDelete)) {
            return responseProducer.getNoSuchKey();
        }
        jsonElementToDelete.getAsJsonObject().remove(keyToDelete);
        DataStorage.writeDbToFile();
        return responseProducer.getOk();
    }
}

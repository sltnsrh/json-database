package server.service;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import server.RequestFromClient;
import server.storage.DataStorage;
import server.util.ReadWriteLocker;

public class DataGetter implements Executable {

    @Override
    public String processData(RequestFromClient requestFromClient) {
        try {
            ReadWriteLocker.readLock.lock();
            JsonElement key = requestFromClient.getKey();
            if (key.isJsonPrimitive() && DataStorage.getDataBase().has(key.getAsString())) {
                return DataStorage.getDataBase().get(key.getAsString()).toString();
            } else if (key.isJsonArray()) {
                JsonArray keys = key.getAsJsonArray();
                if (keys.size() == 1) {
                    return DataStorage.getDataBase().get(keys.getAsString()).toString();
                }
                String keyToGet = keys.remove(keys.size() - 1).getAsString();
                JsonElement jsonElementToGet = DataStorage.findElement(keys);
                if (jsonElementToGet == null) {
                    return "ERROR";
                }
                return jsonElementToGet.getAsJsonObject().get(keyToGet).getAsString();
            }
        } finally {
            ReadWriteLocker.readLock.unlock();
        }
        return "OK";
    }
}

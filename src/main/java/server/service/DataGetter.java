package server.service;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import server.RequestFromClient;
import server.ServerResponse;
import server.storage.DataStorage;
import server.util.ReadWriteLocker;
import server.util.ResponseProducer;

public class DataGetter implements Executable {

    @Override
    public ServerResponse processData(RequestFromClient requestFromClient) {
        try {
            ReadWriteLocker.readLock.lock();
            JsonElement key = requestFromClient.getKey();
            if (key.isJsonPrimitive() && DataStorage.getDataBase().has(key.getAsString())) {
                return getResponseFromPrimitive(key);
            }
            if (key.isJsonArray()) {
                return getResponseFromJsonArray(key);
            }
        } finally {
            ReadWriteLocker.readLock.unlock();
        }
        return ResponseProducer.getNoSuchKey();
    }

    private ServerResponse getResponseFromPrimitive(JsonElement key) {
        String value = DataStorage.getDataBase().get(key.getAsString()).toString();
        return ResponseProducer.getValueOk(value);
    }

    private ServerResponse getResponseFromJsonArray(JsonElement key) {
        JsonArray keys = key.getAsJsonArray();
        if (keys.size() == 1) {
            String value = DataStorage.getDataBase().get(keys.getAsString()).toString();
            return ResponseProducer.getValueOk(value);
        }
        JsonElement jsonElementToGet = DataStorage.findElement(keys);
        if (jsonElementToGet == null) {
            return ResponseProducer.getNoSuchKey();
        }
        String value = getValueFromJsonElement(jsonElementToGet);
        return ResponseProducer.getValueOk(value);
    }

    private String getValueFromJsonElement(JsonElement jsonElement) {
        String value;
        if (jsonElement.isJsonObject()) {
            value = jsonElement.getAsJsonObject().toString();
        } else {
            value = jsonElement.getAsJsonPrimitive().toString();
        }
        return value;
    }
}

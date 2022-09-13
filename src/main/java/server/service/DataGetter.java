package server.service;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import server.RequestFromClient;
import server.ServerResponse;
import server.storage.DataStorage;
import server.util.ReadWriteLocker;

public class DataGetter implements Executable {

    @Override
    public ServerResponse processData(RequestFromClient requestFromClient,
                                      ResponseProducer responseProducer) {
        try {
            ReadWriteLocker.readLock.lock();
            JsonElement key = requestFromClient.getKey();
            if (key.isJsonPrimitive() && DataStorage.getDataBase().has(key.getAsString())) {
                return getResponseFromPrimitive(key, responseProducer);
            }
            if (key.isJsonArray()) {
                return getResponseFromJsonArray(key, responseProducer);
            }
        } finally {
            ReadWriteLocker.readLock.unlock();
        }
        return responseProducer.getNoSuchKey();
    }

    private ServerResponse getResponseFromPrimitive(JsonElement key,
                                                    ResponseProducer responseProducer) {
        String value = DataStorage.getDataBase().get(key.getAsString()).toString();
        return responseProducer.getValueOk(value);
    }

    private ServerResponse getResponseFromJsonArray(JsonElement key,
                                                    ResponseProducer responseProducer) {
        JsonArray keys = key.getAsJsonArray();
        if (keys.size() == 1) {
            String value = DataStorage.getDataBase().get(keys.getAsString()).toString();
            return responseProducer.getValueOk(value);
        }
        JsonElement jsonElementToGet = DataStorage.findElement(keys);
        if (jsonElementToGet == null) {
            return responseProducer.getNoSuchKey();
        }
        String value = getValueFromJsonElement(jsonElementToGet);
        return responseProducer.getValueOk(value);
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

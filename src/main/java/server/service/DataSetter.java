package server.service;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import server.communication.RequestFromClient;
import server.communication.ServerResponse;
import server.storage.DataStorage;
import server.util.ReadWriteLocker;

public class DataSetter implements Executable {

    @Override
    public ServerResponse processData(RequestFromClient requestFromClient,
                                      ResponseProducer responseProducer) {
        try {
            ReadWriteLocker.writeLock.lock();
            JsonElement key = requestFromClient.getKey();
            JsonElement value = requestFromClient.getValue();
            if (key.isJsonPrimitive()) {
                setKeyFromPrimitive(key, value, responseProducer);
            }
            if (key.isJsonArray()) {
                setKeyFromJsonArray(key, value, responseProducer);
            }
        } finally {
            ReadWriteLocker.writeLock.unlock();
        }
        return responseProducer.getOk();
    }

    private void setKeyFromPrimitive(
            JsonElement key, JsonElement value, ResponseProducer responseProducer
    ) {
        DataStorage.getDataBase().add(key.getAsString(), value);
        DataStorage.writeDbToFile();
    }

    private void setKeyFromJsonArray(
            JsonElement key, JsonElement value, ResponseProducer responseProducer
    ) {
        JsonArray keys = key.getAsJsonArray();
        String keyToAddValue = keys.remove(keys.size() - 1).getAsString();
        JsonElement jsonElementToChange = DataStorage.findElement(keys);
        if (jsonElementToChange == null) {
            jsonElementToChange = DataStorage.createElement(keys);
        }
        jsonElementToChange.getAsJsonObject().add(keyToAddValue, value);
        DataStorage.writeDbToFile();
    }
}

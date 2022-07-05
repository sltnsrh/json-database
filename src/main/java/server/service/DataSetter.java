package server.service;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import server.RequestFromClient;
import server.ServerResponse;
import server.storage.DataStorage;
import server.util.ReadWriteLocker;
import server.util.ResponseProducer;

public class DataSetter implements Executable {

    @Override
    public ServerResponse processData(RequestFromClient requestFromClient) {
        try {
            ReadWriteLocker.writeLock.lock();
            JsonElement key = requestFromClient.getKey();
            JsonElement value = requestFromClient.getValue();
            if (key.isJsonPrimitive()) {
                DataStorage.getDataBase().add(key.getAsString(), value);
                DataStorage.writeDbToFile();
                return ResponseProducer.getOk();
            }
            if (key.isJsonArray()) {
                JsonArray keys = key.getAsJsonArray();
                String keyToAddValue = keys.remove(keys.size() - 1).getAsString();
                JsonElement jsonElementToChange = DataStorage.findElement(keys);
                if (jsonElementToChange == null) {
                    jsonElementToChange = DataStorage.createElement(keys);
                }
                jsonElementToChange.getAsJsonObject().add(keyToAddValue, value);
                DataStorage.writeDbToFile();
                return ResponseProducer.getOk();
            }
        } finally {
            ReadWriteLocker.writeLock.unlock();
        }
        return ResponseProducer.getNoSuchKey();
    }
}

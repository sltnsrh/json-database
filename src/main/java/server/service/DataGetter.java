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
                String value = DataStorage.getDataBase().get(key.getAsString()).toString();
                return ResponseProducer.getValueOk(value);
            }
            if (key.isJsonArray()) {
                JsonArray keys = key.getAsJsonArray();
                if (keys.size() == 1) {
                    String value = DataStorage.getDataBase().get(keys.getAsString()).toString();
                    return ResponseProducer.getValueOk(value);
                }
                JsonElement jsonElementToGet = DataStorage.findElement(keys);
                if (jsonElementToGet == null) {
                    return ResponseProducer.getNoSuchKey();
                }
                String value;
                if (jsonElementToGet.isJsonObject()) {
                    value = jsonElementToGet.getAsJsonObject().toString();
                } else {
                    value = jsonElementToGet.getAsJsonPrimitive().toString();
                }
                return ResponseProducer.getValueOk(value);
            }
        } finally {
            ReadWriteLocker.readLock.unlock();
        }
        return ResponseProducer.getNoSuchKey();
    }
}

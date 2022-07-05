package server.service;

import com.google.gson.Gson;
import server.RequestFromClient;
import server.ServerResponse;
import server.util.Operations;
import server.util.ResponseProducer;

public class ProcessExecutor {
    private static final String EXIT_MARK = "exit";

    public ServerResponse getResponse(String jsonFromClient) {
        RequestFromClient requestFromClient = new Gson()
                .fromJson(jsonFromClient, RequestFromClient.class);
        if (requestFromClient.getType().equalsIgnoreCase(EXIT_MARK)) {
            return ResponseProducer.getOk();
        }
        if (Operations.processesMap.containsKey(requestFromClient.getType())) {
            return Operations.processesMap
                    .get(requestFromClient.getType())
                    .processData(requestFromClient);
        }
        return ResponseProducer.getNoSuchKey();
    }
}

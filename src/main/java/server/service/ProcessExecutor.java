package server.service;

import com.google.gson.Gson;
import server.communication.RequestFromClient;
import server.communication.ServerResponse;
import server.util.Operations;

public class ProcessExecutor {
    private static final String EXIT_MARK = "exit";
    private final ResponseProducer responseProducer;

    public ProcessExecutor(ResponseProducer responseProducer) {
        this.responseProducer = responseProducer;
    }

    public ServerResponse getResponse(String jsonFromClient) {
        RequestFromClient requestFromClient = new Gson()
                .fromJson(jsonFromClient, RequestFromClient.class);
        if (requestFromClient.getType().equalsIgnoreCase(EXIT_MARK)) {
            return responseProducer.getOk();
        }
        if (Operations.processesMap.containsKey(requestFromClient.getType())) {
            return Operations.processesMap
                    .get(requestFromClient.getType())
                    .processData(requestFromClient, responseProducer);
        }
        return responseProducer.getNoSuchKey();
    }
}

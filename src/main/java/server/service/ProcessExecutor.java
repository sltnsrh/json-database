package server.service;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import server.RequestFromClient;
import server.ServerResponse;
import server.util.Operations;

public class ProcessExecutor {
    private static final String EXIT_MARK = "exit";
    private static final String OK_STATUS = "OK";
    private static final String ERROR_STATUS = "ERROR";
    private static final String NO_SUCH_KEY_REASON = "No such key";

    public ServerResponse getResponse(String jsonFromClient) {
        RequestFromClient requestFromClient = new Gson()
                .fromJson(jsonFromClient, RequestFromClient.class);
        ServerResponse serverResponse = new ServerResponse();
        if (requestFromClient.getType().equalsIgnoreCase(EXIT_MARK)) {
            serverResponse.setResponse(OK_STATUS);
            return serverResponse;
        }
        if (Operations.processesMap.containsKey(requestFromClient.getType())) {
            String value = Operations.processesMap
                    .get(requestFromClient.getType())
                    .processData(requestFromClient);
            if (!value.equals(ERROR_STATUS)) {
                serverResponse.setResponse(OK_STATUS);
                if (!value.equals(OK_STATUS)) {
                    try {
                        serverResponse.setValue(JsonParser.parseString(value));
                    } catch (JsonParseException e) {
                        serverResponse.setValue(new JsonPrimitive(value));
                    }
                }
                return serverResponse;
            }
        }
        serverResponse.setResponse(ERROR_STATUS);
        serverResponse.setReason(NO_SUCH_KEY_REASON);
        return serverResponse;
    }
}

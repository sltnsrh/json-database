package server.service;

import com.google.gson.JsonParser;
import server.ServerResponse;

public class ResponseProducer {
    private static final String NO_SUCH_KEY_REASON = "No such key";
    private static final String ERROR_STATUS = "ERROR";
    public static final String  OK_STATUS = "OK";

    public ServerResponse getNoSuchKey() {
        return new ServerResponse(ERROR_STATUS, NO_SUCH_KEY_REASON, null);
    }

    public ServerResponse getValueOk(String value) {
        return new ServerResponse(OK_STATUS, null, JsonParser.parseString(value));
    }

    public ServerResponse getOk() {
        return new ServerResponse(OK_STATUS, null, null);
    }
}

package server.util;

import com.google.gson.JsonParser;
import server.ServerResponse;

public final class ResponseProducer {
    private static final String NO_SUCH_KEY_REASON = "No such key";
    private static final String ERROR_STATUS = "ERROR";
    public static final String  OK_STATUS = "OK";

    public static ServerResponse getNoSuchKey() {
        ServerResponse serverResponse = new ServerResponse();
        serverResponse.setStatus(ERROR_STATUS);
        serverResponse.setReason(NO_SUCH_KEY_REASON);
        return serverResponse;
    }

    public static ServerResponse getValueOk(String value) {
        ServerResponse serverResponse = new ServerResponse();
        serverResponse.setValue(JsonParser.parseString(value));
        serverResponse.setStatus(OK_STATUS);
        return serverResponse;
    }

    public static ServerResponse getOk() {
        ServerResponse serverResponse = new ServerResponse();
        serverResponse.setStatus(OK_STATUS);
        return serverResponse;
    }
}

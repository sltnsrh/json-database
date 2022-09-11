package server;

import com.google.gson.JsonElement;

public class ServerResponse {
    private final String status;
    private final String reason;
    private final JsonElement value;

    public ServerResponse(String status, String reason, JsonElement value) {
        this.status = status;
        this.reason = reason;
        this.value = value;
    }
}

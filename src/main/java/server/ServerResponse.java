package server;

import com.google.gson.JsonElement;

public class ServerResponse {
    private String status;
    private String reason;
    private JsonElement value;

    public ServerResponse(String status, String reason, JsonElement value) {
        this.status = status;
        this.reason = reason;
        this.value = value;
    }

    public void setValue(JsonElement value) {
        this.value = value;
    }

    public JsonElement getValue() {
        return value;
    }
}

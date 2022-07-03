package server;

import com.google.gson.JsonElement;

public class ServerResponse {
    private String response;
    private String reason;
    private JsonElement value;

    public void setResponse(String response) {
        this.response = response;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public void setValue(JsonElement value) {
        this.value = value;
    }

    public String getResponse() {
        return response;
    }

    public String getReason() {
        return reason;
    }

    public JsonElement getValue() {
        return value;
    }
}

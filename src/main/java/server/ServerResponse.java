package server;

import com.google.gson.JsonElement;

public class ServerResponse {
    private String status;
    private String reason;
    private JsonElement value;

    public void setStatus(String status) {
        this.status = status;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public void setValue(JsonElement value) {
        this.value = value;
    }

    public String getStatus() {
        return status;
    }

    public String getReason() {
        return reason;
    }

    public JsonElement getValue() {
        return value;
    }
}

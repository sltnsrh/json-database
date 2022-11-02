package client.util;

import com.beust.jcommander.Parameter;
import com.google.gson.Gson;
import com.google.gson.JsonParser;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;

public class RequestToServer {
    private static final String CLIENT_DATA_PATH = "src/main/resources/data/";

    @Parameter(names = {"--type", "-t"})
    private String type;
    @Parameter(names = {"--key", "-k"})
    private String key;
    @Parameter(names = {"--value", "-v"})
    private String value;
    @Parameter(names = {"-in"})
    private String fileName;

    public static Builder builder() {
        return new RequestToServer().new Builder();
    }

    public class Builder {
        private Builder() {
        }

        public Builder setType(String type) {
            RequestToServer.this.type = type;
            return this;
        }

        public Builder setKey(String key) {
            RequestToServer.this.key = key;
            return this;
        }

        public Builder setValue(String value) {
            RequestToServer.this.value = value;
            return this;
        }

        public Builder setFileName(String fileName) {
            RequestToServer.this.fileName = fileName;
            return this;
        }

        public RequestToServer build() {
            return RequestToServer.this;
        }
    }

    public String getStringRequest() {
        if (fileName != null) {
            try (Reader reader = Files.newBufferedReader(Path.of(CLIENT_DATA_PATH + fileName))) {
                return JsonParser.parseReader(reader).getAsJsonObject().toString();
            } catch (IOException e) {
                throw new RuntimeException("Can't parse client request from file " + fileName
                        + " to json string line.", e);
            }
        }
        return new Gson().toJson(this);
    }
}

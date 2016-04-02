package hello;

import com.fasterxml.jackson.annotation.JsonProperty;

public class WarProperties {

    @JsonProperty("Message")
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

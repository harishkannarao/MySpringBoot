package com.harishkannarao.rest.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Status {

    @JsonProperty(value = "ThirdPartyStatus")
    private String thirdPartyStatus;

    public String getThirdPartyStatus() {
        return thirdPartyStatus;
    }

    public void setThirdPartyStatus(String thirdPartyStatus) {
        this.thirdPartyStatus = thirdPartyStatus;
    }
}

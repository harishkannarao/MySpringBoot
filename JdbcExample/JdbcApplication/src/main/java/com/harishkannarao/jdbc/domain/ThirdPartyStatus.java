package com.harishkannarao.jdbc.domain;

public class ThirdPartyStatus {

    private String url;
    private int status;

    public void setUrl(String url) {
        this.url = url;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getUrl() {
        return url;
    }

    public int getStatus() {
        return status;
    }
}

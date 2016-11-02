package com.harishkannarao.rest.exception;

public class MyCustomRuntimeException extends RuntimeException {
    private final String code;
    private final String description;


    public MyCustomRuntimeException(String code, String description) {
        super(code + ":" + description);
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
}

package com.harishkannarao.rest.exception;

public class EvilHeaderException extends Exception {
    private final String description;

    public EvilHeaderException(String description) {
        super(description);
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}

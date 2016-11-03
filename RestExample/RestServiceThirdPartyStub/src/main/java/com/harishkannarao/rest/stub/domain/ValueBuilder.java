package com.harishkannarao.rest.stub.domain;

public class ValueBuilder {
    private Long id;
    private String quote;

    private ValueBuilder() {}

    public static ValueBuilder newBuilder() {
        return new ValueBuilder();
    }

    public ValueBuilder setId(Long id) {
        this.id = id;
        return this;
    }

    public ValueBuilder setQuote(String quote) {
        this.quote = quote;
        return this;
    }

    public Value build() {
        Value value = new Value();
        value.setQuote(quote);
        value.setId(id);
        return value;
    }
}

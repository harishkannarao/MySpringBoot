package com.harishkannarao.rest.domain;

public class QuoteBuilder {
    private String type;
    private Value value;

    private QuoteBuilder() {
    }

    public static QuoteBuilder newBuilder() {
        return new QuoteBuilder();
    }

    public QuoteBuilder setType(String type) {
        this.type = type;
        return this;
    }

    public QuoteBuilder setValue(Value value) {
        this.value = value;
        return this;
    }

    public QuoteBuilder setValue(ValueBuilder valueBuilder) {
        this.value = valueBuilder.build();
        return this;
    }

    public Quote build() {
        Quote quote = new Quote();
        quote.setValue(value);
        quote.setType(type);
        return quote;
    }
}

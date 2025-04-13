package com.shikhar03stark.server.model;

public class ResponseContext {
    private final String correlationId;
    private final int statusCode;
    
    public String getCorrelationId() {
        return correlationId;
    }
    public int getStatusCode() {
        return statusCode;
    }

    public ResponseContext(String correlationId, int statusCode) {
        this.correlationId = correlationId;
        this.statusCode = statusCode;
    }
}

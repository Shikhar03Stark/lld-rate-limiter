package com.shikhar03stark.server.model;

import java.time.Duration;
import java.util.Map;

public class RequestContext {
    private final String correlationId;
    private final Duration processDuration;
    private final Map<String, String> headers;
    
    
    public String getCorrelationId() {
        return correlationId;
    }
    public Map<String, String> getHeaderMap() {
        return headers;
    }

    public String getHeader(String key) {
        return headers.get(key);
    }

    public Duration getProcessDuration() {
        return processDuration;
    }

    public RequestContext(String correlationId, Duration processDuration, Map<String, String> headers) {
        this.correlationId = correlationId;
        this.headers = headers;
        this.processDuration = processDuration;
    }

}

package com.shikhar03stark.server.cor.middleware;

import com.shikhar03stark.server.cor.Middleware;
import com.shikhar03stark.server.model.RequestContext;
import com.shikhar03stark.server.model.ResponseContext;

public class ConsoleLoggerMiddleware extends Middleware {

    private void logWithTimestamp(String message) {
        System.out.println(System.currentTimeMillis() + " - " + "[" + Thread.currentThread().getId() + "] " + message);
    }

    @Override
    public ResponseContext handle(RequestContext requestContext) throws Exception {
        // Log incoming request details
        logWithTimestamp("CorrelationId: " + requestContext.getCorrelationId() + " incoming request");

        final ResponseContext responseContext = next.handle(requestContext);

        // log response details
        logWithTimestamp("CorrelationId: " + responseContext.getCorrelationId() + ", Status code: " + responseContext.getStatusCode());

        return responseContext;
    }

}

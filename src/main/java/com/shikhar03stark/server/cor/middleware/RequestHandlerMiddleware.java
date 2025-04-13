package com.shikhar03stark.server.cor.middleware;

import java.time.Duration;

import com.shikhar03stark.server.cor.Middleware;
import com.shikhar03stark.server.model.RequestContext;
import com.shikhar03stark.server.model.ResponseContext;

public class RequestHandlerMiddleware extends Middleware {

    @Override
    public ResponseContext handle(RequestContext requestContext) throws Exception {
        // Simulate request handling logic
        System.out.println("Handling request: " + requestContext.getCorrelationId());

        final Duration pDuration = requestContext.getProcessDuration();
        try {
            final long sleepTime = pDuration.toMillis();
            if (sleepTime > 0) {
                Thread.sleep(sleepTime);
            }
        } catch (Exception e) {
            // TODO: handle exception
            throw new RuntimeException("Error while sleeping/processing", e);
        }

        ResponseContext responseContext = new ResponseContext(requestContext.getCorrelationId(), 200);
        
        // Return the response context
        return responseContext;
    }

}

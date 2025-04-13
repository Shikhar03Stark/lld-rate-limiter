package com.shikhar03stark.server.cor.middleware;

import com.shikhar03stark.server.cor.Middleware;
import com.shikhar03stark.server.model.RequestContext;
import com.shikhar03stark.server.model.ResponseContext;

public class ExceptionHandlerMiddleware extends Middleware {

    @Override
    public ResponseContext handle(RequestContext requestContext) throws Exception {
        try {
            return next.handle(requestContext);
        } catch (Exception e) {
            // Log the exception (for simplicity, just printing the stack trace)
            e.printStackTrace();
            // Return a generic error response
            return new ResponseContext(requestContext.getCorrelationId(), 500);
        }
    }

}

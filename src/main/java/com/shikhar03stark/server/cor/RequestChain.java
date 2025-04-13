package com.shikhar03stark.server.cor;

import com.shikhar03stark.server.model.RequestContext;
import com.shikhar03stark.server.model.ResponseContext;

public class RequestChain {

    private Middleware startMiddleware;
    private Middleware lastMiddleware;

    public RequestChain() {
        this.startMiddleware = null;
        this.lastMiddleware = null;
    }

    public void addMiddleware(Middleware middleware) {
        if (startMiddleware == null) {
            startMiddleware = middleware;
            lastMiddleware = middleware;
        } else {
            lastMiddleware.setNext(middleware);
            lastMiddleware = lastMiddleware.getNext();
        }
    }

    public static RequestChain from(Iterable<Middleware> middlewares) {
        RequestChain chain = new RequestChain();
        for (Middleware middleware : middlewares) {
            chain.addMiddleware(middleware);
        }
        return chain;
    }

    public ResponseContext invoke(RequestContext requestContext) {
        if (startMiddleware == null) {
            throw new IllegalStateException("No middleware in the chain");
        }
        try {
            return startMiddleware.handle(requestContext);
        } catch (Exception e) {
            System.out.println("Error in middleware chain: " + e.getMessage());
            return new ResponseContext(requestContext.getCorrelationId(), 500);
        }
    }

}

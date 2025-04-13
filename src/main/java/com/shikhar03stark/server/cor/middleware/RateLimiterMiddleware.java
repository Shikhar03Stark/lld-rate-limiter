package com.shikhar03stark.server.cor.middleware;

import com.shikhar03stark.server.cor.Middleware;
import com.shikhar03stark.server.model.RequestContext;
import com.shikhar03stark.server.model.ResponseContext;
import com.shikhar03stark.server.ratelimiter.strategy.RateLimitStrategy;

public class RateLimiterMiddleware extends Middleware {

    private final RateLimitStrategy rateLimitStrategy;

    public RateLimiterMiddleware(RateLimitStrategy rateLimitStrategy) {
        super();
        this.rateLimitStrategy = rateLimitStrategy;
    }

    @Override
    public ResponseContext handle(RequestContext requestContext) throws Exception {
       
        if (!rateLimitStrategy.isAllowed(requestContext)) {
            // If the request is not allowed, return a 429 Too Many Requests response
            return new ResponseContext(requestContext.getCorrelationId(), 429);
        }

        final ResponseContext res = next.handle(requestContext);
        return res;
    }

}

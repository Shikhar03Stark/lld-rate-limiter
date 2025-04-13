package com.shikhar03stark.server.ratelimiter;

import com.shikhar03stark.server.model.RequestContext;

public interface RateLimiter {
    boolean isAllowed(RequestContext requestContext) throws Exception;
}

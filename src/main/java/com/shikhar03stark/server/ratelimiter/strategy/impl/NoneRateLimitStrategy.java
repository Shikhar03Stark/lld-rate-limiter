package com.shikhar03stark.server.ratelimiter.strategy.impl;

import com.shikhar03stark.server.model.RequestContext;
import com.shikhar03stark.server.ratelimiter.strategy.RateLimitStrategy;
import com.shikhar03stark.server.ratelimiter.strategy.RateLimitStrategyType;

public class NoneRateLimitStrategy implements RateLimitStrategy {

    @Override
    public boolean isAllowed(RequestContext requestContext) throws Exception {
        // Always allow all requests
        return true;
    }

    @Override
    public RateLimitStrategyType getRateLimitStrategyType() {
        // Return the type of this strategy
        return RateLimitStrategyType.NONE;
    }

}

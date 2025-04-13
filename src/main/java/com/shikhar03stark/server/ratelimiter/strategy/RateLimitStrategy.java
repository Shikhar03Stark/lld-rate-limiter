package com.shikhar03stark.server.ratelimiter.strategy;

import com.shikhar03stark.server.ratelimiter.RateLimiter;

public interface RateLimitStrategy extends RateLimiter {

    RateLimitStrategyType getRateLimitStrategyType();
}

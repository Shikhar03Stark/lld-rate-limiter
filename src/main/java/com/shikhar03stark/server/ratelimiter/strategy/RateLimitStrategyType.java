package com.shikhar03stark.server.ratelimiter.strategy;

public enum RateLimitStrategyType {
    FIXED_WINDOW,
    TOKEN_BUCKET,
    LEAKY_BUCKET,
    SLIDING_WINDOW,
    SLIDING_WINDOW_LOG,
    NONE,
}

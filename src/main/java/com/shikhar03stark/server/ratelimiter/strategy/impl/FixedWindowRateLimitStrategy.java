package com.shikhar03stark.server.ratelimiter.strategy.impl;

import java.time.Duration;
import java.util.concurrent.Semaphore;

import com.shikhar03stark.server.model.RequestContext;
import com.shikhar03stark.server.ratelimiter.strategy.RateLimitStrategy;
import com.shikhar03stark.server.ratelimiter.strategy.RateLimitStrategyType;

public class FixedWindowRateLimitStrategy implements RateLimitStrategy {

    private final int maxRequests;
    private final long windowDurationInNs;

    private final Semaphore waitForCounter = new Semaphore(1);

    private long requestCount;
    private long lastWindowIndex;

    public FixedWindowRateLimitStrategy(int maxRequests, Duration windowDuration) {
        if (maxRequests <= 0) {
            throw new IllegalArgumentException("Max requests must be greater than 0");
        }
        this.maxRequests = maxRequests;
        this.windowDurationInNs = windowDuration.toNanos();

        requestCount = 0;
        lastWindowIndex = System.nanoTime() / windowDurationInNs;
    }

    @Override
    public boolean isAllowed(RequestContext requestContext) throws Exception {
        // find the time this request
        long requestTime = System.nanoTime();

        // based on time calc the window start time
        long requestWindowIndex = requestTime / windowDurationInNs;

        try {
            waitForCounter.acquire();

            if (requestWindowIndex != lastWindowIndex) {
                // reset the window counter
                requestCount = 1;
                lastWindowIndex = requestWindowIndex;
            } else if (requestCount < maxRequests) {
                // increment the request count
                requestCount++;
            } else {
                // reject the request
                return false;
            }

            return true;

        } catch (InterruptedException e) {
            // TODO: handle exception
            Thread.currentThread().interrupt();
            throw new RuntimeException("Thread interrupted while acquiring semaphore", e);
        } finally {
            waitForCounter.release();
        }

    }

    @Override
    public RateLimitStrategyType getRateLimitStrategyType() {
        return RateLimitStrategyType.FIXED_WINDOW;
    }
}

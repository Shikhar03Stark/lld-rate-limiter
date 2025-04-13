package com.shikhar03stark.server.ratelimiter.strategy.impl;

import java.time.Duration;
import java.util.concurrent.Semaphore;

import com.shikhar03stark.server.model.RequestContext;
import com.shikhar03stark.server.ratelimiter.strategy.RateLimitStrategy;
import com.shikhar03stark.server.ratelimiter.strategy.RateLimitStrategyType;

public class LeakyBucketRateLimitStrategy implements RateLimitStrategy {

    private final long capacity;
    private final long maxRequestPerSecond;
    private Semaphore waitForCounter = new Semaphore(1);

    private long currentLevel;
    private long lastRefillTime;

    public LeakyBucketRateLimitStrategy(Duration maxWait, int throughputInSeconds) {
        this.maxRequestPerSecond = throughputInSeconds;
        final long maxWaitNs = maxWait.toNanos();
        this.capacity = maxWaitNs / 1_000_000_000 * throughputInSeconds;
        this.currentLevel = 0;
        this.lastRefillTime = System.nanoTime();
    }

    @Override
    public boolean isAllowed(RequestContext requestContext) throws Exception {
        long currentTime = System.nanoTime();
        try {
            waitForCounter.acquire();
            long elapsedTime = currentTime - lastRefillTime;
            long consumedRequests = (long) ((elapsedTime / 1_000_000_000f) * maxRequestPerSecond);
            currentLevel = Math.max(0, currentLevel - consumedRequests);
    
            if (currentLevel < capacity) {
                currentLevel += 1;
                lastRefillTime = currentTime;
                return true;
            }
            return false;
        } catch (Exception e) {
            System.out.println("Error in LeakyBucketRateLimitStrategy: " + e.getMessage());
            return false;
        } finally {
            waitForCounter.release();
        }
    }

    @Override
    public RateLimitStrategyType getRateLimitStrategyType() {
        return RateLimitStrategyType.LEAKY_BUCKET;
    }
}

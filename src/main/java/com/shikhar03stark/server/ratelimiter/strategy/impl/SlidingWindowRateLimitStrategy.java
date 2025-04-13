package com.shikhar03stark.server.ratelimiter.strategy.impl;

import java.time.Duration;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Semaphore;

import com.shikhar03stark.server.model.RequestContext;
import com.shikhar03stark.server.ratelimiter.strategy.RateLimitStrategy;
import com.shikhar03stark.server.ratelimiter.strategy.RateLimitStrategyType;

public class SlidingWindowRateLimitStrategy implements RateLimitStrategy {

    private final Queue<Long> requestTimestamps;
    private final int maxRequests;
    private final long timeWindowNs;
    private final Semaphore waitForCounter = new Semaphore(1);

    public SlidingWindowRateLimitStrategy(int maxRequests, Duration timeWindow) {
        if (maxRequests <= 0) {
            throw new IllegalArgumentException("Max requests must be greater than 0");
        }

        this.maxRequests = maxRequests;
        this.timeWindowNs = timeWindow.toNanos();
        this.requestTimestamps = new LinkedList<>();
    }

    @Override
    public boolean isAllowed(RequestContext requestContext) throws Exception {
        final long currentTime = System.nanoTime();
        final long windowStartTime = currentTime - timeWindowNs;

        try {
            waitForCounter.acquire();
            while (!requestTimestamps.isEmpty() && requestTimestamps.peek() < windowStartTime) {
                requestTimestamps.poll();
            }

            if (requestTimestamps.size() < maxRequests) {
                requestTimestamps.add(currentTime);
                return true;
            }
            return false;

        } catch (Exception e) {
            // TODO: handle exception
        } finally {
            waitForCounter.release();
        }

        return true;
    }

    @Override
    public RateLimitStrategyType getRateLimitStrategyType() {
        return RateLimitStrategyType.SLIDING_WINDOW;
    }


}

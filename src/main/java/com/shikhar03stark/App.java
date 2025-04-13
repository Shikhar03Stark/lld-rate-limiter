package com.shikhar03stark;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.shikhar03stark.server.Server;
import com.shikhar03stark.server.cor.Middleware;
import com.shikhar03stark.server.cor.RequestChain;
import com.shikhar03stark.server.cor.middleware.ConsoleLoggerMiddleware;
import com.shikhar03stark.server.cor.middleware.ExceptionHandlerMiddleware;
import com.shikhar03stark.server.cor.middleware.RateLimiterMiddleware;
import com.shikhar03stark.server.cor.middleware.RequestHandlerMiddleware;
import com.shikhar03stark.server.model.RequestContext;
import com.shikhar03stark.server.ratelimiter.strategy.impl.FixedWindowRateLimitStrategy;
import com.shikhar03stark.server.ratelimiter.strategy.impl.LeakyBucketRateLimitStrategy;
import com.shikhar03stark.server.ratelimiter.strategy.impl.NoneRateLimitStrategy;
import com.shikhar03stark.server.ratelimiter.strategy.impl.SlidingWindowRateLimitStrategy;

/**
 * Hello world!
 */
public final class App {
    private App() {
    }

    /**
     * Says hello to the world.
     * @param args The arguments of the program.
     */
    public static void main(String[] args) {
        
        final List<Middleware> middlewares = new ArrayList<>();
        middlewares.add(new ExceptionHandlerMiddleware());
        middlewares.add(new ConsoleLoggerMiddleware());
        // middlewares.add(new RateLimiterMiddleware(new FixedWindowRateLimitStrategy(5, Duration.ofSeconds(1)))); // Fixed Window
        // middlewares.add(new RateLimiterMiddleware(new SlidingWindowRateLimitStrategy(5, Duration.ofSeconds(1)))); // Sliding Window
        middlewares.add(new RateLimiterMiddleware(new LeakyBucketRateLimitStrategy(Duration.ofSeconds(1), 5))); // Leaky Bucket
        middlewares.add(new RequestHandlerMiddleware());

        final Server server = new Server(middlewares, 8);
        server.start();

        int requestCount = 360;
        for (int i = 0; i < requestCount; i++) {
            final String correlationId = "correlationId-" + i;
            final Duration processDuration = Duration.ofMillis(300);
            final HashMap<String, String> headers = new HashMap<>();
            headers.put("headerKey", "headerValue");

            final RequestContext requestContext = new RequestContext(correlationId, processDuration, headers);
            server.acceptRequest(requestContext);
            try {
                long waitTime = 10 + (long)(100.0/(1.1 + Math.sin(i * Math.PI / 90.0))); //sinusoidal wait time
                // long waitTime = 100; // // Fixed wait time of 100ms
                Thread.sleep(waitTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        server.stop();
        System.out.println("Server stopped.");
    }
}

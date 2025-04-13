package com.shikhar03stark.server;

import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import com.shikhar03stark.server.cor.Middleware;
import com.shikhar03stark.server.cor.RequestChain;
import com.shikhar03stark.server.model.RequestContext;

public class Server {

    private final Iterable<Middleware> requestMiddlewares;
    private final BlockingQueue<RequestContext> requestQueue;
    private final ExecutorService executorService;

    private boolean isRunning;
    private Thread serverThread;
    private long pendingRequestsCount = 0;
    private final Semaphore waitForRequestCountSemaphore = new Semaphore(1);

    public Server(Iterable<Middleware> requestMiddlewares, int workers) {
        this.requestMiddlewares = requestMiddlewares;
        this.requestQueue = new LinkedBlockingQueue<>();
        this.executorService = Executors.newFixedThreadPool(workers);
        isRunning = false;
        serverThread = null;
    }

    public void start() {
        if (isRunning) {
            System.out.println("Server is already running.");
            return;
        }

        isRunning = true;
        System.out.println("Server started.");

        serverThread = new Thread(() -> {
            while (isRunning) {
                try {
                    final RequestContext request = requestQueue.take();
                    executorService.execute(() -> {
                        RequestChain requestChain = RequestChain.from(requestMiddlewares);
                        try {
                            waitForRequestCountSemaphore.acquire();
                            pendingRequestsCount++;
                            waitForRequestCountSemaphore.release();
                            requestChain.invoke(request);
                        } catch (Exception e) {
                            System.out.println("Error processing request: " + e.getMessage());
                        } finally {
                            try {
                                waitForRequestCountSemaphore.acquire();
                                pendingRequestsCount--;
                                waitForRequestCountSemaphore.release();
                            } catch (InterruptedException e) {
                                Thread.currentThread().interrupt();
                                System.out.println("Thread interrupted while updating pending request count.");
                            }
                        }
                    });

                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    System.out.println("Server interrupted.");
                } catch (Exception e) {
                    System.out.println("Error processing request: " + e.getMessage());
                }
            }
        });
        serverThread.start();
    }

    public void stop() {
        if (!isRunning) {
            System.out.println("Server is not running.");
            return;
        }

        while (!requestQueue.isEmpty() || pendingRequestsCount > 0) {
            try {
                Thread.sleep(100); // Wait for the queue to drain
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("Server interrupted while stopping.");
            }
        }

        isRunning = false;
        executorService.shutdown();
        serverThread.interrupt();
        System.out.println("Server stopped.");
    }

    public void acceptRequest(RequestContext requestContext) {
        if (requestContext != null) {
            requestQueue.add(requestContext);
            System.out.println("Request accepted: " + requestContext.getCorrelationId());
        }
    }
    
}

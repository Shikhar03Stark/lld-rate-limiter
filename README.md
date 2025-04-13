# Rate Limiter Telemetry

This project implements a **middleware-based server framework** with features like **rate limiting**, **logging**, and **exception handling**. It uses the **Chain of Responsibility** design pattern to process requests through a sequence of middleware components. Additionally, it includes an **analysis module** to evaluate traffic patterns and rate-limiting enforcement.

---

## **1. Design Overview**
- **Purpose**:
  - To process incoming requests through a chain of middleware components.
  - To enforce rate-limiting strategies on requests.
  - To analyze traffic patterns and rate-limiting effectiveness.
- **Key Features**:
  - Middleware-based request processing.
  - Pluggable rate-limiting strategies (e.g., Fixed Window, Sliding Window, Leaky Bucket).
  - Logging, exception handling, and request handling as middleware components.
  - Traffic analysis using Python-based tools.
- **Core Components**:
  - **Middleware Chain**: Processes requests sequentially through middleware.
  - **Rate Limiter**: Enforces rate-limiting policies using different strategies.
  - **Request and Response Contexts**: Encapsulate request and response metadata.
  - **Analysis Module**: Provides insights into traffic patterns and rate-limiting enforcement.

---

## **2. Low-Level Design (LLD)**

### **2.1 Middleware Framework**
- **Base Class**: `Middleware`
  - Abstract class with a `handle` method to process requests.
  - Supports chaining via `setNext` and `getNext` methods.
- **Concrete Middleware**:
  - `ConsoleLoggerMiddleware`: Logs request and response details.
  - `ExceptionHandlerMiddleware`: Handles exceptions and returns a generic error response.
  - `RateLimiterMiddleware`: Enforces rate-limiting policies.
  - `RequestHandlerMiddleware`: Simulates request handling.

### **2.2 Middleware Chain**
- **Class**: `RequestChain`
  - Manages the chain of middleware.
  - Processes requests by invoking the first middleware in the chain.
  - Handles errors and returns appropriate responses.

### **2.3 Rate Limiter**
- **Interface**: `RateLimiter`
  - Defines the `isAllowed` method to check if a request is allowed.
- **Strategies**:
  - `FixedWindowRateLimitStrategy`: Implements fixed-window rate limiting.
  - `SlidingWindowRateLimitStrategy`: Implements sliding-window rate limiting.
  - `LeakyBucketRateLimitStrategy`: Implements leaky bucket rate limiting.
  - `NoneRateLimitStrategy`: Allows all requests without rate limiting.
- **Enum**: `RateLimitStrategyType`
  - Enumerates supported rate-limiting strategies (e.g., FIXED_WINDOW, SLIDING_WINDOW, LEAKY_BUCKET, NONE).

### **2.4 Request and Response Contexts**
- **RequestContext**: Encapsulates request metadata (e.g., correlation ID, headers, processing duration).
- **ResponseContext**: Encapsulates response metadata (e.g., correlation ID, status code).

---

## **3. Execution Flow**
1. **Setup**:
   - Middleware components are instantiated and added to a `RequestChain`.
   - A rate-limiting strategy (e.g., `LeakyBucketRateLimitStrategy`) is configured.
2. **Request Processing**:
   - The `RequestChain` invokes the first middleware.
   - Each middleware processes the request and passes it to the next.
   - The `RateLimiterMiddleware` enforces rate-limiting policies.
   - The `RequestHandlerMiddleware` simulates request handling.
3. **Error Handling**:
   - The `ExceptionHandlerMiddleware` catches exceptions and returns a generic error response.
4. **Logging**:
   - The `ConsoleLoggerMiddleware` logs request and response details.

---

## **4. Key Design Patterns**
- **Chain of Responsibility**:
  - Used for middleware chaining.
  - Each middleware processes the request and delegates to the next.
- **Strategy**:
  - Used for pluggable rate-limiting strategies.

---

## **5. Extensibility**
- **Adding New Middleware**:
  - Extend the `Middleware` class and implement the `handle` method.
- **Adding New Rate-Limiting Strategies**:
  - Implement the `RateLimitStrategy` interface and define the `isAllowed` method.

---

## **6. Analysis Module**
The `analysis` folder contains Python-based tools to analyze traffic patterns and rate-limiting enforcement. It uses libraries like `pandas` and `matplotlib` to process logs and generate visualizations.

### **Key Features**:
- **Traffic Analysis**:
  - Extracts timestamps of incoming requests and outgoing responses.
  - Differentiates between successful responses (`200 OK`) and rate-limited responses (`429 Too Many Requests`).
- **Visualization**:
  - Plots request rates over time.
  - Displays response rates for different status codes.
  - Highlights timestamps of requests and responses.

### **Usage**:
1. Place log files in the `logs` folder.
2. Run the Jupyter Notebook `request_patterns.ipynb` to analyze the logs.
3. View visualizations of traffic patterns and rate-limiting enforcement.

---

## **7 Graphs of Timestamps**

The analysis module generates visualizations to better understand traffic patterns and rate-limiting enforcement. Below are examples of the graphs produced:

### **Request Rates Over Time**
This graph shows the number of incoming requests per second, helping to identify traffic spikes and patterns.

#### Fixed Window

![FixedWindow](analysis\plots\timestamps_fixedwindow_sinewave.log.png)

#### Sliding Window
#### Sliding Window

![SlidingWindow](analysis\plots\timestamps_slidingwindow_sinewave.log.png)

#### Leaky Bucket

![LeakyBucket](analysis\plots\timestamps_leakybucket_sinewave.log.png)

To generate these graphs, run the Jupyter Notebook `request_patterns.ipynb` in the `analysis` folder.

## **8. Project Structure**
```
ratelimiter-telemetry/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── ratelimiter/
│   │   │           ├── middleware/
│   │   │           │   ├── Middleware.java
│   │   │           │   ├── ConsoleLoggerMiddleware.java
│   │   │           │   ├── ExceptionHandlerMiddleware.java
│   │   │           │   ├── RateLimiterMiddleware.java
│   │   │           │   └── RequestHandlerMiddleware.java
│   │   │           ├── ratelimiter/
│   │   │           │   ├── RateLimiter.java
│   │   │           │   ├── FixedWindowRateLimitStrategy.java
│   │   │           │   ├── SlidingWindowRateLimitStrategy.java
│   │   │           │   ├── LeakyBucketRateLimitStrategy.java
│   │   │           │   ├── NoneRateLimitStrategy.java
│   │   │           │   └── RateLimitStrategyType.java
│   │   │           ├── chain/
│   │   │           │   └── RequestChain.java
│   │   │           ├── context/
│   │   │           │   ├── RequestContext.java
│   │   │           │   └── ResponseContext.java
│   │   │           └── Main.java
│   │   └── resources/
│   │       └── application.properties
│   └── test/
│       └── java/
│           └── com/
│               └── ratelimiter/
│                   ├── middleware/
│                   │   └── MiddlewareTest.java
│                   ├── ratelimiter/
│                   │   └── RateLimiterTest.java
│                   ├── chain/
│                   │   └── RequestChainTest.java
│                   └── context/
│                       └── ContextTest.java
├── analysis/
│   ├── logs/
│   └── request_patterns.ipynb
├── README.md
└── pom.xml
```
package cn.com.bucket;

import com.google.common.collect.Maps;
import com.google.common.util.concurrent.RateLimiter;

import java.util.concurrent.ConcurrentMap;

/**
 * 令牌桶限流
 */
public class Shaping {

    private static final ConcurrentMap<String, RateLimiter> resourceLimiterMap = Maps.newConcurrentMap();

    /**
     * 初始化令牌桶
     * @param resource
     * @param qps
     */
    public static void updateResourceQps(String resource, double qps) {
        RateLimiter limiter = resourceLimiterMap.get(resource);
        if (limiter == null) {
            limiter = RateLimiter.create(qps);
            RateLimiter putByOtherThread = resourceLimiterMap.putIfAbsent(resource, limiter);
            if (putByOtherThread != null) {
                limiter = putByOtherThread;
            }
        }
        limiter.setRate(qps);
    }

    /**
     * 尝试获得令牌
     * @param resource
     */
    public static void tryAcquire(String resource){
        RateLimiter limiter = resourceLimiterMap.get(resource);
        if (limiter == null) {
            return;
        }
        if (!limiter.tryAcquire()) {
            throw new RuntimeException(resource+" 接口访问太频繁");
        }
    }

    public static void removeResource(String resource) {
        resourceLimiterMap.remove(resource);
    }

}

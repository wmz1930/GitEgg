//package com.gitegg.platform.cloud.feign.config;
//
//import com.netflix.hystrix.HystrixThreadPoolKey;
//import com.netflix.hystrix.strategy.HystrixPlugins;
//import com.netflix.hystrix.strategy.concurrency.HystrixConcurrencyStrategy;
//import com.netflix.hystrix.strategy.concurrency.HystrixRequestVariable;
//import com.netflix.hystrix.strategy.concurrency.HystrixRequestVariableLifecycle;
//import com.netflix.hystrix.strategy.eventnotifier.HystrixEventNotifier;
//import com.netflix.hystrix.strategy.executionhook.HystrixCommandExecutionHook;
//import com.netflix.hystrix.strategy.metrics.HystrixMetricsPublisher;
//import com.netflix.hystrix.strategy.properties.HystrixPropertiesStrategy;
//import com.netflix.hystrix.strategy.properties.HystrixProperty;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Component;
//import org.springframework.web.context.request.RequestAttributes;
//import org.springframework.web.context.request.RequestContextHolder;
//
//import java.util.concurrent.BlockingQueue;
//import java.util.concurrent.Callable;
//import java.util.concurrent.ThreadPoolExecutor;
//import java.util.concurrent.TimeUnit;
//
///**
// * 自定义并发策略
// * 将现有的并发策略作为新并发策略的成员变量
// * 在新并发策略中，返回现有并发策略的线程池、Queue
// *
// * hystrix.command.default.execution.isolation.strategy=THREAD   Hystrix的默认隔离策略（官方推荐，当使用该隔离策略时，是没办法拿到 ThreadLocal 中的值的，但是
// *                                                               RequestContextHolder 源码中，使用了两个ThreadLocal）
// * hystrix.command.default.execution.isolation.strategy=SEMAPHORE （将隔离策略改为SEMAPHORE 也可以解决这个问题，但是官方并不推荐这个策略，因为这个策略对网络资源消耗比较大）
// *
// * 主要是解决当 Hystrix的默认隔离策略是THREAD时，不能通过RequestContextHolder获取到request对象的问题
// *
// * @author GitEgg
// * @date 2020/12/09
// **/
//@Component
//@Slf4j
//public class FeignConfig extends HystrixConcurrencyStrategy {
//
//    private HystrixConcurrencyStrategy delegate;
//
//    public FeignConfig() {
//        try {
//            this.delegate = HystrixPlugins.getInstance().getConcurrencyStrategy();
//            if (this.delegate instanceof FeignConfig) {
//                // Welcome to singleton hell...
//                return;
//            }
//            HystrixCommandExecutionHook commandExecutionHook =
//                HystrixPlugins.getInstance().getCommandExecutionHook();
//            HystrixEventNotifier eventNotifier = HystrixPlugins.getInstance().getEventNotifier();
//            HystrixMetricsPublisher metricsPublisher = HystrixPlugins.getInstance().getMetricsPublisher();
//            HystrixPropertiesStrategy propertiesStrategy =
//                HystrixPlugins.getInstance().getPropertiesStrategy();
//            this.logCurrentStateOfHystrixPlugins(eventNotifier, metricsPublisher, propertiesStrategy);
//            HystrixPlugins.reset();
//            HystrixPlugins.getInstance().registerConcurrencyStrategy(this);
//            HystrixPlugins.getInstance().registerCommandExecutionHook(commandExecutionHook);
//            HystrixPlugins.getInstance().registerEventNotifier(eventNotifier);
//            HystrixPlugins.getInstance().registerMetricsPublisher(metricsPublisher);
//            HystrixPlugins.getInstance().registerPropertiesStrategy(propertiesStrategy);
//        } catch (Exception e) {
//            log.error("Failed to register Sleuth Hystrix Concurrency Strategy", e);
//        }
//    }
//
//    private void logCurrentStateOfHystrixPlugins(HystrixEventNotifier eventNotifier,
//        HystrixMetricsPublisher metricsPublisher, HystrixPropertiesStrategy propertiesStrategy) {
//        if (log.isDebugEnabled()) {
//            log.debug("Current Hystrix plugins configuration is [" + "concurrencyStrategy ["
//                + this.delegate + "]," + "eventNotifier [" + eventNotifier + "]," + "metricPublisher ["
//                + metricsPublisher + "]," + "propertiesStrategy [" + propertiesStrategy + "]," + "]");
//            log.debug("Registering Sleuth Hystrix Concurrency Strategy.");
//        }
//    }
//
//    @Override
//    public <T> Callable<T> wrapCallable(Callable<T> callable) {
//        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
//        return new WrappedCallable<>(callable, requestAttributes);
//    }
//
//    @Override
//    public ThreadPoolExecutor getThreadPool(HystrixThreadPoolKey threadPoolKey,
//        HystrixProperty<Integer> corePoolSize, HystrixProperty<Integer> maximumPoolSize,
//        HystrixProperty<Integer> keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue) {
//        return this.delegate.getThreadPool(threadPoolKey, corePoolSize, maximumPoolSize, keepAliveTime,
//            unit, workQueue);
//    }
//
//    @Override
//    public BlockingQueue<Runnable> getBlockingQueue(int maxQueueSize) {
//        return this.delegate.getBlockingQueue(maxQueueSize);
//    }
//
//    @Override
//    public <T> HystrixRequestVariable<T> getRequestVariable(HystrixRequestVariableLifecycle<T> rv) {
//        return this.delegate.getRequestVariable(rv);
//    }
//
//    static class WrappedCallable<T> implements Callable<T> {
//        private final Callable<T> target;
//        private final RequestAttributes requestAttributes;
//
//        public WrappedCallable(Callable<T> target, RequestAttributes requestAttributes) {
//            this.target = target;
//            this.requestAttributes = requestAttributes;
//        }
//
//        @Override
//        public T call() throws Exception {
//            try {
//                RequestContextHolder.setRequestAttributes(requestAttributes);
//                return target.call();
//            } finally {
//                RequestContextHolder.resetRequestAttributes();
//            }
//        }
//    }
//
//}

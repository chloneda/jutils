package com.chloneda.jutils.thread;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @Created by chloneda
 * @Description: {@link ThreadFactoryBuilder}
 * {@link Executors}. Executors.defaultThreadFactory()
 */
public final class DefaultThreadFactory implements ThreadFactory {

    private static final AtomicLong threadCounter = new AtomicLong(1L);
    private static final String DEFAULT_PREFIX = "T";
    private final ThreadGroup group;
    private final int NORM_PRIORITY = Thread.NORM_PRIORITY;

    public DefaultThreadFactory() {
        SecurityManager s = System.getSecurityManager();
        group = (s != null) ? s.getThreadGroup() : Thread.currentThread().getThreadGroup();
    }

    @Override
    public Thread newThread(Runnable r) {
        Thread t = new Thread(group, r, DEFAULT_PREFIX + "-" + threadCounter.getAndIncrement());
        if (t.isDaemon())
            t.setDaemon(false);
        if (t.getPriority() != NORM_PRIORITY)
            t.setPriority(NORM_PRIORITY);
        return t;
    }

}

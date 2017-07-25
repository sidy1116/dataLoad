package com.oracle.qa.dataload.service.async;

import java.time.Duration;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

import com.google.common.base.Throwables;

public class AsyncUtil {
    public static void sleep(Duration duration) {
        try {
            Thread.sleep(duration.toMillis());
        } catch (InterruptedException e) {
            Throwables.propagate(e);
        }
    }

    public static void randomSleep(int duration, TimeUnit timeUnit) {
        try {
            timeUnit.sleep(ThreadLocalRandom.current().nextInt(duration));
        } catch (InterruptedException e) {
            Throwables.propagate(e);
        }
    }

    public static String getThreadName() {
        return Thread.currentThread().getName();
    }
}
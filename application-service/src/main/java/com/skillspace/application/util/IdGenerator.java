package com.skillspace.application.util;

import java.util.concurrent.atomic.AtomicLong;

public class IdGenerator {
    private static final AtomicLong counter = new AtomicLong();

    public static Long generateId() {
        return counter.incrementAndGet();
    }
}
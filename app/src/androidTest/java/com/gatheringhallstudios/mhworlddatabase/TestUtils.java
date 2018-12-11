package com.gatheringhallstudios.mhworlddatabase;

import androidx.lifecycle.LiveData;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

public class TestUtils {
    public static <T> T getValue(LiveData<T> data) throws InterruptedException {
        // contains the result, since lambdas can't assign to variables
        AtomicReference<T> value = new AtomicReference<T>();

        CountDownLatch latch = new CountDownLatch(1);
        data.observeForever((result) -> {
            value.set(result);
            latch.countDown();
        });
        latch.await(2, TimeUnit.SECONDS);
        return value.get();
    }
}

package com.gatheringhallstudios.mhworlddatabase.util

import com.gatheringhallstudios.mhworlddatabase.util.ThrottledExecutor
import org.junit.Test
import java.util.*
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import org.junit.Assert.assertEquals

class ThrottledExecutorTest {
    @Test
    fun Runs_Sequentially() {
        val executor = ThrottledExecutor()
        val latch = CountDownLatch(2)

        val results: MutableList<Int> = Collections.synchronizedList(ArrayList<Int>())

        executor.execute {
            Thread.sleep(500)
            results.add(1)
            latch.countDown()
        }

        executor.execute {
            results.add(2)
            latch.countDown()
        }

        latch.await(2, TimeUnit.SECONDS)
        assertEquals("items should have been added in order", results, arrayListOf(1, 2))
    }

    @Test
    fun Skips_Inbetween_Entries() {
        val executor = ThrottledExecutor()
        val latch = CountDownLatch(2)

        val results: MutableList<Int> = Collections.synchronizedList(ArrayList<Int>())

        executor.execute {
            Thread.sleep(500)
            results.add(1)
            latch.countDown()
        }

        executor.execute {
            results.add(5000)
        }

        executor.execute {
            results.add(2)
            latch.countDown()
        }

        latch.await(2, TimeUnit.SECONDS)
        assertEquals("items should have been added in order", results, arrayListOf(1, 2))

    }
}
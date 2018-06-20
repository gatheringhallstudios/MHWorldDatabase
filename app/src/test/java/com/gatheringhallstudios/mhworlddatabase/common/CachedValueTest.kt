package com.gatheringhallstudios.mhworlddatabase.common

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test

class CachedValueTest {
    @Test
    fun ReturnsValue() {
        val value = CachedValue(10000) { 2 }
        assertEquals(2, value.get())
    }

    @Test
    fun MaintainsValue() {
        var currentIdx = 0
        val value = CachedValue(1000000) { currentIdx++ }

        val firstValue = value.get()
        value.get()
        value.get()
        value.get()
        value.get()
        val lastValue = value.get()

        assertEquals("First value should be 0", 0, firstValue)
        assertEquals("expected cached value", firstValue, lastValue)
    }

    @Test
    fun ValueResetsIfExpired() {
        var currentIdx = 0
        val value = CachedValue(10) { currentIdx++ }

        val firstValue = value.get()
        Thread.sleep(100) // sleep for more than cached
        val lastValue = value.get()

        assertNotEquals(lastValue, firstValue)
    }
}
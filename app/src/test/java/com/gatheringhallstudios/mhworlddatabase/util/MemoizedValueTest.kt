package com.gatheringhallstudios.mhworlddatabase.common

import com.gatheringhallstudios.mhworlddatabase.util.tree.MemoizedValue
import com.gatheringhallstudios.mhworlddatabase.util.CachedValue
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test

class MemoizedValueTest {
    @Test
    fun ReturnsValueIfNew() {
        val value = MemoizedValue<Int, Int>()
        assertEquals(2, value.get(25) { 2 })
    }

    @Test
    fun MaintainsValueIfSameKey() {
        val cache = MemoizedValue<Int, Int>()

        cache.get(25) { 1 }
        assertEquals("expected cached value", 1, cache.get(25) { 2 })
    }

    @Test
    fun ValueResetsIfDifferentKey() {
        val cache = MemoizedValue<Int, Int>()

        cache.get(25) { 1 }
        assertEquals("expected new value", 2, cache.get(28) { 2 })
    }
}
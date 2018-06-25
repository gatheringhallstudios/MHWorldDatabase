package com.gatheringhallstudios.mhworlddatabase

import android.content.Context
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import androidx.core.content.ContextCompat
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

// junit assertions
import org.junit.Assert.*

@RunWith(AndroidJUnit4::class)
class MHExtensionsTest {
    // these tests may require a context
    @Rule @JvmField val activityRule = ActivityTestRule<MainActivity>(MainActivity::class.java)

    // note for readers: constant state comparisons only work for drawable resources

    val context : Context
        get() = activityRule.activity

    @Test
    fun getAssetDrawable_returnsOnSuccess() {
        val default = R.drawable.question_mark_grey
        val defaultImage = ContextCompat.getDrawable(context, default)

        val result = context.getAssetDrawable("monster-large/anjanath.png", default)
        assertNotNull("Expected image", result)
        assertNotEquals("Should not be default", defaultImage?.constantState, result?.constantState)
    }

    @Test
    fun getAssetDrawable_defaultOnFailure() {
        val default = R.drawable.question_mark_grey
        val questionMark = ContextCompat.getDrawable(context, default)

        val result = context.getAssetDrawable("fake", default)
        assertNotNull("Expected result", result)
        assertEquals("Expected default result", questionMark?.constantState, result?.constantState)
    }

    @Test
    fun getVectorDrawable_returnsOnSuccess() {
        val default = R.drawable.question_mark_grey
        val questionMark = ContextCompat.getDrawable(context, default)

        val result = context.getVectorDrawable(R.drawable.ic_armor)
        assertNotNull("Expected default non-null result", result)
        assertNotEquals("Should not be default", questionMark?.constantState, result?.constantState)

    }

    @Test
    fun getVectorDrawable_defaultOnFailure() {
        val default = R.drawable.question_mark_grey
        val questionMark = ContextCompat.getDrawable(context, default)

        val result = context.getVectorDrawable("fake", "rare1", default)
        assertNotNull("Expected non-null result", result)
        assertEquals("Expected default result", questionMark?.constantState, result?.constantState)
    }
}
package com.gatheringhallstudios.mhworlddatabase

import android.content.Context
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import android.support.v4.content.ContextCompat
import com.gatheringhallstudios.mhworlddatabase.assets.getAssetDrawable
import com.gatheringhallstudios.mhworlddatabase.assets.getVectorDrawable
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
        val default = R.drawable.ic_question_mark
        val defaultImage = ContextCompat.getDrawable(context, default)

        val result = context.getAssetDrawable("monsters/30.png", default)
        assertNotNull("Expected image", result)
        assertNotEquals("Should not be default", defaultImage?.constantState, result?.constantState)
    }

    @Test
    fun getAssetDrawable_defaultOnFailure() {
        val default = R.drawable.ic_question_mark
        val questionMark = ContextCompat.getDrawable(context, default)

        val result = context.getAssetDrawable("fake", default)
        assertNotNull("Expected result", result)
        assertEquals("Expected default result", questionMark?.constantState, result?.constantState)
    }

    @Test
    fun getVectorDrawable_returnsOnSuccess() {
        val default = R.drawable.ic_question_mark
        val questionMark = ContextCompat.getDrawable(context, default)

        val result = context.getVectorDrawable("ArmorHead", "Red")
        assertNotNull("Expected default non-null result", result)
        assertNotEquals("Should not be default", questionMark?.constantState, result?.constantState)
    }

    @Test
    fun getVectorDrawable_defaultOnFailure() {
        val default = R.drawable.ic_question_mark
        val questionMark = ContextCompat.getDrawable(context, default)

        val result = context.getVectorDrawable("fake", "rare1", default)
        assertNotNull("Expected non-null result", result)
        assertEquals("Expected default result", questionMark?.constantState, result?.constantState)
    }
}
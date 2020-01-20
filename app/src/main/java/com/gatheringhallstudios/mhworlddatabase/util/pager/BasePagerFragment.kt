package com.gatheringhallstudios.mhworlddatabase.util.pager

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import androidx.annotation.StringRes
import com.google.android.material.tabs.TabLayout
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gatheringhallstudios.mhworlddatabase.R
import kotlinx.android.synthetic.main.fragment_generic_pager.*
import kotlinx.android.synthetic.main.fragment_generic_pager.view.*
import java.util.*


/**
 * Abstract Base Fragment for implementing Hubs with multiple detail tabs.
 * Hub Fragments can subclass this and call addTab() to set up hub pages.
 * Butterknife is already called, so onCreateView cannot be inherited.
 */

abstract class BasePagerFragment : androidx.fragment.app.Fragment() {
    companion object {
        val TAG = BasePagerFragment::class.java.simpleName
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate view
        val root = inflater.inflate(R.layout.fragment_generic_pager, container, false)

        // Setup tabs
        val adder = InnerTabAdder(context!!)
        onAddTabs(adder)

        // Pull info from setup phase
        val tabs = adder.getTabs()
        val tabIdx = adder.defaultIdx

        // Retrieve the view's elements (we're in onCreateView, can't use on fragment directly)
        val tabLayout = root.tab_layout as TabLayout
        val viewPager = root.pager_list

        // Initialize ViewPager (tab behavior)
        viewPager.adapter = GenericPagerAdapter(this, tabs)
        tabLayout.setupWithViewPager(viewPager)

        if (tabIdx > 0) {
            pager_list.currentItem = tabIdx
        }

        return root
    }

    /**
     * Called when the fragment wants the tabs, but after Butterknife
     * has binded the view
     * @param tabs
     */
    abstract fun onAddTabs(tabs: TabAdder)

    interface TabAdder {
        /**
         * Adds a tab to the fragment
         *
         * @param title   The title to display for the tab
         * @param builder A TabFactory or lambda that builds the tab fragment
         */
        fun addTab(title: String, builder: () -> androidx.fragment.app.Fragment)


        /**
         * Adds a tab to the fragment
         * @param titleRes The string resource to use as a title
         * @param builder  A lambda that builds the tab fragment
         */
        fun addTab(@StringRes titleRes: Int, builder: () -> androidx.fragment.app.Fragment)

        /**
         * Sets the default selected tab idx
         * @param idx
         */
        fun setDefaultItem(idx: Int)
    }

    /** Internal only implementation of the TabAdder  */
    private class InnerTabAdder(private val ctx: Context): TabAdder {
        var defaultIdx = -1
            private set
        private val tabs = ArrayList<PagerTab>()


        override fun addTab(titleRes: Int, builder: () -> androidx.fragment.app.Fragment) {
            this.addTab(ctx.getString(titleRes), builder)
        }

        override fun addTab(title: String, builder: () -> androidx.fragment.app.Fragment) {
            tabs.add(PagerTab(title, builder))
        }

        override fun setDefaultItem(idx: Int) {
            // note: We're setting an atomic integer due to reassignment restrictions
            defaultIdx = idx
        }

        fun getTabs(): List<PagerTab> {
            return tabs
        }
    }

    /*
     * Contains a screenshot of the child fragment contents while being destroyed. Otherwise
     * children will disappear before transition animations begin.
     * https://gist.github.com/luksprog/4996190
     */
    private var cachedBitmap: Bitmap? = null

    override fun onPause() {
        if (isRemoving) {
            createdCachedBitmap()
        }
        super.onPause()
    }

    /**
     * Snapshots the current state of the viewpager, to then render it as a background during destroy.
     * This is so that animations can work smoothly, since viewpagers clear the view before navigation.
     */
    private fun createdCachedBitmap() {
        try {
            val view = pager_list
            val bitmap = Bitmap.createBitmap(view.width,
                    view.height, Bitmap.Config.ARGB_8888)

            val canvas = Canvas(bitmap)
            canvas.save()
            canvas.translate(-view.scrollX.toFloat(), 0F)
            view.draw(canvas)
            canvas.restore() // restore canvas matrix/clip state

            this.cachedBitmap = bitmap

        } catch (ex: Exception) {
            Log.e(TAG, "Failed to create cached bitmap for smooth navigation", ex)
        }
    }

    override fun onDestroyView() {
        cachedBitmap?.let {
            val bitmapDrawable = BitmapDrawable(resources, it)
            pager_list.background = bitmapDrawable
            cachedBitmap = null
        }

        super.onDestroyView()
    }
}

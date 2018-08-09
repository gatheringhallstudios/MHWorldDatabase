package com.gatheringhallstudios.mhworlddatabase.common

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
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

abstract class BasePagerFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate view
        val root = inflater.inflate(R.layout.fragment_generic_pager, container, false)

        // Setup tabs
        val adder = InnerTabAdder()
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
        fun addTab(title: String, builder: () -> Fragment)

        /**
         * Sets the default selected tab idx
         * @param idx
         */
        fun setDefaultItem(idx: Int)
    }

    /** Internal only implementation of the TabAdder  */
    private class InnerTabAdder : TabAdder {
        var defaultIdx = -1
            private set
        private val tabs = ArrayList<PagerTab>()

        override fun addTab(title: String, builder: () -> Fragment) {
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
     * Displays a screenshot of the child fragment contents while being destroyed. Otherwise
     * children will disappear before transition animations begin.
     * https://gist.github.com/luksprog/4996190
     *
     * TODO Modify for version in comments with bitmap recycling
     */

    private var cachedBitmap: Bitmap? = null

    override fun onPause() {
        cachedBitmap = loadBitmapFromView(pager_list)
        super.onPause()
    }

    private fun loadBitmapFromView(view: View): Bitmap {
        val bitmap = Bitmap.createBitmap(view.width,
                view.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        view.layout(0, 0 + tab_layout.height, view.width, view.height + tab_layout.height)
        view.draw(canvas)
        return bitmap
    }

    override fun onDestroyView() {
        val bitmapDrawable = BitmapDrawable(cachedBitmap)
        pager_list.background = bitmapDrawable
        cachedBitmap = null
        super.onDestroyView()
    }
}

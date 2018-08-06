package com.gatheringhallstudios.mhworlddatabase.common

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gatheringhallstudios.mhworlddatabase.R

import kotlinx.android.synthetic.main.list_generic.*

/**
 * A special version of a recyclerview that updates the adapter
 * to null when it is detatched from the window.
 * Used internally by the RecyclerViewFragment.
 * Do not use for nested recyclerviews.
 */
class DetachingRecyclerView : RecyclerView {
    constructor(context: Context): super(context)

    constructor(context: Context, attrs: AttributeSet?):
            super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, defStyle: Int):
            super(context, attrs, defStyle)

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        adapter = null
    }
}

/**
 * Creates a fragment that contains a recyclerview.
 * This handles most of the setup and handles a potential memory leak case.
 */
open class RecyclerViewFragment : Fragment() {
    /**
     * Returns the recyclerview owned by this fragment to use directly
     */
    val recyclerView get() = recycler_view!!

    /**
     * Overrides onCreateView to return a list_generic.
     * Instead of overriding this, override "onViewCreated".
     */
    final override fun onCreateView(inflater: LayoutInflater, parent: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.list_generic, parent, false)
    }

    /**
     * Sets the adapter of the internal recyclerview.
     * This function has to be called everytime the view is recreated
     * by overriding onViewCreated().
     */
    fun setAdapter(adapter: RecyclerView.Adapter<*>) {
        recycler_view.adapter = adapter
    }

    /**
     * Shows the empty view instead of the recycler view.
     * There is no way to revert. Only call this once you're SURE there is no data.
     */
    fun showEmptyView() {
        recycler_view.visibility = View.GONE
        empty_view.visibility = View.VISIBLE
    }
}
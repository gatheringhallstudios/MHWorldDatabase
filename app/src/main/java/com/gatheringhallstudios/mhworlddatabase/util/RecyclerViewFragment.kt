package com.gatheringhallstudios.mhworlddatabase.util

import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gatheringhallstudios.mhworlddatabase.databinding.ListGenericBinding

/**
 * A special version of a recyclerview that updates the adapter
 * to null when it is detatched from the window.
 * Used internally by the RecyclerViewFragment.
 * Do not use for nested recyclerviews.
 */
class DetachingRecyclerView : androidx.recyclerview.widget.RecyclerView {
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
open class RecyclerViewFragment : androidx.fragment.app.Fragment() {
    private var _binding: ListGenericBinding? = null
    private val binding get() = _binding!!

    /**
     * Returns the recyclerview owned by this fragment to use directly
     */
    val recyclerView get() = binding.recyclerView

    /**
     * Overrides onCreateView to return a list_generic.
     * Instead of overriding this, override "onViewCreated".
     */
    final override fun onCreateView(inflater: LayoutInflater, parent: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        _binding = ListGenericBinding.inflate(inflater, parent, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    /**
     * Sets the adapter of the internal recyclerview.
     * This function has to be called everytime the view is recreated
     * by overriding onViewCreated().
     */
    fun setAdapter(adapter: androidx.recyclerview.widget.RecyclerView.Adapter<*>) {
        binding.recyclerView.adapter = adapter
    }

    /**
     * Shows the empty view instead of the recycler view.
     * There is no way to revert. Only call this once you're SURE there is no data.
     */
    fun showEmptyView() {
        binding.recyclerView.visibility = View.GONE
        binding.emptyView.root.visibility = View.VISIBLE
    }
}

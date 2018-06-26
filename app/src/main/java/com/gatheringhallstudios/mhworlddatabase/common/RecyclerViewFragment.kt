package com.gatheringhallstudios.mhworlddatabase.common

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gatheringhallstudios.mhworlddatabase.R

import kotlinx.android.synthetic.main.list_generic.*

/**
 * Creates a fragment that contains a recyclerview.
 * This handles most of the setup and handles a potential memory leak case.
 */
open class RecyclerViewFragment : Fragment() {
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
    fun setAdapter(adapter: androidx.recyclerview.widget.RecyclerView.Adapter<androidx.recyclerview.widget.RecyclerView.ViewHolder>) {
        recycler_view.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()

        // Removes the adapter from the recyclerview on destroy
        // This also causes the adapter to unregister the view,
        // which prevents a potential cyclical reference memory leak.
        recycler_view.adapter = null
    }
}
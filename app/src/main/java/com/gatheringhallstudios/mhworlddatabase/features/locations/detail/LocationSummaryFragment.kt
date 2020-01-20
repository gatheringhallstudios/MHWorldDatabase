package com.gatheringhallstudios.mhworlddatabase.features.locations.detail

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import com.gatheringhallstudios.mhworlddatabase.R
import com.gatheringhallstudios.mhworlddatabase.util.RecyclerViewFragment
import com.gatheringhallstudios.mhworlddatabase.components.ChildDivider
import com.gatheringhallstudios.mhworlddatabase.components.DashedDividerDrawable
import com.gatheringhallstudios.mhworlddatabase.features.bookmarks.BookmarksFeature
import com.gatheringhallstudios.mhworlddatabase.util.getDrawableCompat

/**
 * Fragment for displaying Location Summary
 */
class LocationSummaryFragment : RecyclerViewFragment() {
    companion object {
        const val ARG_LOCATION_ID = "LOCATION_ID"
    }

    private val viewModel by lazy {
        ViewModelProviders.of(parentFragment!!).get(LocationDetailViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_bookmarkable, menu)
        val locationData = viewModel.location.value
        if (locationData != null && BookmarksFeature.isBookmarked(locationData)) {
            menu.findItem(R.id.action_toggle_bookmark).icon = (context!!.getDrawableCompat(R.drawable.ic_sys_bookmark_on))
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Try to handle the bookmarks button onclick here instead of the main activity
        val id = item.itemId
        super.onOptionsItemSelected(item)
        return if (id == R.id.action_toggle_bookmark) {
            BookmarksFeature.toggleBookmark(viewModel.location.value)
            activity!!.invalidateOptionsMenu()
            true
        } else false
    }

    override fun onViewCreated(view:View, savedInstanceState: Bundle?) {
        val locationId = arguments?.getInt(ARG_LOCATION_ID) ?: -1
        viewModel.setLocation(locationId)

        val adapter = LocationDetailAdapterWrapper()
        setAdapter(adapter.adapter)

        recyclerView.addItemDecoration(ChildDivider(DashedDividerDrawable(context!!)))

        // todo: clean up with coroutines

        viewModel.location.observe(this, Observer {
            if (it != null) {
                adapter.bindLocation(it)
            }

            activity!!.invalidateOptionsMenu()
        })

        viewModel.camps.observe(this, Observer {
            if (it != null) {
                adapter.bindCamps(getString(R.string.header_location_base_camps), it)
            }
        })

        viewModel.locationItems.observe(this, Observer {
            if (it != null) {
                adapter.bindItems(context!!, it)
            }
        })
    }
}
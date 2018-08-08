package com.gatheringhallstudios.mhworlddatabase.features.locations.detail

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.View
import com.gatheringhallstudios.mhworlddatabase.common.RecyclerViewFragment
import com.gatheringhallstudios.mhworlddatabase.components.ChildDivider
import com.gatheringhallstudios.mhworlddatabase.components.DashedDividerDrawable

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

    override fun onViewCreated(view:View, savedInstanceState: Bundle?) {
        val locationId = arguments?.getInt(ARG_LOCATION_ID) ?: -1
        viewModel.setLocation(locationId)

        val adapter = LocationDetailAdapterWrapper()
        setAdapter(adapter.adapter)

        recyclerView.addItemDecoration(ChildDivider(DashedDividerDrawable(context!!)))

        viewModel.location.observe(this, Observer {
            if (it != null) {
                adapter.bindLocation(it)
            }
        })

        viewModel.locationItems.observe(this, Observer {
            if (it != null) {
                adapter.bindItems(context!!, it)
            }
        })
    }
}
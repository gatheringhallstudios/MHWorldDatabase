package com.gatheringhallstudios.mhworlddatabase.features.locations.list

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.View
import com.gatheringhallstudios.mhworlddatabase.adapters.LocationAdapterDelegate
import com.gatheringhallstudios.mhworlddatabase.adapters.common.BasicListDelegationAdapter
import com.gatheringhallstudios.mhworlddatabase.common.RecyclerViewFragment
import com.gatheringhallstudios.mhworlddatabase.data.views.LocationView
import com.gatheringhallstudios.mhworlddatabase.getRouter

/**
 * Fragment for a list of locations
 */

class LocationListFragment : RecyclerViewFragment() {
    private val viewModel by lazy {
        ViewModelProviders.of(this).get(LocationsListViewModel::class.java)
    }

    // Setup adapter and navigation
    private val adapter = BasicListDelegationAdapter(LocationAdapterDelegate({
        getRouter().navigateLocationDetail(it.id)
    }))

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        this.setAdapter(adapter)
        viewModel.locations.observe(this, Observer<List<LocationView>>({
            adapter.items = it
            adapter.notifyDataSetChanged()
        }))
    }
}

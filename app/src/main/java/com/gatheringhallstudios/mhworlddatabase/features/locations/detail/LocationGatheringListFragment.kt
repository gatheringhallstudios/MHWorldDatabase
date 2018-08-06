package com.gatheringhallstudios.mhworlddatabase.features.locations.detail

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.View
import com.gatheringhallstudios.mhworlddatabase.adapters.common.CategoryAdapter
import com.gatheringhallstudios.mhworlddatabase.common.RecyclerViewFragment
import com.gatheringhallstudios.mhworlddatabase.components.ChildDivider
import com.gatheringhallstudios.mhworlddatabase.components.DashedDividerDrawable
import com.gatheringhallstudios.mhworlddatabase.data.models.LocationItem

/**
 * UNUSED
 * A sub-fragment that contains the list of items available to gather at a location.
 */
class LocationGatheringListFragment : RecyclerViewFragment() {
    private val viewModel by lazy {
        ViewModelProviders.of(parentFragment!!).get(LocationDetailViewModel::class.java)
    }

    private val adapter = CategoryAdapter(LocationItemsAdapterDelegate())

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        this.setAdapter(adapter)
        recyclerView.addItemDecoration(ChildDivider(DashedDividerDrawable(context!!)))

        viewModel.locationItems.observe(this, Observer(::setItems))
    }

    private fun setItems(locationItems: List<LocationItem>?) {
        adapter.clear()
        if (locationItems.orEmpty().isEmpty()) {
            return
        }

        val grouped = locationItems!!.asSequence().groupBy { it.area }

        for ((area, items) in grouped) {
            adapter.addSection("Area $area", items)
        }
    }
}

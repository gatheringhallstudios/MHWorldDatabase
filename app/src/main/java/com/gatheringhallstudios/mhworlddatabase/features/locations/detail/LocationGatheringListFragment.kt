package com.gatheringhallstudios.mhworlddatabase.features.locations.detail

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.View
import androidx.navigation.fragment.findNavController
import com.gatheringhallstudios.mhworlddatabase.R
import com.gatheringhallstudios.mhworlddatabase.adapters.BasicListDelegationAdapter
import com.gatheringhallstudios.mhworlddatabase.adapters.LocationAdapterDelegate
import com.gatheringhallstudios.mhworlddatabase.adapters.LocationItemsAdapterDelegate
import com.gatheringhallstudios.mhworlddatabase.adapters.MonsterRewardAdapterDelegate
import com.gatheringhallstudios.mhworlddatabase.data.views.LocationItemView
import com.gatheringhallstudios.mhworlddatabase.data.views.LocationView
import com.gatheringhallstudios.mhworlddatabase.features.items.ItemDetailPagerFragment
import com.gatheringhallstudios.mhworlddatabase.features.items.ItemListFragment
import com.gatheringhallstudios.mhworlddatabase.util.BundleBuilder


class LocationGatheringListFragment : Fragment() {

    companion object {
        private val ARG_LOCATION_ID = "LOCATION_ID"

        @JvmStatic
        fun newInstance(locationId: Int): ItemListFragment {
            val f = ItemListFragment()
            f.arguments = BundleBuilder().putSerializable(this.ARG_LOCATION_ID, locationId).build()
            return f
        }
    }

    private val viewModel by lazy {
        ViewModelProviders.of(parentFragment!!).get(LocationDetailViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.locationItems.observe(this, Observer<List<LocationItemView>>({
            adapter.items = it
            adapter.notifyDataSetChanged()
        }))

    }
    // Setup adapter and navigation
    private val adapter = BasicListDelegationAdapter(LocationItemsAdapterDelegate({
        findNavController().navigate(
                R.id.locationDetailDestination,
                BundleBuilder().putInt(LocationDetailPagerFragment.ARG_LOCATION_ID, it.data.id).build()
        )
    }))

}

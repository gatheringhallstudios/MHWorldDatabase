package com.gatheringhallstudios.mhworlddatabase.features.locations.detail

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import kotlinx.android.synthetic.main.list_generic.*


class LocationGatheringListFragment : Fragment() {

    companion object {
        private val ARG_LOCATION_ID = "LOCATION_ID"

    }

    override fun onCreateView(inflater: LayoutInflater, parent: ViewGroup?, savedInstance: Bundle?): View? {
        return inflater.inflate(R.layout.list_generic, parent, false)
    }

    private val viewModel by lazy {
        ViewModelProviders.of(parentFragment!!).get(LocationDetailViewModel::class.java)
    }

    private lateinit var adapter: BasicListDelegationAdapter<Any>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

//        var locationItemsAdapterDelegate = LocationItemsAdapterDelegate(onSelected={
//            findNavController().navigate(
//                    R.id.itemDetailDestination,
//                    BundleBuilder().putInt(ItemDetailPagerFragment.ARG_ITEM_ID, it.data.location_id).build())
//        })
//
//        adapter = BasicListDelegationAdapter(locationItemsAdapterDelegate)
//        //adapter.items = viewModel.locationItems.value
//
//        recycler_view.adapter = adapter


    }
}

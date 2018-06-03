package com.gatheringhallstudios.mhworlddatabase.features.locations.list

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.gatheringhallstudios.mhworlddatabase.R
import com.gatheringhallstudios.mhworlddatabase.adapters.BasicListDelegationAdapter
import com.gatheringhallstudios.mhworlddatabase.adapters.LocationAdapterDelegate
import com.gatheringhallstudios.mhworlddatabase.adapters.MonsterAdapterDelegate
import com.gatheringhallstudios.mhworlddatabase.common.RecyclerViewFragment
import com.gatheringhallstudios.mhworlddatabase.data.views.LocationView
import com.gatheringhallstudios.mhworlddatabase.data.views.MonsterView
import com.gatheringhallstudios.mhworlddatabase.features.monsters.MonsterDetailPagerFragment
import com.gatheringhallstudios.mhworlddatabase.util.BundleBuilder

/**
 * Fragment for a list of monsters
 */

class LocationsListFragment : RecyclerViewFragment() {
    companion object {
        private val ARG_TAB = "MONSTER_TAB"

        @JvmStatic
        fun newInstance(): LocationsListFragment {
            val f = LocationsListFragment()
            return f
        }
    }

    private val viewModel by lazy {
        ViewModelProviders.of(this).get(LocationsListViewModel::class.java)
    }

    // Setup adapter and navigation
    private val adapter = BasicListDelegationAdapter(LocationAdapterDelegate({
//        findNavController().navigate(
//                R.id.monsterDetailDestination,
//                BundleBuilder().putInt(MonsterDetailPagerFragment.ARG_MONSTER_ID, it.id).build()
//        )
    }))

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        this.setAdapter(adapter)

        viewModel.getLocations();

        viewModel.locations.observe(this, Observer<List<LocationView>>({
            adapter.items = it
            adapter.notifyDataSetChanged()
        }))
    }
}

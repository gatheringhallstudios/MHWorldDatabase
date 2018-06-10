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
<<<<<<< HEAD
import com.gatheringhallstudios.mhworlddatabase.features.locations.detail.LocationDetailPagerFragment
import com.gatheringhallstudios.mhworlddatabase.util.BundleBuilder


=======
import com.gatheringhallstudios.mhworlddatabase.data.views.MonsterView
import com.gatheringhallstudios.mhworlddatabase.features.monsters.MonsterDetailPagerFragment
import com.gatheringhallstudios.mhworlddatabase.util.BundleBuilder

>>>>>>> f15fac5fe64b64d0e4b5fc135c385f3ff0033509
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
<<<<<<< HEAD
        findNavController().navigate(
                R.id.locationDetailDestination,
                BundleBuilder().putInt(LocationDetailPagerFragment.ARG_LOCATION_ID, it.id).build()
        )
=======
//        findNavController().navigate(
//                R.id.monsterDetailDestination,
//                BundleBuilder().putInt(MonsterDetailPagerFragment.ARG_MONSTER_ID, it.id).build()
//        )
>>>>>>> f15fac5fe64b64d0e4b5fc135c385f3ff0033509
    }))

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        this.setAdapter(adapter)

<<<<<<< HEAD
        viewModel.getLocations()
=======
        viewModel.getLocations();
>>>>>>> f15fac5fe64b64d0e4b5fc135c385f3ff0033509

        viewModel.locations.observe(this, Observer<List<LocationView>>({
            adapter.items = it
            adapter.notifyDataSetChanged()
        }))
    }
}

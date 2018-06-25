package com.gatheringhallstudios.mhworlddatabase.features.armor

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.View
import com.gatheringhallstudios.mhworlddatabase.R
import com.gatheringhallstudios.mhworlddatabase.adapters.ArmorSetAdapterDelegate
import com.gatheringhallstudios.mhworlddatabase.adapters.common.BasicListDelegationAdapter
import com.gatheringhallstudios.mhworlddatabase.common.RecyclerViewFragment
import com.gatheringhallstudios.mhworlddatabase.data.types.Rank
import com.gatheringhallstudios.mhworlddatabase.data.views.ArmorSetView

/**
 * Created by Carlos on 3/22/2018.
 */

class ArmorSetListFragment : RecyclerViewFragment() {
    private val viewModel by lazy {
        ViewModelProviders.of(this).get(ArmorSetListViewModel::class.java)
    }

    val adapter = BasicListDelegationAdapter(ArmorSetAdapterDelegate())

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        this.setAdapter(adapter)

        // TODO Add switching for High and Low rank Armor Set List Fragments
        viewModel.getArmorSetList(Rank.HIGH).observe(this, Observer<List<ArmorSetView>>({
            adapter.items = it
            adapter.notifyDataSetChanged()
        }))
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        activity?.title = getString(R.string.armor_title)
    }
}

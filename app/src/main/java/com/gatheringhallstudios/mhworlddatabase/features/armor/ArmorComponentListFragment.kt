package com.gatheringhallstudios.mhworlddatabase.features.armor

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.view.menu.MenuView
import android.view.View
import com.gatheringhallstudios.mhworlddatabase.adapters.ItemAdapterDelegate
import com.gatheringhallstudios.mhworlddatabase.adapters.LocationAdapterDelegate
import com.gatheringhallstudios.mhworlddatabase.adapters.common.BasicListDelegationAdapter
import com.gatheringhallstudios.mhworlddatabase.common.RecyclerViewFragment
import com.gatheringhallstudios.mhworlddatabase.data.views.ArmorComponentView
import com.gatheringhallstudios.mhworlddatabase.getRouter

class ArmorComponentListFragment: RecyclerViewFragment() {
    private val viewModel by lazy {
        ViewModelProviders.of(parentFragment!!).get(ArmorDetailViewModel::class.java)
    }

    val adapter = BasicListDelegationAdapter(ItemAdapterDelegate({
        getRouter().navigateItemDetail(it.id)
    }))

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setAdapter(adapter)

        viewModel.armorComponents.observe(this, Observer(::populateComponents))
    }

    private fun populateComponents(components : List<ArmorComponentView>?) {
        if (components == null) {
            adapter.notifyDataSetChanged()
            return
        }

        val groups = locations.groupBy {
            "${it.data.rank} ${it.location_name}"
        }

        for (group in groups) {
            adapter.addSubSection(group.key, group.value)
        }

        adapter.notifyDataSetChanged()
    }
}
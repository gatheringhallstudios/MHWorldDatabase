package com.gatheringhallstudios.mhworlddatabase.features.armor

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.View
import com.gatheringhallstudios.mhworlddatabase.adapters.ArmorComponentAdapterDelegate
import com.gatheringhallstudios.mhworlddatabase.adapters.common.BasicListDelegationAdapter
import com.gatheringhallstudios.mhworlddatabase.common.RecyclerViewFragment
import com.gatheringhallstudios.mhworlddatabase.data.views.ArmorComponentView
import com.gatheringhallstudios.mhworlddatabase.getRouter

class ArmorComponentListFragment: RecyclerViewFragment() {
    private val viewModel by lazy {
        ViewModelProviders.of(parentFragment!!).get(ArmorDetailViewModel::class.java)
    }

    val adapter = BasicListDelegationAdapter(ArmorComponentAdapterDelegate({
        getRouter().navigateItemDetail(it.result.id)
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
        adapter.items = components
        adapter.notifyDataSetChanged()
    }
}
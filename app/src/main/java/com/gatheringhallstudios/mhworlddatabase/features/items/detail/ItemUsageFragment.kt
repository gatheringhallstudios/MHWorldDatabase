package com.gatheringhallstudios.mhworlddatabase.features.items.detail

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.View
import com.gatheringhallstudios.mhworlddatabase.R
import com.gatheringhallstudios.mhworlddatabase.adapters.ItemCraftingAdapterDelegate
import com.gatheringhallstudios.mhworlddatabase.adapters.common.CategoryAdapter
import com.gatheringhallstudios.mhworlddatabase.common.RecyclerViewFragment
import com.gatheringhallstudios.mhworlddatabase.data.models.ItemUsages

class ItemUsageFragment : RecyclerViewFragment() {
    private val viewModel by lazy {
        ViewModelProviders.of(parentFragment!!).get(ItemDetailViewModel::class.java)
    }

    val adapter = CategoryAdapter(
            ItemCraftingAdapterDelegate()
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setAdapter(adapter)

        viewModel.usageData.observe(this, Observer(::populateData))
    }

    private fun populateData(data: ItemUsages?) {
        adapter.clear()
        if (data == null) {
            adapter.notifyDataSetChanged()
            return
        }

        // todo: add once the adapter can handle them
        adapter.addAll(data.craftRecipes)
        //adapter.addAll(data.charms)
        //adapter.addAll(data.armor)

        adapter.notifyDataSetChanged()
    }
}
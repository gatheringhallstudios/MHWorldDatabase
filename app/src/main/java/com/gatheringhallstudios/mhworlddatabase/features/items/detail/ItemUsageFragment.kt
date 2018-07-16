package com.gatheringhallstudios.mhworlddatabase.features.items.detail

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.View
import com.gatheringhallstudios.mhworlddatabase.R
import com.gatheringhallstudios.mhworlddatabase.adapters.ItemCraftingAdapterDelegate
import com.gatheringhallstudios.mhworlddatabase.adapters.common.CategoryAdapter
import com.gatheringhallstudios.mhworlddatabase.common.RecyclerViewFragment

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

    private fun populateData(data: UsageData?) {
        adapter.clear()
        if (data == null) {
            adapter.notifyDataSetChanged()
            return
        }

        if (data.craftRecipes.isNotEmpty()) {
            adapter.addSection(getString(R.string.item_header_crafting), data.craftRecipes)
        }

        adapter.notifyDataSetChanged()
    }
}
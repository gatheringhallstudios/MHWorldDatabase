package com.gatheringhallstudios.mhworlddatabase.features.items.detail

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.View
import com.gatheringhallstudios.mhworlddatabase.R
import com.gatheringhallstudios.mhworlddatabase.adapters.ItemCraftingAdapterDelegate
import com.gatheringhallstudios.mhworlddatabase.adapters.SimpleUniversalBinderAdapterDelegate
import com.gatheringhallstudios.mhworlddatabase.adapters.SimpleUniversalBinding
import com.gatheringhallstudios.mhworlddatabase.adapters.common.CategoryAdapter
import com.gatheringhallstudios.mhworlddatabase.adapters.createSimpleUniversalBinder
import com.gatheringhallstudios.mhworlddatabase.assets.AssetLoader
import com.gatheringhallstudios.mhworlddatabase.common.RecyclerViewFragment
import com.gatheringhallstudios.mhworlddatabase.components.IconType
import com.gatheringhallstudios.mhworlddatabase.data.models.ItemUsageArmor
import com.gatheringhallstudios.mhworlddatabase.data.models.ItemUsageCharm
import com.gatheringhallstudios.mhworlddatabase.data.models.ItemUsages
import com.gatheringhallstudios.mhworlddatabase.getRouter

fun bindCharmCraft(data: ItemUsageCharm) = createSimpleUniversalBinder { ctx ->
    SimpleUniversalBinding(
            label = data.result.name,
            value = data.quantity.toString(),
            icon = AssetLoader.loadIconFor(data.result),
            iconType = IconType.EMBELLISHED,
            clickFn = { v -> v.getRouter().navigateCharmDetail(data.result.id) }
    )
}

fun bindArmorCraft(data: ItemUsageArmor) = createSimpleUniversalBinder { ctx ->
    SimpleUniversalBinding(
            label = data.result.name,
            value = data.quantity.toString(),
            icon = AssetLoader.loadIconFor(data.result),
            iconType = IconType.ZEMBELLISHED,
            clickFn = { v -> v.getRouter().navigateArmorDetail(data.result.id) }
    )
}

/**
 * A sub-fragment used to display the ways an item can be used.
 */
class ItemUsageFragment : RecyclerViewFragment() {
    private val viewModel by lazy {
        ViewModelProviders.of(parentFragment!!).get(ItemDetailViewModel::class.java)
    }

    val adapter = CategoryAdapter(
            ItemCraftingAdapterDelegate(),
            SimpleUniversalBinderAdapterDelegate()
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setAdapter(adapter)

        viewModel.usageData.observe(this, Observer(::populateData))
    }

    private fun populateData(data: ItemUsages?) {
        adapter.clear()
        if (data == null) {
            return
        }

        adapter.addSections(mapOf(
                getString(R.string.item_header_crafting) to data.craftRecipes,
                getString(R.string.item_header_usage_charms) to data.charms.map(::bindCharmCraft),
                getString(R.string.item_header_usage_armor)to data.armor.map(::bindArmorCraft)
        ), skipEmpty = true)
    }
}
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
import com.gatheringhallstudios.mhworlddatabase.assets.assetLoader
import com.gatheringhallstudios.mhworlddatabase.common.RecyclerViewFragment
import com.gatheringhallstudios.mhworlddatabase.data.models.ArmorBase
import com.gatheringhallstudios.mhworlddatabase.data.models.CharmBase
import com.gatheringhallstudios.mhworlddatabase.data.models.ItemUsages
import com.gatheringhallstudios.mhworlddatabase.getRouter

fun bindCharmCraft(charmBase: CharmBase) = createSimpleUniversalBinder { ctx ->
    SimpleUniversalBinding(
            label = charmBase.name,
            value = ctx.getString(R.string.type_charm),
            icon = ctx.assetLoader.loadIconFor(charmBase),
            clickFn = { }
    )
}

fun bindArmorCraft(armor: ArmorBase) = createSimpleUniversalBinder { ctx ->
    SimpleUniversalBinding(
            label = armor.name,
            value = ctx.getString(R.string.type_armor),
            icon = ctx.assetLoader.loadIconFor(armor),
            clickFn = { v -> v.getRouter().navigateArmorDetail(armor.id) }
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
            adapter.notifyDataSetChanged()
            return
        }

        adapter.addAll(data.craftRecipes)
        adapter.addAll(data.charmBases.map(::bindCharmCraft))
        adapter.addAll(data.armor.map(::bindArmorCraft))

        adapter.notifyDataSetChanged()
    }
}
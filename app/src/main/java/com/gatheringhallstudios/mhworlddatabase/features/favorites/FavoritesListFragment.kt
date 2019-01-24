package com.gatheringhallstudios.mhworlddatabase.features.favorites

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.View
import com.gatheringhallstudios.mhworlddatabase.R
import com.gatheringhallstudios.mhworlddatabase.adapters.ItemAdapterDelegate
import com.gatheringhallstudios.mhworlddatabase.adapters.LocationAdapterDelegate
import com.gatheringhallstudios.mhworlddatabase.adapters.common.CategoryAdapter
import com.gatheringhallstudios.mhworlddatabase.common.RecyclerViewFragment
import com.gatheringhallstudios.mhworlddatabase.components.ChildDivider
import com.gatheringhallstudios.mhworlddatabase.components.DashedDividerDrawable
import com.gatheringhallstudios.mhworlddatabase.data.models.Armor
import com.gatheringhallstudios.mhworlddatabase.data.models.Item
import com.gatheringhallstudios.mhworlddatabase.data.models.ItemSources
import com.gatheringhallstudios.mhworlddatabase.data.types.Rank

/**
 * A sub-fragment that displays the means of acquiring an item
 */
class FavoritesListFragment : RecyclerViewFragment() {
    private val viewModel by lazy {
        ViewModelProviders.of(parentFragment!!).get(FavoritesListViewModel::class.java)
    }

    val adapter = CategoryAdapter(
        ItemAdapterDelegate({})
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setAdapter(adapter)
        recyclerView.addItemDecoration(ChildDivider(DashedDividerDrawable(context!!)))
        viewModel.loadFavorites()
//        viewModel.armorFavorites.observe(this, Observer(::populateArmor))
        viewModel.itemFavorites.observe(this, Observer(::populateItems))
    }

    private fun populateArmor(data: List<Armor>) {
        adapter.clear()

        if (data.isNotEmpty()) {
            adapter.addSection(getString(R.string.header_armor), data)
        }
    }

    private fun populateItems(data: List<Item>) {
        adapter.addSection(getString(R.string.title_item_list), data)
    }

    private fun populateData(data: ItemSources?) {
        adapter.clear()
        if (data == null) {
            return
        }
        if (data.isEmpty()) {
            showEmptyView()
            return
        }

        if (data.craftRecipes.isNotEmpty()) {
            adapter.addSection(getString(R.string.header_crafting), data.craftRecipes)
        }

        if (data.locations.isNotEmpty()) {
            val groups = data.locations.groupBy {
                // todo: centralize
                val rankString = when (it.rank) {
                    Rank.LOW -> getString(R.string.rank_short_low)
                    Rank.HIGH -> getString(R.string.rank_short_high)
                    null -> getString(R.string.rank_short_all)
                }

                "$rankString ${it.location.name}"
            }

            adapter.addSection(getString(R.string.header_gathering), groups)
        }

        if (data.rewards.isNotEmpty()) {
            adapter.addSection(getString(R.string.header_rewards), data.rewards)
        }
    }
}
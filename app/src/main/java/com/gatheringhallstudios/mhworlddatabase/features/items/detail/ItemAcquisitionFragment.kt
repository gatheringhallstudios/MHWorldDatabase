package com.gatheringhallstudios.mhworlddatabase.features.items.detail

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gatheringhallstudios.mhworlddatabase.R
import com.gatheringhallstudios.mhworlddatabase.adapters.ItemCraftingAdapterDelegate
import com.gatheringhallstudios.mhworlddatabase.adapters.common.CategoryAdapter
import com.gatheringhallstudios.mhworlddatabase.common.RecyclerViewFragment
import com.gatheringhallstudios.mhworlddatabase.adapters.common.SimpleListDelegate
import com.gatheringhallstudios.mhworlddatabase.data.models.ItemLocation
import com.gatheringhallstudios.mhworlddatabase.data.models.ItemSources
import com.gatheringhallstudios.mhworlddatabase.data.types.Rank
import kotlinx.android.synthetic.main.listitem_reward.view.*

/**
 * A sub-fragment that displays the means of acquiring an item
 */
class ItemAcquisitionFragment : RecyclerViewFragment() {
    private val viewModel by lazy {
        ViewModelProviders.of(parentFragment!!).get(ItemDetailViewModel::class.java)
    }

    val adapter = CategoryAdapter(
            ItemCraftingAdapterDelegate(),
            ItemLocationAdapterDelegate(),
            MonsterRewardSourceAdapterDelegate())

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setAdapter(adapter)

        viewModel.acquisitionData.observe(this, Observer(::populateData))
    }

    private fun populateData(data: ItemSources?) {
        adapter.clear()
        if (data == null) {
            return
        }

        if (data.craftRecipes.isNotEmpty()) {
            adapter.addSection(getString(R.string.item_header_crafting), data.craftRecipes)
        }

        if (data.locations.isNotEmpty()) {
            val groups = data.locations.groupBy {
                // todo: centralize
                val rankString = when (it.rank) {
                    Rank.LOW -> getString(R.string.low_rank_short)
                    Rank.HIGH -> getString(R.string.high_rank_short)
                    null -> getString(R.string.all_rank_short)
                }

                "$rankString ${it.location.name}"
            }

            adapter.addSection(getString(R.string.item_header_gathering), groups)
        }

        if (data.rewards.isNotEmpty()) {
            adapter.addSection(getString(R.string.item_header_rewards), data.rewards)
        }
    }
}
package com.gatheringhallstudios.mhworlddatabase.features.items.detail

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.View
import com.gatheringhallstudios.mhworlddatabase.R
import com.gatheringhallstudios.mhworlddatabase.adapters.ItemCraftingAdapterDelegate
import com.gatheringhallstudios.mhworlddatabase.adapters.common.CategoryAdapter
import com.gatheringhallstudios.mhworlddatabase.util.RecyclerViewFragment
import com.gatheringhallstudios.mhworlddatabase.components.ChildDivider
import com.gatheringhallstudios.mhworlddatabase.components.DashedDividerDrawable
import com.gatheringhallstudios.mhworlddatabase.data.models.ItemSources
import com.gatheringhallstudios.mhworlddatabase.data.types.Rank

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
            MonsterRewardSourceAdapterDelegate(),
            QuestRewardSourceAdapterDelegate())

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setAdapter(adapter)
        recyclerView.addItemDecoration(ChildDivider(DashedDividerDrawable(context!!)))

        viewModel.acquisitionData.observe(this, Observer(::populateData))
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

        if (data.monsterRewards.isNotEmpty()) {
            adapter.addSection(getString(R.string.header_rewards), data.monsterRewards)
        }

        if (data.questRewards.isNotEmpty()) {
            adapter.addSection(getString(R.string.header_quest_rewards), data.questRewards)
        }
    }
}
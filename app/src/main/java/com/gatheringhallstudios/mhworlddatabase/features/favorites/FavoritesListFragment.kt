package com.gatheringhallstudios.mhworlddatabase.features.favorites

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.View
import com.gatheringhallstudios.mhworlddatabase.R
import com.gatheringhallstudios.mhworlddatabase.adapters.CharmAdapterDelegate
import com.gatheringhallstudios.mhworlddatabase.adapters.DecorationAdapterDelegate
import com.gatheringhallstudios.mhworlddatabase.adapters.ItemAdapterDelegate
import com.gatheringhallstudios.mhworlddatabase.adapters.LocationAdapterDelegate
import com.gatheringhallstudios.mhworlddatabase.adapters.common.CategoryAdapter
import com.gatheringhallstudios.mhworlddatabase.common.RecyclerViewFragment
import com.gatheringhallstudios.mhworlddatabase.components.ChildDivider
import com.gatheringhallstudios.mhworlddatabase.components.DashedDividerDrawable
import com.gatheringhallstudios.mhworlddatabase.data.models.FavoriteEntities
import com.gatheringhallstudios.mhworlddatabase.features.monsters.list.MonsterListAdapter
import com.gatheringhallstudios.mhworlddatabase.features.skills.list.SkillTreeListAdapter
import com.gatheringhallstudios.mhworlddatabase.features.weapons.WeaponTreeAdapter

/**
 * A sub-fragment that displays the means of acquiring an item
 */
class FavoritesListFragment : RecyclerViewFragment() {
    private val viewModel by lazy {
        ViewModelProviders.of(parentFragment!!).get(FavoritesListViewModel::class.java)
    }

    val adapter = CategoryAdapter(
        ItemAdapterDelegate({}),
        LocationAdapterDelegate({}),
        CharmAdapterDelegate({}),
        DecorationAdapterDelegate({}),
        ,

    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setAdapter(adapter)
        recyclerView.addItemDecoration(ChildDivider(DashedDividerDrawable(context!!)))
        viewModel.loadFavorites()
        viewModel.favoriteEntities.observe(this, Observer(::populateFavoriteEntities))
    }

    private fun populateFavoriteEntities(data: FavoriteEntities) {
        adapter.clear()

        if (data.items.isNotEmpty()) {
            adapter.addSection(getString(R.string.title_item_list), data.items)
        }

        if (data.locations.isNotEmpty()) {
            adapter.addSection(getString(R.string.title_locations), data.locations)
        }

        if (data.charms.isNotEmpty()) {
            adapter.addSection(getString(R.string.title_charms), data.charms)
        }

        if (data.decorations.isNotEmpty()) {
            adapter.addSection(getString(R.string.title_decorations), data.decorations)
        }
    }


//
//    private fun populateData(data: ItemSources?) {
//        adapter.clear()
//        if (data == null) {
//            return
//        }
//        if (data.isEmpty()) {
//            showEmptyView()
//            return
//        }
//
//        if (data.craftRecipes.isNotEmpty()) {
//            adapter.addSection(getString(R.string.header_crafting), data.craftRecipes)
//        }
//
//        if (data.locations.isNotEmpty()) {
//            val groups = data.locations.groupBy {
//                // todo: centralize
//                val rankString = when (it.rank) {
//                    Rank.LOW -> getString(R.string.rank_short_low)
//                    Rank.HIGH -> getString(R.string.rank_short_high)
//                    null -> getString(R.string.rank_short_all)
//                }
//
//                "$rankString ${it.location.name}"
//            }
//
//            adapter.addSection(getString(R.string.header_gathering), groups)
//        }
//
//        if (data.rewards.isNotEmpty()) {
//            adapter.addSection(getString(R.string.header_rewards), data.rewards)
//        }
//    }
}
package com.gatheringhallstudios.mhworlddatabase.features.favorites

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.View
import com.gatheringhallstudios.mhworlddatabase.R
import com.gatheringhallstudios.mhworlddatabase.adapters.common.CategoryAdapter
import com.gatheringhallstudios.mhworlddatabase.common.RecyclerViewFragment
import com.gatheringhallstudios.mhworlddatabase.components.ChildDivider
import com.gatheringhallstudios.mhworlddatabase.components.DashedDividerDrawable
import com.gatheringhallstudios.mhworlddatabase.data.models.FavoriteEntities
import com.gatheringhallstudios.mhworlddatabase.getRouter

/**
 * A sub-fragment that displays the means of acquiring an item
 */
class FavoritesListFragment : RecyclerViewFragment() {
    private val viewModel by lazy {
        ViewModelProviders.of(parentFragment!!).get(FavoritesListViewModel::class.java)
    }

    val adapter = CategoryAdapter(
            ItemBookmarkDelegate{ getRouter().navigateItemDetail(it.id) },
            LocationBookmarkDelegate{ getRouter().navigateLocationDetail(it.id) },
            CharmBookmarkDelegate{ getRouter().navigateCharmDetail(it.id) },
            DecorationBaseBookmarkDelegate{ getRouter().navigateDecorationDetail(it.id) },
            MonsterBaseBookmarkDelegate{ getRouter().navigateMonsterDetail(it.id) },
            SkillTreeBookmarkDelegate{ getRouter().navigateSkillDetail(it.id) },
            WeaponBookmarkDelegate{ getRouter().navigateWeaponDetail(it.id) },
            ArmorBookmarkDelegate{ getRouter().navigateArmorDetail(it.id) }
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setAdapter(adapter)
        recyclerView.addItemDecoration(ChildDivider(DashedDividerDrawable(context!!)))
        viewModel.loadFavorites()
        viewModel.favoriteEntities.observe(this, Observer(::populateFavoriteEntities))
    }

    private fun populateFavoriteEntities(data: FavoriteEntities) {
        adapter.clear()
        if (data.armor.isNotEmpty()) {
            adapter.addSection(getString(R.string.title_armor), data.armor)
        }

        if (data.items.isNotEmpty()) {
            adapter.addSection(getString(R.string.title_item_list), data.items)
        }

        if (data.charms.isNotEmpty()) {
            adapter.addSection(getString(R.string.title_charms), data.charms)
        }

        if (data.locations.isNotEmpty()) {
            adapter.addSection(getString(R.string.title_locations), data.locations)
        }

        if (data.monsters.isNotEmpty()) {
            adapter.addSection(getString(R.string.title_monster_list), data.monsters)
        }

        if (data.skillTrees.isNotEmpty()) {
            adapter.addSection(getString(R.string.title_skills), data.skillTrees)
        }

        if (data.weapons.isNotEmpty()) {
            adapter.addSection(getString(R.string.title_weapons), data.weapons)
        }

        if (data.decorations.isNotEmpty()) {
            adapter.addSection(getString(R.string.title_decorations), data.decorations)
        }
    }
}
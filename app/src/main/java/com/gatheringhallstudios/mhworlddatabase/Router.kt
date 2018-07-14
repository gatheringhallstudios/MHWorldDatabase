package com.gatheringhallstudios.mhworlddatabase

import androidx.navigation.NavController
import com.gatheringhallstudios.mhworlddatabase.features.decorations.DecorationDetailFragment
import com.gatheringhallstudios.mhworlddatabase.features.items.ItemDetailPagerFragment
import com.gatheringhallstudios.mhworlddatabase.features.locations.detail.LocationDetailPagerFragment
import com.gatheringhallstudios.mhworlddatabase.features.monsters.MonsterDetailPagerFragment
import com.gatheringhallstudios.mhworlddatabase.features.skills.SkillDetailFragment
import com.gatheringhallstudios.mhworlddatabase.util.BundleBuilder

/**
 * Defines a class that can be used to perform navigation.
 * Do not store this in an instance variable. Request a new router every time
 * you need to navigate.
 */
class Router(private val navController: NavController) {
    fun navigateItemDetail(itemId: Int) {
        navController.navigate(
                R.id.openItemDetailAction,
                BundleBuilder().putInt(ItemDetailPagerFragment.ARG_ITEM_ID, itemId).build())
    }

    fun navigateLocationDetail(locationId: Int) {
        navController.navigate(
                R.id.openLocationDetailAction,
                BundleBuilder().putInt(LocationDetailPagerFragment.ARG_LOCATION_ID, locationId).build())
    }

    fun navigateMonsterDetail(monsterId: Int) {
        navController.navigate(
                R.id.openMonsterDetailAction,
                BundleBuilder().putInt(MonsterDetailPagerFragment.ARG_MONSTER_ID, monsterId).build())
    }

    fun navigateSkillDetail(skillTreeId: Int) {
        navController.navigate(
                R.id.openSkillDetailAction,
                BundleBuilder().putInt(SkillDetailFragment.ARG_SKILLTREE_ID, skillTreeId).build())
    }

    fun navigateDecorationDetail(decorationId: Int) {
        navController.navigate(
                R.id.openDecorationDetailAction,
                BundleBuilder().putInt(DecorationDetailFragment.ARG_DECORATION_ID, decorationId).build())
    }
}
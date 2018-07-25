package com.gatheringhallstudios.mhworlddatabase

import androidx.navigation.NavController
import com.gatheringhallstudios.mhworlddatabase.data.types.DataType
import com.gatheringhallstudios.mhworlddatabase.features.armor.ArmorDetailFragment
import com.gatheringhallstudios.mhworlddatabase.features.charms.CharmDetailFragment
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
    fun navigateObject(type: DataType, id: Int) = when(type) {
        DataType.ITEM -> navigateItemDetail(id)
        DataType.LOCATION -> navigateLocationDetail(id)
        DataType.MONSTER -> navigateMonsterDetail(id)
        DataType.SKILL -> navigateSkillDetail(id)
        DataType.ARMOR -> navigateArmorDetail(id)
        DataType.CHARM -> navigateCharmDetail(id)
        DataType.DECORATION -> navigateDecorationDetail(id)
        DataType.WEAPON -> navigateWeaponDetail(id)
    }

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

    fun navigateArmorDetail(armorId: Int) {
        navController.navigate(
                R.id.armorDetailDestination,
                BundleBuilder().putInt(ArmorDetailFragment.ARG_ARMOR_ID, armorId).build()
        )
    }

    fun navigateCharmDetail(charmId: Int) {
        navController.navigate(
                R.id.charmDetailDestination,
                BundleBuilder().putInt(CharmDetailFragment.ARG_CHARM_ID, charmId).build()
        )
    }

    fun navigateWeaponDetail(weaponId: Int) {
        // todo: implement
    }
}
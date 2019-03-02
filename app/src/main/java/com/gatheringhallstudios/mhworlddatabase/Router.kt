package com.gatheringhallstudios.mhworlddatabase

import androidx.navigation.NavController
import com.gatheringhallstudios.mhworlddatabase.data.types.DataType
import com.gatheringhallstudios.mhworlddatabase.data.types.WeaponType
import com.gatheringhallstudios.mhworlddatabase.features.armor.detail.ArmorDetailPagerFragment
import com.gatheringhallstudios.mhworlddatabase.features.charms.detail.CharmDetailFragment
import com.gatheringhallstudios.mhworlddatabase.features.decorations.detail.DecorationDetailFragment
import com.gatheringhallstudios.mhworlddatabase.features.items.detail.ItemDetailPagerFragment
import com.gatheringhallstudios.mhworlddatabase.features.locations.detail.LocationSummaryFragment
import com.gatheringhallstudios.mhworlddatabase.features.monsters.detail.MonsterDetailPagerFragment
import com.gatheringhallstudios.mhworlddatabase.features.skills.detail.SkillDetailFragment
import com.gatheringhallstudios.mhworlddatabase.features.weapons.detail.WeaponDetailPagerFragment
import com.gatheringhallstudios.mhworlddatabase.features.weapons.list.WeaponTreeListFragment.Companion.ARG_WEAPON_TREE_TYPE
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
        else -> Unit
    }

    fun navigateItemDetail(itemId: Int) {
        navController.navigate(
                R.id.openItemDetailAction,
                BundleBuilder().putInt(ItemDetailPagerFragment.ARG_ITEM_ID, itemId).build())
    }

    fun navigateLocationDetail(locationId: Int) {
        navController.navigate(
                R.id.openLocationDetailAction,
                BundleBuilder().putInt(LocationSummaryFragment.ARG_LOCATION_ID, locationId).build())
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
                R.id.openArmorDetailAction,
                BundleBuilder().putInt(ArmorDetailPagerFragment.ARG_ARMOR_ID, armorId).build()
        )
    }

    fun navigateCharmDetail(charmId: Int) {
        navController.navigate(
                R.id.openCharmDetailAction,
                BundleBuilder().putInt(CharmDetailFragment.ARG_CHARM_ID, charmId).build()
        )
    }

    fun navigateWeaponTree(weaponType: WeaponType) {
        navController.navigate(
                R.id.openWeaponTreeAction,
                BundleBuilder().putSerializable(ARG_WEAPON_TREE_TYPE, weaponType).build()
        )
    }

    fun navigateWeaponDetail(weaponId: Int) {
        navController.navigate(
                R.id.openWeaponDetailAction,
                BundleBuilder().putInt(WeaponDetailPagerFragment.ARG_WEAPON_ID, weaponId).build()
        )
    }
}
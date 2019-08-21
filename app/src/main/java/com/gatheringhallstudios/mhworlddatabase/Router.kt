package com.gatheringhallstudios.mhworlddatabase

import androidx.navigation.NavController
import com.gatheringhallstudios.mhworlddatabase.data.models.UserEquipment
import com.gatheringhallstudios.mhworlddatabase.data.models.UserEquipmentSet
import com.gatheringhallstudios.mhworlddatabase.data.types.ArmorType
import com.gatheringhallstudios.mhworlddatabase.data.types.DataType
import com.gatheringhallstudios.mhworlddatabase.data.types.WeaponType
import com.gatheringhallstudios.mhworlddatabase.features.armor.detail.ArmorDetailPagerFragment
import com.gatheringhallstudios.mhworlddatabase.features.armor.list.ArmorSetListPagerFragment
import com.gatheringhallstudios.mhworlddatabase.features.charms.detail.CharmDetailFragment
import com.gatheringhallstudios.mhworlddatabase.features.charms.list.CharmListFragment
import com.gatheringhallstudios.mhworlddatabase.features.decorations.detail.DecorationDetailFragment
import com.gatheringhallstudios.mhworlddatabase.features.items.detail.ItemDetailPagerFragment
import com.gatheringhallstudios.mhworlddatabase.features.locations.detail.LocationSummaryFragment
import com.gatheringhallstudios.mhworlddatabase.features.monsters.detail.MonsterDetailPagerFragment
import com.gatheringhallstudios.mhworlddatabase.features.skills.detail.SkillDetailFragment
import com.gatheringhallstudios.mhworlddatabase.features.userequipmentsetbuilder.detail.UserEquipmentSetDetailPagerFragment
import com.gatheringhallstudios.mhworlddatabase.features.userequipmentsetbuilder.selectors.UserEquipmentSetSelectorListFragment
import com.gatheringhallstudios.mhworlddatabase.features.weapons.detail.WeaponDetailPagerFragment
import com.gatheringhallstudios.mhworlddatabase.features.weapons.list.WeaponTreePagerFragment.Companion.ARG_WEAPON_TREE_TYPE
import com.gatheringhallstudios.mhworlddatabase.util.BundleBuilder

/**
 * Defines a class that can be used to perform navigation.
 * Do not store this in an instance variable. Request a new router every time
 * you need to navigate.
 */
class Router(private val navController: NavController) {
    fun navigateObject(type: DataType, id: Int) = when (type) {
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

    fun navigateUserEquipmentSetDetail(userEquipmentSet: UserEquipmentSet) {
        navController.navigate(
                R.id.openUserEquipmentSetDetailAction,
                BundleBuilder().putSerializable(UserEquipmentSetDetailPagerFragment.ARG_USER_EQUIPMENT_SET, userEquipmentSet).build())
    }

    fun navigateUserEquipmentArmorSelector(userEquipmentSetId: Int?, prevId: Int?, filter: ArmorType?) {
        val bundle = BundleBuilder()
                .putSerializable(ArmorSetListPagerFragment.ARG_MODE, ArmorSetListPagerFragment.ArmorSetListMode.BUILDER)

        if (userEquipmentSetId != null) bundle.putInt(ArmorSetListPagerFragment.ARG_SET_ID, userEquipmentSetId)
        if (prevId != null) bundle.putInt(ArmorSetListPagerFragment.ARG_PREV_ID, prevId)
        if (filter != null) bundle.putSerializable(ArmorSetListPagerFragment.ARG_ITEM_FILTER, filter)

        navController.navigate(
                R.id.userArmorSelectionListAction,
                bundle.build()
        )
    }

    fun navigateUserEquipmentCharmSelector(userEquipmentSetId: Int?, prevId: Int?) {
        val bundle = BundleBuilder()
                .putSerializable(CharmListFragment.ARG_MODE, CharmListFragment.Companion.CharmListMode.BUILDER)

        if (userEquipmentSetId != null) bundle.putInt(CharmListFragment.ARG_SET_ID, userEquipmentSetId)
        if (prevId != null) bundle.putInt(CharmListFragment.ARG_PREV_ID, prevId)

        navController.navigate(
                R.id.userCharmSelectionListAction,
                bundle.build()
        )
    }

    fun navigateUserEquipmentPieceSelector(activeEquipment: UserEquipment, userEquipmentSetId: Int?, filter: ArmorType?) {
        val bundle = BundleBuilder()
                .putSerializable(UserEquipmentSetSelectorListFragment.ARG_ACTIVE_EQUIPMENT, activeEquipment)
        if (userEquipmentSetId != null) bundle.putInt(ArmorSetListPagerFragment.ARG_SET_ID, userEquipmentSetId)
        if (filter != null) bundle.putSerializable(ArmorSetListPagerFragment.ARG_ITEM_FILTER, filter)
        navController.navigate(
                R.id.equipmentSetSelectorAction,
                bundle.build()
        )
    }

    fun goBack() {
        navController.popBackStack()
    }
}
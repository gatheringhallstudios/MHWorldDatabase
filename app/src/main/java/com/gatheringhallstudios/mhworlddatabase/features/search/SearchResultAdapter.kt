package com.gatheringhallstudios.mhworlddatabase.features.search

import com.gatheringhallstudios.mhworlddatabase.R
import com.gatheringhallstudios.mhworlddatabase.adapters.SimpleUniversalBinderAdapterDelegate
import com.gatheringhallstudios.mhworlddatabase.adapters.SimpleUniversalBinding
import com.gatheringhallstudios.mhworlddatabase.adapters.common.BasicListDelegationAdapter
import com.gatheringhallstudios.mhworlddatabase.adapters.createSimpleUniversalBinder
import com.gatheringhallstudios.mhworlddatabase.assets.AssetLoader
import com.gatheringhallstudios.mhworlddatabase.components.IconType
import com.gatheringhallstudios.mhworlddatabase.data.models.*
import com.gatheringhallstudios.mhworlddatabase.data.types.ItemCategory
import com.gatheringhallstudios.mhworlddatabase.data.types.MonsterSize
import com.gatheringhallstudios.mhworlddatabase.data.types.WeaponType
import com.gatheringhallstudios.mhworlddatabase.getRouter

/**
 * Defines a search result adapter that can handle all types of search results.
 * Binding the search results populates the adapter
 */
class SearchResultAdapter: BasicListDelegationAdapter<Any>(SimpleUniversalBinderAdapterDelegate()) {
    fun bindSearchResults(results: SearchResults) {
        val items = mutableListOf<Any>()
        items.addAll(results.locations.map(::createLocationBinder))
        items.addAll(results.monsters.map(::createMonsterBinder))
        items.addAll(results.skillTrees.map(::createSkillTreeBinder))
        items.addAll(results.charms.map(::createCharmBinder))
        items.addAll(results.decorations.map(::createDecorationBinder))
        items.addAll(results.armor.map(::createArmorBinder))
        items.addAll(results.items.map(::createItemBinder))
        items.addAll(results.weapons.map(::createWeaponBinder))
        items.addAll(results.quests.map(::createQuestBinder))
        items.addAll(results.kinsects.map(::createKinsectBinder))

        this.items = items
        notifyDataSetChanged()
    }
}

fun createLocationBinder(location: Location) = createSimpleUniversalBinder { ctx ->
    val icon = AssetLoader.loadIconFor(location)
    val name = location.name
    val typeString = ctx.getString(R.string.type_location)

    SimpleUniversalBinding(name, typeString, IconType.PAPER, icon) {
        it.getRouter().navigateLocationDetail(location.id)
    }
}

fun createMonsterBinder(monster: MonsterBase) = createSimpleUniversalBinder { ctx ->
    val icon = AssetLoader.loadIconFor(monster)
    val sizeString = ctx.getString(when (monster.size) {
        MonsterSize.SMALL -> R.string.type_monster_small
        MonsterSize.LARGE -> R.string.type_monster_large
    })
    SimpleUniversalBinding(monster.name, sizeString, IconType.EMBELLISHED, icon) {
        it.getRouter().navigateMonsterDetail(monster.id)
    }
}

fun createSkillTreeBinder(skillTree: SkillTreeBase) = createSimpleUniversalBinder { ctx ->
    val icon = AssetLoader.loadIconFor(skillTree)
    val typeString = ctx.getString(R.string.type_skilltree)
    SimpleUniversalBinding(skillTree.name, typeString, IconType.NORMAL, icon) {
        it.getRouter().navigateSkillDetail(skillTree.id)
    }
}

fun createCharmBinder(charm: CharmBase) = createSimpleUniversalBinder { ctx ->
    val icon = AssetLoader.loadIconFor(charm)
    val typeString = ctx.getString(R.string.type_charm)
    SimpleUniversalBinding(charm.name, typeString, IconType.EMBELLISHED, icon) {
        it.getRouter().navigateCharmDetail(charm.id)
    }
}

fun createDecorationBinder(decoration: DecorationBase) = createSimpleUniversalBinder { ctx ->
    val icon = AssetLoader.loadIconFor(decoration)
    val typeString = ctx.getString(R.string.type_decoration)
    SimpleUniversalBinding(decoration.name, typeString, IconType.EMBELLISHED, icon) {
        it.getRouter().navigateDecorationDetail(decoration.id)
    }
}

fun createArmorBinder(armor: ArmorBase) = createSimpleUniversalBinder { ctx ->
    val icon = AssetLoader.loadIconFor(armor)
    val typeString = ctx.getString(R.string.type_armor)
    SimpleUniversalBinding(armor.name, typeString, IconType.ZEMBELLISHED, icon) {
        it.getRouter().navigateArmorDetail(armor.id)
    }
}

fun createItemBinder(item: ItemBase) = createSimpleUniversalBinder { ctx ->
    val icon = AssetLoader.loadIconFor(item)
    val name = item.name
    val typeString = ctx.getString(when (item.category) {
        ItemCategory.ITEM -> R.string.type_item_basic
        ItemCategory.MATERIAL -> R.string.type_item_material
        ItemCategory.AMMO -> R.string.type_item_ammo
        ItemCategory.MISC -> R.string.type_item_misc
        else -> R.string.type_item
    })

    SimpleUniversalBinding(name, typeString, IconType.EMBELLISHED, icon) {
        it.getRouter().navigateItemDetail(item.id)
    }
}

fun createWeaponBinder(weapon: WeaponBase) = createSimpleUniversalBinder { ctx ->
    val icon = AssetLoader.loadIconFor(weapon)
    val name = weapon.name
    val typeString = ctx.getString(when (weapon.weapon_type) {
        WeaponType.GREAT_SWORD -> R.string.title_great_sword
        WeaponType.LONG_SWORD -> R.string.title_long_sword
        WeaponType.SWORD_AND_SHIELD -> R.string.title_sword_and_shield
        WeaponType.DUAL_BLADES -> R.string.title_dual_blades
        WeaponType.HAMMER -> R.string.title_hammer
        WeaponType.HUNTING_HORN -> R.string.title_hunting_horn
        WeaponType.LANCE -> R.string.title_lance
        WeaponType.GUNLANCE -> R.string.title_gunlance
        WeaponType.SWITCH_AXE -> R.string.title_switch_axe
        WeaponType.CHARGE_BLADE -> R.string.title_charge_blade
        WeaponType.INSECT_GLAIVE -> R.string.title_insect_glaive
        WeaponType.BOW -> R.string.title_bow
        WeaponType.LIGHT_BOWGUN -> R.string.title_light_bowgun
        WeaponType.HEAVY_BOWGUN -> R.string.title_heavy_bowgun
    })

    SimpleUniversalBinding(name, typeString, IconType.ZEMBELLISHED, icon) {
        it.getRouter().navigateWeaponDetail(weapon.id)
    }
}

fun createQuestBinder(quest: QuestBase) = createSimpleUniversalBinder { ctx ->
    val icon = AssetLoader.loadIconFor(quest)
    val name = quest.name
    val typeString = ctx.getString(R.string.type_quest)
    val category = AssetLoader.localizeQuestCategory(quest.category)
    val categoryFull = ctx.getString(R.string.quest_category_combined, category, quest.stars)

    SimpleUniversalBinding(name, typeString, IconType.EMBELLISHED, icon, categoryFull) {
        it.getRouter().navigateObject(quest)
    }
}

fun createKinsectBinder(kinsect: Kinsect) = createSimpleUniversalBinder { ctx ->
    val icon = AssetLoader.loadIconFor(kinsect)
    val name = kinsect.name
    val typeString = ctx.getString(R.string.type_kinsect)
    
    SimpleUniversalBinding(name, typeString, IconType.ZEMBELLISHED, icon) {
        it.getRouter().navigateObject(kinsect)
    }
}
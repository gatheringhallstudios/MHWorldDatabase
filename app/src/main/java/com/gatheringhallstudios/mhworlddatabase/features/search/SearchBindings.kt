package com.gatheringhallstudios.mhworlddatabase.features.search

import com.gatheringhallstudios.mhworlddatabase.R
import com.gatheringhallstudios.mhworlddatabase.adapters.SimpleUniversalBinding
import com.gatheringhallstudios.mhworlddatabase.adapters.createSimpleUniversalBinder
import com.gatheringhallstudios.mhworlddatabase.assets.assetLoader
import com.gatheringhallstudios.mhworlddatabase.data.models.*
import com.gatheringhallstudios.mhworlddatabase.data.types.ItemCategory
import com.gatheringhallstudios.mhworlddatabase.data.types.MonsterSize
import com.gatheringhallstudios.mhworlddatabase.getRouter

fun createLocationBinder(location: Location) = createSimpleUniversalBinder { ctx ->
    val icon = ctx.assetLoader.loadIconFor(location)
    val name = location.name
    val typeString = ctx.getString(R.string.type_location)

    SimpleUniversalBinding(name, typeString, icon) {
        it.getRouter().navigateLocationDetail(location.id)
    }
}

fun createMonsterBinder(monster: MonsterBase) = createSimpleUniversalBinder { ctx ->
    val icon = ctx.assetLoader.loadIconFor(monster)
    val sizeString = ctx.getString(when (monster.size) {
        MonsterSize.SMALL -> R.string.type_monster_small
        MonsterSize.LARGE -> R.string.type_monster_large
    })
    SimpleUniversalBinding(monster.name, sizeString, icon) {
        it.getRouter().navigateMonsterDetail(monster.id)
    }
}

fun createSkillTreeBinder(skillTree: SkillTreeBase) = createSimpleUniversalBinder { ctx ->
    val icon = ctx.assetLoader.loadIconFor(skillTree)
    val typeString = ctx.getString(R.string.type_skilltree)
    SimpleUniversalBinding(skillTree.name, typeString, icon) {
        it.getRouter().navigateSkillDetail(skillTree.id)
    }
}

fun createCharmBinder(charm: Charm) = createSimpleUniversalBinder { ctx ->
    val icon = ctx.assetLoader.loadIconFor(charm)
    val typeString = ctx.getString(R.string.type_charm)
    SimpleUniversalBinding(charm.name, typeString, icon) {
        it.getRouter().navigateCharmDetail(charm.id)
    }
}

fun createDecorationBinder(decoration: DecorationBase) = createSimpleUniversalBinder { ctx ->
    val icon = ctx.assetLoader.loadIconFor(decoration)
    val typeString = ctx.getString(R.string.type_decoration)
    SimpleUniversalBinding(decoration.name, typeString, icon) {
        it.getRouter().navigateDecorationDetail(decoration.id)
    }
}

fun createArmorBinder(armor: ArmorBase) = createSimpleUniversalBinder { ctx ->
    val icon = ctx.assetLoader.loadIconFor(armor)
    val typeString = ctx.getString(R.string.type_armor)
    SimpleUniversalBinding(armor.name, typeString, icon) {
        it.getRouter().navigateArmorDetail(armor.id)
    }
}

fun createItemBinder(item: ItemBase) = createSimpleUniversalBinder { ctx ->
    val icon = ctx.assetLoader.loadIconFor(item)
    val name = item.name
    val typeString = ctx.getString(when (item.category) {
        ItemCategory.ITEM -> R.string.type_item_basic
        ItemCategory.MATERIAL -> R.string.type_item_material
        ItemCategory.AMMO -> R.string.type_item_ammo
        ItemCategory.MISC -> R.string.type_item_misc
        else -> R.string.type_item
    })

    SimpleUniversalBinding(name, typeString, icon) {
        it.getRouter().navigateItemDetail(item.id)
    }
}

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

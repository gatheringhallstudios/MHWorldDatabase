package com.gatheringhallstudios.mhworlddatabase.data.models

import com.gatheringhallstudios.mhworlddatabase.data.types.DataType
import java.util.*

open class Bookmark(
        val dataId: Int,
        val dataType: DataType,
        val dateAdded: Date
)

class BookmarkEntities(
        val locations: List<Location> = emptyList(),
        val monsters: List<MonsterBase> = emptyList(),
        val skillTrees: List<SkillTree> = emptyList(),
        val charms: List<Charm> = emptyList(),
        val decorations: List<DecorationBase> = emptyList(),
        val armor: List<Armor> = emptyList(),
        val items: List<Item> = emptyList(),
        val weapons: List<Weapon> = emptyList()
) {
    fun isEmpty() : Boolean {
        return locations.isNullOrEmpty() &&
                monsters.isNullOrEmpty() &&
                skillTrees.isNullOrEmpty() &&
                charms.isNullOrEmpty() &&
                decorations.isNullOrEmpty() &&
                armor.isNullOrEmpty() &&
                items.isNullOrEmpty() &&
                weapons.isNullOrEmpty()
    }

}

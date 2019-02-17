package com.gatheringhallstudios.mhworlddatabase.data.models

import androidx.room.Embedded
import com.gatheringhallstudios.mhworlddatabase.data.types.DataType


open class SkillTreeBase(
        val id: Int,
        val name: String?,
        val max_level: Int,
        val icon_color: String?
) : MHModel {
    override val entityId get() = id
    override val entityType get() = DataType.SKILL
}

/**
 * Basic representation of a skill tree.
 * This is returned when querying for all data.
 * Created by Carlos on 3/6/2018.
 */
open class SkillTree(
        id: Int,
        name: String?,
        max_level: Int,
        icon_color: String?,
        val description: String?
) : SkillTreeBase(id, name, max_level, icon_color)

/**
 * A skill tree with skill information included.
 */
class SkillTreeFull(
        id: Int,
        name: String?,
        max_level: Int,
        icon_color: String?,
        description: String?,

        val skills: List<Skill>
) : SkillTree(id, name, max_level, icon_color, description)

data class Skill(
        val skilltree_id: Int,
        val level: Int,
        val description: String?
)


/**
 * Represents a skill and an associated level.
 */
open class SkillLevel(
        val level: Int
) {
    // note: Embeddeds must be a var in a superclass or it won't work

    @Embedded(prefix = "skill_") lateinit var skillTree: SkillTreeBase
}

/**
 * Represents an armor and its skill level.
 */
class ArmorSkillLevel(
        @Embedded(prefix = "armor_") val armor: ArmorBase,
        level: Int
): SkillLevel(level)

/**
 * Represents a charm and its skill level.
 */
class CharmSkillLevel(
        @Embedded(prefix = "charm_") val charm: CharmBase,
        level: Int
): SkillLevel(level)

/**
 * Represents a charm and its skill level.
 */
class DecorationSkillLevel(
        @Embedded(prefix = "deco_") val decoration: DecorationBase,
        level: Int
): SkillLevel(level)
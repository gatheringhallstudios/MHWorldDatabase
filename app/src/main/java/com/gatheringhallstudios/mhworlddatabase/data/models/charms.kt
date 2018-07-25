package com.gatheringhallstudios.mhworlddatabase.data.models

import android.arch.persistence.room.Embedded

open class CharmBase(
        val id: Int,
        val name: String?,
        val rarity: Int,
        val previous_id: Int?
)

class Charm(
        id: Int,
        name: String?,
        rarity: Int,
        previous_id: Int?,

        @Embedded(prefix = "component_") val component: CharmComponent,

        val skillLevel: Int,
        val description: String?,
        val skilltree_id: Int,
        val skillName: String,
        val skillIconColor: String?
) : CharmBase(id, name, rarity, previous_id)

class CharmFull(
        @Embedded val data: CharmBase,
        val skills: List<CharmSkill>,
        val components: List<CharmComponent>
)

class CharmSkill(
        @Embedded(prefix = "charm_") val data: CharmBase?,
        @Embedded(prefix = "skill_") val skill: SkillTreeBase?,
        val skillLevel: Int
)

class CharmComponent(
        @Embedded val result: ItemBase,
        val quantity: Int
)


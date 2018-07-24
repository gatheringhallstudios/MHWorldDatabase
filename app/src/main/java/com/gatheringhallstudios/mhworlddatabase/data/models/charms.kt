package com.gatheringhallstudios.mhworlddatabase.data.models

import android.arch.persistence.room.Embedded
import com.gatheringhallstudios.mhworlddatabase.data.entities.CharmEntity

class CharmBase(
        val id: Int,
        val name: String?,
        val rarity: Int
)

class Charm(
        @Embedded val data: CharmEntity,
        @Embedded(prefix = "component_") val component: CharmComponent,
        val name: String?,
        val skillLevel: Int,
        val description: String?,
        val skilltree_id: Int,
        val skillName: String,
        val skillIconColor: String
)

class CharmFull(
        @Embedded val data: CharmEntity,
        val name: String?,
        val skillLevel: Int,
        val components: List<CharmComponent>,
        val description: String?,
        val skilltree_id: Int,
        val skillName: String,
        val skillIconColor: String
)

class CharmSkill(
        @Embedded val data: CharmEntity,
        val name: String?,
        val skillLevel: Int
)

class CharmComponent(
        @Embedded val result: ItemBase,
        val quantity: Int
)


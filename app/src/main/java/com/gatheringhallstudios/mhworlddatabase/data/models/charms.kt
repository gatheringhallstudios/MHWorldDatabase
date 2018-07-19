package com.gatheringhallstudios.mhworlddatabase.data.models

import android.arch.persistence.room.Embedded
import com.gatheringhallstudios.mhworlddatabase.data.entities.CharmEntity

class Charm(
        val id: Int,
        val name: String?,
        val rarity: Int
)

class CharmSkill(
        @Embedded val data: CharmEntity,
        val name: String?,
        val skillLevel: Int
)

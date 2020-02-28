package com.gatheringhallstudios.mhworlddatabase.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "kinsect")
data class KinsectEntity(
        @PrimaryKey var id: Int,
        var rarity: Int,
        var previous_kinsect_id: Int?,
        var recipe_id: Int?,
        var attack_type: String,
        var dust_effect: String,
        var power: Int,
        var speed: Int,
        var heal: Int,
        var final: Boolean
) {
    constructor() : this(0, 0, null, null, "", "", 0,0,0,false)
}

@Entity(tableName = "kinsect_text", primaryKeys = ["id", "lang_id"])
data class KinsectText(
        var id: Int,
        var lang_id: String,
        var name: String?
) {
    constructor() : this(0, "", null)
}
package com.gatheringhallstudios.mhworlddatabase.data.models

import android.arch.persistence.room.Embedded
import com.gatheringhallstudios.mhworlddatabase.data.entities.*
import com.gatheringhallstudios.mhworlddatabase.data.types.DataType
import com.gatheringhallstudios.mhworlddatabase.data.types.WeaponType

/*
todo: refactor. The current version has problems. Details below:
The current system has several problems:
1) Inconsistently nested sub-data that ends up loading a full query
2) Multiple versions of the same "object". Leads to trouble with icon loading.

It will also need a split like how entities are split.
 */

data class Location(
        val id: Int,
        val name: String?
)

data class LocationItem(
        @Embedded val data: LocationItemEntity,
        val item_name: String?
)


data class Decoration(
        @Embedded val data: DecorationEntity,
        val name: String?
) {
    val id get() = data.id
}


/**
 * A view for basic weapon information.
 * TODO: Replace
 */
data class WeaponBasic(
        var id: Int,
        var name: String?,

        var weapon_type: WeaponType?,
        var rarity: Int,
        var attack: Int,

        var slot_1: Int,
        var slot_2: Int,
        var slot_3: Int
)


data class SearchResult(
        val data_type: DataType,
        val id: Int,
        val name: String,
        val icon_name: String?,
        val icon_color: String?
)
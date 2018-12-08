package com.gatheringhallstudios.mhworlddatabase.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.gatheringhallstudios.mhworlddatabase.data.types.ItemCategory
import com.gatheringhallstudios.mhworlddatabase.data.types.ItemSubcategory

/**
 * Created by Carlos on 3/5/2018.
 */
@Entity(tableName = "item")
class ItemEntity(
        @PrimaryKey val id: Int,

        val category: ItemCategory,
        val subcategory: ItemSubcategory,
        val rarity: Int,
        val buy_price: Int?,
        val sell_price: Int,
        val points: Int,
        val carry_limit: Int?,
        val icon_name: String?,
        val icon_color: String?
)

/**
 * Translation data for Item
 * Created by Carlos on 3/5/2018.
 */
@Entity(tableName = "item_text", primaryKeys = ["id", "lang_id"])
data class ItemText(
        val id: Int,
        val lang_id: String,
        val name: String?,
        val description: String?
)

/**
 * Item combination data
 */
@Entity(tableName = "item_combination")
data class ItemCombinationEntity(
        @PrimaryKey val id: Int,
        val result_id: Int,
        val first_id: Int,
        val second_id: Int?,
        val quantity: Int
)

package com.gatheringhallstudios.mhworlddatabase.assets

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.support.v4.content.ContextCompat
import android.util.Log
import com.gatheringhallstudios.mhworlddatabase.R
import com.gatheringhallstudios.mhworlddatabase.data.entities.ArmorEntity
import com.gatheringhallstudios.mhworlddatabase.data.types.ArmorType
import com.gatheringhallstudios.mhworlddatabase.data.views.ArmorComponentView
import com.gatheringhallstudios.mhworlddatabase.data.views.ItemView

/**
 * A class used to load assets for the data objects
 */
class AssetLoader(context: Context) {
    val ctx: Context = context.applicationContext

    fun loadItemIcon(item: ItemView): Drawable? {
        return ctx.getVectorDrawable(item.data.icon_name ?: "", item.data.icon_color)
    }

    fun loadArmorIcon(type: ArmorType, rarity: Int): Drawable? {
        val iconId = VectorArmorRegistry(type)
        val rareColor = "rare${rarity}"
        return ctx.getVectorDrawable(iconId, rareColor)
    }

    fun loadArmorIcon(entity: ArmorEntity): Drawable? {
        return loadArmorIcon(entity.armor_type, entity.rarity)
    }

    fun loadRarityColor(rarity: Int) : Int {
        var color = "rare${rarity}"
        val registryValue = ColorRegistry(color)
        if (registryValue == null) {
            return ctx.resources.getColor(ColorRegistry("rare1")!!)
        }
        else {
            return ctx.resources.getColor(registryValue)
        }
    }
}
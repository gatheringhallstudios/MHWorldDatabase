package com.gatheringhallstudios.mhworlddatabase.assets

import android.content.Context
import android.graphics.drawable.Drawable
import android.support.v4.content.ContextCompat
import com.gatheringhallstudios.mhworlddatabase.data.models.*
import com.gatheringhallstudios.mhworlddatabase.data.types.ArmorType

/**
 * A class used to load assets for the data objects.
 * This class wraps around a context and uses it to load an asset.
 * Accessible via extension functions on fragments and views.
 */
class AssetLoader(context: Context) {
    val ctx: Context = context.applicationContext

    fun loadItemIcon(item: ItemBase): Drawable? {
        return ctx.getVectorDrawable(item.icon_name ?: "", item.icon_color)
    }

    fun loadSkillIcon(color: String?): Drawable? {
        return ctx.getVectorDrawable("Skill", color)
    }

    fun loadArmorIcon(type: ArmorType, rarity: Int): Drawable? {
        val name = when (type) {
            ArmorType.HEAD -> "ArmorArms"
            ArmorType.CHEST -> "ArmorChest"
            ArmorType.ARMS -> "ArmorArms"
            ArmorType.WAIST -> "ArmorWaist"
            ArmorType.LEGS -> "ArmorLegs"
        }

        return ctx.getVectorDrawable(name, "rare$rarity")
    }

    fun loadArmorSetIcon(armorSet: ArmorSet): Drawable? {
        return ctx.getVectorDrawable("ArmorSet", "rare${armorSet.rarity}")
    }

    fun loadArmorIcon(entity: ArmorBase): Drawable? {
        return loadArmorIcon(entity.armor_type, entity.rarity)
    }

    fun loadDecorationIcon(decoration: Decoration): Drawable? {
        val assetName = "Decoration${decoration.data.slot}"
        return ctx.getVectorDrawable(assetName, decoration.data.icon_color)
    }

    fun loadRarityColor(rarity: Int) : Int {
        val colorId = ColorRegistry("rare$rarity") ?: ColorRegistry("rare1")
        return ContextCompat.getColor(ctx, colorId!!)
    }
}
package com.gatheringhallstudios.mhworlddatabase.assets

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.graphics.drawable.Drawable
import android.support.v4.content.ContextCompat
import com.gatheringhallstudios.mhworlddatabase.data.models.*
import com.gatheringhallstudios.mhworlddatabase.data.models.WeaponType
import com.gatheringhallstudios.mhworlddatabase.data.types.*
import com.gatheringhallstudios.mhworlddatabase.data.types.WeaponType as WeaponTypeEnum
import com.gatheringhallstudios.mhworlddatabase.util.getDrawableCompat

// we are storing an application context, so its fine
@SuppressLint("StaticFieldLeak")
/**
 * A class used to load assets for the data objects.
 * This class wraps around a context and uses it to load an asset.
 * Accessible via extension functions on fragments and views.
 */
object AssetLoader {
    lateinit private var ctx: Context

    fun bindApplication(app: Application) {
        this.ctx = app.applicationContext
    }

    fun loadIconFor(item: ItemBase): Drawable? {
        return ctx.getVectorDrawable(item.icon_name ?: "", item.icon_color)
    }

    fun loadIconFor(location: Location): Drawable? {
        return ctx.getDrawableCompat(LocationDrawableRegistry(location.id))
    }

    fun loadIconFor(monster: MonsterBase): Drawable? {
        return ctx.getAssetDrawable("monsters/${monster.id}.png")
    }

    fun loadIconFor(skill: SkillTreeBase): Drawable? {
        return loadSkillIcon(skill.icon_color)
    }

    // loads an attempted potential representation. This will need to be tweaked
    fun loadIconFor(setBonus: ArmorSetBonus): Drawable? {
        return ctx.getVectorDrawable("ArmorSet", "rare1")
    }

    fun loadIconFor(armorSet: ArmorSet): Drawable? {
        return ctx.getVectorDrawable("ArmorSet", "rare${armorSet.rarity}")
    }
    
    fun loadIconFor(entity: ArmorBase): Drawable? {
        return loadArmorIcon(entity.armor_type, entity.rarity)
    }

    fun loadIconFor(charm: CharmBase): Drawable? {
        return ctx.getVectorDrawable("Charm", "rare${charm.rarity}")
    }

    fun loadIconFor(decoration: DecorationBase): Drawable? {
        val assetName = "Decoration${decoration.slot}"
        return ctx.getVectorDrawable(assetName, decoration.icon_color)
    }

//    fun loadIconFor(weaponTree: WeaponTree): Drawable? {
//        val assetName = WeaponTypeEnum.valueOf(weaponTree.weapon_type!!).
//
//    }

    fun loadIconFor(type: WeaponType): Drawable? {
        val name = when (type.weapon_type) {
            WeaponTypeEnum.GREAT_SWORD -> "GreatSword"
            WeaponTypeEnum.LONG_SWORD -> "LongSword"
            WeaponTypeEnum.SWORD_AND_SHIELD -> "SwordAndShield"
            WeaponTypeEnum.DUAL_BLADES -> "DualBlades"
            WeaponTypeEnum.HAMMER -> "Hammer"
            WeaponTypeEnum.HUNTING_HORN -> "HuntingHorn"
            WeaponTypeEnum.LANCE -> "Lance"
            WeaponTypeEnum.GUNLANCE -> "Gunlance"
            WeaponTypeEnum.SWITCH_AXE -> "SwitchAxe"
            WeaponTypeEnum.CHARGE_BLADE -> "ChargeBlade"
            WeaponTypeEnum.INSECT_GLAIVE -> "InsectGlaive"
            WeaponTypeEnum.BOW -> "Bow"
            WeaponTypeEnum.LIGHT_BOWGUN -> "LightBowgun"
            WeaponTypeEnum.HEAVY_BOWGUN -> "HeavyBowgun"
        }

        return ctx.getVectorDrawable(name, null)
    }

    fun loadSkillIcon(color: String?): Drawable? {
        return ctx.getVectorDrawable("Skill", color)
    }

    fun loadArmorIcon(type: ArmorType, rarity: Int): Drawable? {
        val name = when (type) {
            ArmorType.HEAD -> "ArmorHead"
            ArmorType.CHEST -> "ArmorChest"
            ArmorType.ARMS -> "ArmorArms"
            ArmorType.WAIST -> "ArmorWaist"
            ArmorType.LEGS -> "ArmorLegs"
        }

        return ctx.getVectorDrawable(name, "rare$rarity")
    }


    fun loadRarityColor(rarity: Int) : Int {
        val colorId = ColorRegistry("rare$rarity") ?: ColorRegistry("rare1")
        return ContextCompat.getColor(ctx, colorId!!)
    }

}
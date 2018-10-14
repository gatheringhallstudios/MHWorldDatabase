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

    fun loadIconFor(coating_type: CoatingType): Drawable? {
        return when (coating_type) {
            CoatingType.POWER ->  ctx.getVectorDrawable("Bottle", "Red")
            CoatingType.SLEEP -> ctx.getVectorDrawable("Bottle", "Cyan")
            CoatingType.CLOSE_RANGE -> ctx.getVectorDrawable("Bottle", "White")
            CoatingType.POISON -> ctx.getVectorDrawable("Bottle", "DarkPurple")
            CoatingType.BLAST -> ctx.getVectorDrawable("Bottle", "Green")
            CoatingType.PARALYSIS -> ctx.getVectorDrawable("Bottle", "Yellow")
        }
    }

    fun loadIconFor(ammo_type: AmmoType): Drawable? {
        return when (ammo_type) {
            AmmoType.NORMAL_AMMO1, AmmoType.NORMAL_AMMO2, AmmoType.NORMAL_AMMO3, AmmoType.SLICING_AMMO, AmmoType.FREEZE_AMMO ->  ctx.getVectorDrawable("Ammo", "White")
            AmmoType.PIERCE_AMMO1, AmmoType.PIERCE_AMMO2, AmmoType.PIERCE_AMMO3 -> ctx.getVectorDrawable("Ammo", "Blue")
            AmmoType.SPREAD_AMMO1, AmmoType.SPREAD_AMMO2, AmmoType.SPREAD_AMMO3 -> ctx.getVectorDrawable("Ammo", "DarkGreen")
            AmmoType.STICKY_AMMO1, AmmoType.STICKY_AMMO2, AmmoType.STICKY_AMMO3 -> ctx.getVectorDrawable("Ammo", "Beige")
            AmmoType.RECOVER_AMMO1, AmmoType.RECOVER_AMMO2 -> ctx.getVectorDrawable("Ammo", "Green")
            AmmoType.POISON_AMMO1, AmmoType.POISON_AMMO2 -> ctx.getVectorDrawable("Ammo", "LightPurple")
            AmmoType.SLEEP_AMMO1, AmmoType.SLEEP_AMMO2 -> ctx.getVectorDrawable("Ammo", "Cyan")
            AmmoType.EXHAUST_AMMO1, AmmoType.EXHAUST_AMMO2, AmmoType.WATER_AMMO -> ctx.getVectorDrawable("Ammo", "DarkPurple")
            AmmoType.TRANQ_AMMO -> ctx.getVectorDrawable("Ammo", "Pink")
            AmmoType.CLUSTER_AMMO1, AmmoType.CLUSTER_AMMO2, AmmoType.CLUSTER_AMMO3, AmmoType.DRAGON_AMMO -> ctx.getVectorDrawable("Ammo", "DarkRed")
            AmmoType.ARMOR_AMMO -> ctx.getVectorDrawable("Ammo", "Beige")
            AmmoType.PARALYSIS_AMMO1, AmmoType.PARALYSIS_AMMO2 -> ctx.getVectorDrawable("Ammo", "Yellow")
            AmmoType.THUNDER_AMMO -> ctx.getVectorDrawable("Ammo", "Gold")
            AmmoType.WYVERN_AMMO -> ctx.getVectorDrawable("Ammo", "LightBrown")
            AmmoType.DEMON_AMMO ->  ctx.getVectorDrawable("Ammo", "Red")
            AmmoType.FLAMING_AMMO -> ctx.getVectorDrawable("Ammo", "Orange")
        }
    }

    fun loadIconFor(weapon: Weapon): Drawable? {
        val name = when (weapon.weapon_type) {
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
        return ctx.getVectorDrawable(name, "rare${weapon.rarity}")
    }

    fun loadIconFor(weaponTree: WeaponTree): Drawable? {
        val name = when (weaponTree.weapon_type) {
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
        return ctx.getVectorDrawable(name, "rare${weaponTree.rarity}")
    }

    fun loadIconFor(type: WeaponType, color: String? = null): Drawable? {
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

        return ctx.getVectorDrawable(name, color)
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
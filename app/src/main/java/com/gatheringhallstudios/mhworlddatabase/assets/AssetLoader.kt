package com.gatheringhallstudios.mhworlddatabase.assets

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import com.gatheringhallstudios.mhworlddatabase.R
import com.gatheringhallstudios.mhworlddatabase.data.models.*
import com.gatheringhallstudios.mhworlddatabase.data.types.*
import com.gatheringhallstudios.mhworlddatabase.features.weapons.TreeNode
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

    fun loadIconFor(armorSet: ArmorSetBase): Drawable? {
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

    fun loadIconFor(phial: PhialType?): Drawable? {
        return when (PhialRegistry(phial)) {
            0 -> null
            else -> ctx.getDrawableCompat(PhialRegistry(phial))
        }
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
            AmmoType.POISON_AMMO1, AmmoType.POISON_AMMO2 -> ctx.getVectorDrawable("Ammo", "Violet")
            AmmoType.SLEEP_AMMO1, AmmoType.SLEEP_AMMO2 -> ctx.getVectorDrawable("Ammo", "Cyan")
            AmmoType.EXHAUST_AMMO1, AmmoType.EXHAUST_AMMO2, AmmoType.WATER_AMMO -> ctx.getVectorDrawable("Ammo", "DarkPurple")
            AmmoType.TRANQ_AMMO -> ctx.getVectorDrawable("Ammo", "Pink")
            AmmoType.CLUSTER_AMMO1, AmmoType.CLUSTER_AMMO2, AmmoType.CLUSTER_AMMO3, AmmoType.DRAGON_AMMO -> ctx.getVectorDrawable("Ammo", "DarkRed")
            AmmoType.ARMOR_AMMO -> ctx.getVectorDrawable("Ammo", "Beige")
            AmmoType.PARALYSIS_AMMO1, AmmoType.PARALYSIS_AMMO2 -> ctx.getVectorDrawable("Ammo", "Yellow")
            AmmoType.THUNDER_AMMO -> ctx.getVectorDrawable("Ammo", "Gold")
            AmmoType.WYVERN_AMMO -> ctx.getVectorDrawable("Ammo", "LightBeige")
            AmmoType.DEMON_AMMO ->  ctx.getVectorDrawable("Ammo", "Red")
            AmmoType.FLAMING_AMMO -> ctx.getVectorDrawable("Ammo", "Orange")
        }
    }

    fun loadIconFor(weapon: WeaponBase): Drawable? {
        return loadIconFor(weapon.weapon_type, "rare${weapon.rarity}")
    }

    fun loadIconFor(type: WeaponType, color: String? = null): Drawable? {
        val name = when (type) {
            WeaponType.GREAT_SWORD -> "GreatSword"
            WeaponType.LONG_SWORD -> "LongSword"
            WeaponType.SWORD_AND_SHIELD -> "SwordAndShield"
            WeaponType.DUAL_BLADES -> "DualBlades"
            WeaponType.HAMMER -> "Hammer"
            WeaponType.HUNTING_HORN -> "HuntingHorn"
            WeaponType.LANCE -> "Lance"
            WeaponType.GUNLANCE -> "Gunlance"
            WeaponType.SWITCH_AXE -> "SwitchAxe"
            WeaponType.CHARGE_BLADE -> "ChargeBlade"
            WeaponType.INSECT_GLAIVE -> "InsectGlaive"
            WeaponType.BOW -> "Bow"
            WeaponType.LIGHT_BOWGUN -> "LightBowgun"
            WeaponType.HEAVY_BOWGUN -> "HeavyBowgun"
        }

        return ctx.getVectorDrawable(name, color)
    }

    fun loadIconFor(node: TreeNode, rarity: Int): Drawable? {
        val name = when (node) {
            TreeNode.START -> "NodeStart"
            TreeNode.START_COLLAPSED -> "NodeStartCollapsed"
            TreeNode.MID -> "NodeMid"
            TreeNode.MID_COLLAPSED -> "NodeMidCollapsed"
            TreeNode.END -> "NodeEnd"
        }
        return ctx.getVectorDrawable(name, "rare$rarity")
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

    fun loadElementIcon(element: ElementStatus?): Drawable? {
        return ctx.getDrawableCompat(ElementRegistry(element))
    }

    fun loadNoteFromChar(type: Char, noteNumber: Int): Drawable? {
        val vectorName = "HuntingHornNote" + (noteNumber + 1)
        return when (type) {
            'W' -> ctx.getVectorDrawable(vectorName, "White")
            'R' -> ctx.getVectorDrawable(vectorName, "Red")
            'B' -> ctx.getVectorDrawable(vectorName, "Blue")
            'O' -> ctx.getVectorDrawable(vectorName, "Orange")
            'Y' -> ctx.getVectorDrawable(vectorName, "Yellow")
            'P' -> ctx.getVectorDrawable(vectorName, "Violet")
            'G' -> ctx.getVectorDrawable(vectorName, "Green")
            'C' -> ctx.getVectorDrawable(vectorName, "Cyan")
            else -> null
        }
    }

    fun loadRarityColor(rarity: Int) : Int {
        val colorId = ColorRegistry("rare$rarity") ?: ColorRegistry("rare1")
        return ContextCompat.getColor(ctx, colorId!!)
    }

    /**
     * Returns a localized name for the given weapon type
     */
    fun getNameFor(weaponType: WeaponType) = when (weaponType) {
        WeaponType.GREAT_SWORD -> ctx.getString(R.string.title_great_sword)
        WeaponType.LONG_SWORD -> ctx.getString(R.string.title_long_sword)
        WeaponType.SWORD_AND_SHIELD -> ctx.getString(R.string.title_sword_and_shield)
        WeaponType.DUAL_BLADES -> ctx.getString(R.string.title_dual_blades)
        WeaponType.HAMMER -> ctx.getString(R.string.title_hammer)
        WeaponType.HUNTING_HORN -> ctx.getString(R.string.title_hunting_horn)
        WeaponType.LANCE -> ctx.getString(R.string.title_lance)
        WeaponType.GUNLANCE -> ctx.getString(R.string.title_gunlance)
        WeaponType.SWITCH_AXE -> ctx.getString(R.string.title_switch_axe)
        WeaponType.CHARGE_BLADE -> ctx.getString(R.string.title_charge_blade)
        WeaponType.INSECT_GLAIVE -> ctx.getString(R.string.title_insect_glaive)
        WeaponType.LIGHT_BOWGUN -> ctx.getString(R.string.title_light_bowgun)
        WeaponType.HEAVY_BOWGUN -> ctx.getString(R.string.title_heavy_bowgun)
        WeaponType.BOW -> ctx.getString(R.string.title_bow)
    }

    /**
     * Returns the localized element or status name.
     */
    fun localizeElementStatus(elementStatus: ElementStatus?): String = when(elementStatus) {
        null -> ""
        ElementStatus.FIRE -> ctx.getString(R.string.element_fire)
        ElementStatus.WATER -> ctx.getString(R.string.element_water)
        ElementStatus.THUNDER -> ctx.getString(R.string.element_thunder)
        ElementStatus.ICE -> ctx.getString(R.string.element_ice)
        ElementStatus.DRAGON -> ctx.getString(R.string.element_dragon)
        ElementStatus.POISON -> ctx.getString(R.string.status_poison)
        ElementStatus.SLEEP -> ctx.getString(R.string.status_sleep)
        ElementStatus.PARALYSIS -> ctx.getString(R.string.status_paralysis)
        ElementStatus.BLAST -> ctx.getString(R.string.status_blast)
    }

    /**
     * Returns the phial type in the current app language setting
     */
    fun localizePhialType(phialType: PhialType): String = when (phialType) {
        PhialType.NONE -> ""
        PhialType.EXHAUST -> ctx.getString(R.string.weapon_phial_exhaust)
        PhialType.POWER -> ctx.getString(R.string.weapon_phial_power)
        PhialType.POISON -> ctx.getString(R.string.weapon_phial_poison)
        PhialType.DRAGON -> ctx.getString(R.string.weapon_phial_dragon)
        PhialType.POWER_ELEMENT -> ctx.getString(R.string.weapon_phial_power_element)
        PhialType.PARALYSIS -> ctx.getString(R.string.weapon_phial_paralysis)
        PhialType.IMPACT -> ctx.getString(R.string.weapon_phial_impact)
    }

    /**
     * Return the kinsect bonus in the current app language setting.
     */
    fun localizeKinsectBonus(kinsectBonus: KinsectBonus): String = when (kinsectBonus) {
        KinsectBonus.NONE -> ""
        KinsectBonus.SEVER -> ctx.getString(R.string.weapon_kinsect_bonus_sever)
        KinsectBonus.SPEED -> ctx.getString(R.string.weapon_kinsect_bonus_speed)
        KinsectBonus.ELEMENT -> ctx.getString(R.string.weapon_kinsect_bonus_element)
        KinsectBonus.HEALTH -> ctx.getString(R.string.weapon_kinsect_bonus_health)
        KinsectBonus.STAMINA -> ctx.getString(R.string.weapon_kinsect_bonus_stamina)
        KinsectBonus.BLUNT -> ctx.getString(R.string.weapon_kinsect_bonus_blunt)
    }

    /**
     * Returns the shelling type in the current app language setting.
     */
    fun localizeShellingType(shellingType: ShellingType): String = when (shellingType) {
        ShellingType.NONE -> ""
        ShellingType.NORMAL -> ctx.getString(R.string.weapon_gunlance_shelling_normal)
        ShellingType.WIDE -> ctx.getString(R.string.weapon_gunlance_shelling_wide)
        ShellingType.LONG -> ctx.getString(R.string.weapon_gunlance_shelling_long)
    }

    /**
     * Localizes the special ammo type using the current app language settings.
     */
    fun localizeSpecialAmmoType(type: SpecialAmmoType?): String = when (type) {
        null -> ""
        SpecialAmmoType.WYVERNBLAST -> ctx.getString(R.string.weapon_bowgun_special_ammo_wyvernblast)
        SpecialAmmoType.WYVERNHEART -> ctx.getString(R.string.weapon_bowgun_special_ammo_wyvernheart)
        SpecialAmmoType.WYVERNSNIPE -> ctx.getString(R.string.weapon_bowgun_special_ammo_wyvernsnipe)
    }
}
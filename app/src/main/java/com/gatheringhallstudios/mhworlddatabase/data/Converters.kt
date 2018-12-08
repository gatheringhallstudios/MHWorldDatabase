package com.gatheringhallstudios.mhworlddatabase.data

import androidx.room.TypeConverter
import com.gatheringhallstudios.mhworlddatabase.data.types.*
import com.gatheringhallstudios.mhworlddatabase.util.Converter

private val RankConverter = Converter(
        "LR" to Rank.LOW,
        "HR" to Rank.HIGH,
        null to null
)

private val MonsterSizeConverter = Converter(
        "small" to MonsterSize.SMALL,
        "large" to MonsterSize.LARGE,
        null to null
)

private val AilmentStrengthConverter = Converter(
        null to AilmentStrength.NONE,
        "" to AilmentStrength.NONE,
        "small" to AilmentStrength.SMALL,
        "large" to AilmentStrength.LARGE,
        "extreme" to AilmentStrength.EXTREME
)

private val ExtractConverter = Converter(
        "red" to Extract.RED,
        "orange" to Extract.ORANGE,
        "white" to Extract.WHITE,
        "green" to Extract.GREEN,
        null to null
)

private val ItemCategoryConverter = Converter(
        "item" to ItemCategory.ITEM,
        "material" to ItemCategory.MATERIAL,
        "misc" to ItemCategory.MISC,
        "ammo" to ItemCategory.AMMO,
        "hidden" to ItemCategory.HIDDEN,
        null to null
)

private val ItemSubcategoryConverter = Converter(
        null to ItemSubcategory.NONE,
        "appraisal" to ItemSubcategory.APPRAISAL,
        "account" to ItemSubcategory.ACCOUNT,
        "supply" to ItemSubcategory.SUPPLY,
        "trade" to ItemSubcategory.TRADE
)

private val ArmorTypeConverter = Converter(
        "head" to ArmorType.HEAD,
        "chest" to ArmorType.CHEST,
        "arms" to ArmorType.ARMS,
        "waist" to ArmorType.WAIST,
        "legs" to ArmorType.LEGS,
        null to null
)

private val ElderSealLevelConverter = Converter(
        null to ElderSealLevel.NONE,
        "low" to ElderSealLevel.LOW,
        "average" to ElderSealLevel.AVERAGE,
        "high" to ElderSealLevel.HIGH
)

private val WeaponTypeConverter = Converter(
        "great-sword" to WeaponType.GREAT_SWORD,
        "long-sword" to WeaponType.LONG_SWORD,
        "sword-and-shield" to WeaponType.SWORD_AND_SHIELD,
        "dual-blades" to WeaponType.DUAL_BLADES,
        "hammer" to WeaponType.HAMMER,
        "hunting-horn" to WeaponType.HUNTING_HORN,
        "lance" to WeaponType.LANCE,
        "gunlance" to WeaponType.GUNLANCE,
        "switch-axe" to WeaponType.SWITCH_AXE,
        "charge-blade" to WeaponType.CHARGE_BLADE,
        "insect-glaive" to WeaponType.INSECT_GLAIVE ,
        "bow" to WeaponType.BOW,
        "light-bowgun" to WeaponType.LIGHT_BOWGUN,
        "heavy-bowgun" to WeaponType.HEAVY_BOWGUN,
        null to null
)

private val PhialTypeConverter = Converter(
        "impact" to PhialType.IMPACT,
        "power element" to PhialType.POWER_ELEMENT,
        "power" to PhialType.POWER,
        "poison" to PhialType.POISON,
        "paralysis" to PhialType.PARALYSIS,
        "dragon" to PhialType.DRAGON,
        "exhaust" to PhialType.EXHAUST,
        null to PhialType.NONE
)

private val KinsectBonusTypeConverter = Converter(
        "sever" to KinsectBonus.SEVER,
        "speed" to KinsectBonus.SPEED,
        "element" to KinsectBonus.ELEMENT,
        "health" to KinsectBonus.HEALTH,
        "stamina" to KinsectBonus.STAMINA,
        "blunt" to KinsectBonus.BLUNT,
        null to KinsectBonus.NONE
)

private val ShellingTypeConverter = Converter(
        null to ShellingType.NONE,
        "long" to ShellingType.LONG,
        "normal" to ShellingType.NORMAL,
        "wide" to ShellingType.WIDE
)

private val ReloadTypeConverter = Converter(
        null to ReloadType.NONE,
        "very slow" to ReloadType.VERY_SLOW,
        "slow" to ReloadType.SLOW,
        "normal" to ReloadType.NORMAL,
        "fast" to ReloadType.FAST,
        "very fast" to ReloadType.VERY_FAST
)



/**
 * Type conversions for things like enumerations.
 * Change this to add new enum values
 * These are registered to the database class via an annotation.
 * Created by Carlos on 3/6/2018.
 */

class Converters {
    @TypeConverter fun rankFromString(value: String?) = RankConverter.deserialize(value)
    @TypeConverter fun fromRank(type: Rank?) = RankConverter.serialize(type)

    @TypeConverter fun monsterSizefromString(value: String) = MonsterSizeConverter.deserialize(value)
    @TypeConverter fun fromMonsterSize(type: MonsterSize?) = MonsterSizeConverter.serialize(type)

    @TypeConverter fun ailmentFromString(value: String?) = AilmentStrengthConverter.deserialize(value)
    @TypeConverter fun fromAilment(ailment: AilmentStrength) = AilmentStrengthConverter.serialize(ailment)

    @TypeConverter fun extractFromString(value: String) = ExtractConverter.deserialize(value)
    @TypeConverter fun fromExtract(type: Extract?) = ExtractConverter.serialize(type)

    @TypeConverter fun itemCategoryFromString(value: String) = ItemCategoryConverter.deserialize(value)
    @TypeConverter fun fromItemCategory(category: ItemCategory?) = ItemCategoryConverter.serialize(category)

    @TypeConverter fun itemSubcategoryFromString(value: String?) = ItemSubcategoryConverter.deserialize(value)
    @TypeConverter fun fromItemSubcategory(subcategory: ItemSubcategory?) = ItemSubcategoryConverter.serialize(subcategory ?: ItemSubcategory.NONE)

    @TypeConverter fun armorTypefromString(value: String) = ArmorTypeConverter.deserialize(value)
    @TypeConverter fun fromArmorType(type: ArmorType?) = ArmorTypeConverter.serialize(type)

    @TypeConverter fun weaponTypeFromString(value: String) = WeaponTypeConverter.deserialize(value)
    @TypeConverter fun fromWeaponType(type: WeaponType?) = WeaponTypeConverter.serialize(type)

    @TypeConverter fun elderSealFromString(value: String?) = ElderSealLevelConverter.deserialize(value)
    @TypeConverter fun fromElderSealLevel(type: ElderSealLevel) = ElderSealLevelConverter.serialize(type)

    @TypeConverter fun phialTypeFromString(value: String?) = PhialTypeConverter.deserialize(value)
    @TypeConverter fun fromPhialType(type: PhialType) = PhialTypeConverter.serialize(type)

    @TypeConverter fun kinsectBonusFromString(value: String?) = KinsectBonusTypeConverter.deserialize(value)
    @TypeConverter fun fromKinsectBonus(type: KinsectBonus) = KinsectBonusTypeConverter.serialize(type)

    @TypeConverter fun shellingTypeFromString(value: String?) = ShellingTypeConverter.deserialize(value)
    @TypeConverter fun fromShellingType(type: ShellingType) = ShellingTypeConverter.serialize(type)

    @TypeConverter fun reloadTypeFromString(value: String?) = ReloadTypeConverter.deserialize(value)
    @TypeConverter fun fromReloadType(type: ReloadType) = ReloadTypeConverter.serialize(type)
}

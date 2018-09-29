package com.gatheringhallstudios.mhworlddatabase.data

import android.arch.persistence.room.TypeConverter
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
}

package com.gatheringhallstudios.mhworlddatabase.data

import androidx.room.TypeConverter
import com.gatheringhallstudios.mhworlddatabase.data.types.*

import com.google.common.collect.EnumHashBiMap

// internal helper to create a lookup map
inline fun <reified T : Enum<T>> createLookupMap(): EnumHashBiMap<T, String> {
    return EnumHashBiMap.create<T, String>(T::class.java)
}

// internal helper to get an enum from a lookup map, and throw an error on failure
fun <T : Enum<T>> EnumHashBiMap<T, String>.toEnum(value : String) : T {
    try {
        return this.inverse()[value]!!
    } catch (ex: NullPointerException) {
        throw IllegalArgumentException("Unknown value $value")
    }
}

// internal helper to get a string from the lookup map, and throw an error on failure
fun <T: Enum<T>> EnumHashBiMap<T, String>.toString(value : T?): String? {
    if (value == null) {
        return null
    }
    return this[value] ?:
        throw NoSuchFieldException("Value $value was not registered in the converter")
}

/**
 * Type conversions for things like enumerations.
 * Change this to add new enum values
 * These are registered to the database class via an annotation.
 * Created by Carlos on 3/6/2018.
 */

class Converters {
    companion object {
        private var rankMap = createLookupMap<Rank>()
        private var itemCategoryMap = createLookupMap<ItemCategory>()
        private var monsterSizeMap = createLookupMap<MonsterSize>()
        private var extractMap = createLookupMap<Extract>()
        private var armorMap = createLookupMap<ArmorType>()
        private var weaponMap = createLookupMap<WeaponType>()

        init {
            rankMap[Rank.LOW] = "LR"
            rankMap[Rank.HIGH] = "HR"

            monsterSizeMap[MonsterSize.SMALL] = "small"
            monsterSizeMap[MonsterSize.LARGE] = "large"

            extractMap[Extract.RED] = "red"
            extractMap[Extract.ORANGE] = "orange"
            extractMap[Extract.WHITE] = "white"
            extractMap[Extract.GREEN] = "green"

            itemCategoryMap[ItemCategory.ITEM] = "item"
            itemCategoryMap[ItemCategory.MATERIAL] = "material"
            itemCategoryMap[ItemCategory.MISC] = "misc"
            itemCategoryMap[ItemCategory.AMMO] = "ammo"
            itemCategoryMap[ItemCategory.HIDDEN] = "hidden"

            armorMap[ArmorType.HEAD] = "head"
            armorMap[ArmorType.CHEST] = "chest"
            armorMap[ArmorType.ARMS] = "arms"
            armorMap[ArmorType.WAIST] = "waist"
            armorMap[ArmorType.LEGS] = "legs"

            weaponMap[WeaponType.GREAT_SWORD] = "great-sword"
            weaponMap[WeaponType.LONG_SWORD] = "long-sword"
            weaponMap[WeaponType.SWORD_AND_SHIELD] = "sword-and-shield"
            weaponMap[WeaponType.DUAL_BLADES] = "dual-blades"
            weaponMap[WeaponType.HAMMER] = "hammer"
            weaponMap[WeaponType.HUNTING_HORN] = "hunting-horn"
            weaponMap[WeaponType.LANCE] = "lance"
            weaponMap[WeaponType.GUNLANCE] = "gunlance"
            weaponMap[WeaponType.SWITCH_AXE] = "switch-axe"
            weaponMap[WeaponType.CHARGE_BLADE] = "charge-blade"
            weaponMap[WeaponType.INSECT_GLAIVE] = "insect-glaive"
            weaponMap[WeaponType.BOW] = "bow"
            weaponMap[WeaponType.LIGHT_BOWGUN] = "light-bowgun"
            weaponMap[WeaponType.HEAVY_BOWGUN] = "heavy-bowgun"
        }
    }

    @TypeConverter fun rankFromString(value: String) = rankMap.toEnum(value)
    @TypeConverter fun fromRank(type: Rank?) = rankMap.toString(type)

    @TypeConverter fun itemCategoryFromString(value: String) = itemCategoryMap.toEnum(value)
    @TypeConverter fun fromItemCategory(category: ItemCategory?) = itemCategoryMap.toString(category)

    @TypeConverter fun monsterSizefromString(value: String) = monsterSizeMap.toEnum(value)
    @TypeConverter fun fromMonsterSize(type: MonsterSize?) = monsterSizeMap.toString(type)

    @TypeConverter fun extractFromString(value: String) = extractMap.toEnum(value)
    @TypeConverter fun fromExtract(type: Extract?) = extractMap.toString(type)

    @TypeConverter fun armorTypefromString(value: String) = armorMap.toEnum(value)
    @TypeConverter fun fromArmorType(type: ArmorType?) = armorMap.toString(type)

    @TypeConverter fun weaponTypeFromString(value: String) = weaponMap.toEnum(value)
    @TypeConverter fun fromWeaponType(type: WeaponType?) = weaponMap.toString(type)
}

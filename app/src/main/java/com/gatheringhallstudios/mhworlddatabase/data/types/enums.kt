package com.gatheringhallstudios.mhworlddatabase.data.types

/**
 * This file contains a collection of enumerations used in the MHWorld app.
 * These enums are automatically converted via "Converters.kt".
 * These can be used in queries without having to convert them.
 *
 * KEEP THEM SIMPLE
 * If these enums become complex, they are allegedly no longer optimized by Proguard.
 * TODO: Verify the above claim
 */

/**
 * Represents an object's type.
 */
enum class DataType {
    LOCATION,
    ITEM,
    MONSTER,
    SKILL,
    DECORATION,
    CHARM,
    ARMOR,
    WEAPON,
    QUEST,
    NONE
}

/**
 * Denotes a quest rank. Mapped by the Converters class.
 */
enum class Rank {
    LOW,
    HIGH,
    MASTER
}


enum class ItemCategory {
    ITEM,
    MATERIAL,
    AMMO,
    MISC,

    /** Cannot be queried in the item list, only directly. **/
    HIDDEN
}

enum class ItemSubcategory {
    NONE,
    APPRAISAL,
    ACCOUNT,
    SUPPLY,
    TRADE
}

/**
 * An enumeration used to represent a monster's size.
 */
enum class MonsterSize {
    SMALL,
    LARGE
}

/**
 * Represents the strength of a monster ailment.
 * Those in the extreme category usually require special measures.
 * Kushala's wind pressure is an example of an extreme ailment.
 */
enum class AilmentStrength {
    NONE,
    SMALL,
    LARGE,
    EXTREME
}

enum class Extract {
    RED,
    ORANGE,
    WHITE,
    GREEN
}

/**
 * An enumeration that defines where a piece of armor is worn.
 */
enum class ArmorType {
    HEAD,
    CHEST,
    ARMS,
    WAIST,
    LEGS
}

/**
 * Represents an element or a status.
 * Tied to the same object as a weapon may be either/or.
 */
enum class ElementStatus {
    FIRE,
    WATER,
    THUNDER,
    ICE,
    DRAGON,
    POISON,
    SLEEP,
    PARALYSIS,
    BLAST
}

/**
 * An enumeration that defines the type of weapon.
 */
enum class WeaponType {
    GREAT_SWORD,
    LONG_SWORD,
    SWORD_AND_SHIELD,
    DUAL_BLADES,
    HAMMER,
    HUNTING_HORN,
    LANCE,
    GUNLANCE,
    SWITCH_AXE,
    CHARGE_BLADE,
    INSECT_GLAIVE,
    BOW,
    LIGHT_BOWGUN,
    HEAVY_BOWGUN
}

/**
 * Defines the categorization of the weapon.
 * Use Regular for crafted weapons.
 */
enum class WeaponCategory {
    REGULAR,
    KULVE
}

enum class ElderSealLevel {
    NONE,
    LOW,
    AVERAGE,
    HIGH
}

enum class CoatingType {
    POWER,
    POISON,
    CLOSE_RANGE,
    SLEEP,
    BLAST,
    PARALYSIS
}

enum class PhialType {
    NONE,
    EXHAUST,
    POWER,
    DRAGON,
    POWER_ELEMENT,
    POISON,
    PARALYSIS,
    IMPACT
}

enum class KinsectBonus {
    NONE,
    SEVER,
    SPEED,
    ELEMENT,
    HEALTH,
    STAMINA,
    BLUNT,
    STAMINA_HEALTH,
    SPIRIT_STRENGTH,
}

enum class ShellingType {
    NONE,
    WIDE,
    LONG,
    NORMAL
}

enum class SpecialAmmoType {
    WYVERNBLAST,
    WYVERNHEART,
    WYVERNSNIPE
}

enum class AmmoType {
    NORMAL_AMMO1,
    NORMAL_AMMO2,
    NORMAL_AMMO3,
    PIERCE_AMMO1,
    PIERCE_AMMO2,
    PIERCE_AMMO3,
    SPREAD_AMMO1,
    SPREAD_AMMO2,
    SPREAD_AMMO3,
    STICKY_AMMO1,
    STICKY_AMMO2,
    STICKY_AMMO3,
    CLUSTER_AMMO1,
    CLUSTER_AMMO2,
    CLUSTER_AMMO3,
    RECOVER_AMMO1,
    RECOVER_AMMO2,
    POISON_AMMO1,
    POISON_AMMO2,
    PARALYSIS_AMMO1,
    PARALYSIS_AMMO2,
    SLEEP_AMMO1,
    SLEEP_AMMO2,
    EXHAUST_AMMO1,
    EXHAUST_AMMO2,
    FLAMING_AMMO,
    WATER_AMMO,
    FREEZE_AMMO,
    THUNDER_AMMO,
    DRAGON_AMMO,
    SLICING_AMMO,
    WYVERN_AMMO,
    DEMON_AMMO,
    ARMOR_AMMO,
    TRANQ_AMMO
}

enum class ReloadType {
    NONE,
    VERY_SLOW,
    SLOW,
    NORMAL,
    FAST,
    VERY_FAST
}

enum class QuestCategory {
    ASSIGNED,
    OPTIONAL,
    EVENT,
    ARENA,
    SPECIAL
}

enum class QuestType {
    HUNT,
    SLAY,
    ASSIGNMENT,
    DELIVER,
    CAPTURE
}
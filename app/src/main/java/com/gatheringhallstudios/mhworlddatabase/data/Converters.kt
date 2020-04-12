package com.gatheringhallstudios.mhworlddatabase.data

import androidx.room.TypeConverter
import com.gatheringhallstudios.mhworlddatabase.data.types.*

private val RankConverter = Converter(
        "LR" to Rank.LOW,
        "HR" to Rank.HIGH,
        "MR" to Rank.MASTER,
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

private val ElementStatusConverter = Converter(
        null to null,
        "Fire" to ElementStatus.FIRE,
        "Water" to ElementStatus.WATER,
        "Thunder" to ElementStatus.THUNDER,
        "Ice" to ElementStatus.ICE,
        "Dragon" to ElementStatus.DRAGON,
        "Poison" to ElementStatus.POISON,
        "Sleep" to ElementStatus.SLEEP,
        "Paralysis" to ElementStatus.PARALYSIS,
        "Blast" to ElementStatus.BLAST
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
        "insect-glaive" to WeaponType.INSECT_GLAIVE,
        "bow" to WeaponType.BOW,
        "light-bowgun" to WeaponType.LIGHT_BOWGUN,
        "heavy-bowgun" to WeaponType.HEAVY_BOWGUN,
        null to null
)

private val WeaponCategoryConverter = Converter(
        null to WeaponCategory.REGULAR,
        "Kulve" to WeaponCategory.KULVE
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
        "spirit_strength" to KinsectBonus.SPIRIT_STRENGTH,
        "stamina_health" to KinsectBonus.STAMINA_HEALTH,
        null to KinsectBonus.NONE
)

private val ShellingTypeConverter = Converter(
        null to ShellingType.NONE,
        "long" to ShellingType.LONG,
        "normal" to ShellingType.NORMAL,
        "wide" to ShellingType.WIDE
)

private val SpecialAmmoTypeConverter = Converter(
        null to null,
        "Wyvernblast" to SpecialAmmoType.WYVERNBLAST,
        "Wyvernheart" to SpecialAmmoType.WYVERNHEART,
        "Wyvernsnipe" to SpecialAmmoType.WYVERNSNIPE
)

private val ReloadTypeConverter = Converter(
        null to ReloadType.NONE,
        "very slow" to ReloadType.VERY_SLOW,
        "slow" to ReloadType.SLOW,
        "normal" to ReloadType.NORMAL,
        "fast" to ReloadType.FAST,
        "very fast" to ReloadType.VERY_FAST
)

private val QuestCategoryConverter = Converter(
        "optional" to QuestCategory.OPTIONAL,
        "assigned" to QuestCategory.ASSIGNED,
        "arena" to QuestCategory.ARENA,
        "event" to QuestCategory.EVENT,
        "special" to QuestCategory.SPECIAL,
        null to QuestCategory.OPTIONAL
)

private val QuestTypeConverter = Converter(
        "hunt" to QuestType.HUNT,
        "deliver" to QuestType.DELIVER,
        "capture" to QuestType.CAPTURE,
        null to QuestType.HUNT
)

private val KinsectAttackTypeConverter = Converter(
        "Sever" to KinsectAttackType.SEVER,
        "Blunt" to KinsectAttackType.BLUNT,
        null to null
)

private val KinsectDustEffectConverter = Converter(
        "Poison" to KinsectDustEffect.POISON,
        "Paralysis" to KinsectDustEffect.PARALYSIS,
        "Heal" to KinsectDustEffect.HEAL,
        "Blast" to KinsectDustEffect.BLAST,
        null to null
)

private val ToolTypeConverter = Converter(
        "mantle" to ToolType.MANTLE,
        "booster" to ToolType.BOOSTER,
        null to null
)

/**
 * Type conversions for things like enumerations.
 * Change this to add new enum values
 * These are registered to the database class via an annotation.
 * Created by Carlos on 3/6/2018.
 */

class Converters {
    @TypeConverter
    fun rankFromString(value: String?) = RankConverter.deserialize(value)

    @TypeConverter
    fun fromRank(type: Rank?) = RankConverter.serialize(type)

    @TypeConverter
    fun monsterSizefromString(value: String) = MonsterSizeConverter.deserialize(value)

    @TypeConverter
    fun fromMonsterSize(type: MonsterSize?) = MonsterSizeConverter.serialize(type)

    @TypeConverter
    fun ailmentFromString(value: String?) = AilmentStrengthConverter.deserialize(value)

    @TypeConverter
    fun fromAilment(ailment: AilmentStrength) = AilmentStrengthConverter.serialize(ailment)

    @TypeConverter
    fun extractFromString(value: String) = ExtractConverter.deserialize(value)

    @TypeConverter
    fun fromExtract(type: Extract?) = ExtractConverter.serialize(type)

    @TypeConverter
    fun itemCategoryFromString(value: String) = ItemCategoryConverter.deserialize(value)

    @TypeConverter
    fun fromItemCategory(category: ItemCategory?) = ItemCategoryConverter.serialize(category)

    @TypeConverter
    fun itemSubcategoryFromString(value: String?) = ItemSubcategoryConverter.deserialize(value)

    @TypeConverter
    fun fromItemSubcategory(subcategory: ItemSubcategory?) = ItemSubcategoryConverter.serialize(subcategory
            ?: ItemSubcategory.NONE)

    @TypeConverter
    fun armorTypefromString(value: String) = ArmorTypeConverter.deserialize(value)

    @TypeConverter
    fun fromArmorType(type: ArmorType?) = ArmorTypeConverter.serialize(type)

    @TypeConverter
    fun elementStatusFromString(value: String?) = ElementStatusConverter.deserialize(value)

    @TypeConverter
    fun fromElementStatus(value: ElementStatus?) = ElementStatusConverter.serialize(value)

    @TypeConverter
    fun weaponTypeFromString(value: String) = WeaponTypeConverter.deserialize(value)

    @TypeConverter
    fun fromWeaponType(type: WeaponType?) = WeaponTypeConverter.serialize(type)

    @TypeConverter
    fun weaponCategoryFromString(value: String?) = WeaponCategoryConverter.deserialize(value)

    @TypeConverter
    fun fromWeaponCategory(category: WeaponCategory?) = WeaponCategoryConverter.serialize(category
            ?: WeaponCategory.REGULAR)

    @TypeConverter
    fun elderSealFromString(value: String?) = ElderSealLevelConverter.deserialize(value)

    @TypeConverter
    fun fromElderSealLevel(type: ElderSealLevel) = ElderSealLevelConverter.serialize(type)

    @TypeConverter
    fun phialTypeFromString(value: String?) = PhialTypeConverter.deserialize(value)

    @TypeConverter
    fun fromPhialType(type: PhialType) = PhialTypeConverter.serialize(type)

    @TypeConverter
    fun kinsectBonusFromString(value: String?) = KinsectBonusTypeConverter.deserialize(value)

    @TypeConverter
    fun fromKinsectBonus(type: KinsectBonus) = KinsectBonusTypeConverter.serialize(type)

    @TypeConverter
    fun shellingTypeFromString(value: String?) = ShellingTypeConverter.deserialize(value)

    @TypeConverter
    fun fromShellingType(type: ShellingType) = ShellingTypeConverter.serialize(type)

    @TypeConverter
    fun specialAmmoTypeFromString(value: String?) = SpecialAmmoTypeConverter.deserialize(value)

    @TypeConverter
    fun fromSpecialAmmoType(type: SpecialAmmoType?) = SpecialAmmoTypeConverter.serialize(type)

    @TypeConverter
    fun reloadTypeFromString(value: String?) = ReloadTypeConverter.deserialize(value)

    @TypeConverter
    fun fromReloadType(type: ReloadType) = ReloadTypeConverter.serialize(type)

    @TypeConverter
    fun questCategoryFromString(value: String?) = QuestCategoryConverter.deserialize(value)

    @TypeConverter
    fun fromQuestCategory(category: QuestCategory) = QuestCategoryConverter.serialize(category)

    @TypeConverter
    fun questTypeFromString(value: String?) = QuestTypeConverter.deserialize(value)

    @TypeConverter
    fun fromQuestType(type: QuestType) = QuestTypeConverter.serialize(type)

    @TypeConverter
    fun kinsectAttackTypeFromString(value: String?) = KinsectAttackTypeConverter.deserialize(value)

    @TypeConverter
    fun fromKinsectAttackType(type: KinsectAttackType) = KinsectAttackTypeConverter.serialize(type)

    @TypeConverter
    fun kinsectDustEffectFromString(value: String?) = KinsectDustEffectConverter.deserialize(value)

    @TypeConverter
    fun fromKinsectDustEffect(type: KinsectDustEffect) = KinsectDustEffectConverter.serialize(type)

    @TypeConverter
    fun toolTypeFromString(value: String?) = ToolTypeConverter.deserialize(value)

    @TypeConverter
    fun fromToolType(type: ToolType) = ToolTypeConverter.serialize(type)
}

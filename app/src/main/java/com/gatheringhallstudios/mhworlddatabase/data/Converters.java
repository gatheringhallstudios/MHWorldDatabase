package com.gatheringhallstudios.mhworlddatabase.data;

import android.arch.persistence.room.TypeConverter;

import com.gatheringhallstudios.mhworlddatabase.data.types.ArmorType;
import com.gatheringhallstudios.mhworlddatabase.data.types.ItemCategory;
import com.gatheringhallstudios.mhworlddatabase.data.types.MonsterSize;
import com.gatheringhallstudios.mhworlddatabase.data.types.Rank;
import com.gatheringhallstudios.mhworlddatabase.data.types.WeaponType;
import com.google.common.collect.EnumHashBiMap;

/**
 * Type conversions for things like enumerations.
 * These are registered to the database class via an annotation.
 * Created by Carlos on 3/6/2018.
 */

public class Converters {
    private static EnumHashBiMap<Rank, String> rankMap;
    private static EnumHashBiMap<ItemCategory, String> categoryMap;
    private static EnumHashBiMap<MonsterSize, String> monsterSizeMap;
    private static EnumHashBiMap<ArmorType, String> armorMap;
    private static EnumHashBiMap<WeaponType, String> weaponMap;

    static {
        rankMap = EnumHashBiMap.create(Rank.class);
        categoryMap = EnumHashBiMap.create(ItemCategory.class);
        monsterSizeMap = EnumHashBiMap.create(MonsterSize.class);
        armorMap = EnumHashBiMap.create(ArmorType.class);
        weaponMap = EnumHashBiMap.create(WeaponType.class);

        rankMap.put(Rank.LOW, "LR");
        rankMap.put(Rank.HIGH, "HR");

        monsterSizeMap.put(MonsterSize.SMALL, "small");
        monsterSizeMap.put(MonsterSize.LARGE, "large");

        categoryMap.put(ItemCategory.ITEM, "items"); // note: will change to singular in future update
        categoryMap.put(ItemCategory.MATERIAL, "material");
        categoryMap.put(ItemCategory.ACCOUNT, "account");
        categoryMap.put(ItemCategory.AMMO, "ammo");

        armorMap.put(ArmorType.HEAD, "head");
        armorMap.put(ArmorType.CHEST, "chest");
        armorMap.put(ArmorType.ARMS, "arms");
        armorMap.put(ArmorType.WAIST, "waist");
        armorMap.put(ArmorType.LEGS, "legs");

        weaponMap.put(WeaponType.GREAT_SWORD, "great-sword");
        weaponMap.put(WeaponType.LONG_SWORD, "long-sword");
        weaponMap.put(WeaponType.SWORD_AND_SHIELD, "sword-and-shield");
        weaponMap.put(WeaponType.DUAL_BLADES, "dual-blades");
        weaponMap.put(WeaponType.HAMMER, "hammer");
        weaponMap.put(WeaponType.HUNTING_HORN, "hunting-horn");
        weaponMap.put(WeaponType.LANCE, "lance");
        weaponMap.put(WeaponType.GUNLANCE, "gunlance");
        weaponMap.put(WeaponType.SWITCH_AXE, "switch-axe");
        weaponMap.put(WeaponType.CHARGE_BLADE, "charge-blade");
        weaponMap.put(WeaponType.INSECT_GLAIVE, "insect-glaive");
        weaponMap.put(WeaponType.BOW, "bow");
        weaponMap.put(WeaponType.LIGHT_BOWGUN, "light-bowgun");
        weaponMap.put(WeaponType.HEAVY_BOWGUN, "heavy-bowgun");
    }

    @TypeConverter
    public Rank rankFromString(String value) {
        try {
            return rankMap.inverse().get(value);
        } catch (NullPointerException ex) {
            throw new IllegalArgumentException("Unknown monster size " + value);
        }
    }

    @TypeConverter
    public String fromRank(Rank type) {
        return rankMap.get(type);
    }

    @TypeConverter
    public ItemCategory categoryFromString(String value) {
        try {
            return categoryMap.inverse().get(value);
        } catch (NullPointerException ex) {
            throw new IllegalArgumentException("Unknown category " + value);
        }
    }

    @TypeConverter
    public String fromCategory(ItemCategory category) {
        return categoryMap.get(category);
    }

    @TypeConverter
    public MonsterSize monsterSizefromString(String value) {
        try {
            return monsterSizeMap.inverse().get(value);
        } catch (NullPointerException ex) {
            throw new IllegalArgumentException("Unknown monster size " + value);
        }
    }

    @TypeConverter
    public String fromMonsterSize(MonsterSize type) {
        return monsterSizeMap.get(type);
    }

    @TypeConverter
    public ArmorType armorTypefromString(String value) {
        try {
            return armorMap.inverse().get(value);
        } catch (NullPointerException ex) {
            throw new IllegalArgumentException("Unknown armor type " + value);
        }
    }

    @TypeConverter
    public String fromArmorType(ArmorType type) {
        return armorMap.get(type);
    }

    @TypeConverter
    public WeaponType weaponTypeFromString(String value) {
        try {
            return weaponMap.inverse().get(value);
        } catch (NullPointerException ex) {
            throw new IllegalArgumentException("Unknown weapon type " + value);
        }
    }

    @TypeConverter
    public String fromWeaponType(WeaponType type) {
        return weaponMap.get(type);
    }
}

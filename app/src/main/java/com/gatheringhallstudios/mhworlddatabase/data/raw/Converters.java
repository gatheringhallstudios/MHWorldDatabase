package com.gatheringhallstudios.mhworlddatabase.data.raw;

import android.arch.persistence.room.TypeConverter;

import com.gatheringhallstudios.mhworlddatabase.data.ArmorType;

/**
 * Type conversions for things like enumerations
 * Created by Carlos on 3/6/2018.
 */

public class Converters {
    @TypeConverter
    public ArmorType fromString(String value) {
        switch (value) {
            case "head": return ArmorType.HEAD;
            case "chest": return ArmorType.CHEST;
            case "arms": return ArmorType.ARMS;
            case "waist": return ArmorType.WAIST;
            case "legs": return ArmorType.LEGS;
        }
        throw new IllegalArgumentException("Unknown enum value " + value);
    }

//    @TypeConverter
//    public String stringFromArmorType(ArmorType type) {
//        switch (ArmorType) {
//            case ArmorType.HEAD: return "head";
//            case Armor
//        }
//    }
}

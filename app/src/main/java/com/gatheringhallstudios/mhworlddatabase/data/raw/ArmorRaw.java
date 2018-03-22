package com.gatheringhallstudios.mhworlddatabase.data.raw;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import com.gatheringhallstudios.mhworlddatabase.data.ArmorType;

/**
 * Raw entity for Armor
 * Created by Carlos on 3/5/2018.
 */
@Entity(tableName = "armor")
public class ArmorRaw {
    @PrimaryKey
    public int id;

    public int rarity;
    public ArmorType armor_type;
    public boolean male;
    public boolean female;

    public int slot_1;
    public int slot_2;
    public int slot_3;

    public int defense;
    public int fire;
    public int water;
    public int thunder;
    public int ice;
    public int dragon;
}

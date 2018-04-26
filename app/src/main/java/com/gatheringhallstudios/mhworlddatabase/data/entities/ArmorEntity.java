package com.gatheringhallstudios.mhworlddatabase.data.entities;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import com.gatheringhallstudios.mhworlddatabase.data.types.ArmorType;

/**
 * Raw entity for Armor
 * Created by Carlos on 3/5/2018.
 */
@Entity(tableName = "armor")
public class ArmorEntity {
    @PrimaryKey
    public int id;

    public int rarity;
    public ArmorType armor_type;
    public boolean male;
    public boolean female;

    public int slot_1;
    public int slot_2;
    public int slot_3;

    public int defense_base;
    public int defense_max;
    public int defense_augment_max;
    public int fire;
    public int water;
    public int thunder;
    public int ice;
    public int dragon;
}

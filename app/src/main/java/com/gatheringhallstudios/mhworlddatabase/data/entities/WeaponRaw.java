package com.gatheringhallstudios.mhworlddatabase.data.entities;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import com.gatheringhallstudios.mhworlddatabase.data.types.WeaponType;

/**
 * Created by Carlos on 3/20/2018.
 */
@Entity(tableName="weapon")
public class WeaponRaw {
    @PrimaryKey
    public int id;

    public WeaponType weapon_type;
    public int rarity;
    public int attack;

    public int slot_1;
    public int slot_2;
    public int slot_3;
}

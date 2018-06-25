package com.gatheringhallstudios.mhworlddatabase.data.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Created by Carlos on 4/3/2018.
 */
@Entity(tableName="decoration")
public class DecorationEntity {
    @PrimaryKey
    public int id;

    public int skilltree_id;
    public int slot;

    // the below may be moved out to some sort of feystone table and require a join to get the chances?
    public double mysterious_feystone_chance;
    public double glowing_feystone_chance;
    public double worn_feystone_chance;
    public double warped_feystone_chance;
}

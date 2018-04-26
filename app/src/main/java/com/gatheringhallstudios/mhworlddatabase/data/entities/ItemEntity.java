package com.gatheringhallstudios.mhworlddatabase.data.entities;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by Carlos on 3/5/2018.
 */
@Entity(tableName = "item")
public class ItemEntity {
    @PrimaryKey
    public int id;

    public int rarity;
    public int buy_price;
    public int sell_price;
    public int carry_capacity;
}

package com.gatheringhallstudios.mhworlddatabase.data.views;

import android.arch.persistence.room.PrimaryKey;

public class ItemView {
    @PrimaryKey
    public int id;

    public String name;
    public String description;

    public int rarity;
    public int buy_price;
    public int sell_price;
    public int carry_limit;
}

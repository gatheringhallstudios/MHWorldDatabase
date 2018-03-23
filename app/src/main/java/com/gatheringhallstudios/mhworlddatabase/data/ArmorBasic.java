package com.gatheringhallstudios.mhworlddatabase.data;

import com.gatheringhallstudios.mhworlddatabase.data.ArmorType;

/**
 * Contains basic armor information.
 * For more information, query for the armor directly
 * Created by Carlos on 3/6/2018.
 */
public class ArmorBasic {
    public int id;
    public String name;
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

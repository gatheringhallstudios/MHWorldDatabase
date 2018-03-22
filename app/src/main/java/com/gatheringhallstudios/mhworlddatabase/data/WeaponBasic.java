package com.gatheringhallstudios.mhworlddatabase.data;

/**
 * A view for basic weapon information.
 * For more data, query for the weapon directly by id.
 * Created by Carlos on 3/21/2018.
 */
public class WeaponBasic {
    public int id;
    public String name;

    public WeaponType weapon_type;
    public int rarity;
    public int attack;

    public int slot_1;
    public int slot_2;
    public int slot_3;
}

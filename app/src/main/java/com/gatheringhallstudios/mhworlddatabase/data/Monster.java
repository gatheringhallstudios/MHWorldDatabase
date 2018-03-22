package com.gatheringhallstudios.mhworlddatabase.data;

/**
 * Represents basic information for a single monster.
 * This class is not complete. Eventually there will be two versions.
 * 1) A basic version for list display
 * 2) A complete version for the detail page
 * Created by Carlos on 3/4/2018.
 */
public class Monster {
    // todo: see if there's a way to make these private/protected + getters only without room complaining

    public int id;
    public String name;
    public String description;
    public MonsterSize size;
}

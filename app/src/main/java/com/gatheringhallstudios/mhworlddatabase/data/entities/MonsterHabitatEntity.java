package com.gatheringhallstudios.mhworlddatabase.data.entities;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "monster_habitat")
public class MonsterHabitatEntity {
    @PrimaryKey
    public int id;

    public int monster_id;
    public int location_id;

    public String start_area;
    public String move_area;
    public String rest_area;
}

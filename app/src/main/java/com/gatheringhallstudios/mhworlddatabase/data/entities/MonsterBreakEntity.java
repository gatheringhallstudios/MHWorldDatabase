package com.gatheringhallstudios.mhworlddatabase.data.entities;

import android.arch.persistence.room.Entity;

@Entity(tableName="monster_break",
        primaryKeys = {"monster_id", "part_id"})
public class MonsterBreakEntity {
    public int id;

    public int monster_id;
    public int part_id;

    public Integer flinch;
    public Integer wound;
    public Integer sever;
    public String extract;
}

package com.gatheringhallstudios.mhworlddatabase.data.entities;

import android.arch.persistence.room.Entity;

@Entity(tableName="monster_hitzone",
        primaryKeys = {"monster_id", "part_id"})
public class MonsterHitzoneEntity {
    public int id;

    public int monster_id;
    public int part_id;

    public int cut;
    public int impact;
    public int shot;

    public int fire;
    public int water;
    public int ice;
    public int thunder;
    public int dragon;

    public int ko;
}

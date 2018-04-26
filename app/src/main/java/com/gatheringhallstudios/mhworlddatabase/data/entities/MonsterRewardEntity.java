package com.gatheringhallstudios.mhworlddatabase.data.entities;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import com.gatheringhallstudios.mhworlddatabase.data.types.Rank;

/**
 * MonsterView reward entity. Note that because rewards can have duplicate
 * entries, the monster/condition is not the primary key.
 */
@Entity(tableName = "monster_reward")
public class MonsterRewardEntity {
    @PrimaryKey
    public int id;

    public int monster_id;

    public int condition_id;

    public Rank rank;

    public int item_id;

    public int stack_size;

    public int percentage;
}

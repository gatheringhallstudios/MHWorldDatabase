package com.gatheringhallstudios.mhworlddatabase.data.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "charm")
public class CharmEntity {
    @PrimaryKey
    public int id;
}

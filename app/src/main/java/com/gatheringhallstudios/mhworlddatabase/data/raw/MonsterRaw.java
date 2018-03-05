package com.gatheringhallstudios.mhworlddatabase.data.raw;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by Carlos on 3/4/2018.
 */
@Entity(tableName = "monster")
public class MonsterRaw {
    @PrimaryKey
    public int id;
}

package com.gatheringhallstudios.mhworlddatabase.data.entities;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by Carlos on 3/5/2018.
 */
@Entity(tableName = "item")
public class ItemRaw {
    @PrimaryKey
    public int id;

    // todo: add more data once we have more item data
}

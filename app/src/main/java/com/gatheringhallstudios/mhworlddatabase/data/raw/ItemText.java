package com.gatheringhallstudios.mhworlddatabase.data.raw;

import android.arch.persistence.room.Entity;
import android.support.annotation.NonNull;

/**
 * Translation data for Item
 * Created by Carlos on 3/5/2018.
 */
@Entity(tableName = "item_text",
        primaryKeys = {"id", "lang_id"})
public class ItemText {
    public int id;

    @NonNull
    public String lang_id;

    public String name;
}
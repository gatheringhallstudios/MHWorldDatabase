package com.gatheringhallstudios.mhworlddatabase.data.raw;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.support.annotation.NonNull;

/**
 * Entity for Monster translation data
 * Created by Carlos on 3/4/2018.
 */
@Entity(tableName = "monster_text",
        primaryKeys = {"id", "lang_id"},
        foreignKeys = {@ForeignKey(childColumns = "id", parentColumns = "id", entity = MonsterRaw.class)}
)
public class MonsterText {
    public int id;

    @NonNull
    public String lang_id;

    public String name;
    public String description;
}

package com.gatheringhallstudios.mhworlddatabase.data.entities;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.support.annotation.NonNull;

@Entity(tableName = "charm_text",
        primaryKeys = {"id", "lang_id"},
        foreignKeys = {@ForeignKey(childColumns = "id", parentColumns = "id", entity = CharmEntity.class)}
)
public class CharmText {
    public int id;

    @NonNull
    public String lang_id;

    public String name;
    public String description;
}

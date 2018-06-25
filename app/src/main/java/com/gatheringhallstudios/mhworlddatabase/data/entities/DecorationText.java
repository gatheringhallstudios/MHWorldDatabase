package com.gatheringhallstudios.mhworlddatabase.data.entities;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.annotation.NonNull;

@Entity(tableName="decoration_text",
        primaryKeys = {"id", "lang_id"},
        foreignKeys = {@ForeignKey(
                childColumns = "id", parentColumns = "id",  entity = DecorationEntity.class)}
)
public class DecorationText {
    public int id;

    @NonNull
    public String lang_id;

    public String name;
}

package com.gatheringhallstudios.mhworlddatabase.data.entities;

/**
 * Created by Carlos on 3/21/2018.
 */

import androidx.room.Entity;
import androidx.annotation.NonNull;

@Entity(tableName = "weapon_text",
        primaryKeys = {"id", "lang_id"})
public class WeaponText {
    public int id;

    @NonNull
    public String lang_id;

    public String name;
}

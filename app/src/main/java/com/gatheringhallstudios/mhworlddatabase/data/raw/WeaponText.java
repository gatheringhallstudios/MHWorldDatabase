package com.gatheringhallstudios.mhworlddatabase.data.raw;

/**
 * Created by Carlos on 3/21/2018.
 */

import android.arch.persistence.room.Entity;
import android.support.annotation.NonNull;

@Entity(tableName = "weapon_text",
        primaryKeys = {"id", "lang_id"})
public class WeaponText {
    public int id;

    @NonNull
    public String lang_id;

    public String name;
}

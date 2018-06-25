package com.gatheringhallstudios.mhworlddatabase.data.entities;

import androidx.room.Entity;

import com.gatheringhallstudios.mhworlddatabase.data.entities.base.NameEntity;

/**
 * Translation data for armor
 * Created by Carlos on 3/5/2018.
 */
@Entity(tableName = "armor_text",
        primaryKeys = {"id", "lang_id"})
public class ArmorText extends NameEntity {
}

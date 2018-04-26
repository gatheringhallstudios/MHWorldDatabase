package com.gatheringhallstudios.mhworlddatabase.data.entities;

import android.arch.persistence.room.Entity;

import com.gatheringhallstudios.mhworlddatabase.data.entities.base.NameEntity;

@Entity(tableName = "location_text",
        primaryKeys = {"id", "lang_id"}
)
public class LocationEntity extends NameEntity {
}

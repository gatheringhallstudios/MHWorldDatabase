package com.gatheringhallstudios.mhworlddatabase.data.entities;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class LanguageEntity {
    @PrimaryKey
    public String id;

    public String name;
    public boolean complete;
}

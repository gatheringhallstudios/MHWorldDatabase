package com.gatheringhallstudios.mhworlddatabase.data.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class LanguageEntity {
    @PrimaryKey
    public String id;

    public String name;
    public boolean complete;
}

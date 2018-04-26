package com.gatheringhallstudios.mhworlddatabase.data.entities;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Raw entity for skill tree
 * Created by Carlos on 3/5/2018.
 */
@Entity(tableName = "skilltree")
public class SkillTreeEntity {
    @PrimaryKey
    public int id;

    // there is no more data, the rest requires joins
    // if it stays this way, we may remove it and just query on the skilltree_text table instead...
}

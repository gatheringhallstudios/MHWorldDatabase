package com.gatheringhallstudios.mhworlddatabase.data.entities;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;

@Entity(tableName = "charm_skill",
        primaryKeys = {"charm_id", "skilltree_id"},
        foreignKeys = {
        @ForeignKey(
                entity = CharmEntity.class,
                parentColumns = "id",
                childColumns = "charm_id"
        ),
        @ForeignKey(
                entity = SkillTreeEntity.class,
                parentColumns = "id",
                childColumns = "skilltree_id"
        )})
public class CharmSkill {
    public int charm_id;
    public int skilltree_id;
    public int level;
}

package com.gatheringhallstudios.mhworlddatabase.data.entities;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;

/**
 * Created by Carlos on 3/6/2018.
 */
@Entity(tableName = "armor_skill",
        primaryKeys = {"armor_id", "skilltree_id"},
        foreignKeys = {
            @ForeignKey(
                    entity=ArmorEntity.class,
                    parentColumns = "id",
                    childColumns = "armor_id"
                    ),
            @ForeignKey(
                    entity=SkillTreeEntity.class,
                    parentColumns = "id",
                    childColumns = "skilltree_id"
            )})
public class ArmorSkill {
    public int armor_id;
    public int skilltree_id;
    public int level;
}

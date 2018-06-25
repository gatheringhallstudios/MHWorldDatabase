package com.gatheringhallstudios.mhworlddatabase.data.entities;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;

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
            )},
        indices = @Index("skilltree_id"))
public class ArmorSkill {
    public int armor_id;
    public int skilltree_id;
    public int level;
}

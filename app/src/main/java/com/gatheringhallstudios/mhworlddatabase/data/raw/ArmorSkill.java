package com.gatheringhallstudios.mhworlddatabase.data.raw;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;

/**
 * Created by Carlos on 3/6/2018.
 */
@Entity(tableName = "armor_skill",
        primaryKeys = {"armor_id", "skill_id"},
        foreignKeys = {
            @ForeignKey(
                    entity=ArmorRaw.class,
                    parentColumns = "id",
                    childColumns = "armor_id"
                    ),
            @ForeignKey(
                    entity=SkillTreeRaw.class,
                    parentColumns = "id",
                    childColumns = "skill_id"
            )})
public class ArmorSkill {
    public int armor_id;
    public int skill_id;
    public int level;
}

package com.gatheringhallstudios.mhworlddatabase.data.entities;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;

/**
 * Created by Carlos on 3/5/2018.
 */
@Entity(tableName = "skill",
        primaryKeys = {"skilltree_id", "lang_id", "level"},
        foreignKeys = {
            @ForeignKey(
                    entity=SkillTreeRaw.class,
                    parentColumns={ "id" },
                    childColumns={ "skilltree_id" }
            )
        }
)
public class SkillRaw {
    public int skilltree_id;
    public int lang_id;
    public int level;
    public String description;
}

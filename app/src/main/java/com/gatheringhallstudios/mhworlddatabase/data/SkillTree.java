package com.gatheringhallstudios.mhworlddatabase.data;

import android.arch.persistence.room.Relation;

import com.gatheringhallstudios.mhworlddatabase.data.raw.SkillRaw;

import java.util.List;

/**
 * Created by Carlos on 3/6/2018.
 */

public class SkillTree {
    public int id;
    public String name;
    public String description;

    @Relation(entity= SkillRaw.class, parentColumn="id", entityColumn ="skilltree_id")
    public List<Skill> skills;
}

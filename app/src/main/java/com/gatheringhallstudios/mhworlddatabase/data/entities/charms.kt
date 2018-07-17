package com.gatheringhallstudios.mhworlddatabase.data.entities

import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.PrimaryKey


@Entity(tableName = "charm")
data class CharmEntity(
        @PrimaryKey val id: Int
        // todo: icon color
)

@Entity(tableName = "charm_text",
        primaryKeys = ["id", "lang_id"],
        foreignKeys = [
            (ForeignKey(
                    childColumns = ["id"],
                    parentColumns = ["id"],
                    entity = CharmEntity::class))
        ])
data class CharmText(
        val id: Int,
        val lang_id: String,
        val name: String?,
        val description: String?
)

@Entity(tableName = "charm_skill",
        primaryKeys = ["charm_id", "skilltree_id"],
        foreignKeys = [
            (ForeignKey(entity = CharmEntity::class,
                    parentColumns = ["id"],
                    childColumns = ["charm_id"])),
            (ForeignKey(entity = SkillTreeEntity::class,
                    parentColumns = ["id"],
                    childColumns = ["skilltree_id"]))
        ])
data class CharmSkill(
        val charm_id: Int,
        val skilltree_id: Int,
        val level: Int
)

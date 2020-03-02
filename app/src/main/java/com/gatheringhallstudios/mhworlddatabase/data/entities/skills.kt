package com.gatheringhallstudios.mhworlddatabase.data.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey


@Entity(tableName = "skilltree")
data class SkillTreeEntity(
        @PrimaryKey val id: Int,
        val max_level: Int,
        val icon_color: String?,
        val secret: Int,
        val unlocks_id: Int?
)

/**
 * Translation data for SkillTreeFull
 * Created by Carlos on 3/5/2018.
 */
@Entity(tableName = "skilltree_text",
        primaryKeys = ["id", "lang_id"])
data class SkillTreeText(
        val id: Int,
        val lang_id: String,

        val name: String?,
        val description: String?
)

/**
 * Created by Carlos on 3/5/2018.
 */
@Entity(tableName = "skill",
        primaryKeys = ["skilltree_id", "lang_id", "level"],
        foreignKeys = [
            (ForeignKey(
                    entity = SkillTreeEntity::class,
                    parentColumns = ["id"],
                    childColumns = ["skilltree_id"]))
        ])
data class SkillEntity(
        val skilltree_id: Int,
        val lang_id: Int,
        val level: Int,
        val description: String?
)

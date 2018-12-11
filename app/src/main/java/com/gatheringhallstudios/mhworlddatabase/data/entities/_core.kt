package com.gatheringhallstudios.mhworlddatabase.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "language")
data class Language(
        /**
         * The id of this language entry, which is basically the language code
         */
        @PrimaryKey val id: String,

        /**
         * The name of the language entry written in that language.
         */
        val name: String
)
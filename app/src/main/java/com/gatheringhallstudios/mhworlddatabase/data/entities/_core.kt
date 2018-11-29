package com.gatheringhallstudios.mhworlddatabase.data.entities

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

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
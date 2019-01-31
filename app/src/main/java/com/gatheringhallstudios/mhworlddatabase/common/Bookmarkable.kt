package com.gatheringhallstudios.mhworlddatabase.common

import com.gatheringhallstudios.mhworlddatabase.data.types.DataType

/**
 * This interface defines the methods required for the entity to be saveable as a Bookmark
 */
interface Bookmarkable {
    fun getEntityId() : Int
    fun getType() : DataType
}
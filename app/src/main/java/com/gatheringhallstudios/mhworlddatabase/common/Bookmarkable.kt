package com.gatheringhallstudios.mhworlddatabase.common

import com.gatheringhallstudios.mhworlddatabase.data.types.DataType

/**
 * This interface defines the methods required for the entity to be saveable as a Favorite
 */
interface Favoritable {
    fun getEntityId() : Int
    fun getType() : DataType
}
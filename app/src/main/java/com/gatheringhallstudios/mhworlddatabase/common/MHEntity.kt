package com.gatheringhallstudios.mhworlddatabase.common

import com.gatheringhallstudios.mhworlddatabase.data.types.DataType

/**
 * Main interface for monster hunter entities.
 * A standardized way to receive the entity type and identifier per object.
 */
interface MHEntity {
    val entityId : Int
    val entityType : DataType
}
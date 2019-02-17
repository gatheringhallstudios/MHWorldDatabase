package com.gatheringhallstudios.mhworlddatabase.data.models

import com.gatheringhallstudios.mhworlddatabase.data.types.DataType

/**
 * Main interface for monster hunter model objects.
 * A standardized way to receive the entity type and identifier per object.
 */
interface MHModel {
    val entityId: Int
    val entityType: DataType
}

/**
 * Main interface for Monster Hunter Model parents that are part of a tree.
 */
interface MHParentedModel: MHModel {
    /**
     * Returns the id of the parent model object, or null if this is a root.
     */
    val parentId: Int?
}
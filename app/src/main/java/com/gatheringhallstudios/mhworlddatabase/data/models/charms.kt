package com.gatheringhallstudios.mhworlddatabase.data.models

import com.gatheringhallstudios.mhworlddatabase.data.types.DataType

open class CharmBase(
        val id: Int,
        val name: String?,
        val rarity: Int,
        val previous_id: Int?
) : MHParentedModel {
    override val entityId get() = id
    override val entityType get() = DataType.CHARM
    override val parentId: Int? get() = previous_id
}

/**
 * The base charm class.
 * As charms don't have any special data outside of providing an object to join to,
 * the base is the same as the normal load.
 * TODO: This will be removed in the future if it receives no extra data.
 */
open class Charm(
        id: Int,
        name: String?,
        rarity: Int,
        previous_id: Int?
) : CharmBase(id, name, rarity, previous_id)

/**
 * Contains the charm and any additional join data related to the charm
 */
class CharmFull(
        val charm: Charm,
        val skills: List<SkillLevel>,
        val components: List<ItemQuantity>
) : MHModel {
    override val entityId get() = charm.id
    override val entityType get() = DataType.CHARM
}

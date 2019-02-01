package com.gatheringhallstudios.mhworlddatabase.data.models

import com.gatheringhallstudios.mhworlddatabase.common.MHEntity
import com.gatheringhallstudios.mhworlddatabase.data.types.DataType

open class CharmBase(
        val id: Int,
        val name: String?,
        val rarity: Int
) : MHEntity {
    override val entityId get() = id
    override val entityType get() = DataType.CHARM
}

/**
 * The base charm class.
 * As charms don't have any special data outside of providing an object to join to,
 * the base is the same as the normal load.
 */
open class Charm(
        id: Int,
        name: String?,
        rarity: Int,
        val previous_id: Int?
) : CharmBase(id, name, rarity)

/**
 * Contains the charm and any additional join data related to the charm
 */
class CharmFull(
        val charm: Charm,
        val skills: List<SkillLevel>,
        val components: List<ItemQuantity>
) : MHEntity {
    override val entityId get() = charm.id
    override val entityType get() = DataType.CHARM
}

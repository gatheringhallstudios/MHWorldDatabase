package com.gatheringhallstudios.mhworlddatabase.data.models

import com.gatheringhallstudios.mhworlddatabase.data.types.*

class Kinsect(
        var id: Int,
        var name: String,
        var rarity: Int,
        var previous_kinsect_id: Int?,
        var attack_type: KinsectAttackType,
        var dust_effect: KinsectDustEffect,
        var power: Int,
        var speed: Int,
        var heal: Int,
        var final: Boolean
) : MHParentedModel {
    override val entityId get() = id
    override val entityType get() = DataType.KINSECT
    override val parentId get() = previous_kinsect_id

    constructor() : this(0, "", 0, null, KinsectAttackType.SEVER, KinsectDustEffect.POISON, 0,0,0,false)
}

class KinsectFull(
        var kinsect: Kinsect,
        var recipe: List<ItemQuantity>
) : MHModel {
    override val entityId get() = kinsect.id
    override val entityType get() = DataType.KINSECT

    constructor() : this(Kinsect(), ArrayList<ItemQuantity>())
}
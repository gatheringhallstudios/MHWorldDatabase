package com.gatheringhallstudios.mhworlddatabase.features.kinsects.list

import com.gatheringhallstudios.mhworlddatabase.util.tree.Filter
import com.gatheringhallstudios.mhworlddatabase.data.models.Kinsect
import com.gatheringhallstudios.mhworlddatabase.data.types.*

class KinsectAttackTypeFilter(private  val attackTypes: Set<KinsectAttackType>): Filter<Kinsect> {
    override fun runFilter(obj: Kinsect): Boolean {
        return (obj.attack_type in attackTypes)
    }
}

class KinsectDustEffectFilter(private  val dustEffects: Set<KinsectDustEffect>): Filter<Kinsect> {
    override fun runFilter(obj: Kinsect): Boolean {
        return (obj.dust_effect in dustEffects)
    }
}
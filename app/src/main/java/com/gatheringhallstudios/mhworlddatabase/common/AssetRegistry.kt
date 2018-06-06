package com.gatheringhallstudios.mhworlddatabase.common

import com.gatheringhallstudios.mhworlddatabase.R

/**
 * The AssetRegistry is used to create key-value maps between any specified types
 * @param T Type for Key
 * @param K Type for Value
 */

typealias AdderFun<T, K> = (name: T, resId: K) -> Unit
private fun <T, K> createRegistry(initLambda: (AdderFun<T, K>) -> Unit): Map<T, K> {
    val mutableRegistry = HashMap<T, K>()
    initLambda({ name: T, resId: K ->
        mutableRegistry[name] = resId
    })
    return mutableRegistry
}

val VectorRegistry = createRegistry<String, Int>{ register ->
    register("armor", R.drawable.ic_armor)
    register("head", R.drawable.question_mark_grey)
    register("chest", R.drawable.question_mark_grey)
    register("arms", R.drawable.question_mark_grey)
    register("waist", R.drawable.question_mark_grey)
    register("leg", R.drawable.question_mark_grey)
    register("greatsword", R.drawable.question_mark_grey)
}

val ColorRegistry = createRegistry<String, Int>{ register ->
    register("rare1", R.color.rarity_1)
    register("rare2", R.color.rarity_2)
    register("rare3", R.color.rarity_3)
    register("rare4", R.color.rarity_4)
    register("rare5", R.color.rarity_5)
    register("rare6", R.color.rarity_6)
    register("rare7", R.color.rarity_7)
    register("rare8", R.color.rarity_8)
    register("blue", R.color.blue)
}
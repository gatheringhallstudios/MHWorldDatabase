package com.gatheringhallstudios.mhworlddatabase.common

import com.gatheringhallstudios.mhworlddatabase.R

typealias AdderFun<T, K> = (name: T, resId: K) -> Unit
private fun <T, K> createRegistry(initLambda: (AdderFun<T, K>) -> Unit): Map<T, K> {
    val mutableRegistry = HashMap<T, K>()
    initLambda({ name: T, resId: K ->
        mutableRegistry[name] = resId
    })
    return mutableRegistry
}

/**
 * Map to retrieve Drawable Resources from string values provided by the Database
 */
val VectorRegistry = createRegistry<String, Int>{ register ->
    register("armor", R.drawable.ic_armor)
    register("head", R.drawable.question_mark_grey)
    register("chest", R.drawable.question_mark_grey)
    register("arms", R.drawable.question_mark_grey)
    register("waist", R.drawable.question_mark_grey)
    register("leg", R.drawable.question_mark_grey)
    register("greatsword", R.drawable.question_mark_grey)
}

/*
    Rarity 1/2: #CAC8C6
    Rarity 3:   #A9B978
    Rarity 4:   #6AAC85
    Rarity 5:   #7CC2B1
    Rarity 6:   #757DFF
    Rarity 7:   #7B61AE
    Rarity 8:   #B58377
 */
val ColorRegistry = createRegistry<String, Int>{ register ->
    register("1", R.color.rarity_1)
    register("2", R.color.rarity_2)
    register("3", R.color.rarity_3)
    register("4", R.color.rarity_4)
    register("5", R.color.rarity_5)
    register("6", R.color.rarity_6)
    register("7", R.color.rarity_7)
    register("8", R.color.rarity_8)
}
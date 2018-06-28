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
    initLambda { name: T, resId: K ->
        mutableRegistry[name] = resId
    }
    return mutableRegistry
}

val VectorRegistry = createRegistry<String, Int>{ register ->
    // Armor
    register("armor", R.drawable.ic_armor) // TODO This icon is unused and should eventually be replaced with armorset icon
    register("head", R.drawable.ic_head)
    register("chest", R.drawable.ic_chest)
    register("arms", R.drawable.ic_arm)
    register("waist", R.drawable.ic_waist)
    register("leg", R.drawable.ic_leg)

    // Weapons
    register("greatsword", R.drawable.question_mark_grey)

    // Items
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

    register("White", R.color.icon_white)
    register("Gray", R.color.icon_gray)
    register("Red", R.color.icon_red)
    register("DarkRed", R.color.icon_dark_red)
    register("Orange", R.color.icon_orange)
    register("Khaki", R.color.icon_khaki)
    register("Gold", R.color.icon_gold)
    register("Yellow", R.color.icon_yellow)
    register("Violet", R.color.icon_violet)
    register("Blue", R.color.icon_blue)
    register("Cyan", R.color.icon_cyan)
    register("Green", R.color.icon_green)
    register("DarkGreen", R.color.icon_dark_green)
}
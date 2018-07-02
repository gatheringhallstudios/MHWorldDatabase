package com.gatheringhallstudios.mhworlddatabase.common

import android.util.Log
import com.gatheringhallstudios.mhworlddatabase.R
import com.gatheringhallstudios.mhworlddatabase.data.types.ArmorType

/**
 * The AssetRegistry is used to create key-value maps between any specified types
 * @param T Type for Key
 * @param K Type for Value
 */

typealias AdderFun<T, K> = (name: T, resId: K) -> Unit

class Registry<T, K>(val source: Map<T, K>) {
    operator fun get(key: T): K? {
        if (!source.containsKey(key)) {
            Log.w("AssetRegistry", "Value $key not found in registry")
        }
        return source[key]
    }

    fun get(key: T, default: K): K {
        return get(key) ?: default
    }
}

private fun <T, K> createRegistry(initLambda: (AdderFun<T, K>) -> Unit): Registry<T, K> {
    val mutableRegistry = HashMap<T, K>()
    initLambda { name: T, resId: K ->
        mutableRegistry[name] = resId
    }
    return Registry(mutableRegistry)
}

private fun <T, K> createRegistry(vararg items: Pair<T, K>): Registry<T, K> {
    return Registry(mapOf(*items))
}

val ArmorRegistry = createRegistry(
        ArmorType.HEAD to R.drawable.ic_head,
        ArmorType.CHEST to R.drawable.ic_chest,
        ArmorType.ARMS to R.drawable.ic_arm,
        ArmorType.WAIST to R.drawable.ic_waist,
        ArmorType.LEGS to R.drawable.ic_leg
)

val VectorRegistry = createRegistry<String, Int>{ register ->
    // Armor
    register("armor", R.drawable.ic_armor) // TODO This icon is unused and should eventually be replaced with armorset icon
    register("head", R.drawable.ic_head)
    register("chest", R.drawable.ic_chest)
    register("arms", R.drawable.ic_arm)
    register("waist", R.drawable.ic_waist)
    register("leg", R.drawable.ic_leg)

    // Weapons
    register("greatsword", R.drawable.ic_question_mark)

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
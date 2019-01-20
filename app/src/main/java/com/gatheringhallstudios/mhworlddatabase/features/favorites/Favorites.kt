package com.gatheringhallstudios.mhworlddatabase.features.favorites

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import androidx.core.content.edit
import com.gatheringhallstudios.mhworlddatabase.data.models.*
import com.gatheringhallstudios.mhworlddatabase.data.types.DataType


// we are storing an application context, so its fine
@SuppressLint("StaticFieldLeak")
object Favorites {
    lateinit private var ctx: Context
    lateinit private var preferences : SharedPreferences

    fun bindApplication(app: Application) {
        this.ctx = app.applicationContext
        preferences = PreferenceManager.getDefaultSharedPreferences(ctx)
    }

    private val favoritesMap : MutableMap<DataType, MutableSet<Int>> = mutableMapOf(
            DataType.MONSTER to mutableSetOf(),
            DataType.LOCATION to mutableSetOf(),
            DataType.ARMOR to mutableSetOf(),
            DataType.CHARM to mutableSetOf(),
            DataType.ITEM to mutableSetOf(),
            DataType.DECORATION to mutableSetOf(),
            DataType.WEAPON to mutableSetOf(),
            DataType.SKILL to mutableSetOf()
    )

    fun toggleFavorite(entity: Any?) {
        if (entity != null) {
            val enumerator = classToEnum(entity)
            if (enumerator != null) {
                if (isFavorited(entity)) {
                    favoritesMap[classToEnum(entity)]!!.remove(getId(entity))
                } else {
                    favoritesMap[classToEnum(entity)]!!.add(getId(entity))
                }

                preferences.edit().putString(JSON.stringify)
            }
        }
    }

    fun isFavorited(entity: Any?) : Boolean {
        if (entity != null) {
            val enumerator = classToEnum(entity)
            return if (enumerator != null) {
                favoritesMap[classToEnum(entity)]!!.contains(getId(entity))
            } else {
                return false
            }
        } else {
            return false
        }
    }

    private fun classToEnum(entity: Any?) : DataType? {
        return when (entity) {
            is Monster -> DataType.MONSTER
            is Location -> DataType.LOCATION
            is Armor -> DataType.ARMOR
            is Charm -> DataType.CHARM
            is Item -> DataType.ITEM
            is Decoration -> DataType.DECORATION
            is Skill -> DataType.SKILL
            is WeaponFull -> DataType.WEAPON
            else -> null
        }
    }

    private fun getId(entity: Any) : Int {
        return when (entity) {
            is Monster -> entity.id
            is Location -> entity.id
            is Armor -> entity.id
            is Charm -> entity.id
            is Item -> entity.id
            is Decoration -> entity.id
            is Skill -> entity.skilltree_id
            is WeaponFull -> entity.weapon.id
            else -> 0
        }
    }
}
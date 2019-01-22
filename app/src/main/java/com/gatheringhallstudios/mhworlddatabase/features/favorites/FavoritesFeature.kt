package com.gatheringhallstudios.mhworlddatabase.features.favorites

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import androidx.lifecycle.LiveData
import com.gatheringhallstudios.mhworlddatabase.data.AppDatabase
import com.gatheringhallstudios.mhworlddatabase.data.dao.FavoriteDao
import com.gatheringhallstudios.mhworlddatabase.data.entities.FavoriteEntity
import com.gatheringhallstudios.mhworlddatabase.data.models.*
import com.gatheringhallstudios.mhworlddatabase.data.types.DataType
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.*


// we are storing an application context, so its fine
@SuppressLint("StaticFieldLeak")
object FavoritesFeature {
    lateinit private var ctx: Context
    lateinit private var favoritesList : MutableList<Favorite>
    lateinit private var dao : FavoriteDao

    fun bindApplication(app: Application) {
        this.ctx = app.applicationContext
        dao = AppDatabase.getAppDataBase(ctx)!!.favoriteDao()
        val deferred = GlobalScope.async { dao.loadFavorites() }
        runBlocking { favoritesList = deferred.await() }
    }

    fun toggleFavorite(entity: Any?) {
        if (entity != null) {
            if (!isFavorited(entity)) {
                val date = Date()
                favoritesList.add(Favorite(getId(entity), classToEnum(entity), date))
                GlobalScope.launch { dao.insert(FavoriteEntity(getId(entity), classToEnum(entity), date)) }
            } else {
                val index = favoritesList.indexOfFirst {it.dataId == getId(entity) && it.dataType == classToEnum(entity)}
                GlobalScope.launch { dao.delete(FavoriteEntity(favoritesList[index].dataId,
                        favoritesList[index].dataType, favoritesList[index].dateAdded)) }
                favoritesList.removeAt(index)
            }
        }
    }

    fun isFavorited(entity: Any): Boolean {
        return favoritesList.indexOfFirst {it.dataId == getId(entity) && it.dataType == classToEnum(entity)} != -1
    }

    private fun classToEnum(entity: Any?): DataType {
        return when (entity) {
            is Monster -> DataType.MONSTER
            is Location -> DataType.LOCATION
            is Armor -> DataType.ARMOR
            is Charm -> DataType.CHARM
            is Item -> DataType.ITEM
            is Decoration -> DataType.DECORATION
            is Skill -> DataType.SKILL
            is WeaponFull -> DataType.WEAPON
            else -> DataType.NONE
        }
    }

    private fun getId(entity: Any): Int {
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
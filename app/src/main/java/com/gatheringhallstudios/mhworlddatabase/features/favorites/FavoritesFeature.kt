package com.gatheringhallstudios.mhworlddatabase.features.favorites

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import androidx.lifecycle.LiveData
import com.gatheringhallstudios.mhworlddatabase.common.Favoritable
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
    //For the sake of setting a cap
    private val FAVORITES_MAX = 200
    lateinit private var ctx: Context
    lateinit private var favoritesList: MutableList<Favorite>
    lateinit private var dao: FavoriteDao

    fun bindApplication(app: Application) {
        this.ctx = app.applicationContext
        dao = AppDatabase.getAppDataBase(ctx)!!.favoriteDao()
        val deferred = GlobalScope.async { dao.loadFavorites() }
        runBlocking { favoritesList = deferred.await() }
    }

    fun toggleFavorite(entity: Favoritable?) {
        if (entity != null) {
            if (!isFavorited(entity) && favoritesList.size < FAVORITES_MAX) {
                val date = Date()
                favoritesList.add(Favorite(entity.getEntityId(), entity.getType(), date))
                GlobalScope.launch { dao.insert(FavoriteEntity(entity.getEntityId(), entity.getType(), date)) }
            } else {
                val index = favoritesList.indexOfFirst { it.dataId == entity.getEntityId() && it.dataType == entity.getType() }
                val favorite = favoritesList[index]
                favoritesList.removeAt(index)
                GlobalScope.launch {
                    dao.delete(FavoriteEntity(favorite.dataId,
                            favorite.dataType, favorite.dateAdded))
                }
            }
        }
    }


    fun isFavorited(entity: Favoritable): Boolean {
        return favoritesList.indexOfFirst { it.dataId == entity.getEntityId() && it.dataType == entity.getType() } != -1
    }

    fun getFavoritesByType(dataType: DataType) : List<Favorite> {
        return favoritesList.filter {it.dataType == dataType}
    }
}
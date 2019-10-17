package com.gatheringhallstudios.mhworlddatabase.features.bookmarks

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import com.gatheringhallstudios.mhworlddatabase.data.AppDatabase
import com.gatheringhallstudios.mhworlddatabase.data.dao.BookmarksDao
import com.gatheringhallstudios.mhworlddatabase.data.entities.BookmarkEntity
import com.gatheringhallstudios.mhworlddatabase.data.models.Bookmark
import com.gatheringhallstudios.mhworlddatabase.data.models.MHModel
import com.gatheringhallstudios.mhworlddatabase.data.types.DataType
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.*


/**
 * Singleton object used to drive the bookmarks feature
 * Needs to be binded to the application in the main Application class.
 */
// we are storing an application context, so its fine
@SuppressLint("StaticFieldLeak")
object BookmarksFeature {
    //For the sake of setting a cap
    private val BOOKMARKS_MAX = 200
    private lateinit var ctx: Context
    private lateinit var bookmarksList: MutableList<Bookmark>
    private lateinit var dao: BookmarksDao

    fun bindApplication(app: Application) {
        this.ctx = app.applicationContext
        dao = AppDatabase.getAppDataBase(ctx)!!.bookmarkDao()
        val deferred = GlobalScope.async { dao.loadBookmarks() }
        runBlocking { bookmarksList = deferred.await() }
    }

    fun toggleBookmark(model: MHModel?) {
        if (model != null) {
            if (!isBookmarked(model) && bookmarksList.size < BOOKMARKS_MAX) {
                val date = Date()
                bookmarksList.add(Bookmark(model.entityId, model.entityType, date))
                GlobalScope.launch { dao.insert(BookmarkEntity(model.entityId, model.entityType, date)) }
            } else {
                val index = bookmarksList.indexOfFirst {
                    it.dataId == model.entityId && it.dataType == model.entityType
                }
                val bookmark = bookmarksList[index]
                bookmarksList.removeAt(index)
                GlobalScope.launch {
                    dao.delete(BookmarkEntity(bookmark.dataId,
                            bookmark.dataType, bookmark.dateAdded))
                }
            }
        }
    }

    fun isBookmarked(model: MHModel): Boolean {
        return bookmarksList.indexOfFirst {
            it.dataId == model.entityId && it.dataType == model.entityType
        } != -1
    }

    fun getBookmarksByType(dataType: DataType) : List<Bookmark> {
        return bookmarksList.filter {it.dataType == dataType}
    }
}
package com.gatheringhallstudios.mhworlddatabase.features.bookmarks

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import com.gatheringhallstudios.mhworlddatabase.common.Bookmarkable
import com.gatheringhallstudios.mhworlddatabase.data.AppDatabase
import com.gatheringhallstudios.mhworlddatabase.data.dao.BookmarksDao
import com.gatheringhallstudios.mhworlddatabase.data.entities.BookmarkEntity
import com.gatheringhallstudios.mhworlddatabase.data.models.*
import com.gatheringhallstudios.mhworlddatabase.data.types.DataType
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.*


// we are storing an application context, so its fine
@SuppressLint("StaticFieldLeak")
object BookmarksFeature {
    //For the sake of setting a cap
    private val BOOKMARKS_MAX = 200
    lateinit private var ctx: Context
    lateinit private var bookmarksList: MutableList<Bookmark>
    lateinit private var dao: BookmarksDao

    fun bindApplication(app: Application) {
        this.ctx = app.applicationContext
        dao = AppDatabase.getAppDataBase(ctx)!!.bookmarkDao()
        val deferred = GlobalScope.async { dao.loadBookmarks() }
        runBlocking { bookmarksList = deferred.await() }
    }

    fun toggleBookmark(entity: Bookmarkable?) {
        if (entity != null) {
            if (!isBookmarked(entity) && bookmarksList.size < BOOKMARKS_MAX) {
                val date = Date()
                bookmarksList.add(Bookmark(entity.getEntityId(), entity.getType(), date))
                GlobalScope.launch { dao.insert(BookmarkEntity(entity.getEntityId(), entity.getType(), date)) }
            } else {
                val index = bookmarksList.indexOfFirst { it.dataId == entity.getEntityId() && it.dataType == entity.getType() }
                val bookmark = bookmarksList[index]
                bookmarksList.removeAt(index)
                GlobalScope.launch {
                    dao.delete(BookmarkEntity(bookmark.dataId,
                            bookmark.dataType, bookmark.dateAdded))
                }
            }
        }
    }

    fun isBookmarked(entity: Bookmarkable): Boolean {
        return bookmarksList.indexOfFirst { it.dataId == entity.getEntityId() && it.dataType == entity.getType() } != -1
    }

    fun getBookmarksByType(dataType: DataType) : List<Bookmark> {
        return bookmarksList.filter {it.dataType == dataType}
    }
}
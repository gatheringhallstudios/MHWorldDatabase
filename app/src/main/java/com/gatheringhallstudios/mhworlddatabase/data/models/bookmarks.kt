package com.gatheringhallstudios.mhworlddatabase.data.models

import com.gatheringhallstudios.mhworlddatabase.data.types.DataType
import java.util.*

open class Bookmark(
        val dataId: Int,
        val dataType: DataType,
        val dateAdded: Date
)
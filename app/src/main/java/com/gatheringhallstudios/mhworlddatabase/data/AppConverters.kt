package com.gatheringhallstudios.mhworlddatabase.data

import android.util.Log
import androidx.room.TypeConverter
import com.gatheringhallstudios.mhworlddatabase.data.types.DataType
import com.gatheringhallstudios.mhworlddatabase.util.Converter
import java.text.SimpleDateFormat
import java.util.*

val DataTypeConverter = Converter(
        "LOCATION" to DataType.LOCATION,
        "ITEM" to DataType.ITEM,
        "MONSTER" to DataType.MONSTER,
        "SKILL" to DataType.SKILL,
        "DECORATION" to DataType.DECORATION,
        "CHARM" to DataType.CHARM,
        "ARMOR" to DataType.ARMOR,
        "WEAPON" to DataType.WEAPON,
        null to null
)

/**
 * Type conversions for things like enumerations.
 * Change this to add new enum values
 * These are registered to the database class via an annotation.
 */

class AppConverters {
    @TypeConverter fun dataTypeFromString(value: String?) = DataTypeConverter.deserialize(value)
    @TypeConverter fun fromDataType(type: DataType?) = DataTypeConverter.serialize(type)

    @TypeConverter fun dateFromString(value: String?) =
        try {
            SimpleDateFormat("MMM dd yyyy HH:mma", Locale.US).parse(value)
        } catch (ex: Exception) {
            try {
                // this may be an old date instance before locale was hardcoded. Try that.
                SimpleDateFormat("MMM dd yyyy HH:mma").parse(value)
            } catch (ex: Exception) {
                Log.e("AppConverters", "Failed to convert date $value, falling back to new date object")
                Date()
            }
        }

    @TypeConverter fun stringFromDate(type: Date) = SimpleDateFormat("MMM dd yyyy HH:mma", Locale.US).format(type)
}

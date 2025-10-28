package dev.viviantung.myruns

import androidx.room.TypeConverter
import com.google.android.gms.maps.model.LatLng
import java.util.Date

class Converters {
    // converts long to date object
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    // converts date to long object
    @TypeConverter
    fun dateToTimeStamp(date: Date?): Long? {
        return date?.time?.toLong()
    }

    // convert latLng to string
    @TypeConverter
    fun latLngToString(latLng: LatLng): String {
        return "(${latLng.latitude},${latLng.longitude}"
    }

    // converts string back to latLng
    @TypeConverter
    fun stringToLatLng(string: String): LatLng {
        val s = string.replace("(", "").replace(")", "")
        val list = s.split(",")
        return LatLng(list.first().toDouble(), list.last().toDouble())
    }
}
package dev.viviantung.myruns

import android.icu.util.Calendar
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.android.gms.maps.model.LatLng

@Entity(tableName = "exercise_table")
data class Exercise (
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0L,

    @ColumnInfo(name = "input_column")
    var inputType: Int,

    @ColumnInfo(name = "activity_column")
    var activityType: Int,

    @ColumnInfo(name = "date_column")
    var dateTime: Long,

    @ColumnInfo(name = "duration_column")
    var duration: Double,

    @ColumnInfo(name = "distance_column")
    var distance: Double,

    @ColumnInfo(name = "speed_column")
    var avgSpeed: Double,

    @ColumnInfo(name = "calorie_column")
    var calorie: Double,

    @ColumnInfo(name = "climb_column")
    var climb: Double,

    @ColumnInfo(name = "heart_column")
    var heartRate: Double,

    @ColumnInfo(name = "comment_column")
    var comment: String = "",

    @ColumnInfo(name = "lat_column")
    var location: String = "",
)

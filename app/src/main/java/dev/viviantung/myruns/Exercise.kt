package dev.viviantung.myruns

import android.icu.util.Calendar
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.android.gms.maps.model.LatLng
import java.util.Date

@Entity(tableName = "exercise_table")
data class Exercise (
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0L,

    @ColumnInfo(name = "input_column")
    var inputType: Int,

    @ColumnInfo(name = "activity_column")
    var activityType: Int,

    @ColumnInfo(name = "date_column")
    var dateTime: Date,

    @ColumnInfo(name = "duration_column")
    var duration: Double = 0.0,

    @ColumnInfo(name = "distance_column")
    var distance: Double = 0.0,

    @ColumnInfo(name = "pace_column")
    var avgPace: Double = 0.0,

    @ColumnInfo(name = "speed_column")
    var avgSpeed: Double = 0.0,

    @ColumnInfo(name = "calorie_column")
    var calorie: Double = 0.0,

//    @ColumnInfo(name = "climb_column")
//    var climb: Double = 0.0,

    @ColumnInfo(name = "heart_column")
    var heartRate: Double = 0.0,

    @ColumnInfo(name = "comment_column")
    var comment: String = "",

    @ColumnInfo(name = "lat_column")
    var location: String = "",
)

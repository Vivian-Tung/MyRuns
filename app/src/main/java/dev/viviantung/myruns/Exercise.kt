package dev.viviantung.myruns

import android.icu.util.Calendar
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.android.gms.maps.model.LatLng

@Entity(tableName = "exercise_table")
data class ExerciseEntry (
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0L,

    @ColumnInfo(name = "exercise_column")
    var inputType: Int,

    @ColumnInfo(name = "exercise_column")
    var activityType: Int,

    @ColumnInfo(name = "exercise_column")
    var dateTime: Calendar,

    @ColumnInfo(name = "exercise_column")
    var duration: Double,

    @ColumnInfo(name = "exercise_column")
    var distance: Double,

    @ColumnInfo(name = "exercise_column")
    var avgSpeed: Double,

    @ColumnInfo(name = "exercise_column")
    var calorie: Double,

    @ColumnInfo(name = "exercise_column")
    var climb: Double,

    @ColumnInfo(name = "exercise_column")
    var heartRate: Double,

    @ColumnInfo(name = "exercise_column")
    var comment: String = "",

    @ColumnInfo(name = "exercise_column")
    var locationList: ArrayList <LatLng>
)

package dev.viviantung.myruns

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

// to build the database
@Database(entities = [Exercise::class], version = 2)
@TypeConverters(Converters::class)
abstract class ExerciseDatabase : RoomDatabase() {
    abstract val exerciseDatabaseDao: ExerciseDatabaseDao

    companion object {
        //The Volatile keyword guarantees visibility of changes to the INSTANCE variable across threads

        @Volatile
        private var INSTANCE: ExerciseDatabase? = null

        fun getInstance(context: Context): ExerciseDatabase{
            synchronized(this) {
                var instance = INSTANCE
                if(instance == null) {
                    instance = Room.databaseBuilder(context.applicationContext,
                                        ExerciseDatabase::class.java, "exercise_table")
                        .fallbackToDestructiveMigration(true) // just drop the tables if theres a new version
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}
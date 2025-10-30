package dev.viviantung.myruns

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ExerciseDatabaseDao {

    // insert from start tab
    @Insert
    suspend fun insertExercise(exercise: Exercise)

    // view all from history tab
    @Query("SELECT * FROM exercise_table")
    fun getAllExercises(): Flow<List<Exercise>>

    // get exercise by id so we can display this entry
    @Query("SELECT * FROM exercise_table WHERE id =:key LIMIT 1")
    fun getExerciseById(key: Long): Flow<Exercise>

    // delete from display entry tab, want to delete on a specific key or the unique id
    @Query("DELETE FROM exercise_table WHERE id = :key") //":" indicates that it is a Bind variable
    suspend fun deleteExercise(key: Long)
}
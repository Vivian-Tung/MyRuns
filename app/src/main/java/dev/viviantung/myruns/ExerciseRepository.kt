package dev.viviantung.myruns

import androidx.annotation.WorkerThread
import androidx.compose.ui.input.key.Key
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

//A Repository manages queries and allows you to use multiple backends.
// In the most common example, the Repository implements the logic for
// deciding whether to fetch data from a network or use results cached in a local database.
class ExerciseRepository(private val exerciseDatabaseDao: ExerciseDatabaseDao) {
    // functions basically calls all the functions defined in Dao

    // get all exercises
    var allExercises: Flow<List<Exercise>> = exerciseDatabaseDao.getAllExercises()

    // insert exercise
    fun insert(exercise: Exercise){
        CoroutineScope(IO).launch {
            exerciseDatabaseDao.insertExercise(exercise)
        }
    }
    
    fun delete(id: Long){
        CoroutineScope(IO).launch {
            exerciseDatabaseDao.deleteExercise(id)
        }
    }
}
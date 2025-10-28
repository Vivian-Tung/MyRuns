package dev.viviantung.myruns

import android.app.Application
import androidx.lifecycle.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

class ExerciseViewModel(private val repository: ExerciseRepository) : ViewModel() {
    // interacts with the repository, same functions

    // this is the live data
    val allExerciseLiveData: LiveData<List<Exercise>> = repository.allExercises.asLiveData()

    // insert
    fun insert(exercise: Exercise) {
        repository.insert(exercise)
    }

    // delete
    fun delete(id: Long) {
        val exerciseList = allExerciseLiveData.value
        // logical check
        if (exerciseList != null && exerciseList.isNotEmpty()) {
            repository.delete(id)
        }
    }
}

class ExerciseViewModelFactory (private val repository: ExerciseRepository) : ViewModelProvider.Factory {
    //create() creates a new instance of the modelClass
    override fun<T: ViewModel> create(modelClass: Class<T>) : T{
        if(modelClass.isAssignableFrom(ExerciseViewModel::class.java))
            return ExerciseViewModel(repository) as T
        throw kotlin.IllegalArgumentException("Unknown ViewModel class")
    }
}
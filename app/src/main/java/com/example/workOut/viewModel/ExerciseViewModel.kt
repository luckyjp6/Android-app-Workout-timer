package com.example.workOut.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.workOut.data.Exercise
import com.example.workOut.data.ExerciseDao
import com.example.workOut.data.ExerciseType
import com.example.workOut.data.Menu
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExerciseViewModel @Inject constructor(private val dao: ExerciseDao) : ViewModel() {
    val allMenus: StateFlow<List<Menu>> = dao.getAllMenus()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
//    val allExercise: StateFlow<List<Exercise>> = dao.getAllExercises()
//        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
    private val _exercises = MutableStateFlow<List<Exercise>>(emptyList())
    val exercises: StateFlow<List<Exercise>> = _exercises.asStateFlow()
    val estimatedTime = _exercises.map { e ->
        e.filter { it.type == ExerciseType.TIMER }
            .sumOf { (it.countOrTime + it.restTime) * it.numSet } +
        e.filter { it.type == ExerciseType.COUNTER }
            .sumOf { (it.restTime) * it.numSet }
    }.stateIn(viewModelScope, SharingStarted.Lazily, 0)

    var exerciseId = dao.getExerciseMaxId() + 1

    // Menu
    fun insertMenu(menu: Menu) = viewModelScope.launch {
        dao.insertMenu(menu)
    }
    fun updateMenuName(oldName: String, newName: String) = viewModelScope.launch {
        dao.updateMenuName(oldName, newName)
    }
    fun deleteMenu(menuName: String) = viewModelScope.launch {
        dao.deleteMenu(menuName)
    }
    fun isMenuNameExists(name: String, callback: (Boolean) -> Unit) = viewModelScope.launch {
        val isExists = dao.isMenuNameExists(name)
        callback(isExists)
    }
    fun updateMenuEstimatedTime(name: String) = viewModelScope.launch {
        dao.updateMenuEstimatedTime(name, estimatedTime.value)
    }

    // Exercises
    fun loadExercises(menuName: String, isNew: Boolean = false) {
        viewModelScope.launch {
            dao.getExercisesForMenu(menuName).collect { newExercises ->
                _exercises.value = newExercises
                if (isNew) {
                    // first item
                    addNewExercise(menuName)
                }
            }
        }
    }
    fun addNewExercise(menuName: String) = viewModelScope.launch {
        _exercises.value += Exercise(menu = menuName, id = exerciseId)
        exerciseId++
    }
    fun updateExercise(updatedExercise: Exercise) {
        _exercises.value = _exercises.value.map {
            if (it.id == updatedExercise.id) updatedExercise else it
        }
    }
    fun saveExercises() = viewModelScope.launch {
        _exercises.value.forEach { exercise ->
            saveExercise(exercise)
        }
        updateMenuEstimatedTime(_exercises.value[0].menu)
    }
    fun saveExercise(exercise: Exercise) = viewModelScope.launch {
        if (dao.isExerciseExist(exercise.id)) { dao.updateExercise(exercise) }
        else { dao.insertExercise(exercise) }
        updateMenuEstimatedTime(exercise.menu)
    }
    fun deleteExercise(exercise: Exercise) = viewModelScope.launch {
        if (dao.isExerciseExist(exercise.id)) { dao.deleteExercise(exercise) }
        _exercises.value = _exercises.value.filter { it.id != exercise.id }

        if (_exercises.value.isEmpty()) {
            addNewExercise(menuName = exercise.menu)
        }
    }
}

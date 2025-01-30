package com.example.workOut.data

import kotlinx.coroutines.flow.Flow
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Delete
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

// DAO interface (Data Access Object)
@Dao
interface ExerciseDao {
//    Get data
    @Query("SELECT * FROM menus")
    fun getAllMenus(): Flow<List<Menu>>

    // for debug
    @Query("SELECT * FROM exercises")
    fun getAllExercises(): Flow<List<Exercise>>

    @Query("SELECT MAX(id) FROM exercises")
    fun getExerciseMaxId(): Int

    @Query("SELECT EXISTS(SELECT 1 FROM menus WHERE name = :menuName)")
    suspend fun isMenuNameExists(menuName: String): Boolean

    @Query("SELECT * FROM exercises WHERE menu = :menu")
    fun getExercisesForMenu(menu: String): Flow<List<Exercise>>

    @Query("SELECT EXISTS(SELECT 1 FROM exercises WHERE id = :exerciseId)")
    suspend fun isExerciseExist(exerciseId: Int): Boolean

//    @Query("SELECT COALESCE(SUM(countOrTime), 0) FROM exercises WHERE menu =:menuName AND type = :exerciseType")
//    suspend fun getEstimatedTimeForMenu(menuName: String, exerciseType: ExerciseType): Int


//    Insert
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertMenu(menu: Menu)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertExercise(exercise: Exercise)


//    Update
    @Query("UPDATE menus SET name = :newName WHERE name = :oldName")
    suspend fun updateMenuName(oldName: String, newName: String)

    @Query("UPDATE menus SET estimatedTime = :estimatedTime WHERE name = :name")
    suspend fun updateMenuEstimatedTime(name: String, estimatedTime: Int)

    @Query("UPDATE exercises SET name = :newName WHERE name = :oldName AND menu = :menuName")
    suspend fun updateExerciseName(oldName: String, newName: String, menuName: String)
    @Update
    suspend fun updateExercise(exercise: Exercise)
//    suspend fun updateExercise(name: String, description: String, type: ExerciseType, countOrTime: Int, restTime: Int)


//    Delete
    @Delete
    suspend fun deleteMenu(menu: Menu)
    @Query("DELETE FROM menus WHERE name = :menuName")
    suspend fun deleteMenu(menuName: String)

    @Delete
    suspend fun deleteExercise(exercise: Exercise)
}
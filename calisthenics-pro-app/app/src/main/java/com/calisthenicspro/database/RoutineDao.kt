package com.calisthenicspro.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.calisthenicspro.models.Routine
import com.calisthenicspro.models.RoutineExercise

@Dao
interface RoutineDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRoutine(routine: Routine)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRoutineExercise(routineExercise: RoutineExercise)

    @Query("SELECT * FROM routines ORDER BY id ASC")
    suspend fun getAllRoutines(): List<Routine>

    @Query("SELECT * FROM routine_exercises WHERE routineId = :routineId ORDER BY `order` ASC")
    suspend fun getRoutineExercises(routineId: Int): List<RoutineExercise>
}

package com.aksharadeepa.tutor.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.aksharadeepa.tutor.data.local.entity.DailyGoalEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DailyGoalDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(goal: DailyGoalEntity)

    @Query("SELECT * FROM daily_goals WHERE dateKey = :dateKey")
    fun observeGoal(dateKey: String): Flow<DailyGoalEntity?>

    @Query("SELECT * FROM daily_goals WHERE dateKey = :dateKey")
    suspend fun getGoal(dateKey: String): DailyGoalEntity?
}

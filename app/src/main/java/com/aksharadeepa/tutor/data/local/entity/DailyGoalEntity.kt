package com.aksharadeepa.tutor.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "daily_goals")
data class DailyGoalEntity(
    @PrimaryKey val dateKey: String,
    val completed: Boolean = false,
    val chaptersCompletedCount: Int = 0
)

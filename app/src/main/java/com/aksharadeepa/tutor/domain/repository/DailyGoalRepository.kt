package com.aksharadeepa.tutor.domain.repository

import kotlinx.coroutines.flow.Flow

data class DailyGoalStatus(
    val dateKey: String,
    val completed: Boolean,
    val chaptersCompletedToday: Int
)

interface DailyGoalRepository {
    fun observeTodayGoal(): Flow<DailyGoalStatus>
    suspend fun recordChapterCompletionToday()
}

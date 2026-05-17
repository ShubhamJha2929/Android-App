package com.aksharadeepa.tutor.data.repository

import com.aksharadeepa.tutor.data.local.dao.DailyGoalDao
import com.aksharadeepa.tutor.data.local.entity.DailyGoalEntity
import com.aksharadeepa.tutor.domain.repository.DailyGoalRepository
import com.aksharadeepa.tutor.domain.repository.DailyGoalStatus
import com.aksharadeepa.tutor.util.DateUtils
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DailyGoalRepositoryImpl(
    private val dailyGoalDao: DailyGoalDao
) : DailyGoalRepository {

    override fun observeTodayGoal(): Flow<DailyGoalStatus> {
        val today = DateUtils.todayKey()
        return dailyGoalDao.observeGoal(today).map { entity ->
            DailyGoalStatus(
                dateKey = today,
                completed = entity?.completed == true,
                chaptersCompletedToday = entity?.chaptersCompletedCount ?: 0
            )
        }
    }

    override suspend fun recordChapterCompletionToday() {
        val today = DateUtils.todayKey()
        val existing = dailyGoalDao.getGoal(today)
        val count = (existing?.chaptersCompletedCount ?: 0) + 1
        dailyGoalDao.upsert(
            DailyGoalEntity(
                dateKey = today,
                completed = count >= 1,
                chaptersCompletedCount = count
            )
        )
    }
}

package com.aksharadeepa.tutor.data.repository

import com.aksharadeepa.tutor.data.local.dao.ChapterDao
import com.aksharadeepa.tutor.data.local.dao.SubjectDao
import com.aksharadeepa.tutor.domain.model.ChapterItem
import com.aksharadeepa.tutor.domain.model.SubjectProgress
import com.aksharadeepa.tutor.domain.repository.DailyGoalRepository
import com.aksharadeepa.tutor.domain.repository.SyllabusRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SyllabusRepositoryImpl(
    private val subjectDao: SubjectDao,
    private val chapterDao: ChapterDao,
    private val dailyGoalRepository: DailyGoalRepository
) : SyllabusRepository {

    override fun observeSubjectProgress(): Flow<List<SubjectProgress>> =
        subjectDao.observeSubjectsWithChapters().map { list ->
            list.map { item ->
                val completed = item.chapters.count { it.isCompleted }
                SubjectProgress(
                    subjectId = item.subject.id,
                    name = item.subject.name,
                    colorHex = item.subject.colorHex,
                    completedChapters = completed,
                    totalChapters = item.chapters.size
                )
            }
        }

    override fun observeChapters(subjectId: Long): Flow<List<ChapterItem>> =
        chapterDao.observeChaptersBySubject(subjectId).map { chapters ->
            chapters.map {
                ChapterItem(
                    id = it.id,
                    subjectId = it.subjectId,
                    title = it.title,
                    isCompleted = it.isCompleted,
                    orderIndex = it.orderIndex
                )
            }
        }

    override suspend fun toggleChapterCompletion(chapterId: Long, completed: Boolean) {
        chapterDao.setChapterCompleted(chapterId, completed)
        if (completed) {
            dailyGoalRepository.recordChapterCompletionToday()
        }
    }

    override suspend fun markChapterStudied(chapterId: Long) {
        chapterDao.setChapterCompleted(chapterId, true)
        dailyGoalRepository.recordChapterCompletionToday()
    }
}

package com.aksharadeepa.tutor.domain.repository

import com.aksharadeepa.tutor.domain.model.ChapterItem
import com.aksharadeepa.tutor.domain.model.SubjectProgress
import kotlinx.coroutines.flow.Flow

interface SyllabusRepository {
    fun observeSubjectProgress(): Flow<List<SubjectProgress>>
    fun observeChapters(subjectId: Long): Flow<List<ChapterItem>>
    suspend fun toggleChapterCompletion(chapterId: Long, completed: Boolean)
    suspend fun markChapterStudied(chapterId: Long)
}

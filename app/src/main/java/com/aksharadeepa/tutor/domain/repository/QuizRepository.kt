package com.aksharadeepa.tutor.domain.repository

import com.aksharadeepa.tutor.domain.model.QuizQuestion
import com.aksharadeepa.tutor.domain.model.QuizReviewItem

interface QuizRepository {
    suspend fun getQuizQuestions(chapterId: Long): List<QuizQuestion>
    suspend fun submitQuiz(
        chapterId: Long,
        subjectId: Long,
        answers: Map<Long, Int>,
        timeTakenSeconds: Int
    ): Long
    suspend fun getReviewItems(attemptId: Long): List<QuizReviewItem>
}

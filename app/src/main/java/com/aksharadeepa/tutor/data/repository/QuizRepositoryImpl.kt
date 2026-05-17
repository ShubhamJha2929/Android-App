package com.aksharadeepa.tutor.data.repository

import com.aksharadeepa.tutor.data.local.dao.ChapterDao
import com.aksharadeepa.tutor.data.local.dao.QuestionDao
import com.aksharadeepa.tutor.data.local.dao.QuizDao
import com.aksharadeepa.tutor.data.local.dao.SubjectScoreDao
import com.aksharadeepa.tutor.data.local.entity.QuizAnswerEntity
import com.aksharadeepa.tutor.data.local.entity.QuizAttemptEntity
import com.aksharadeepa.tutor.data.local.entity.SubjectScoreEntity
import com.aksharadeepa.tutor.domain.model.QuizQuestion
import com.aksharadeepa.tutor.domain.model.QuizReviewItem
import com.aksharadeepa.tutor.domain.repository.DailyGoalRepository
import com.aksharadeepa.tutor.domain.repository.QuizRepository
import kotlin.math.roundToInt

class QuizRepositoryImpl(
    private val questionDao: QuestionDao,
    private val quizDao: QuizDao,
    private val subjectScoreDao: SubjectScoreDao,
    private val chapterDao: ChapterDao,
    private val dailyGoalRepository: DailyGoalRepository
) : QuizRepository {

    override suspend fun getQuizQuestions(chapterId: Long): List<QuizQuestion> =
        questionDao.getQuestionsForChapter(chapterId, limit = 5).map { q ->
            QuizQuestion(
                id = q.id,
                questionText = q.questionText,
                options = listOf(q.optionA, q.optionB, q.optionC, q.optionD),
                correctOptionIndex = q.correctOptionIndex
            )
        }

    override suspend fun submitQuiz(
        chapterId: Long,
        subjectId: Long,
        answers: Map<Long, Int>,
        timeTakenSeconds: Int
    ): Long {
        val questions = questionDao.getQuestionsForChapter(chapterId, limit = 5)
        val answerEntities = questions.map { q ->
            val selected = answers[q.id] ?: -1
            QuizAnswerEntity(
                attemptId = 0,
                questionId = q.id,
                selectedOptionIndex = selected,
                isCorrect = selected == q.correctOptionIndex
            )
        }
        val correctCount = answerEntities.count { it.isCorrect }
        val scorePercent = if (questions.isEmpty()) 0f
        else (correctCount * 100f) / questions.size

        val attemptId = quizDao.insertAttempt(
            QuizAttemptEntity(
                chapterId = chapterId,
                subjectId = subjectId,
                scorePercent = scorePercent,
                timeTakenSeconds = timeTakenSeconds
            )
        )
        quizDao.insertAnswers(answerEntities.map { it.copy(attemptId = attemptId) })
        updateStrengthScore(subjectId, correctCount, questions.size)
        if (scorePercent >= 60f) {
            chapterDao.setChapterCompleted(chapterId, true)
            dailyGoalRepository.recordChapterCompletionToday()
        }
        return attemptId
    }

    private suspend fun updateStrengthScore(subjectId: Long, correct: Int, total: Int) {
        val existing = subjectScoreDao.getScore(subjectId)
        val newAnswered = (existing?.totalAnswered ?: 0) + total
        val newCorrect = (existing?.totalCorrect ?: 0) + correct
        val mastery = if (newAnswered == 0) 0f
        else ((newCorrect * 100f) / newAnswered).roundToInt().toFloat()

        subjectScoreDao.upsert(
            SubjectScoreEntity(
                subjectId = subjectId,
                masteryScore = mastery,
                quizzesTaken = (existing?.quizzesTaken ?: 0) + 1,
                totalCorrect = newCorrect,
                totalAnswered = newAnswered
            )
        )
    }

    override suspend fun getReviewItems(attemptId: Long): List<QuizReviewItem> {
        return quizDao.getAnswersForAttempt(attemptId).mapNotNull { answer ->
            val q = questionDao.getQuestionById(answer.questionId) ?: return@mapNotNull null
            QuizReviewItem(
                questionText = q.questionText,
                options = listOf(q.optionA, q.optionB, q.optionC, q.optionD),
                selectedIndex = answer.selectedOptionIndex,
                correctIndex = q.correctOptionIndex,
                isCorrect = answer.isCorrect
            )
        }
    }
}

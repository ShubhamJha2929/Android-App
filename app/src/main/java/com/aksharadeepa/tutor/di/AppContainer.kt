package com.aksharadeepa.tutor.di

import android.content.Context
import com.aksharadeepa.tutor.data.local.AppDatabase
import com.aksharadeepa.tutor.data.local.DatabaseSeeder
import com.aksharadeepa.tutor.data.repository.DailyGoalRepositoryImpl
import com.aksharadeepa.tutor.data.repository.QuizRepositoryImpl
import com.aksharadeepa.tutor.data.repository.StrengthRepositoryImpl
import com.aksharadeepa.tutor.data.repository.SyllabusRepositoryImpl
import com.aksharadeepa.tutor.domain.repository.DailyGoalRepository
import com.aksharadeepa.tutor.domain.repository.QuizRepository
import com.aksharadeepa.tutor.domain.repository.StrengthRepository
import com.aksharadeepa.tutor.domain.repository.SyllabusRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class AppContainer(context: Context) {
    private val database = AppDatabase.getInstance(context)
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    val dailyGoalRepository: DailyGoalRepository = DailyGoalRepositoryImpl(
        database.dailyGoalDao()
    )

    val syllabusRepository: SyllabusRepository = SyllabusRepositoryImpl(
        database.subjectDao(),
        database.chapterDao(),
        dailyGoalRepository
    )

    val quizRepository: QuizRepository = QuizRepositoryImpl(
        database.questionDao(),
        database.quizDao(),
        database.subjectScoreDao(),
        database.chapterDao(),
        dailyGoalRepository
    )

    val strengthRepository: StrengthRepository = StrengthRepositoryImpl(
        database.subjectScoreDao(),
        database.subjectDao()
    )

    init {
        scope.launch { seedIfNeeded() }
    }

    private suspend fun seedIfNeeded() {
        if (database.questionDao().getQuestionCount() > 0) return
        val chapters = DatabaseSeeder.buildChapters()
        database.subjectDao().insertAll(DatabaseSeeder.subjects)
        database.chapterDao().insertAll(chapters)
        database.questionDao().insertAll(DatabaseSeeder.buildQuestions(chapters))
        database.subjectScoreDao().insertAll(DatabaseSeeder.buildInitialScores())
    }
}

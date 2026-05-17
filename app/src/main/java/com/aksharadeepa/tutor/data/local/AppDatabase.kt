package com.aksharadeepa.tutor.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.aksharadeepa.tutor.data.local.dao.ChapterDao
import com.aksharadeepa.tutor.data.local.dao.DailyGoalDao
import com.aksharadeepa.tutor.data.local.dao.QuestionDao
import com.aksharadeepa.tutor.data.local.dao.QuizDao
import com.aksharadeepa.tutor.data.local.dao.SubjectDao
import com.aksharadeepa.tutor.data.local.dao.SubjectScoreDao
import com.aksharadeepa.tutor.data.local.entity.ChapterEntity
import com.aksharadeepa.tutor.data.local.entity.DailyGoalEntity
import com.aksharadeepa.tutor.data.local.entity.QuestionEntity
import com.aksharadeepa.tutor.data.local.entity.QuizAnswerEntity
import com.aksharadeepa.tutor.data.local.entity.QuizAttemptEntity
import com.aksharadeepa.tutor.data.local.entity.SubjectEntity
import com.aksharadeepa.tutor.data.local.entity.SubjectScoreEntity

@Database(
    entities = [
        SubjectEntity::class,
        ChapterEntity::class,
        QuestionEntity::class,
        SubjectScoreEntity::class,
        QuizAttemptEntity::class,
        QuizAnswerEntity::class,
        DailyGoalEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun subjectDao(): SubjectDao
    abstract fun chapterDao(): ChapterDao
    abstract fun questionDao(): QuestionDao
    abstract fun subjectScoreDao(): SubjectScoreDao
    abstract fun quizDao(): QuizDao
    abstract fun dailyGoalDao(): DailyGoalDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "akshara_deepa.db"
                ).build().also { INSTANCE = it }
            }
        }
    }
}

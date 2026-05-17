package com.aksharadeepa.tutor.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.aksharadeepa.tutor.data.local.entity.QuizAnswerEntity
import com.aksharadeepa.tutor.data.local.entity.QuizAttemptEntity

@Dao
interface QuizDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAttempt(attempt: QuizAttemptEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAnswers(answers: List<QuizAnswerEntity>)

    @Query("SELECT * FROM quiz_attempts WHERE id = :attemptId")
    suspend fun getAttempt(attemptId: Long): QuizAttemptEntity?

    @Query("SELECT * FROM quiz_answers WHERE attemptId = :attemptId ORDER BY id")
    suspend fun getAnswersForAttempt(attemptId: Long): List<QuizAnswerEntity>
}

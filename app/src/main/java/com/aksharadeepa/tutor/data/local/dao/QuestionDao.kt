package com.aksharadeepa.tutor.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.aksharadeepa.tutor.data.local.entity.QuestionEntity

@Dao
interface QuestionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(questions: List<QuestionEntity>)

    @Query(
        """
        SELECT * FROM questions 
        WHERE chapterId = :chapterId 
        ORDER BY id 
        LIMIT :limit
        """
    )
    suspend fun getQuestionsForChapter(chapterId: Long, limit: Int = 5): List<QuestionEntity>

    @Query("SELECT COUNT(*) FROM questions")
    suspend fun getQuestionCount(): Int

    @Query("SELECT * FROM questions WHERE id = :questionId")
    suspend fun getQuestionById(questionId: Long): QuestionEntity?
}

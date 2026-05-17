package com.aksharadeepa.tutor.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.aksharadeepa.tutor.data.local.entity.SubjectScoreEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SubjectScoreDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(scores: List<SubjectScoreEntity>)

    @Query("SELECT * FROM subject_scores ORDER BY subjectId")
    fun observeScores(): Flow<List<SubjectScoreEntity>>

    @Query("SELECT * FROM subject_scores WHERE subjectId = :subjectId")
    suspend fun getScore(subjectId: Long): SubjectScoreEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(score: SubjectScoreEntity)
}

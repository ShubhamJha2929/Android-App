package com.aksharadeepa.tutor.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.aksharadeepa.tutor.data.local.entity.SubjectEntity
import com.aksharadeepa.tutor.data.local.model.SubjectWithChapters
import kotlinx.coroutines.flow.Flow

@Dao
interface SubjectDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(subjects: List<SubjectEntity>)

    @Transaction
    @Query("SELECT * FROM subjects ORDER BY id")
    fun observeSubjectsWithChapters(): Flow<List<SubjectWithChapters>>

    @Query("SELECT * FROM subjects WHERE id = :subjectId")
    suspend fun getSubject(subjectId: Long): SubjectEntity?
}

package com.aksharadeepa.tutor.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.aksharadeepa.tutor.data.local.entity.ChapterEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ChapterDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(chapters: List<ChapterEntity>)

    @Query("SELECT * FROM chapters WHERE subjectId = :subjectId ORDER BY orderIndex")
    fun observeChaptersBySubject(subjectId: Long): Flow<List<ChapterEntity>>

    @Query("SELECT * FROM chapters WHERE id = :chapterId")
    suspend fun getChapter(chapterId: Long): ChapterEntity?

    @Query("UPDATE chapters SET isCompleted = :completed WHERE id = :chapterId")
    suspend fun setChapterCompleted(chapterId: Long, completed: Boolean)

    @Query(
        """
        SELECT COUNT(*) FROM chapters 
        WHERE subjectId = :subjectId AND isCompleted = 1
        """
    )
    fun observeCompletedCount(subjectId: Long): Flow<Int>

    @Query("SELECT COUNT(*) FROM chapters WHERE subjectId = :subjectId")
    suspend fun getTotalCount(subjectId: Long): Int

    @Query("SELECT COUNT(*) FROM chapters WHERE subjectId = :subjectId AND isCompleted = 1")
    suspend fun getCompletedCount(subjectId: Long): Int
}

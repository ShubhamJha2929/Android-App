package com.aksharadeepa.tutor.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "subject_scores",
    foreignKeys = [
        ForeignKey(
            entity = SubjectEntity::class,
            parentColumns = ["id"],
            childColumns = ["subjectId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class SubjectScoreEntity(
    @PrimaryKey val subjectId: Long,
    val masteryScore: Float,
    val quizzesTaken: Int = 0,
    val totalCorrect: Int = 0,
    val totalAnswered: Int = 0
)

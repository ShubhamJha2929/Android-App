package com.aksharadeepa.tutor.domain.model

data class ChapterItem(
    val id: Long,
    val subjectId: Long,
    val title: String,
    val isCompleted: Boolean,
    val orderIndex: Int
)

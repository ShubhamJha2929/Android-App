package com.aksharadeepa.tutor.domain.model

data class SubjectProgress(
    val subjectId: Long,
    val name: String,
    val colorHex: String,
    val completedChapters: Int,
    val totalChapters: Int
) {
    val progressFraction: Float
        get() = if (totalChapters == 0) 0f else completedChapters.toFloat() / totalChapters
}

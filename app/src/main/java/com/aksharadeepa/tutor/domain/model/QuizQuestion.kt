package com.aksharadeepa.tutor.domain.model

data class QuizQuestion(
    val id: Long,
    val questionText: String,
    val options: List<String>,
    val correctOptionIndex: Int
)

package com.aksharadeepa.tutor.domain.model

data class QuizReviewItem(
    val questionText: String,
    val options: List<String>,
    val selectedIndex: Int,
    val correctIndex: Int,
    val isCorrect: Boolean
)

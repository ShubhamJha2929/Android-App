package com.aksharadeepa.tutor.domain.model

data class StrengthData(
    val subjectName: String,
    val masteryScore: Float,
    val colorHex: String
)

data class GapArea(
    val subjectName: String,
    val masteryScore: Float,
    val recommendation: String
)

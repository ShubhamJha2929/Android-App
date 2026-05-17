package com.aksharadeepa.tutor.ui.navigation

object NavRoutes {
    const val SPLASH = "splash"
    const val DASHBOARD = "dashboard"
    const val SYLLABUS = "syllabus/{subjectId}"
    const val QUIZ_LIST = "quiz_list"
    const val QUIZ = "quiz/{chapterId}/{subjectId}"
    const val STRENGTH = "strength"
    const val GAP_ANALYSIS = "gap_analysis"

    fun syllabus(subjectId: Long) = "syllabus/$subjectId"
    fun quiz(chapterId: Long, subjectId: Long) = "quiz/$chapterId/$subjectId"
}

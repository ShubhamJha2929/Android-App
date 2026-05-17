package com.aksharadeepa.tutor.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.aksharadeepa.tutor.domain.model.ChapterItem
import com.aksharadeepa.tutor.domain.repository.SyllabusRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

data class QuizChapterItem(
    val chapter: ChapterItem,
    val subjectName: String
)

class QuizListViewModel(
    syllabusRepository: SyllabusRepository
) : ViewModel() {

    val quizChapters: StateFlow<List<QuizChapterItem>> = combine(
        syllabusRepository.observeChapters(1L),
        syllabusRepository.observeChapters(2L),
        syllabusRepository.observeChapters(3L),
        syllabusRepository.observeSubjectProgress()
    ) { science, math, social, subjects ->
        val nameMap = subjects.associate { it.subjectId to it.name }
        science.map { QuizChapterItem(it, nameMap[it.subjectId] ?: "Science") } +
            math.map { QuizChapterItem(it, nameMap[it.subjectId] ?: "Math") } +
            social.map { QuizChapterItem(it, nameMap[it.subjectId] ?: "Social Studies") }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    class Factory(
        private val syllabusRepository: SyllabusRepository
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            QuizListViewModel(syllabusRepository) as T
    }
}

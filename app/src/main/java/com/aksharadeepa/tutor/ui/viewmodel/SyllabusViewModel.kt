package com.aksharadeepa.tutor.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.aksharadeepa.tutor.domain.model.ChapterItem
import com.aksharadeepa.tutor.domain.model.SubjectProgress
import com.aksharadeepa.tutor.domain.repository.SyllabusRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SyllabusViewModel(
    private val syllabusRepository: SyllabusRepository,
    private val subjectId: Long
) : ViewModel() {

    val chapters: StateFlow<List<ChapterItem>> = syllabusRepository
        .observeChapters(subjectId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    val subjectProgress: StateFlow<SubjectProgress?> = syllabusRepository
        .observeSubjectProgress()
        .map { list -> list.find { it.subjectId == subjectId } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)

    fun toggleChapter(chapterId: Long, completed: Boolean) {
        viewModelScope.launch {
            syllabusRepository.toggleChapterCompletion(chapterId, completed)
        }
    }

    class Factory(
        private val syllabusRepository: SyllabusRepository,
        private val subjectId: Long
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            SyllabusViewModel(syllabusRepository, subjectId) as T
    }
}

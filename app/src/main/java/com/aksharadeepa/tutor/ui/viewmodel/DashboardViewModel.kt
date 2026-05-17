package com.aksharadeepa.tutor.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.aksharadeepa.tutor.domain.model.SubjectProgress
import com.aksharadeepa.tutor.domain.repository.DailyGoalRepository
import com.aksharadeepa.tutor.domain.repository.DailyGoalStatus
import com.aksharadeepa.tutor.domain.repository.SyllabusRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

data class DashboardUiState(
    val subjects: List<SubjectProgress> = emptyList(),
    val dailyGoal: DailyGoalStatus? = null,
    val isLoading: Boolean = true
)

class DashboardViewModel(
    syllabusRepository: SyllabusRepository,
    dailyGoalRepository: DailyGoalRepository
) : ViewModel() {

    val subjects: StateFlow<List<SubjectProgress>> = syllabusRepository
        .observeSubjectProgress()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    val dailyGoal: StateFlow<DailyGoalStatus?> = dailyGoalRepository
        .observeTodayGoal()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)

    class Factory(
        private val syllabusRepository: SyllabusRepository,
        private val dailyGoalRepository: DailyGoalRepository
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            DashboardViewModel(syllabusRepository, dailyGoalRepository) as T
    }
}

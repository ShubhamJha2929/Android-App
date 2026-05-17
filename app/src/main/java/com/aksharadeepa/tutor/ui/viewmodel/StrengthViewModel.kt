package com.aksharadeepa.tutor.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.aksharadeepa.tutor.domain.model.GapArea
import com.aksharadeepa.tutor.domain.model.StrengthData
import com.aksharadeepa.tutor.domain.repository.StrengthRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class StrengthViewModel(
    strengthRepository: StrengthRepository
) : ViewModel() {

    val strengthData: StateFlow<List<StrengthData>> = strengthRepository
        .observeStrengthMap()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    val gapAreas: StateFlow<List<GapArea>> = strengthRepository
        .observeGapAreas(threshold = 60f)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    class Factory(
        private val strengthRepository: StrengthRepository
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            StrengthViewModel(strengthRepository) as T
    }
}

package com.aksharadeepa.tutor.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.aksharadeepa.tutor.domain.model.QuizQuestion
import com.aksharadeepa.tutor.domain.model.QuizReviewItem
import com.aksharadeepa.tutor.domain.repository.QuizRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class QuizUiState(
    val questions: List<QuizQuestion> = emptyList(),
    val currentIndex: Int = 0,
    val selectedAnswers: Map<Long, Int> = emptyMap(),
    val secondsRemaining: Int = QUIZ_DURATION_SECONDS,
    val isFinished: Boolean = false,
    val attemptId: Long? = null,
    val reviewItems: List<QuizReviewItem> = emptyList(),
    val isLoading: Boolean = true
) {
    val currentQuestion: QuizQuestion?
        get() = questions.getOrNull(currentIndex)

    companion object {
        const val QUIZ_DURATION_SECONDS = 300
    }
}

class QuizViewModel(
    private val quizRepository: QuizRepository,
    private val chapterId: Long,
    private val subjectId: Long
) : ViewModel() {

    private val _uiState = MutableStateFlow(QuizUiState())
    val uiState: StateFlow<QuizUiState> = _uiState.asStateFlow()

    private var timerJob: Job? = null
    private var elapsedSeconds = 0

    init {
        viewModelScope.launch {
            val questions = quizRepository.getQuizQuestions(chapterId)
            _uiState.update {
                it.copy(questions = questions, isLoading = false, secondsRemaining = QuizUiState.QUIZ_DURATION_SECONDS)
            }
            startTimer()
        }
    }

    private fun startTimer() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (_uiState.value.secondsRemaining > 0 && !_uiState.value.isFinished) {
                delay(1_000)
                elapsedSeconds++
                _uiState.update { it.copy(secondsRemaining = it.secondsRemaining - 1) }
            }
            if (!_uiState.value.isFinished) {
                finishQuiz()
            }
        }
    }

    fun selectAnswer(optionIndex: Int) {
        val question = _uiState.value.currentQuestion ?: return
        _uiState.update {
            it.copy(selectedAnswers = it.selectedAnswers + (question.id to optionIndex))
        }
    }

    fun nextQuestion() {
        val state = _uiState.value
        if (state.currentIndex < state.questions.lastIndex) {
            _uiState.update { it.copy(currentIndex = it.currentIndex + 1) }
        } else {
            finishQuiz()
        }
    }

    fun previousQuestion() {
        if (_uiState.value.currentIndex > 0) {
            _uiState.update { it.copy(currentIndex = it.currentIndex - 1) }
        }
    }

    fun finishQuiz() {
        if (_uiState.value.isFinished) return
        timerJob?.cancel()
        viewModelScope.launch {
            val state = _uiState.value
            val timeTaken = QuizUiState.QUIZ_DURATION_SECONDS - state.secondsRemaining
            val attemptId = quizRepository.submitQuiz(
                chapterId = chapterId,
                subjectId = subjectId,
                answers = state.selectedAnswers,
                timeTakenSeconds = timeTaken.coerceAtLeast(1)
            )
            val review = quizRepository.getReviewItems(attemptId)
            _uiState.update {
                it.copy(isFinished = true, attemptId = attemptId, reviewItems = review)
            }
        }
    }

    override fun onCleared() {
        timerJob?.cancel()
        super.onCleared()
    }

    class Factory(
        private val quizRepository: QuizRepository,
        private val chapterId: Long,
        private val subjectId: Long
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            QuizViewModel(quizRepository, chapterId, subjectId) as T
    }
}

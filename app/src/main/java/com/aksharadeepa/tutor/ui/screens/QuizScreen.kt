package com.aksharadeepa.tutor.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.aksharadeepa.tutor.domain.model.QuizReviewItem
import com.aksharadeepa.tutor.ui.components.QuizTimerDisplay
import com.aksharadeepa.tutor.ui.theme.ErrorRed
import com.aksharadeepa.tutor.ui.theme.GreenLight
import com.aksharadeepa.tutor.ui.viewmodel.QuizViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizScreen(
    viewModel: QuizViewModel,
    onBack: () -> Unit,
    onQuizFinished: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (state.isFinished) "Review Answers" else "Quiz") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        when {
            state.isLoading -> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            state.isFinished -> {
                ReviewAnswersSection(
                    items = state.reviewItems,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .verticalScroll(rememberScrollState()),
                    onDone = onQuizFinished
                )
            }

            else -> {
                val question = state.currentQuestion
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "Q ${state.currentIndex + 1} / ${state.questions.size}",
                            style = MaterialTheme.typography.titleMedium
                        )
                        QuizTimerDisplay(state.secondsRemaining)
                    }

                    LinearProgressIndicator(
                        progress = {
                            (state.currentIndex + 1).toFloat() / state.questions.size.coerceAtLeast(1)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp)
                    )

                    question?.let { q ->
                        Text(q.questionText, style = MaterialTheme.typography.titleLarge)

                        val selected = state.selectedAnswers[q.id]
                        q.options.forEachIndexed { index, option ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .selectable(
                                        selected = selected == index,
                                        onClick = { viewModel.selectAnswer(index) },
                                        role = Role.RadioButton
                                    )
                                    .padding(vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(
                                    selected = selected == index,
                                    onClick = { viewModel.selectAnswer(index) }
                                )
                                Text(option, modifier = Modifier.padding(start = 8.dp))
                            }
                        }
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Button(
                            onClick = { viewModel.previousQuestion() },
                            enabled = state.currentIndex > 0
                        ) { Text("Previous") }

                        Button(
                            onClick = {
                                if (state.currentIndex == state.questions.lastIndex) {
                                    viewModel.finishQuiz()
                                } else {
                                    viewModel.nextQuestion()
                                }
                            }
                        ) {
                            Text(if (state.currentIndex == state.questions.lastIndex) "Submit" else "Next")
                        }
                    }

                    Button(
                        onClick = { viewModel.finishQuiz() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp)
                    ) {
                        Text("Finish Early")
                    }
                }
            }
        }
    }
}

@Composable
private fun ReviewAnswersSection(
    items: List<QuizReviewItem>,
    modifier: Modifier = Modifier,
    onDone: () -> Unit
) {
    Column(
        modifier = modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Review Answers", style = MaterialTheme.typography.headlineSmall)
        val correct = items.count { it.isCorrect }
        Text("Score: $correct / ${items.size}")

        items.forEachIndexed { index, item ->
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = if (item.isCorrect)
                        GreenLight.copy(alpha = 0.2f) else ErrorRed.copy(alpha = 0.1f)
                )
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text("Q${index + 1}: ${item.questionText}", style = MaterialTheme.typography.titleSmall)
                    item.options.forEachIndexed { optIndex, opt ->
                        val prefix = when {
                            optIndex == item.correctIndex -> "✓ "
                            optIndex == item.selectedIndex && !item.isCorrect -> "✗ "
                            else -> "  "
                        }
                        Text("$prefix$opt", style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
        }

        Button(onClick = onDone, modifier = Modifier.fillMaxWidth()) {
            Text("Back to Dashboard")
        }
    }
}

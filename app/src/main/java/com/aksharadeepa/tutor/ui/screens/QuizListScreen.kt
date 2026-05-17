package com.aksharadeepa.tutor.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.aksharadeepa.tutor.ui.viewmodel.QuizListViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizListScreen(
    viewModel: QuizListViewModel,
    onBack: () -> Unit,
    onStartQuiz: (chapterId: Long, subjectId: Long) -> Unit
) {
    val chapters by viewModel.quizChapters.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Quiz Mode") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            items(chapters, key = { it.chapter.id }) { item ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 6.dp)
                        .clickable {
                            onStartQuiz(item.chapter.id, item.chapter.subjectId)
                        }
                ) {
                    Text(
                        text = "${item.subjectName} — ${item.chapter.title}",
                        style = MaterialTheme.typography.titleSmall,
                        modifier = Modifier.padding(16.dp)
                    )
                    Text(
                        text = "5 questions • Timed mock exam",
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 12.dp)
                    )
                }
            }
        }
    }
}

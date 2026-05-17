package com.aksharadeepa.tutor.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.aksharadeepa.tutor.ui.components.SubjectProgressBar
import com.aksharadeepa.tutor.ui.viewmodel.SyllabusViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SyllabusScreen(
    viewModel: SyllabusViewModel,
    onBack: () -> Unit,
    onStartQuiz: (chapterId: Long, subjectId: Long) -> Unit
) {
    val chapters by viewModel.chapters.collectAsState()
    val progress by viewModel.subjectProgress.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(progress?.name ?: "Syllabus") },
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
                .padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                progress?.let {
                    SubjectProgressBar(
                        label = it.name,
                        progress = it.progressFraction,
                        completed = it.completedChapters,
                        total = it.totalChapters
                    )
                }
            }

            items(chapters, key = { it.id }) { chapter ->
                Card(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = chapter.isCompleted,
                            onCheckedChange = { checked ->
                                viewModel.toggleChapter(chapter.id, checked)
                            }
                        )
                        Column(modifier = Modifier.weight(1f)) {
                            Text(chapter.title, style = MaterialTheme.typography.titleSmall)
                            Text(
                                "Chapter ${chapter.orderIndex + 1}",
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                        Icon(
                            imageVector = if (chapter.isCompleted) Icons.Default.CheckCircle
                            else Icons.Default.RadioButtonUnchecked,
                            contentDescription = null
                        )
                        TextButton(onClick = { onStartQuiz(chapter.id, chapter.subjectId) }) {
                            Text("Quiz")
                        }
                    }
                }
            }
        }
    }
}

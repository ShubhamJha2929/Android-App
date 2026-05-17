package com.aksharadeepa.tutor.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.aksharadeepa.tutor.ui.components.DashboardNavCard
import com.aksharadeepa.tutor.ui.components.SubjectProgressBar
import com.aksharadeepa.tutor.ui.theme.AmberAccent
import com.aksharadeepa.tutor.ui.theme.GreenPrimary
import com.aksharadeepa.tutor.ui.viewmodel.DashboardViewModel
import androidx.core.graphics.toColorInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    viewModel: DashboardViewModel,
    onNavigateSyllabus: (Long) -> Unit,
    onNavigateQuizList: () -> Unit,
    onNavigateStrength: () -> Unit,
    onNavigateGapAnalysis: () -> Unit
) {
    val subjects by viewModel.subjects.collectAsState()
    val dailyGoal by viewModel.dailyGoal.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mission Map") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = GreenPrimary,
                    titleContentColor = Color.White
                )
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = if (dailyGoal?.completed == true)
                            Color(0xFFE8F5E9) else Color(0xFFFFF8E1)
                    )
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Icon(Icons.Default.Flag, contentDescription = null)
                        Text(
                            text = if (dailyGoal?.completed == true)
                                "Daily goal achieved!" else "Daily Goal: Complete 1 topic today",
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                        Text(
                            text = "Chapters done today: ${dailyGoal?.chaptersCompletedToday ?: 0}",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }

            item {
                Text("Progress Velocity", style = MaterialTheme.typography.titleLarge)
            }

            items(subjects) { subject ->
                val color = try {
                    Color(subject.colorHex.toColorInt())
                } catch (_: Exception) {
                    GreenPrimary
                }
                SubjectProgressBar(
                    label = subject.name,
                    progress = subject.progressFraction,
                    completed = subject.completedChapters,
                    total = subject.totalChapters,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                )
            }

            item {
                Text("Navigate", style = MaterialTheme.typography.titleLarge, modifier = Modifier.padding(top = 8.dp))
            }

            item {
                DashboardNavCard(
                    title = "Syllabus Tracker",
                    subtitle = "Science, Math & Social — chapter checklist",
                    containerColor = Color(0xFF2E7D32),
                    onClick = { subjects.firstOrNull()?.let { onNavigateSyllabus(it.subjectId) } ?: onNavigateSyllabus(1L) }
                )
            }

            items(subjects) { subject ->
                val color = try {
                    Color(subject.colorHex.toColorInt())
                } catch (_: Exception) {
                    GreenPrimary
                }
                DashboardNavCard(
                    title = subject.name,
                    subtitle = "${subject.completedChapters}/${subject.totalChapters} chapters done",
                    containerColor = color,
                    onClick = { onNavigateSyllabus(subject.subjectId) }
                )
            }

            item {
                DashboardNavCard(
                    title = "Quiz Mode",
                    subtitle = "5 questions per chapter — timed mock exam",
                    containerColor = Color(0xFF1565C0),
                    onClick = onNavigateQuizList
                )
            }

            item {
                DashboardNavCard(
                    title = "Strength Map",
                    subtitle = "Spider web chart of subject mastery",
                    containerColor = Color(0xFF6A1B9A),
                    onClick = onNavigateStrength
                )
            }

            item {
                DashboardNavCard(
                    title = "Gap Analysis",
                    subtitle = "Weak subjects that need attention",
                    containerColor = AmberAccent,
                    onClick = onNavigateGapAnalysis
                )
            }
        }
    }
}

package com.aksharadeepa.tutor.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import com.aksharadeepa.tutor.ui.theme.ErrorRed
import com.aksharadeepa.tutor.ui.viewmodel.StrengthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GapAnalysisScreen(
    viewModel: StrengthViewModel,
    onBack: () -> Unit
) {
    val gaps by viewModel.gapAreas.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Gap Analysis") },
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
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Text(
                    "Subjects below 60% mastery need more attention (Progress Velocity gaps).",
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            if (gaps.isEmpty()) {
                item {
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer
                        )
                    ) {
                        Text(
                            "No major gaps detected. Keep your daily goal streak!",
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
            }

            items(gaps) { gap ->
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = ErrorRed.copy(alpha = 0.08f)
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(gap.subjectName, style = MaterialTheme.typography.titleMedium)
                        Text(
                            "Mastery: ${gap.masteryScore.toInt()}%",
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Text(
                            gap.recommendation,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }
                }
            }
        }
    }
}

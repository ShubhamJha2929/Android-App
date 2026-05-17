package com.aksharadeepa.tutor.ui.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun QuizTimerDisplay(secondsRemaining: Int, modifier: Modifier = Modifier) {
    val minutes = secondsRemaining / 60
    val seconds = secondsRemaining % 60
    val formatted = "%02d:%02d".format(minutes, seconds)
    Text(
        text = "Time: $formatted",
        style = MaterialTheme.typography.titleMedium,
        color = if (secondsRemaining < 60) MaterialTheme.colorScheme.error
        else MaterialTheme.colorScheme.primary,
        modifier = modifier
    )
}

package com.aksharadeepa.tutor.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.aksharadeepa.tutor.domain.model.StrengthData
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin

@Composable
fun StrengthRadarChart(
    data: List<StrengthData>,
    modifier: Modifier = Modifier
) {
    if (data.isEmpty()) {
        Text("Take quizzes to build your Strength Map", modifier = modifier.padding(16.dp))
        return
    }

    val primaryColor = MaterialTheme.colorScheme.primary

    Column(modifier = modifier) {
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .padding(16.dp)
        ) {
            val center = Offset(size.width / 2f, size.height / 2f)
            val radius = min(size.width, size.height) / 2f * 0.8f
            val count = data.size
            val angleStep = (2 * Math.PI / count).toFloat()

            for (ring in 1..4) {
                drawCircle(
                    color = Color.Gray.copy(alpha = 0.25f),
                    radius = radius * ring / 4f,
                    center = center,
                    style = Stroke(width = 1.5f)
                )
            }

            for (i in data.indices) {
                val angle = -Math.PI / 2 + i * angleStep
                val end = Offset(
                    center.x + radius * cos(angle).toFloat(),
                    center.y + radius * sin(angle).toFloat()
                )
                drawLine(Color.Gray.copy(alpha = 0.35f), center, end, strokeWidth = 1.5f)
            }

            val dataPath = Path()
            data.forEachIndexed { i, item ->
                val angle = -Math.PI / 2 + i * angleStep
                val valueRadius = radius * (item.masteryScore / 100f).coerceIn(0f, 1f)
                val point = Offset(
                    center.x + valueRadius * cos(angle).toFloat(),
                    center.y + valueRadius * sin(angle).toFloat()
                )
                if (i == 0) dataPath.moveTo(point.x, point.y) else dataPath.lineTo(point.x, point.y)
            }
            dataPath.close()
            drawPath(dataPath, primaryColor.copy(alpha = 0.35f))
            drawPath(dataPath, primaryColor, style = Stroke(width = 3f))
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            data.forEach { item ->
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(item.subjectName, style = MaterialTheme.typography.labelSmall)
                    Text("${item.masteryScore.toInt()}%", style = MaterialTheme.typography.titleSmall)
                }
            }
        }
    }
}

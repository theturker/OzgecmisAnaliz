package com.alperenturker.cviews.presentation.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.alperenturker.cviews.presentation.ui.theme.AppColors
import com.alperenturker.cviews.presentation.ui.theme.Spacing

/**
 * Dairesel skor göstergesi (0–max arası). Ortada skor, altında isteğe bağlı label.
 */
@Composable
fun CircularScoreIndicator(
    score: Int,
    maxScore: Int,
    modifier: Modifier = Modifier,
    size: Dp = 120.dp,
    strokeWidth: Dp = 10.dp,
    label: String? = null,
    showScoreInCenter: Boolean = true,
) {
    val progress = (score.toFloat() / maxScore).coerceIn(0f, 1f)
    val progressColor = when {
        progress >= 0.8f -> AppColors.Success
        progress >= 0.5f -> MaterialTheme.colorScheme.primary
        else -> AppColors.Warning
    }
    val trackColor = MaterialTheme.colorScheme.surfaceVariant

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(contentAlignment = Alignment.Center) {
            Canvas(modifier = Modifier.size(size)) {
                val w = size.toPx()
                val h = size.toPx()
                val stroke = strokeWidth.toPx()
                val radius = (minOf(w, h) / 2f) - stroke
                val center = Offset(w / 2f, h / 2f)
                drawCircle(
                    color = trackColor,
                    radius = radius,
                    center = center,
                    style = Stroke(width = stroke, cap = StrokeCap.Round),
                )
                drawArc(
                    color = progressColor,
                    startAngle = -90f,
                    sweepAngle = 360f * progress,
                    useCenter = false,
                    topLeft = Offset(center.x - radius, center.y - radius),
                    size = Size(radius * 2f, radius * 2f),
                    style = Stroke(width = stroke, cap = StrokeCap.Round),
                )
            }
            if (showScoreInCenter) {
                Text(
                    text = "$score",
                    style = MaterialTheme.typography.headlineLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                )
            }
        }
        if (label != null) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = Spacing.xs),
            )
        }
    }
}

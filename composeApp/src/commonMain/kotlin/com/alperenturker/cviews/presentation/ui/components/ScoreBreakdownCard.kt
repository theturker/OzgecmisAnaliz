package com.alperenturker.cviews.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.alperenturker.cviews.presentation.ui.theme.AppShape
import com.alperenturker.cviews.presentation.ui.theme.Spacing

@Composable
fun ScoreBreakdownCard(
    title: String,
    score: Int,
    maxScore: Int,
    modifier: Modifier = Modifier,
    useCircularChart: Boolean = false,
) {
    val progress = (score.toFloat() / maxScore).coerceIn(0f, 1f)
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(AppShape.medium)
            .background(MaterialTheme.colorScheme.surface)
            .padding(Spacing.md),
    ) {
        if (useCircularChart) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.weight(1f),
                )
                CircularScoreIndicator(
                    score = score,
                    maxScore = maxScore,
                    size = 48.dp,
                    strokeWidth = 4.dp,
                    showScoreInCenter = true,
                    label = null,
                )
            }
        } else {
            Text(
                text = title,
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurface,
            )
            Text(
                text = "$score / $maxScore",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary,
            )
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = Spacing.sm),
                color = MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.surfaceVariant,
            )
        }
    }
}

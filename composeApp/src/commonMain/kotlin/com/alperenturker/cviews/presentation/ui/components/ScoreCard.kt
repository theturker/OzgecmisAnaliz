package com.alperenturker.cviews.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.alperenturker.cviews.presentation.ui.theme.AppShape
import com.alperenturker.cviews.presentation.ui.theme.Spacing

@Composable
fun ScoreCard(
    score: Int,
    maxScore: Int,
    label: String,
    modifier: Modifier = Modifier,
    useCircularChart: Boolean = true,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(AppShape.large)
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(Spacing.lg),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        if (useCircularChart) {
            CircularScoreIndicator(
                score = score,
                maxScore = maxScore,
                label = label,
                size = 140.dp,
                strokeWidth = 12.dp,
            )
        } else {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                androidx.compose.material3.Text(
                    text = "$score / $maxScore",
                    style = MaterialTheme.typography.displayMedium,
                    color = MaterialTheme.colorScheme.primary,
                )
                androidx.compose.material3.Text(
                    text = label,
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}

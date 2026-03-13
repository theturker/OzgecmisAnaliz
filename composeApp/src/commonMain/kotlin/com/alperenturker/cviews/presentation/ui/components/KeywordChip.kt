package com.alperenturker.cviews.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import com.alperenturker.cviews.presentation.ui.theme.AppShape
import com.alperenturker.cviews.presentation.ui.theme.Spacing

@Composable
fun KeywordChip(
    keyword: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = keyword,
        style = MaterialTheme.typography.labelMedium,
        color = MaterialTheme.colorScheme.primary,
        modifier = modifier
            .clip(AppShape.chip)
            .background(MaterialTheme.colorScheme.primaryContainer)
            .padding(horizontal = Spacing.md, vertical = Spacing.sm),
    )
}

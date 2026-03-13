package com.alperenturker.cviews.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.alperenturker.cviews.presentation.ui.theme.AppColors
import com.alperenturker.cviews.presentation.ui.theme.AppShape
import com.alperenturker.cviews.presentation.ui.theme.Spacing

@Composable
fun StrengthWeaknessItem(
    title: String,
    explanation: String,
    isStrength: Boolean,
    modifier: Modifier = Modifier,
) {
    val icon = if (isStrength) Icons.Default.Check else Icons.Default.Warning
    val tint = if (isStrength) AppColors.Success else AppColors.Warning
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(AppShape.medium)
            .background(MaterialTheme.colorScheme.surface)
            .padding(Spacing.md),
        horizontalArrangement = Arrangement.spacedBy(Spacing.md),
        verticalAlignment = Alignment.Top,
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = tint,
            modifier = Modifier.padding(top = 2.dp),
        )
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
            )
            Text(
                text = explanation,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

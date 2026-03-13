package com.alperenturker.cviews.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Upload
import androidx.compose.material3.Icon
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
fun UploadArea(
    modifier: Modifier = Modifier,
    onUploadClick: () -> Unit,
    isDragActive: Boolean = false,
    selectedFileName: String? = null,
) {
    val borderColor = when {
        selectedFileName != null -> MaterialTheme.colorScheme.primary
        isDragActive -> MaterialTheme.colorScheme.primary
        else -> MaterialTheme.colorScheme.outline
    }
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(AppShape.large)
            .background(MaterialTheme.colorScheme.surface)
            .border(2.dp, borderColor, AppShape.large)
            .padding(Spacing.xxl),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Icon(
            imageVector = Icons.Default.Upload,
            contentDescription = null,
            modifier = Modifier
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .padding(Spacing.md),
            tint = MaterialTheme.colorScheme.primary,
        )
        Spacer(modifier = Modifier.height(Spacing.md))
        Text(
            text = "PDF dosyanızı buraya sürükleyin veya",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Spacer(modifier = Modifier.height(Spacing.xs))
        PrimaryButton(
            text = if (selectedFileName != null) "Başka dosya seç" else "Dosya Seç",
            onClick = onUploadClick,
            modifier = Modifier.fillMaxWidth(0.6f),
        )
        if (selectedFileName != null) {
            Spacer(modifier = Modifier.height(Spacing.sm))
            Text(
                text = "Seçilen: $selectedFileName",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary,
            )
        }
    }
}

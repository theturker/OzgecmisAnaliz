package com.alperenturker.cviews.presentation.upload

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import com.alperenturker.cviews.presentation.ui.components.BackButton
import com.alperenturker.cviews.presentation.ui.components.PrimaryButton
import com.alperenturker.cviews.presentation.ui.components.SectionHeader
import com.alperenturker.cviews.presentation.ui.components.UploadArea
import com.alperenturker.cviews.presentation.ui.theme.AppColors
import com.alperenturker.cviews.presentation.ui.theme.Spacing

@Composable
fun UploadScreen(
    selectedFileName: String? = null,
    onSelectFileClick: () -> Unit = {},
    onAnalyzeClick: (targetRole: String?) -> Unit,
    onBackClick: () -> Unit,
    isAnalyzing: Boolean = false,
    analysisError: String? = null,
    canAnalyzeNow: Boolean = true,
    remainingCooldownMs: Long = 0L,
    modifier: Modifier = Modifier,
) {
    var targetRole by rememberSaveable { mutableStateOf("") }
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier.fillMaxSize(),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            AppColors.Primary.copy(alpha = 0.1f),
                            MaterialTheme.colorScheme.surface,
                        ),
                    ),
                )
                .padding(Spacing.lg),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                BackButton(onClick = onBackClick)
            }
            SectionHeader(
                title = "CV Yükle",
                subtitle = "PDF dosyanızı yükleyin ve hedef rolü belirleyin; analiz bu role göre yapılır.",
            )
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(Spacing.lg),
        ) {
        Spacer(modifier = Modifier.height(Spacing.md))

        UploadArea(
            modifier = Modifier.fillMaxWidth(),
            onUploadClick = onSelectFileClick,
            selectedFileName = selectedFileName,
        )

        if (analysisError != null) {
            Spacer(modifier = Modifier.height(Spacing.sm))
            Text(
                text = analysisError,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(horizontal = Spacing.xs),
            )
        }

        Spacer(modifier = Modifier.height(Spacing.sm))
        Text(
            text = "İpucu: PDF en iyi sonucu verir. Maksimum 5 MB.",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(horizontal = Spacing.xs),
        )

        Spacer(modifier = Modifier.height(Spacing.xl))
        OutlinedTextField(
            value = targetRole,
            onValueChange = { targetRole = it },
            label = { Text("Hedef rol *") },
            placeholder = { Text("Örn: Senior iOS Developer") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            supportingText = if (targetRole.isBlank() && selectedFileName != null) {
                { Text("Analiz için hedef rol zorunludur.", color = MaterialTheme.colorScheme.error) }
            } else null,
            isError = targetRole.isBlank() && selectedFileName != null,
        )

        if (!canAnalyzeNow) {
            Spacer(modifier = Modifier.height(Spacing.md))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f), shape = MaterialTheme.shapes.medium)
                    .padding(Spacing.md),
            ) {
                Text(
                    text = "Yeni analiz için bekleme süresi",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                Spacer(modifier = Modifier.height(Spacing.xs))
                Text(
                    text = formatDuration(remainingCooldownMs),
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.primary,
                )
                Spacer(modifier = Modifier.height(Spacing.xs))
                Text(
                    text = "Her yeni analiz arasında 48 saat bekleme süresi vardır.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }

        Spacer(modifier = Modifier.height(Spacing.xl))
        PrimaryButton(
            text = if (isAnalyzing) "Analiz ediliyor..." else "Analiz Et",
            onClick = { onAnalyzeClick(targetRole.takeIf { it.isNotBlank() }) },
            enabled = !isAnalyzing && selectedFileName != null && targetRole.isNotBlank() && canAnalyzeNow,
        )
        if (isAnalyzing) {
            Spacer(modifier = Modifier.height(Spacing.md))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = androidx.compose.foundation.layout.Arrangement.Center,
            ) {
                CircularProgressIndicator()
            }
        }
        Spacer(modifier = Modifier.height(Spacing.xxl))
        }
    }
}

private fun formatDuration(remainingMs: Long): String {
    val totalSeconds = (remainingMs / 1000).coerceAtLeast(0L)
    val hours = totalSeconds / 3600
    val minutes = (totalSeconds % 3600) / 60
    val seconds = totalSeconds % 60
    return "${hours.toString().padStart(2, '0')}:${minutes.toString().padStart(2, '0')}:${seconds.toString().padStart(2, '0')}"
}

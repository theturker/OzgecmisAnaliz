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
                            AppColors.Surface,
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
            placeholder = { Text("Örn: Senior Android Developer") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            supportingText = if (targetRole.isBlank() && selectedFileName != null) {
                { Text("Analiz için hedef rol zorunludur.", color = MaterialTheme.colorScheme.error) }
            } else null,
            isError = targetRole.isBlank() && selectedFileName != null,
        )

        Spacer(modifier = Modifier.height(Spacing.xl))
        PrimaryButton(
            text = if (isAnalyzing) "Analiz ediliyor..." else "Analiz Et",
            onClick = { onAnalyzeClick(targetRole.takeIf { it.isNotBlank() }) },
            enabled = !isAnalyzing && selectedFileName != null && targetRole.isNotBlank(),
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

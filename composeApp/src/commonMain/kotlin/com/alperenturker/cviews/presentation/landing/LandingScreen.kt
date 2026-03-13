package com.alperenturker.cviews.presentation.landing

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.alperenturker.cviews.data.mock.MockData
import com.alperenturker.cviews.presentation.ui.components.PrimaryButton
import com.alperenturker.cviews.presentation.ui.components.SecondaryButton
import com.alperenturker.cviews.presentation.ui.theme.AppColors
import com.alperenturker.cviews.presentation.ui.theme.Spacing

@Composable
fun LandingScreen(
    onUploadClick: () -> Unit,
    onExampleAnalysisClick: () -> Unit,
    onHistoryClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier,
) {
    val scrollState = rememberScrollState()
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            AppColors.Primary.copy(alpha = 0.12f),
                            AppColors.Surface,
                        ),
                    ),
                )
                .padding(Spacing.lg),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.height(Spacing.xl))
            Icon(
                imageVector = Icons.Default.Assessment,
                contentDescription = null,
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .background(AppColors.Primary.copy(alpha = 0.15f))
                    .padding(12.dp),
                tint = AppColors.Primary,
            )
            Spacer(modifier = Modifier.height(Spacing.md))
            Text(
                text = "CV Rehberi",
                style = MaterialTheme.typography.displayLarge,
                color = MaterialTheme.colorScheme.primary,
            )
            Text(
                text = "Özgeçmişinizi ATS uyumluluğu ve içerik açısından analiz edin",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Spacer(modifier = Modifier.height(Spacing.lg))
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Spacing.lg),
            verticalArrangement = Arrangement.spacedBy(Spacing.md),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            PrimaryButton(text = "CV Yükle", onClick = onUploadClick)
            SecondaryButton(text = "Örnek Analiz", onClick = onExampleAnalysisClick)
            onHistoryClick?.let { SecondaryButton(text = "Geçmiş", onClick = it) }
        }

        Spacer(modifier = Modifier.height(Spacing.lg))
        Text(
            text = "Özellikler",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Spacing.lg),
        )
        Spacer(modifier = Modifier.height(Spacing.sm))
        val featureIcons: List<ImageVector> = listOf(
            Icons.Default.Assessment,
            Icons.Default.Description,
            Icons.Default.Lightbulb,
            Icons.Default.Description,
            Icons.Default.Lightbulb,
        )
        MockData.landingFeatures.forEachIndexed { index, feature ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Spacing.lg, vertical = Spacing.xs)
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f))
                    .padding(Spacing.md),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    imageVector = featureIcons.getOrElse(index) { Icons.Default.Lightbulb },
                    contentDescription = null,
                    tint = AppColors.Primary,
                )
                Text(
                    text = feature,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(start = Spacing.sm),
                )
            }
        }
        Spacer(modifier = Modifier.height(Spacing.xxl))
    }
}

package com.alperenturker.cviews.presentation.history

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.alperenturker.cviews.domain.model.CvAnalysis
import com.alperenturker.cviews.presentation.ui.components.BackButton
import com.alperenturker.cviews.presentation.ui.components.HistoryListItem
import com.alperenturker.cviews.presentation.ui.components.SectionHeader
import com.alperenturker.cviews.presentation.ui.theme.Spacing

@Composable
fun HistoryScreen(
    analyses: List<CvAnalysis>,
    onAnalysisClick: (CvAnalysis) -> Unit,
    onDeleteAnalysis: (CvAnalysis) -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(Spacing.lg),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
        ) {
            BackButton(onClick = onBackClick)
        }
        SectionHeader(
            title = "Geçmiş analizler",
            subtitle = "Daha önce analiz ettiğiniz CV'ler.",
        )
        Spacer(modifier = Modifier.height(Spacing.lg))

        if (analyses.isEmpty()) {
            Text(
                text = "Henüz analiz yok. CV yükleyerek başlayın.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = Spacing.md),
            )
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(Spacing.sm),
            ) {
                items(analyses, key = { it.id }) { analysis ->
                    HistoryListItem(
                        analysis = analysis,
                        onClick = { onAnalysisClick(analysis) },
                        onDeleteClick = { onDeleteAnalysis(analysis) },
                    )
                }
            }
        }
    }
}

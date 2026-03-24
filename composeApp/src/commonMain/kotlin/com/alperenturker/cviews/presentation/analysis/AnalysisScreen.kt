package com.alperenturker.cviews.presentation.analysis

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import com.alperenturker.cviews.domain.model.CvAnalysis
import com.alperenturker.cviews.presentation.ui.components.BackButton
import com.alperenturker.cviews.presentation.ui.components.PrimaryButton
import com.alperenturker.cviews.presentation.ui.components.SecondaryButton
import com.alperenturker.cviews.presentation.ui.components.KeywordChip
import com.alperenturker.cviews.presentation.ui.components.ProfileSummaryCard
import com.alperenturker.cviews.presentation.ui.components.ScoreBreakdownCard
import com.alperenturker.cviews.presentation.ui.components.ScoreCard
import com.alperenturker.cviews.presentation.ui.components.SectionHeader
import com.alperenturker.cviews.presentation.ui.components.StrengthWeaknessItem
import com.alperenturker.cviews.presentation.ui.components.SuggestionCard
import com.alperenturker.cviews.presentation.ui.theme.AppShape
import com.alperenturker.cviews.presentation.ui.theme.Spacing
import com.alperenturker.cviews.util.putTextToClipboard

@Composable
fun AnalysisScreen(
    analysis: CvAnalysis?,
    onBackClick: () -> Unit,
    onCreateAnalysisClick: () -> Unit,
    draftText: String?,
    isDraftInProgress: Boolean,
    draftError: String?,
    onGenerateDraftClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    if (analysis == null) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(Spacing.lg),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            SectionHeader(
                title = "Henüz analiziniz yok",
                subtitle = "İlk CV analizinizi başlatmak için aşağıdaki butona tıklayın.",
            )
            Spacer(modifier = Modifier.height(Spacing.lg))
            PrimaryButton(
                text = "CV Yükle ve Analiz Et",
                onClick = onCreateAnalysisClick,
                modifier = Modifier.fillMaxWidth(),
            )
        }
        return
    }

    val scrollState = rememberScrollState()
    val s = analysis.atsScore
    val p = analysis.profile

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(Spacing.lg),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
        ) {
            BackButton(onClick = onBackClick)
        }
        ScoreCard(
            score = s.total,
            maxScore = 100,
            label = "ATS Uyumluluk Skoru",
            modifier = Modifier.fillMaxWidth(),
        )
        Text(
            text = "Bu skor, CV'nizin başvuru takip sistemleri (ATS) tarafından ne kadar iyi işlendiğini ve hedef role uygunluğunu yansıtır.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = Spacing.xs),
        )

        Spacer(modifier = Modifier.height(Spacing.lg))
        SectionHeader(
            title = "Skor detayı",
            subtitle = "Her kriter 0–100 arasında değerlendirilir. Parsing: metin çıkarımı; anahtar kelime: ilanla eşleşme; yapı: bölüm düzeni; okunabilirlik: dil ve format; etki: özelliklerin vurgulanması.",
        )
        Spacer(modifier = Modifier.height(Spacing.sm))
        ScoreBreakdownCard(title = "Parsing Skoru", score = s.parsingScore, maxScore = 100, useCircularChart = true)
        Spacer(modifier = Modifier.height(Spacing.sm))
        ScoreBreakdownCard(title = "Anahtar Kelime Eşleşmesi", score = s.keywordMatch, maxScore = 100, useCircularChart = true)
        Spacer(modifier = Modifier.height(Spacing.sm))
        ScoreBreakdownCard(title = "Yapı Skoru", score = s.structureScore, maxScore = 100, useCircularChart = true)
        Spacer(modifier = Modifier.height(Spacing.sm))
        ScoreBreakdownCard(title = "Okunabilirlik Skoru", score = s.readabilityScore, maxScore = 100, useCircularChart = true)
        Spacer(modifier = Modifier.height(Spacing.sm))
        ScoreBreakdownCard(title = "Etki Skoru", score = s.impactScore, maxScore = 100, useCircularChart = true)

        Spacer(modifier = Modifier.height(Spacing.xl))
        SectionHeader(
            title = "Çıkarılan profil",
            subtitle = "CV metninden otomatik çıkarılan kişisel ve mesleki bilgiler.",
        )
        Spacer(modifier = Modifier.height(Spacing.sm))
        ProfileSummaryCard(label = "İsim", value = p.name)
        Spacer(modifier = Modifier.height(Spacing.sm))
        ProfileSummaryCard(label = "Tespit edilen rol", value = p.detectedRole ?: "—")
        Spacer(modifier = Modifier.height(Spacing.sm))
        ProfileSummaryCard(
            label = "Deneyim",
            value = p.yearsOfExperience?.let { "$it yıl" } ?: "—",
        )
        Spacer(modifier = Modifier.height(Spacing.sm))
        ProfileSummaryCard(
            label = "Öne çıkan beceriler",
            value = p.topSkills.joinToString(", "),
        )
        Spacer(modifier = Modifier.height(Spacing.sm))
        ProfileSummaryCard(label = "Eğitim", value = p.education ?: "—")

        Spacer(modifier = Modifier.height(Spacing.xl))
        SectionHeader(
            title = "Güçlü yönler",
            subtitle = "CV'nizde öne çıkan ve işverenlere fayda sağlayabilecek noktalar.",
        )
        Spacer(modifier = Modifier.height(Spacing.sm))
        analysis.strengths.forEach { item ->
            StrengthWeaknessItem(
                title = item.title,
                explanation = item.explanation,
                isStrength = true,
                modifier = Modifier.fillMaxWidth(),
            )
            Spacer(modifier = Modifier.height(Spacing.sm))
        }

        Spacer(modifier = Modifier.height(Spacing.lg))
        SectionHeader(
            title = "Geliştirilmesi gerekenler",
            subtitle = "Hedef role göre güçlendirilmesi veya daha net yazılması önerilen alanlar.",
        )
        Spacer(modifier = Modifier.height(Spacing.sm))
        analysis.weaknesses.forEach { item ->
            StrengthWeaknessItem(
                title = item.title,
                explanation = item.explanation,
                isStrength = false,
                modifier = Modifier.fillMaxWidth(),
            )
            Spacer(modifier = Modifier.height(Spacing.sm))
        }

        Spacer(modifier = Modifier.height(Spacing.xl))
        SectionHeader(
            title = "İyileştirme önerileri",
            subtitle = "CV'nizi güçlendirmek için somut adımlar. Kopyala ile metni panoya alıp notlarınıza yapıştırabilirsiniz.",
        )
        Spacer(modifier = Modifier.height(Spacing.sm))
        analysis.improvementSuggestions.forEach { suggestion ->
            SuggestionCard(
                title = suggestion.title,
                explanation = suggestion.explanation,
                onCopyClick = {
                    putTextToClipboard("${suggestion.title}\n\n${suggestion.explanation}")
                },
                modifier = Modifier.fillMaxWidth(),
            )
            Spacer(modifier = Modifier.height(Spacing.sm))
        }

        Spacer(modifier = Modifier.height(Spacing.xl))
        SectionHeader(
            title = "Eksik anahtar kelimeler",
            subtitle = "Hedef ilanda geçen ve CV'nize eklemeniz skoru yükseltebilecek terimler.",
        )
        Spacer(modifier = Modifier.height(Spacing.sm))
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(Spacing.sm),
            verticalArrangement = Arrangement.spacedBy(Spacing.sm),
        ) {
            analysis.missingKeywords.forEach { keyword ->
                KeywordChip(keyword = keyword)
            }
        }

        Spacer(modifier = Modifier.height(Spacing.xl))
        SectionHeader(
            title = "İyileştirilmiş CV Taslağı",
            subtitle = "Analiz sonuçlarına göre kopyalayabileceğiniz bir taslak metin üretin."
        )
        Spacer(modifier = Modifier.height(Spacing.sm))

        PrimaryButton(
            text = if (isDraftInProgress) "Taslak oluşturuluyor..." else "CV Taslağını Oluştur",
            onClick = onGenerateDraftClick,
            enabled = !isDraftInProgress,
            modifier = Modifier.fillMaxWidth(),
        )

        if (isDraftInProgress) {
            Spacer(modifier = Modifier.height(Spacing.sm))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
            ) {
                CircularProgressIndicator()
            }
        }

        if (draftError != null) {
            Spacer(modifier = Modifier.height(Spacing.sm))
            Text(
                text = draftError,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.error,
            )
        }

        if (draftText != null) {
            Spacer(modifier = Modifier.height(Spacing.md))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(AppShape.medium)
                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f))
                    .padding(Spacing.md),
            ) {
                Text(
                    text = draftText,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                )
            }

            Spacer(modifier = Modifier.height(Spacing.sm))
            SecondaryButton(
                text = "Taslağı Kopyala",
                onClick = { putTextToClipboard(draftText) },
                enabled = !isDraftInProgress,
                modifier = Modifier.fillMaxWidth(),
            )
        }

        Spacer(modifier = Modifier.height(Spacing.xxl))
    }
}

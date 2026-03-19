package com.alperenturker.cviews.presentation.landing

import androidx.compose.foundation.background
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource
import cviews.composeapp.generated.resources.*
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
    var expandedFeatureIndex by remember { mutableStateOf<Int?>(null) }
    val featureDescriptions = mapOf(
        "ATS Uyumluluk Skoru" to "CV’nizin ATS sistemleri tarafından ne kadar iyi parse edildiğini ve rol hedefiyle ne kadar uyumlu olduğunu görürsünüz.",
        "Yetenek Çıkarımı" to "Profiliniz ve öne çıkan beceriler otomatik olarak çıkarılır; böylece başvuru için hızlı bir özet oluşur.",
        "Deneyim Tespiti" to "Deneyim yılı ve hedef roldeki uyum analiz edilir; güçlü olduğunuz alanlar netleşir.",
        "Eksik Anahtar Kelimeler" to "İlanda geçen ve CV’nizde bulunmayan anahtar kelimeler listelenir; skorunuzu yükseltmek için öneriler alırsınız.",
        "İyileştirme Önerileri" to "CV’nizi güçlendirecek somut adımlar sunulur; hangi bölümü nasıl geliştireceğinizi kolayca görürsünüz.",
    )
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
                            MaterialTheme.colorScheme.surface,
                        ),
                    ),
                )
                .padding(Spacing.lg),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.height(Spacing.xl))
            Spacer(modifier = Modifier.height(Spacing.md))
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(30.dp))
                    .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.22f))
            ) {
                Image(
                    painter = painterResource(Res.drawable.image33),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.size(60.dp),
                )
            }
            Spacer(modifier = Modifier.height(Spacing.md))
            Text(
                text = "Özgeçmiş Analiz",
                style = MaterialTheme.typography.displayLarge,
                color = MaterialTheme.colorScheme.primary,
            )
            Spacer(modifier = Modifier.height(Spacing.sm))
            Text(
                text = "CV’nizi ATS uyumluluğu, güçlü yönler ve eksik anahtar kelimeler açısından analiz edin. Sisteminizin metni nasıl okuyabildiğini, rol ilanıyla eşleşen kelimeleri ve okunabilirlik/format için iyileştirmeleri görün.",
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
            text = "Neler Yapabilirsiniz?",
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
            val expanded = expandedFeatureIndex == index
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = Spacing.lg, vertical = Spacing.xs)
                        .clip(RoundedCornerShape(12.dp))
                        .background(
                            MaterialTheme.colorScheme.surfaceVariant.copy(
                                alpha = if (expanded) 0.8f else 0.6f
                            )
                        )
                        .clickable { expandedFeatureIndex = if (expanded) null else index }
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
                        modifier = Modifier.padding(start = Spacing.sm).weight(1f),
                    )
                    Icon(
                        imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                        contentDescription = null,
                        tint = AppColors.Primary,
                    )
                }

                val details = featureDescriptions[feature].orEmpty()
                if (expanded && details.isNotBlank()) {
                    Spacer(modifier = Modifier.height(Spacing.xs))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = Spacing.lg)
                            .clip(RoundedCornerShape(12.dp))
                            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f))
                            .padding(Spacing.md),
                    ) {
                        Text(
                            text = details,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(Spacing.xxl))
    }
}

package com.alperenturker.cviews.presentation.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = AppColors.Primary,
    onPrimary = AppColors.OnPrimary,
    primaryContainer = AppColors.Primary.copy(alpha = 0.12f),
    onPrimaryContainer = AppColors.PrimaryVariant,
    surface = AppColors.Surface,
    onSurface = AppColors.OnSurface,
    surfaceVariant = AppColors.SurfaceVariant,
    onSurfaceVariant = AppColors.OnSurfaceVariant,
    outline = AppColors.Outline,
    outlineVariant = AppColors.OutlineVariant,
    error = AppColors.Error,
    onError = Color.White,
)

@Composable
fun CvRehberiTheme(
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = AppTypography,
        shapes = androidx.compose.material3.Shapes(
            extraSmall = AppShape.small,
            small = AppShape.small,
            medium = AppShape.medium,
            large = AppShape.large,
            extraLarge = AppShape.extraLarge,
        ),
        content = content,
    )
}

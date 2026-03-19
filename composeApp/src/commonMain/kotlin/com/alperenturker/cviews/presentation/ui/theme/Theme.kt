package com.alperenturker.cviews.presentation.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
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

private val DarkColorScheme = darkColorScheme(
    primary = AppColors.Primary,
    onPrimary = AppColors.OnPrimary,
    primaryContainer = AppColors.Primary.copy(alpha = 0.25f),
    onPrimaryContainer = AppColors.PrimaryVariant,
    surface = Color(0xFF0B1220),
    onSurface = Color(0xFFE5E7EB),
    surfaceVariant = Color(0xFF111C2E),
    onSurfaceVariant = Color(0xFF9CA3AF),
    outline = Color(0xFF253244),
    outlineVariant = Color(0xFF2B3B52),
    error = AppColors.Error,
    onError = Color.White,
)

@Composable
fun CvRehberiTheme(
    content: @Composable () -> Unit,
) {
    val dark = isSystemInDarkTheme()
    MaterialTheme(
        colorScheme = if (dark) DarkColorScheme else LightColorScheme,
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

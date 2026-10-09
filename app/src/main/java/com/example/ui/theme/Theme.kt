package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = PandaPinkLight,
    onPrimary = Color.White,
    primaryContainer = PandaPinkDark,
    onPrimaryContainer = PandaPinkContainer,
    secondary = Color(0xFFA0AEC0),
    onSecondary = Color.Black,
    secondaryContainer = PandaCharcoalLight,
    onSecondaryContainer = Color.White,
    tertiary = PetrolAmber,
    onTertiary = Color.White,
    background = BackgroundDark,
    onBackground = TextPrimaryDark,
    surface = SurfaceDark,
    onSurface = TextPrimaryDark,
    surfaceVariant = SurfaceVariantDark,
    onSurfaceVariant = TextSecondaryDark,
    outline = OutlineDark,
    error = LossRed,
    onError = Color.White
)

private val LightColorScheme = lightColorScheme(
    primary = PandaPinkPrimary,
    onPrimary = Color.White,
    primaryContainer = PandaPinkContainer,
    onPrimaryContainer = PandaPinkOnContainer,
    secondary = PandaCharcoal,
    onSecondary = Color.White,
    secondaryContainer = SurfaceVariantLight,
    onSecondaryContainer = PandaCharcoal,
    tertiary = PetrolAmber,
    onTertiary = Color.White,
    background = BackgroundLight,
    onBackground = TextPrimaryLight,
    surface = SurfaceLight,
    onSurface = TextPrimaryLight,
    surfaceVariant = SurfaceVariantLight,
    onSurfaceVariant = TextSecondaryLight,
    outline = OutlineLight,
    error = LossRed,
    onError = Color.White
)

@Composable
fun RiderFinanceTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = MinimalPrimary,
    onPrimary = Color.White,
    secondary = MinimalContainerPill,
    onSecondary = MinimalTextDarkSec,
    tertiary = SuccessGreen,
    background = Color(0xFF141218), // Elegant deep slate/violet black
    surface = Color(0xFF1D1B20),    // Dark container surface
    onBackground = Color(0xFFE6E1E5),
    onSurface = Color(0xFFE6E1E5),
    surfaceVariant = Color(0xFF49454F),
    onSurfaceVariant = Color(0xFFCAC4D0)
)

private val LightColorScheme = lightColorScheme(
    primary = MinimalPrimary,
    onPrimary = Color.White,
    secondary = MinimalContainerPill,
    onSecondary = MinimalTextDarkSec,
    tertiary = SuccessGreen,
    background = MinimalBackground,
    surface = MinimalSurface,
    onBackground = MinimalTextDark,
    onSurface = MinimalTextDark,
    surfaceVariant = MinimalSurfaceVariant,
    onSurfaceVariant = MinimalTextDark
)

    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Keep dynamic color disabled to preserve our majestic green brand identity
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit,
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

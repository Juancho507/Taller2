package com.example.taller2.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val ColorPalette = lightColorScheme(
    primary = Color(0xFFFF9800),
    onPrimary = Color.White,
    secondary = Color(0xFF00BCD4),
    onSecondary = Color.White,
    background = Color(0xFFFFF3E0),
    surface = Color(0xFFFFE0B2),
    onSurface = Color(0xFF212121)
)

@Composable
fun EmojiGuessTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = ColorPalette,
        typography = Typography,
        content = content
    )
}
